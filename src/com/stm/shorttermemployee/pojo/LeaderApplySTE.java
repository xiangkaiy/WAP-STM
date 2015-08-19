package com.stm.shorttermemployee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class LeaderApplySTE implements Serializable {
	private static final long serialVersionUID = -3656773655242454522L;
	private Long id;
	private Long leaderid;
	private Long steamount;
	private String officeskill;
	private String programlanguage;
	private Timestamp applytime;
	private Date expectjoindate;
	private Integer expectworkhour;
	private Integer status;

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

	public Long getSteamount() {
		return steamount;
	}

	public void setSteamount(Long steamount) {
		this.steamount = steamount;
	}

	public String getOfficeskill() {
		return officeskill;
	}

	public void setOfficeskill(String officeskill) {
		this.officeskill = officeskill;
	}

	public String getProgramlanguage() {
		return programlanguage;
	}

	public void setProgramlanguage(String programlanguage) {
		this.programlanguage = programlanguage;
	}

	public Timestamp getApplytime() {
		return applytime;
	}

	public void setApplytime(Timestamp applytime) {
		this.applytime = applytime;
	}

	public Date getExpectjoindate() {
		return expectjoindate;
	}

	public void setExpectjoindate(Date expectjoindate) {
		this.expectjoindate = expectjoindate;
	}

	public Integer getExpectworkhour() {
		return expectworkhour;
	}

	public void setExpectworkhour(Integer expectworkhour) {
		this.expectworkhour = expectworkhour;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
