package com.stm.shorttermemployee.controller;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.stm.shorttermemployee.pojo.ApplyRegular;
import com.stm.shorttermemployee.pojo.User;
import com.stm.shorttermemployee.pojo.UserSTE;
import com.stm.shorttermemployee.service.UserSTEService;
import com.stm.shorttermemployee.util.Constants;
import com.stm.shorttermemployee.util.LoginSession;
import com.stm.shorttermemployee.util.MessageTip;

@ManagedBean
@Scope("session")
@Controller
public class SteAskForRegularController implements Serializable {
	private static final long serialVersionUID = 8798084333824812378L;
	private String text;
	private List<ApplyRegular> applyList = null;

	@Autowired
	UserSTEService steService;

	public void applyForRegular() {
		if (text == null || text.trim().equals("")) {
			MessageTip.showMessage(Constants.TIPID, "The text can't be null!");
			return;
		}

		User curUser = LoginSession.getCurrentUser();
		UserSTE curSte = steService.getUserSTE(curUser.getId());
		if (curSte.getStatus().equals(Constants.STE_WORK) == false) {
			MessageTip.showMessage(Constants.TIPID, "You can't ask for turning into regular because you aren't at working!");
			return;
		}

		boolean existUnhandledApply = (steService.getUnhandledApplyRegular(curUser.getId()) != null);
		if (existUnhandledApply) {
			MessageTip.showMessage(Constants.TIPID, "The former apply has not been handled yet, you can't submit a new apply!");
			return;
		}

		ApplyRegular apply = new ApplyRegular();
		apply.setIntro(text);
		Date now = new Date();
		apply.setApplytime(new Timestamp(now.getTime()));
		apply.setStatus(Constants.APPLY_REGULAR_SUBMIT);

		apply.setSteid(curUser.getId());
		User leader = steService.getLeader(curUser);
		apply.setLeaderid(leader.getId());
		if (text.length() > Constants.MAX_TEXT_LENGTH) {
			MessageTip.showMessage(Constants.TIPID, "The text can not be long than " + Constants.MAX_TEXT_LENGTH);
			return;
		}
		steService.submitApplyRegular(apply);
		MessageTip.showMessage(Constants.TIPID, "Submit successfully!");
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<ApplyRegular> getApplyList() {
		User curUser = LoginSession.getCurrentUser();
		applyList = steService.getApplyRegularList(curUser.getId());
		return applyList;
	}

	public void setApplyList(List<ApplyRegular> applyList) {
		this.applyList = applyList;
	}
}
