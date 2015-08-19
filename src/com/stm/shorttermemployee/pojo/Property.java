package com.stm.shorttermemployee.pojo;

import java.io.Serializable;

public class Property implements Serializable {
	private static final long serialVersionUID = 4823224745991684548L;
	private Long id;
	private String name;
	private String value;

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
