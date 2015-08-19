package com.stm.shorttermemployee.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

public class SteTrain implements Serializable {
	private static final long serialVersionUID = -3475186934311373116L;
	private Long id;
	private Long steid;
	private Long trainid;
	private Long score;
	private Timestamp finishtime;
	private String status;
	private String answer;

	//for view
	private String trainName;

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

	public Long getTrainid() {
		return trainid;
	}

	public void setTrainid(Long trainid) {
		this.trainid = trainid;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public Timestamp getFinishtime() {
		return finishtime;
	}

	public void setFinishtime(Timestamp finishtime) {
		this.finishtime = finishtime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getTrainName() {
		return trainName;
	}

	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}

}
