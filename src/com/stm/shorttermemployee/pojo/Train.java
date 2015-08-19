package com.stm.shorttermemployee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

public class Train implements Serializable {
	private static final long serialVersionUID = -7012288003483727755L;
	private Long id;
	private Long hrid;
	private String name;
	private Timestamp starttime;
	private Timestamp endtime;
	private Long paperid;
	private Long departid;
	private String required;

	//for view
	private String status;
	private Long score;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Long getPaperid() {
		return paperid;
	}

	public void setPaperid(Long paperid) {
		this.paperid = paperid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getDepartid() {
		return departid;
	}

	public void setDepartid(Long departid) {
		this.departid = departid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

}
