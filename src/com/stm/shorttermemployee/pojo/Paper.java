package com.stm.shorttermemployee.pojo;

import java.io.Serializable;

public class Paper implements Serializable {
	private static final long serialVersionUID = 9198265294064669114L;
	private Long id;
	private String question;
	private String options;
	private String answers;
	private String name;

	// for view
	private String questionView;
	private String optionView;
	private String answerView;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getAnswers() {
		return answers;
	}

	public void setAnswers(String answers) {
		this.answers = answers;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuestionView() {
		return questionView;
	}

	public void setQuestionView(String questionView) {
		this.questionView = questionView;
	}

	public String getOptionView() {
		return optionView;
	}

	public void setOptionView(String optionView) {
		this.optionView = optionView;
	}

	public String getAnswerView() {
		return answerView;
	}

	public void setAnswerView(String answerView) {
		this.answerView = answerView;
	}

}
