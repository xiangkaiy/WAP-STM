package com.stm.shorttermemployee.pojo;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;

public class User implements Serializable {
	private static final long serialVersionUID = 4398248330766053141L;
	private Long id;
	private String username;
	private String pwd;
	private String email;
	private String gender;
	private Timestamp logintime;
	private String rolename;
	private Date birthday;
	private Date joinday;
	private String positionlevel;
	private String idno;
	private Long departmentid;

	// for view
	private String departmentName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Timestamp getLogintime() {
		return logintime;
	}

	public void setLogintime(Timestamp logintime) {
		this.logintime = logintime;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getJoinday() {
		return joinday;
	}

	public void setJoinday(Date joinday) {
		this.joinday = joinday;
	}

	public String getPositionlevel() {
		return positionlevel;
	}

	public void setPositionlevel(String positionlevel) {
		this.positionlevel = positionlevel;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public Long getDepartmentid() {
		return departmentid;
	}

	public void setDepartmentid(Long departmentid) {
		this.departmentid = departmentid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public User() {
		this.birthday = null;
		this.email = "";
		this.gender = "";
		this.id = 0L;
		this.joinday = null;
		this.logintime = null;
		this.positionlevel = "";
		this.pwd = "";
		this.rolename = "";
		this.username = "";
		this.idno = "";
		this.departmentid = -1L;
	}

	public User(User copy) {
		this.birthday = copy.birthday;
		this.email = copy.email;
		this.gender = copy.gender;
		this.id = copy.id;
		this.joinday = copy.joinday;
		this.logintime = copy.logintime;
		this.positionlevel = copy.positionlevel;
		this.pwd = copy.pwd;
		this.rolename = copy.rolename;
		this.username = copy.username;
		this.idno = copy.idno;
		this.departmentid = copy.departmentid;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

}
