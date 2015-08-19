package com.stm.shorttermemployee.controller;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.stm.shorttermemployee.model.SteLeaveModel;
import com.stm.shorttermemployee.model.UserSTEModel;
import com.stm.shorttermemployee.model.UserSTEView;
import com.stm.shorttermemployee.pojo.Department;
import com.stm.shorttermemployee.pojo.LeaderEvaluation;
import com.stm.shorttermemployee.pojo.SteLeave;
import com.stm.shorttermemployee.pojo.User;
import com.stm.shorttermemployee.service.DepartmentService;
import com.stm.shorttermemployee.service.LeaderService;
import com.stm.shorttermemployee.service.UserSTEService;
import com.stm.shorttermemployee.service.UserService;
import com.stm.shorttermemployee.util.Constants;
import com.stm.shorttermemployee.util.LoginSession;
import com.stm.shorttermemployee.util.MessageTip;

@ManagedBean
@Scope("session")
@Controller
public class LeaderSTEManageController implements Serializable {
	private static final long serialVersionUID = 5335096882402384010L;

	@Autowired
	private UserService userService;

	@Autowired
	private UserSTEService steService;

	@Autowired
	private DepartmentService departService;

	@Autowired
	private LeaderService leaderService;

	private UserSTEModel userList;

	private List<UserSTEView> userSteList = null;

	private List<UserSTEView> filteredSTE;

	private UserSTEView selectedSTE;

	private int first;

	private List<SteLeave> allApplyLeaves = null;
	private SteLeaveModel allApplyLeavesModel = null;

	private SteLeave[] selectedApplyLeaves;

	private String comment;

	private List<SteLeave> handledLeaves = null;
	private SteLeaveModel handledLeavesModel = null;

	// evaluationscore begin
	private Integer workEfficiency = 0;

	private Integer studyAbility = 0;

	private Integer meetingParticipate = 0;

	private Integer teamWork = 0;
	// evaluationscore end

	private boolean showEvaluationDlg = false;

	private LeaderEvaluation evaluation;

	public List<UserSTEView> getUserSteList() {
		if (userSteList == null) {
			Department depart = departService.getDepart(LoginSession.getCurrentUser().getDepartmentid());
			userSteList = steService.getUserSTEByDepart(depart);
			for (UserSTEView v : userSteList) {
				String statusStr = "";
				switch (v.getSte().getStatus()) {
				case Constants.STE_IDEL:
					statusStr = "Idle";
					break;
				case Constants.STE_LEAVE:
					statusStr = "Rest";
					break;
				case Constants.STE_LEAVE_OFFICE:
					statusStr = "Dimission";
					break;
				case Constants.STE_PREPARE_WORK:
					statusStr = "Prepare to work";
					break;
				case Constants.STE_WORK:
					statusStr = "At work";
					break;
				default:
					break;

				}
				v.getSte().setStatusStr(statusStr);
			}
		}
		return userSteList;
	}

	public void setUserSteList(List<UserSTEView> userSteList) {
		this.userSteList = userSteList;
	}

	public List<UserSTEView> getFilteredSTE() {
		return filteredSTE;
	}

	public void setFilteredSTE(List<UserSTEView> filteredSTE) {
		this.filteredSTE = filteredSTE;
	}

	public UserSTEView getSelectedSTE() {
		return selectedSTE;
	}

	public void setSelectedSTE(UserSTEView selectedSTE) {
		this.selectedSTE = selectedSTE;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public UserSTEModel getUserList() {
		getUserSteList();
		userList = new UserSTEModel(userSteList);
		return userList;
	}

	public void setUserList(UserSTEModel userList) {
		this.userList = userList;
	}

	public List<SteLeave> getAllApplyLeaves() {
		return allApplyLeaves;
	}

	public void setAllApplyLeaves(List<SteLeave> allApplyLeaves) {
		this.allApplyLeaves = allApplyLeaves;
	}

	public SteLeave[] getSelectedApplyLeaves() {
		return selectedApplyLeaves;
	}

	public void setSelectedApplyLeaves(SteLeave[] selectedApplyLeaves) {
		this.selectedApplyLeaves = selectedApplyLeaves;
	}

	public void passLeaveApply() {
		if (selectedApplyLeaves == null || selectedApplyLeaves.length == 0) {
			MessageTip.showMessage(Constants.TIPID, "You should choose at least one apply!");
			return;
		}
		Long leaderid = LoginSession.getCurrentUser().getId();
		for (SteLeave leave : selectedApplyLeaves) {
			leave.setLeadercomment(comment);
			leave.setLeaderid(leaderid);
		}
		steService.passApplyForLeave(selectedApplyLeaves);
		MessageTip.showMessage(Constants.TIPID, "Pass successfully!");
	}

	public void refuseLeaveApply() {
		if (selectedApplyLeaves == null || selectedApplyLeaves.length == 0) {
			MessageTip.showMessage(Constants.TIPID, "You should choose at least one apply!");
			return;
		}
		Long leaderid = LoginSession.getCurrentUser().getId();
		for (SteLeave leave : selectedApplyLeaves) {
			leave.setLeadercomment(comment);
			leave.setLeaderid(leaderid);
		}
		steService.refuseApplyForLeave(selectedApplyLeaves);
		MessageTip.showMessage(Constants.TIPID, "Refuse successfully!");
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<SteLeave> getHandledLeaves() {
		return handledLeaves;
	}

	public void setHandledLeaves(List<SteLeave> handledLeaves) {
		this.handledLeaves = handledLeaves;
	}

	public void onRowSelect(SelectEvent event) {
		selectedSTE = (UserSTEView) event.getObject();
		User curUser = LoginSession.getCurrentUser();
		evaluation = leaderService.getEvaluationBySteAndLeader(selectedSTE.getSte().getId(), curUser.getId());
		if (evaluation == null) {
			evaluation = new LeaderEvaluation();
		} else {
			String[] score = evaluation.getEvaluationscore().split(" ");
			workEfficiency = Integer.parseInt(score[0]);

			studyAbility = Integer.parseInt(score[1]);

			meetingParticipate = Integer.parseInt(score[2]);

			teamWork = Integer.parseInt(score[3]);
		}
	}

	public Integer getWorkEfficiency() {
		return workEfficiency;
	}

	public void setWorkEfficiency(Integer workEfficiency) {
		this.workEfficiency = workEfficiency;
	}

	public Integer getStudyAbility() {
		return studyAbility;
	}

	public void setStudyAbility(Integer studyAbility) {
		this.studyAbility = studyAbility;
	}

	public Integer getMeetingParticipate() {
		return meetingParticipate;
	}

	public void setMeetingParticipate(Integer meetingParticipate) {
		this.meetingParticipate = meetingParticipate;
	}

	public Integer getTeamWork() {
		return teamWork;
	}

	public void setTeamWork(Integer teamWork) {
		this.teamWork = teamWork;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void submitEvaluation() {
		Long steId = selectedSTE.getSte().getId();
		Date now = new Date();
		evaluation.setGivetime(new Timestamp(now.getTime()));
		evaluation.setLeaderid(LoginSession.getCurrentUser().getId());
		evaluation.setSteid(steId);
		String evaluationscore = workEfficiency.toString() + " " + studyAbility.toString() + " " + meetingParticipate.toString() + " " + teamWork.toString();
		evaluation.setEvaluationscore(evaluationscore);
		leaderService.submitEvaluation(evaluation);

		workEfficiency = studyAbility = meetingParticipate = teamWork = 0;
		MessageTip.showMessage(Constants.TIPID, "Submit successfully!");
		showEvaluationDlg = false;
	}

	public boolean isShowEvaluationDlg() {
		return showEvaluationDlg;
	}

	public void setShowEvaluationDlg(boolean showEvaluationDlg) {
		this.showEvaluationDlg = showEvaluationDlg;
	}

	public LeaderEvaluation getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(LeaderEvaluation evaluation) {
		this.evaluation = evaluation;
	}

	private void refreshHandedLeave() {
		Long leaderid = LoginSession.getCurrentUser().getId();
		handledLeaves = steService.getApplyLeaveByLeader(leaderid);
		for (SteLeave lea : handledLeaves) {
			String steName = userService.getUserById(lea.getSteid()).getUsername();
			lea.setStename(steName);
		}
		handledLeavesModel = new SteLeaveModel(handledLeaves);
	}

	public SteLeaveModel getHandledLeavesModel() {
		if (handledLeavesModel == null) {
			refreshHandedLeave();
		}
		return handledLeavesModel;
	}

	public void setHandledLeavesModel(SteLeaveModel handledLeavesModel) {
		this.handledLeavesModel = handledLeavesModel;
	}

	private void refreshAllApplyLeaves() {
		allApplyLeaves = steService.getAllApplyLeave();
		for (SteLeave lea : allApplyLeaves) {
			String steName = userService.getUserById(lea.getSteid()).getUsername();
			lea.setStename(steName);
		}
		allApplyLeavesModel = new SteLeaveModel(allApplyLeaves);
	}

	public SteLeaveModel getAllApplyLeavesModel() {
		if (allApplyLeavesModel == null) {
			refreshAllApplyLeaves();
		}
		return allApplyLeavesModel;
	}

	public void setAllApplyLeavesModel(SteLeaveModel allApplyLeavesModel) {
		this.allApplyLeavesModel = allApplyLeavesModel;
	}

}
