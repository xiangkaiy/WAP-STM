package com.stm.shorttermemployee.pojo;

import java.io.Serializable;

public class Department implements Serializable {
	private static final long serialVersionUID = -867175819617139598L;
	private Long id;
	private String name;
	private Integer maxstenumber;
	private Integer usedstenumber;
	private Integer priority;
	private Long leaderid;
	private String introduction;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMaxstenumber() {
		return maxstenumber;
	}

	public void setMaxstenumber(Integer maxstenumber) {
		this.maxstenumber = maxstenumber;
	}

	public Integer getUsedstenumber() {
		return usedstenumber;
	}

	public void setUsedstenumber(Integer usedstenumber) {
		this.usedstenumber = usedstenumber;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getLeaderid() {
		return leaderid;
	}

	public void setLeaderid(Long leaderid) {
		this.leaderid = leaderid;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

}
