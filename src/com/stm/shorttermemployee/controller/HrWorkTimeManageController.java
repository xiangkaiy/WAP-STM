package com.stm.shorttermemployee.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.stm.shorttermemployee.model.WorkTimeListModel;
import com.stm.shorttermemployee.model.WorkTimeView;
import com.stm.shorttermemployee.pojo.Property;
import com.stm.shorttermemployee.service.DepartmentService;
import com.stm.shorttermemployee.service.PaymentService;
import com.stm.shorttermemployee.service.WorkTimeService;
import com.stm.shorttermemployee.util.Constants;
import com.stm.shorttermemployee.util.MessageTip;

@ManagedBean
@Scope("session")
@Controller
public class HrWorkTimeManageController implements Serializable {
	private static final long serialVersionUID = -3160560788139172325L;
	private Date startTime;
	private Date endTime;
	private TimeZone timeZone;
	private List<WorkTimeView> baseWorktimeList = null;
	private List<WorkTimeView> filteredWorktime = null;

	private WorkTimeListModel worktimeList = null;
	private WorkTimeView selectedWorktime = null;
	private int first;
	private Property fineUnit;

	@Autowired
	private WorkTimeService worktimeService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private DepartmentService departService;

	private String referenceScore;

	public Date getStartTime() {
		startTime = worktimeService.getStartTime();
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		endTime = worktimeService.getEndTime();
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public TimeZone getTimeZone() {
		timeZone = TimeZone.getDefault();
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public List<WorkTimeView> getBaseWorktimeList() {
		return baseWorktimeList;
	}

	public void setBaseWorktimeList(List<WorkTimeView> baseWorktimeList) {
		this.baseWorktimeList = baseWorktimeList;
	}

	public WorkTimeListModel getWorktimeList() {
		if (baseWorktimeList == null) {
			baseWorktimeList = worktimeService.getAllWorkTime();
			worktimeList = new WorkTimeListModel(baseWorktimeList);
		}
		return worktimeList;
	}

	public void setWorktimeList(WorkTimeListModel worktimeList) {
		this.worktimeList = worktimeList;
	}

	public WorkTimeView getSelectedWorktime() {
		return selectedWorktime;
	}

	public void setSelectedWorktime(WorkTimeView selectedWorktime) {
		this.selectedWorktime = selectedWorktime;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public void modifyProperty() {
		worktimeService.modifyWorkTime(startTime, endTime);
		paymentService.modifyFineUnit(fineUnit);
		departService.modifyReferenceScore(referenceScore);
		MessageTip.showMessage(Constants.TIPID, "Modify Successfully!");
	}

	public List<WorkTimeView> getFilteredWorktime() {
		return filteredWorktime;
	}

	public void setFilteredWorktime(List<WorkTimeView> filteredWorktime) {
		this.filteredWorktime = filteredWorktime;
	}

	public Property getFineUnit() {
		fineUnit = paymentService.getFineUnit();
		return fineUnit;
	}

	public void setFineUnit(Property fineUnit) {
		this.fineUnit = fineUnit;
	}

	public String getReferenceScore() {
		referenceScore = String.valueOf(departService.getReferenceScore());
		return referenceScore;
	}

	public void setReferenceScore(String referenceScore) {
		this.referenceScore = referenceScore;
	}

}
