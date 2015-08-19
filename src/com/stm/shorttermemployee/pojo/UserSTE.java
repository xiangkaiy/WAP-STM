package com.stm.shorttermemployee.pojo;

import java.io.Serializable;
import java.util.Date;

public class UserSTE implements Serializable {
	private static final long serialVersionUID = 1506293517016309389L;
	private Long id;
	private Long leasthoursweek;
	private Date joindate;
	private String expectedsalary;
	private Integer status;
	private String officeskill;
	private String programlanguage;
	private Float currentsalary;

	//for view
	private String statusStr;
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLeasthoursweek() {
		return leasthoursweek;
	}

	public void setLeasthoursweek(Long leasthoursweek) {
		this.leasthoursweek = leasthoursweek;
	}

	public Date getJoindate() {
		return joindate;
	}

	public void setJoindate(Date joindate) {
		this.joindate = joindate;
	}

	public String getExpectedsalary() {
		return expectedsalary;
	}

	public void setExpectedsalary(String expectedsalary) {
		this.expectedsalary = expectedsalary;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Float getCurrentsalary() {
		return currentsalary;
	}

	public void setCurrentsalary(Float currentsalary) {
		this.currentsalary = currentsalary;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
