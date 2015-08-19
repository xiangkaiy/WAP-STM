package com.stm.shorttermemployee.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.stm.shorttermemployee.pojo.Department;
import com.stm.shorttermemployee.pojo.LeaderEvaluation;
import com.stm.shorttermemployee.pojo.User;
import com.stm.shorttermemployee.pojo.UserSTE;
import com.stm.shorttermemployee.service.LeaderService;
import com.stm.shorttermemployee.service.UserSTEService;
import com.stm.shorttermemployee.util.Constants;
import com.stm.shorttermemployee.util.LoginSession;
import com.stm.shorttermemployee.util.MessageTip;

@ManagedBean
@Scope("session")
@Controller
public class SteDetailController implements Serializable {
	private static final long serialVersionUID = -6745776929090578534L;

	private UserSTE curSTE = null;

	@Autowired
	private UserSTEService userSTEservice;

	@Autowired
	private UserSTEService steService;

	@Autowired
	private LeaderService leaderService;

	private List<String> officeSkillList = new ArrayList<String>();
	private List<String> proLanguageList = new ArrayList<String>();
	private String[] selectedOfficeSkill;
	private String[] selectedProLanguage;
	private Long leasthoursweek;
	private Date joindate;
	private Department belongDepart = null;
	private User leader = null;
	private String steStatus;

	private String cmt;
	private int workEfficiency;
	private int studyAbility;
	private int meetingParticipate;
	private int teamWork;

	private String evaluationScoreStr = null;
	private boolean haveEvaluation = false;

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
		if (curSTE != null) {
			String skills = curSTE.getOfficeskill();
			if (skills != null) {
				selectedOfficeSkill = skills.split(Constants.SPLIT_STR);
			} else {
				selectedOfficeSkill = null;
			}
		}
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
		if (curSTE != null) {
			String skills = curSTE.getProgramlanguage();
			if (skills != null) {
				selectedProLanguage = skills.split(Constants.SPLIT_STR);
			} else {
				selectedProLanguage = null;
			}
		}
		return selectedProLanguage;
	}

	public void setSelectedProLanguage(String[] selectedProLanguage) {
		this.selectedProLanguage = selectedProLanguage;
	}

	public UserSTE getCurSTE() {
		long curID = 0;
		if (LoginSession.getCurrentUser() != null) {
			curID = LoginSession.getCurrentUser().getId();
			curSTE = userSTEservice.getUserSTE(curID);
		}
		return curSTE;
	}

	public void setCurSTE(UserSTE curSTE) {
		this.curSTE = curSTE;
	}

	public void submitDetailInfo() {
		if (selectedOfficeSkill.length == 0) {
			MessageTip.showMessage(Constants.TIPID, "You should choose at least one skill!");
			return;
		}
		if (selectedProLanguage.length == 0) {
			MessageTip.showMessage(Constants.TIPID, "You should choose at least one program language!");
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
		curSTE.setOfficeskill(skillStr);
		// curSTE.setStatus(Constants.STE_PREPARE_WORK);
		curSTE.setProgramlanguage(lanStr);
		curSTE.setLeasthoursweek(leasthoursweek);
		curSTE.setJoindate(joindate);
		userSTEservice.saveOrUpdate(curSTE);
		leasthoursweek = null;
		joindate = null;
		MessageTip.showMessage(Constants.TIPID, "Save successfully!");
	}

	public Long getLeasthoursweek() {
		if (curSTE != null) {
			leasthoursweek = curSTE.getLeasthoursweek();
		} else {
			long curID = LoginSession.getCurrentUser().getId();
			curSTE = userSTEservice.getUserSTE(curID);
			leasthoursweek = curSTE.getLeasthoursweek();
		}
		return leasthoursweek;
	}

	public void setLeasthoursweek(Long leasthoursweek) {
		this.leasthoursweek = leasthoursweek;
	}

	public Date getJoindate() {
		if (curSTE != null) {
			joindate = curSTE.getJoindate();
		} else {
			long curID = LoginSession.getCurrentUser().getId();
			curSTE = userSTEservice.getUserSTE(curID);
			joindate = curSTE.getJoindate();
		}
		return joindate;
	}

	public void setJoindate(Date joindate) {
		this.joindate = joindate;
	}

	public Department getBelongDepart() {
		if (belongDepart == null) {
			belongDepart = userSTEservice.getDepartment(LoginSession.getCurrentUser());
		}

		return belongDepart;
	}

	public void setBelongDepart(Department belongDepart) {
		this.belongDepart = belongDepart;
	}

	public User getLeader() {
		if (leader == null) {
			leader = userSTEservice.getLeader(LoginSession.getCurrentUser());
		}
		return leader;
	}

	public void setLeader(User leader) {
		this.leader = leader;
	}

	public String getSteStatus() {
		long curID = LoginSession.getCurrentUser().getId();
		curSTE = userSTEservice.getUserSTE(curID);
		steStatus = curSTE.getStatus().toString();
		return steStatus;
	}

	public void setSteStatus(String steStatus) {
		this.steStatus = steStatus;
	}

	public int getMeetingParticipate() {
		if (evaluationScoreStr == null) {
			User curUser = LoginSession.getCurrentUser();
			evaluationScoreStr = steService.getEvaluationScore(curUser.getId());
		}
		String[] evaluationArray = evaluationScoreStr.split(" ");
		meetingParticipate = Integer.parseInt(evaluationArray[2]);
		return meetingParticipate;
	}

	public void setMeetingParticipate(int meetingParticipate) {
		this.meetingParticipate = meetingParticipate;
	}

	public int getWorkEfficiency() {
		if (evaluationScoreStr == null) {
			User curUser = LoginSession.getCurrentUser();
			evaluationScoreStr = steService.getEvaluationScore(curUser.getId());
		}
		if (evaluationScoreStr != null) {
			String[] evaluationArray = evaluationScoreStr.split(" ");
			workEfficiency = Integer.parseInt(evaluationArray[0]);
		}
		return workEfficiency;
	}

	public void setWorkEfficiency(int workEfficiency) {
		this.workEfficiency = workEfficiency;
	}

	public int getStudyAbility() {
		if (evaluationScoreStr == null) {
			User curUser = LoginSession.getCurrentUser();
			evaluationScoreStr = steService.getEvaluationScore(curUser.getId());
		}
		if (evaluationScoreStr != null) {
			String[] evaluationArray = evaluationScoreStr.split(" ");
			studyAbility = Integer.parseInt(evaluationArray[1]);
		}
		return studyAbility;
	}

	public void setStudyAbility(int studyAbility) {
		this.studyAbility = studyAbility;
	}

	public int getTeamWork() {
		if (evaluationScoreStr == null) {
			User curUser = LoginSession.getCurrentUser();
			evaluationScoreStr = steService.getEvaluationScore(curUser.getId());
		}
		if (evaluationScoreStr != null) {
			String[] evaluationArray = evaluationScoreStr.split(" ");
			teamWork = Integer.parseInt(evaluationArray[3]);
		}
		return teamWork;
	}

	public void setTeamWork(int teamWork) {
		this.teamWork = teamWork;
	}

	public String getCmt() {
		User curUser = LoginSession.getCurrentUser();
		LeaderEvaluation eval = leaderService.getEvaluationBySte(curUser.getId());
		if (eval != null) {
			cmt = eval.getCmt();
		}
		return cmt;
	}

	public void setCmt(String cmt) {
		this.cmt = cmt;
	}

	public boolean isHaveEvaluation() {
		User curUser = LoginSession.getCurrentUser();
		evaluationScoreStr = steService.getEvaluationScore(curUser.getId());
		haveEvaluation = evaluationScoreStr == null ? false : true;
		return haveEvaluation;
	}

	public void setHaveEvaluation(boolean haveEvaluation) {
		this.haveEvaluation = haveEvaluation;
	}

}
