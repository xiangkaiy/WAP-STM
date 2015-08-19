package com.stm.shorttermemployee.controller;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.stm.shorttermemployee.model.LeaderApplyListModel;
import com.stm.shorttermemployee.model.LeaderApplyView;
import com.stm.shorttermemployee.pojo.LeaderApplySTE;
import com.stm.shorttermemployee.pojo.User;
import com.stm.shorttermemployee.service.LeaderService;
import com.stm.shorttermemployee.util.Constants;
import com.stm.shorttermemployee.util.LoginSession;
import com.stm.shorttermemployee.util.MessageTip;

@ManagedBean
@Scope("session")
@Controller
public class LeaderApplyController implements Serializable {
	private static final long serialVersionUID = 5245780251652572682L;
	private LeaderApplySTE apply = new LeaderApplySTE();

	private List<String> officeSkillList = new ArrayList<String>();
	private List<String> proLanguageList = new ArrayList<String>();
	private String[] selectedOfficeSkill;
	private String[] selectedProLanguage;

	private LeaderApplyListModel applyList = null;
	private List<LeaderApplyView> baseApplyList = null;
	private int first;

	@Autowired
	private LeaderService leaderService;

	public List<String> getOfficeSkillList() {
		if (officeSkillList.size() == 0) {
			officeSkillList.addAll(Constants.officeskill);
		}
		return officeSkillList;
	}

	public void setOfficeSkillList(List<String> officeSkillList) {
		this.officeSkillList = officeSkillList;
	}

	public String[] getSelectedOfficeSkill() {
		return selectedOfficeSkill;
	}

	public void setSelectedOfficeSkill(String[] selectedOfficeSkill) {
		this.selectedOfficeSkill = selectedOfficeSkill;
	}

	public List<String> getProLanguageList() {
		if (proLanguageList.size() == 0) {
			proLanguageList.addAll(Constants.programLan);
		}
		return proLanguageList;
	}

	public void setProLanguageList(List<String> proLanguageList) {
		this.proLanguageList = proLanguageList;
	}

	public String[] getSelectedProLanguage() {
		return selectedProLanguage;
	}

	public void setSelectedProLanguage(String[] selectedProLanguage) {
		this.selectedProLanguage = selectedProLanguage;
	}

	public LeaderApplySTE getApply() {
		return apply;
	}

	public void setApply(LeaderApplySTE apply) {
		this.apply = apply;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void submitApply() {
		if (selectedOfficeSkill.length == 0) {
			MessageTip.showMessage("tip", "You should choose at least one skill!");
			return;
		}
		if (selectedProLanguage.length == 0) {
			MessageTip.showMessage("tip", "You should choose at least one program language!");
			return;
		}
		String skillStr = "";
		for (String item : selectedOfficeSkill) {
			skillStr += (item + Constants.SPLIT_STR);
		}
		skillStr = skillStr.substring(0, skillStr.length() - 1);

		String lanStr = "";
		for (String item : selectedProLanguage) {
			lanStr += (item + Constants.SPLIT_STR);
		}
		lanStr = lanStr.substring(0, lanStr.length() - 1);
		Date now = new Date();
		apply.setApplytime(new Timestamp(now.getTime()));
		apply.setLeaderid(LoginSession.getCurrentUser().getId());
		apply.setOfficeskill(skillStr);
		apply.setProgramlanguage(lanStr);
		apply.setStatus(Constants.LEADERAPPLY_SUBMIT);
		leaderService.addApplySTE(apply);
		MessageTip.showMessage(Constants.TIPID, "Submit Successfully!");
		refreshApplyList();
	}

	public LeaderApplyListModel getApplyList() {
		if (applyList == null) {
			refreshApplyList();
		}
		return applyList;
	}

	private void refreshApplyList() {
		User curLeader = LoginSession.getCurrentUser();
		baseApplyList = leaderService.getApplyListViewByLeader(curLeader);

		applyList = new LeaderApplyListModel(baseApplyList);
	}

	public void setApplyList(LeaderApplyListModel applyList) {
		this.applyList = applyList;
	}

	public List<LeaderApplyView> getBaseApplyList() {
		return baseApplyList;
	}

	public void setBaseApplyList(List<LeaderApplyView> baseApplyList) {
		this.baseApplyList = baseApplyList;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

}
