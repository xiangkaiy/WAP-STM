package com.stm.shorttermemployee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

public class LeaderEvaluation implements Serializable {
	private static final long serialVersionUID = -1065178729205275711L;
	private Long id;
	private Long leaderid;
	private Long steid;
	private Timestamp givetime;
	private String evaluationscore;
	private String cmt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLeaderid() {
		return leaderid;
	}

	public void setLeaderid(Long leaderid) {
		this.leaderid = leaderid;
	}

	public Long getSteid() {
		return steid;
	}

	public void setSteid(Long steid) {
		this.steid = steid;
	}

	public Timestamp getGivetime() {
		return givetime;
	}

	public void setGivetime(Timestamp givetime) {
		this.givetime = givetime;
	}

	public String getEvaluationscore() {
		return evaluationscore;
	}

	public void setEvaluationscore(String evaluationscore) {
		this.evaluationscore = evaluationscore;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCmt() {
		return cmt;
	}

	public void setCmt(String cmt) {
		this.cmt = cmt;
	}

}
