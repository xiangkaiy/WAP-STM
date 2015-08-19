package com.stm.shorttermemployee.controller;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.stm.shorttermemployee.pojo.SteLeave;
import com.stm.shorttermemployee.pojo.User;
import com.stm.shorttermemployee.pojo.UserSTE;
import com.stm.shorttermemployee.service.UserSTEService;
import com.stm.shorttermemployee.util.CalculateEngine;
import com.stm.shorttermemployee.util.Constants;
import com.stm.shorttermemployee.util.LoginSession;
import com.stm.shorttermemployee.util.MessageTip;

@ManagedBean
@Scope("session")
@Controller
public class SteAskForLeaveController implements Serializable {
	private static final long serialVersionUID = -4495252399630799538L;
	private SteLeave leaveApply = new SteLeave();
	private Date starttime;
	private Date endtime;

	private List<SteLeave> applyList;

	@Autowired
	private UserSTEService steService;

	public SteLeave getLeaveApply() {
		return leaveApply;
	}

	public void setLeaveApply(SteLeave leaveApply) {
		this.leaveApply = leaveApply;
	}

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public void submitLeaveApply() {
		User curUser = LoginSession.getCurrentUser();
		UserSTE curSte = steService.getUserSTE(curUser.getId());
		if (curSte.getStatus().equals(Constants.STE_WORK) == false) {
			MessageTip.showMessage(Constants.TIPID, "You can't ask for leave because you aren't at working!");
			return;
		}

		Date now = new Date();
		leaveApply.setStarttime(new Timestamp(starttime.getTime()));
		leaveApply.setEndtime(new Timestamp(endtime.getTime()));
		leaveApply.setApplytime(new Timestamp(now.getTime()));
		leaveApply.setSteid(curUser.getId());
		leaveApply.setStatus(Constants.ASK_FOR_LEAVE_SUBMIT);

		List<SteLeave> allPassedLeaveList = steService.getAllPassedLeave(curUser.getId());
		SteLeave overlap = CalculateEngine.checkAskForLeaveCross(leaveApply, allPassedLeaveList);
		if (overlap != null) {
			MessageTip.showMessage(Constants.TIPID, "The ask for leave time has been overlapped with a passed apply!");
			return;
		}

		steService.applyForLeave(leaveApply);
		MessageTip.showMessage(Constants.TIPID, "Submit successfully!");
	}

	public List<SteLeave> getApplyList() {
		Long steid = LoginSession.getCurrentUser().getId();
		applyList = steService.getApplyLeaveBySTE(steid);
		return applyList;
	}

	public void setApplyList(List<SteLeave> applyList) {
		this.applyList = applyList;
	}

	// public void valueChangeEvent(ValueChangeEvent e) {
	//
	// }
}
