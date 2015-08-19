package com.stm.shorttermemployee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class STEWorktime implements Serializable {
	private static final long serialVersionUID = -3060194852372707377L;
	private Long id;
	private Date signtime;
	private Timestamp starttime;
	private Timestamp endtime;
	private Long totaltime;
	private Long steid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getSigntime() {
		return signtime;
	}

	public void setSigntime(Date signtime) {
		this.signtime = signtime;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getTotaltime() {
		return totaltime;
	}

	public void setTotaltime(Long totaltime) {
		this.totaltime = totaltime;
	}

	public Long getSteid() {
		return steid;
	}

	public void setSteid(Long steid) {
		this.steid = steid;
	}

}
