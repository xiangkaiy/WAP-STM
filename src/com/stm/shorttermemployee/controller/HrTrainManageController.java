package com.stm.shorttermemployee.controller;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.stm.shorttermemployee.model.QuestionView;
import com.stm.shorttermemployee.pojo.Department;
import com.stm.shorttermemployee.pojo.Paper;
import com.stm.shorttermemployee.pojo.Train;
import com.stm.shorttermemployee.pojo.User;
import com.stm.shorttermemployee.service.DepartmentService;
import com.stm.shorttermemployee.service.TrainService;
import com.stm.shorttermemployee.util.Constants;
import com.stm.shorttermemployee.util.LoginSession;
import com.stm.shorttermemployee.util.MessageTip;

@ManagedBean
@Scope("session")
@Controller
public class HrTrainManageController implements Serializable {
	private static final long serialVersionUID = -244707951005650080L;
	private ScheduleModel eventModel = null;;
	private ScheduleEvent event = new DefaultScheduleEvent();

	@Autowired
	private TrainService trainservice;

	@Autowired
	private DepartmentService departService;

	private QuestionView questionView;
	private List<Paper> paperList = new ArrayList<Paper>();
	private Long selectedPaperId = null;

	private List<Department> departmentList = null;
	private Long selectedDepartId = null;
	private boolean showDialog;
	private boolean required;

	public List<Department> getDepartmentList() {
		if (departmentList == null) {
			departmentList = departService.getAllDepartment();
		}
		return departmentList;
	}

	public void setDepartmentList(List<Department> departmentList) {
		this.departmentList = departmentList;
	}

	public Long getSelectedDepartId() {
		return selectedDepartId;
	}

	public void setSelectedDepartId(Long selectedDepartId) {
		this.selectedDepartId = selectedDepartId;
	}

	private void updateEventModel() {
		eventModel = new DefaultScheduleModel();
		List<Train> trainlist = trainservice.getTrainList();
		for (Train t : trainlist) {
			ScheduleEvent e = new DefaultScheduleEvent(t.getName(), t.getStarttime(), t.getEndtime(), t.getId());
			eventModel.addEvent(e);
		}
	}

	public ScheduleModel getEventModel() {
		if (eventModel == null) {
			updateEventModel();
		}
		return eventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public void onDateSelect(SelectEvent selectEvent) {
		event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
		showDialog = true;
	}

	public void onEventSelect(SelectEvent selectEvent) {
		event = (ScheduleEvent) selectEvent.getObject();
		Long trainId = (Long) event.getData();
		Train selectedTrain = trainservice.getTrain(trainId);
		selectedPaperId = selectedTrain.getPaperid();
		selectedDepartId = selectedTrain.getDepartid();
		showDialog = true;
		required = (selectedTrain.getRequired().equals("true") ? true : false);
	}

	public void onEventMove(ScheduleEntryMoveEvent event) {
		modifyTrain(event.getScheduleEvent());

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:"
				+ event.getMinuteDelta());

		addMessage(message);
	}

	public void onEventResize(ScheduleEntryResizeEvent event) {
		modifyTrain(event.getScheduleEvent());
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:"
				+ event.getMinuteDelta());

		addMessage(message);
	}

	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	private void modifyTrain(ScheduleEvent e) {
		Train newtrain = new Train();
		User user = LoginSession.getCurrentUser();
		newtrain.setId((Long) e.getData());
		newtrain.setHrid(user.getId());
		newtrain.setName(e.getTitle());
		newtrain.setStarttime(new Timestamp(e.getStartDate().getTime()));
		newtrain.setEndtime(new Timestamp(e.getEndDate().getTime()));
		newtrain.setPaperid(selectedPaperId);
		newtrain.setDepartid(selectedDepartId);
		trainservice.saveOrUpdateTrain(newtrain);
	}

	public void addEvent(ActionEvent actionEvent) {
		Train newtrain = new Train();
		User user = LoginSession.getCurrentUser();
		newtrain.setHrid(user.getId());
		newtrain.setName(event.getTitle());
		newtrain.setStarttime(new Timestamp(event.getStartDate().getTime()));
		newtrain.setEndtime(new Timestamp(event.getEndDate().getTime()));
		newtrain.setPaperid(selectedPaperId);
		newtrain.setDepartid(selectedDepartId);
		newtrain.setRequired(required ? "true" : "false");
		if (event.getId() == null) {
			trainservice.addOneTrain(newtrain);
			eventModel.addEvent(event);
		} else {
			eventModel.updateEvent(event);
			newtrain.setId((Long) event.getData());
			trainservice.updateTrain(newtrain);
		}

		MessageTip.showMessage(Constants.TIPID, "Save Successfully!");
		event = new DefaultScheduleEvent();
		selectedPaperId = selectedDepartId = null;
		updateEventModel();
	}

	public QuestionView getQuestionView() {
		questionView = new QuestionView();
		return questionView;
	}

	public void setQuestionView(QuestionView questionView) {
		this.questionView = questionView;
	}

	public List<Paper> getPaperList() {
		paperList = trainservice.getPaperList();
		return paperList;
	}

	public void setPaperList(List<Paper> paperList) {
		this.paperList = paperList;
	}

	public Long getSelectedPaperId() {
		return selectedPaperId;
	}

	public void setSelectedPaperId(Long selectedPaperId) {
		this.selectedPaperId = selectedPaperId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void discard() {
		setShowDialog(false);
		selectedPaperId = selectedDepartId = null;
	}

	public boolean isShowDialog() {
		return showDialog;
	}

	public void setShowDialog(boolean showDialog) {
		this.showDialog = showDialog;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

}
