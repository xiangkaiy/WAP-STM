package com.stm.shorttermemployee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class Payment implements Serializable {
	private static final long serialVersionUID = 5508147788485211881L;
	private Long id;
	private Long hrid;
	private Timestamp givetime;
	private Date startdate;
	private Date enddate;
	private Float money;
	private Long totaltime;
	private Long steid;
	private String fine;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getHrid() {
		return hrid;
	}

	public void setHrid(Long hrid) {
		this.hrid = hrid;
	}

	public Timestamp getGivetime() {
		return givetime;
	}

	public void setGivetime(Timestamp givetime) {
		this.givetime = givetime;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public Float getMoney() {
		return money;
	}

	public void setMoney(Float money) {
		this.money = money;
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

	public String getFine() {
		return fine;
	}

	public void setFine(String fine) {
		this.fine = fine;
	}

}
