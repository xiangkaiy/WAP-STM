package com.stm.shorttermemployee.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.stm.shorttermemployee.pojo.Payment;
import com.stm.shorttermemployee.pojo.User;
import com.stm.shorttermemployee.service.PaymentService;
import com.stm.shorttermemployee.service.WorkTimeService;
import com.stm.shorttermemployee.util.LoginSession;

@ManagedBean
@Scope("session")
@Controller
public class SteSalaryController implements Serializable {
	private static final long serialVersionUID = -608515190102867242L;
	private List<Payment> paylist = null;
	@Autowired
	private PaymentService paymentService;

	@Autowired
	private WorkTimeService workTimeService;

	private int lateCount;
	private int leaveEarlyCount;
	private int onScheduleCount;

	private User curUser = null;

	public List<Payment> getPaylist() {
		if (paylist == null) {
			paylist = paymentService.getPaymentList(LoginSession.getCurrentUser());
		}
		return paylist;
	}

	public void setPaylist(List<Payment> paylist) {
		this.paylist = paylist;
	}

	public int getLateCount() {

		Date now = new Date();
		lateCount = workTimeService.getLateCount(curUser, null, now);
		return lateCount;
	}

	public void setLateCount(int lateCount) {
		this.lateCount = lateCount;
	}

	public int getLeaveEarlyCount() {
		Date now = new Date();
		leaveEarlyCount = workTimeService.getLeaveEarlyCount(curUser, null, now);
		return leaveEarlyCount;
	}

	public void setLeaveEarlyCount(int leaveEarlyCount) {
		this.leaveEarlyCount = leaveEarlyCount;
	}

	public int getOnScheduleCount() {
		Date now = new Date();
		onScheduleCount = workTimeService.getTotalSignCount(curUser, null, now) - lateCount - leaveEarlyCount;
		return onScheduleCount;
	}

	public void setOnScheduleCount(int onScheduleCount) {
		this.onScheduleCount = onScheduleCount;
	}

	public SteSalaryController() {
		if (curUser == null) {
			curUser = LoginSession.getCurrentUser();
		}

	}

}
