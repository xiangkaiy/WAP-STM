package com.stm.shorttermemployee.controller;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.stm.shorttermemployee.pojo.STEWorktime;
import com.stm.shorttermemployee.pojo.User;
import com.stm.shorttermemployee.pojo.UserSTE;
import com.stm.shorttermemployee.service.UserSTEService;
import com.stm.shorttermemployee.service.WorkTimeService;
import com.stm.shorttermemployee.util.Constants;
import com.stm.shorttermemployee.util.LoginSession;
import com.stm.shorttermemployee.util.MessageTip;

//@ManagedBean
//@SessionScoped
//@Component
@ManagedBean
@Scope("session")
@Controller
public class SteWorkTimeController implements Serializable {
	private static final long serialVersionUID = -6099438384636580328L;
	private ScheduleModel worktimeModel = null;

	@Autowired
	private WorkTimeService worktimeService;

	@Autowired
	private UserSTEService steService;

	private ScheduleEvent event = new DefaultScheduleEvent();

	private User user;
	private String startTime;
	private String endTime;

	public void refreshModel() {
		user = LoginSession.getCurrentUser();
		worktimeModel = new DefaultScheduleModel();
		List<STEWorktime> list = worktimeService.getWorkTimeList(user);
		for (STEWorktime time : list) {
			worktimeModel.addEvent(new DefaultScheduleEvent(Constants.SCHEDULE_WORK, time.getStarttime(), time.getEndtime()));
		}
	}

	public ScheduleModel getWorktimeModel() {
		if (worktimeModel == null) {
			refreshModel();
		}

		return worktimeModel;
	}

	public void setWorktimeModel(ScheduleModel worktimeModel) {
		this.worktimeModel = worktimeModel;
	}

	public void signIn() {
		User curUser = LoginSession.getCurrentUser();
		UserSTE curSte = steService.getUserSTE(curUser.getId());
		if (curSte.getStatus().equals(Constants.STE_WORK) == false) {
			MessageTip.showMessage(Constants.TIPID, "Your status is not at working, you can't sign in or sign out!");
			return;
		}
		if (worktimeService.haveSignIn(curUser, new Date())) {
			MessageTip.showMessage(Constants.TIPID, "You have already signed in!");
		} else {
			Date today = new Date();
			Timestamp time = new Timestamp(today.getTime());
			worktimeService.signIn(today, time, LoginSession.getCurrentUser().getId());
			MessageTip.showMessage(Constants.TIPID, "Sign in successfully!");
		}
	}

	public void signOut() {
		User curUser = LoginSession.getCurrentUser();
		UserSTE curSte = steService.getUserSTE(curUser.getId());
		if (curSte.getStatus().equals(Constants.STE_WORK) == false) {
			MessageTip.showMessage(Constants.TIPID, "Your status is not at working, you can't sign in or sign out!");
			return;
		}
		if (worktimeService.haveSignIn(curUser, new Date()) == false) {
			MessageTip.showMessage(Constants.TIPID, "You should sign in first!");
		} else {
			Date today = new Date();
			Timestamp time = new Timestamp(today.getTime());
			worktimeService.signOut(today, time, curUser.getId());
			refreshModel();
			MessageTip.showMessage(Constants.TIPID, "Sign out successfully!");
		}
	}

	public void onOneSelect(SelectEvent selectEvent) {
		event = (ScheduleEvent) selectEvent.getObject();
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public String getStartTime() {
		Date stime = worktimeService.getStartTime();
		Calendar c = Calendar.getInstance();
		c.setTime(stime);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minutes = c.get(Calendar.MINUTE);
		String minutesStr = minutes < 10 ? "0" + String.valueOf(minutes) : String.valueOf(minutes);
		startTime = String.valueOf(hour) + ":" + minutesStr;
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		Date etime = worktimeService.getEndTime();
		Calendar c = Calendar.getInstance();
		c.setTime(etime);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minutes = c.get(Calendar.MINUTE);
		String minutesStr = minutes < 10 ? "0" + String.valueOf(minutes) : String.valueOf(minutes);
		endTime = String.valueOf(hour) + ":" + minutesStr;
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
