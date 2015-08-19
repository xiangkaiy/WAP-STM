package com.stm.shorttermemployee.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.SlideEndEvent;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.stm.shorttermemployee.model.ApplyRegularView;
import com.stm.shorttermemployee.pojo.Department;
import com.stm.shorttermemployee.pojo.SteTrain;
import com.stm.shorttermemployee.pojo.User;
import com.stm.shorttermemployee.service.DepartmentService;
import com.stm.shorttermemployee.service.TrainService;
import com.stm.shorttermemployee.service.UserSTEService;
import com.stm.shorttermemployee.service.WorkTimeService;
import com.stm.shorttermemployee.util.Constants;
import com.stm.shorttermemployee.util.LoginSession;
import com.stm.shorttermemployee.util.MessageTip;

@ManagedBean
@Scope("session")
@Controller
public class HrApplyRegularController implements Serializable {
	private static final long serialVersionUID = 1802600748197701772L;
	private List<ApplyRegularView> applyList = null;
	private List<ApplyRegularView> filteredApply;
	private List<ApplyRegularView> constantApplyList = null;

	@Autowired
	private UserSTEService steService;

	@Autowired
	private WorkTimeService workTimeService;

	@Autowired
	private TrainService trainService;

	@Autowired
	private DepartmentService departService;

	private int first = 0;
	private ApplyRegularView selectedApply;
	private PieChartModel workTimeModel = new PieChartModel();
	private PieChartModel evaluationModel = new PieChartModel();
	private List<SteTrain> trainList = null;
	private List<Department> departmentList = null;
	private BarChartModel barModel = new BarChartModel();
	private BarChartModel leaderBarModel = new BarChartModel();
	private int workEfficiency;
	private int studyAbility;
	private int meetingParticipate;
	private int teamWork;
	private int lateCount;
	private int leaveEarlyCount;
	private int onScheduleCount;

	private int curTrainScore;
	private int curWorkTimeScore;
	private int curEvaluationScore;
	private int curTotalScore;
	private int referenceScore;

	private int minScore = 0;
	private int maxScore = 0;
	private int maxScoreRange = 500;

	public List<ApplyRegularView> getApplyList() {
		if (applyList == null) {
			applyList = steService.getApplyRegularView();
			int maxTemp = 0;
			for (ApplyRegularView view : applyList) {
				Department curDepart = steService.getDepartment(view.getSte());
				view.getSte().setDepartmentName(curDepart.getName());

				int evaluationScore = steService.calculateEvaluationScore(view.getSte());
				int trainScore = steService.calculateTrainScore(view.getSte());
				int worktimeScore = steService.calculateWorkTimeScore(view.getSte());
				int totalScore = evaluationScore + trainScore + worktimeScore;
				view.setTotalScore(totalScore);
				if (totalScore > maxTemp) {
					maxTemp = totalScore;
				}
			}
			maxScoreRange = maxTemp;
			maxScore = maxTemp;
			constantApplyList = new ArrayList<ApplyRegularView>();
			constantApplyList.addAll(applyList);
		}

		return applyList;
	}

	public void setApplyList(List<ApplyRegularView> applyList) {
		this.applyList = applyList;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public ApplyRegularView getSelectedApply() {
		return selectedApply;
	}

	public void setSelectedApply(ApplyRegularView selectedApply) {
		this.selectedApply = selectedApply;
	}

	public void discard() {
		selectedApply = null;
	}

	public PieChartModel getWorkTimeModel() {
		return workTimeModel;
	}

	public void setWorkTimeModel(PieChartModel workTimeModel) {
		this.workTimeModel = workTimeModel;
	}

	public PieChartModel getEvaluationModel() {
		return evaluationModel;
	}

	public void setEvaluationModel(PieChartModel evaluationModel) {
		this.evaluationModel = evaluationModel;
	}

	public List<SteTrain> getTrainList() {
		return trainList;
	}

	public void setTrainList(List<SteTrain> trainList) {
		this.trainList = trainList;
	}

	private void showDetailPanel() {
		Date now = new Date();
		lateCount = workTimeService.getLateCount(selectedApply.getSte(), null, now);
		leaveEarlyCount = workTimeService.getLeaveEarlyCount(selectedApply.getSte(), null, now);
		int totalCount = workTimeService.getTotalSignCount(selectedApply.getSte(), null, now);
		onScheduleCount = totalCount - lateCount - leaveEarlyCount;

		String evaluationScoreStr = steService.getEvaluationScore(selectedApply.getSte().getId());
		workEfficiency = studyAbility = meetingParticipate = teamWork = 0;
		if (evaluationScoreStr != null) {
			String[] evaluationArray = evaluationScoreStr.split(" ");
			workEfficiency = Integer.parseInt(evaluationArray[0]);
			studyAbility = Integer.parseInt(evaluationArray[1]);
			meetingParticipate = Integer.parseInt(evaluationArray[2]);
			teamWork = Integer.parseInt(evaluationArray[3]);
		}
		trainList = trainService.getTrainListBySte(selectedApply.getSte().getId());
		curTrainScore = 0;
		for (SteTrain t : trainList) {
			curTrainScore += t.getScore();
		}

		curWorkTimeScore = (int) (100 * ((float) onScheduleCount / (float) totalCount)) + onScheduleCount;
		curEvaluationScore = workEfficiency + studyAbility + meetingParticipate + teamWork;
		curTotalScore = curTrainScore + curWorkTimeScore + curEvaluationScore;
	}

	public void onRowSelect(SelectEvent event) {
		selectedApply = (ApplyRegularView) event.getObject();
		showDetailPanel();
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("hrTurnRegular.jsf");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void passApplyTurn() {
		User hr = LoginSession.getCurrentUser();
		String status = selectedApply.getApplyRegular().getStatus();
		if (status.equals(Constants.APPLY_REGULAR_SUBMIT) == false) {
			MessageTip.showMessage(Constants.TIPID, "This apply has already been handled!");
			return;
		}
		// if have passed the train
		if (trainList == null || trainList.size() == 0) {
			MessageTip.showMessage(Constants.TIPID, "This short-term employee has not completed any train!");
			return;
		}

		steService.passApplyForTurnHR(selectedApply.getApplyRegular(), hr);
		MessageTip.showMessage(Constants.TIPID, "Pass successfully!");
	}

	public void refuseApplyTurn() {
		User hr = LoginSession.getCurrentUser();
		String status = selectedApply.getApplyRegular().getStatus();
		if (status.equals(Constants.APPLY_REGULAR_SUBMIT) == false) {
			MessageTip.showMessage(Constants.TIPID, "This apply has already been handled!");
			return;
		}
		steService.refuseApplyForTurnHR(selectedApply.getApplyRegular(), hr);
		MessageTip.showMessage(Constants.TIPID, "Refuse successfully!");
	}

	public List<Department> getDepartmentList() {
		if (departmentList == null) {
			departmentList = departService.getAllDepartment();
		}
		return departmentList;
	}

	public void setDepartmentList(List<Department> departmentList) {
		this.departmentList = departmentList;
	}

	public BarChartModel getBarModel() {
		return barModel;
	}

	public void setBarModel(BarChartModel barModel) {
		this.barModel = barModel;
	}

	public BarChartModel getLeaderBarModel() {
		return leaderBarModel;
	}

	public void setLeaderBarModel(BarChartModel leaderBarModel) {
		this.leaderBarModel = leaderBarModel;
	}

	public int getWorkEfficiency() {
		return workEfficiency;
	}

	public void setWorkEfficiency(int workEfficiency) {
		this.workEfficiency = workEfficiency;
	}

	public int getStudyAbility() {
		return studyAbility;
	}

	public void setStudyAbility(int studyAbility) {
		this.studyAbility = studyAbility;
	}

	public int getMeetingParticipate() {
		return meetingParticipate;
	}

	public void setMeetingParticipate(int meetingParticipate) {
		this.meetingParticipate = meetingParticipate;
	}

	public int getTeamWork() {
		return teamWork;
	}

	public void setTeamWork(int teamWork) {
		this.teamWork = teamWork;
	}

	public int getLateCount() {
		return lateCount;
	}

	public void setLateCount(int lateCount) {
		this.lateCount = lateCount;
	}

	public int getLeaveEarlyCount() {
		return leaveEarlyCount;
	}

	public void setLeaveEarlyCount(int leaveEarlyCount) {
		this.leaveEarlyCount = leaveEarlyCount;
	}

	public int getOnScheduleCount() {
		return onScheduleCount;
	}

	public void setOnScheduleCount(int onScheduleCount) {
		this.onScheduleCount = onScheduleCount;
	}

	public int getCurTrainScore() {
		return curTrainScore;
	}

	public void setCurTrainScore(int curTrainScore) {
		this.curTrainScore = curTrainScore;
	}

	public int getCurWorkTimeScore() {
		return curWorkTimeScore;
	}

	public void setCurWorkTimeScore(int curWorkTimeScore) {
		this.curWorkTimeScore = curWorkTimeScore;
	}

	public int getCurEvaluationScore() {
		return curEvaluationScore;
	}

	public void setCurEvaluationScore(int curEvaluationScore) {
		this.curEvaluationScore = curEvaluationScore;
	}

	public int getCurTotalScore() {
		return curTotalScore;
	}

	public void setCurTotalScore(int curTotalScore) {
		this.curTotalScore = curTotalScore;
	}

	public int getReferenceScore() {
		referenceScore = departService.getReferenceScore();
		return referenceScore;
	}

	public void setReferenceScore(int referenceScore) {
		this.referenceScore = referenceScore;
	}

	public List<ApplyRegularView> getFilteredApply() {
		return filteredApply;
	}

	public void setFilteredApply(List<ApplyRegularView> filteredApply) {
		this.filteredApply = filteredApply;
	}

	public int getMinScore() {
		return minScore;
	}

	public void setMinScore(int minScore) {
		this.minScore = minScore;
	}

	public int getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}

	public void onSlideEnd(SlideEndEvent event) {
		List<ApplyRegularView> filteredResult = new ArrayList<ApplyRegularView>();
		for (ApplyRegularView item : constantApplyList) {
			if (item.getTotalScore() >= minScore && item.getTotalScore() <= maxScore) {
				filteredResult.add(item);
			}
		}
		applyList = filteredResult;
	}

	public int getMaxScoreRange() {
		return maxScoreRange;
	}

	public void setMaxScoreRange(int maxScoreRange) {
		this.maxScoreRange = maxScoreRange;
	}

}
