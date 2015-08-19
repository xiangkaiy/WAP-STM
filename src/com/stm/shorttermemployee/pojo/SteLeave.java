package com.stm.shorttermemployee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

public class SteLeave implements Serializable {
	private static final long serialVersionUID = -3572104958246787263L;
	private Long id;
	private Long steid;
	private Timestamp starttime;
	private Timestamp endtime;
	private String reason;
	private Timestamp applytime;
	private String status;
	private String leadercomment;
	private Long leaderid;

	// for view
	private String stename;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSteid() {
		return steid;
	}

	public void setSteid(Long steid) {
		this.steid = steid;
	}

	public Timestamp getStarttime() {
		return starttime;
	}

	public void setStarttime(Timestamp starttime) {
		this.starttime = starttime;
	}

	public Timestamp getEndtime() {
		return endtime;
	}

	public void setEndtime(Timestamp endtime) {
		this.endtime = endtime;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Timestamp getApplytime() {
		return applytime;
	}

	public void setApplytime(Timestamp applytime) {
		this.applytime = applytime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getLeadercomment() {
		return leadercomment;
	}

	public void setLeadercomment(String leadercomment) {
		this.leadercomment = leadercomment;
	}

	public Long getLeaderid() {
		return leaderid;
	}

	public void setLeaderid(Long leaderid) {
		this.leaderid = leaderid;
	}

	public String getStename() {
		return stename;
	}

	public void setStename(String stename) {
		this.stename = stename;
	}

}
