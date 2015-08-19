package com.stm.shorttermemployee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

public class ApplyRegular implements Serializable {
	private static final long serialVersionUID = -1237672667913293244L;
	private Long id;
	private Long steid;
	private Timestamp applytime;
	private String status;
	private Long leaderid;
	private Long bossid;
	private Long hrid;
	private String intro;

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

	public Long getLeaderid() {
		return leaderid;
	}

	public void setLeaderid(Long leaderid) {
		this.leaderid = leaderid;
	}

	public Long getBossid() {
		return bossid;
	}

	public void setBossid(Long bossid) {
		this.bossid = bossid;
	}

	public Long getHrid() {
		return hrid;
	}

	public void setHrid(Long hrid) {
		this.hrid = hrid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

}
