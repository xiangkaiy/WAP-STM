package com.stm.shorttermemployee.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.stm.shorttermemployee.model.PaperListModel;
import com.stm.shorttermemployee.model.QuestionView;
import com.stm.shorttermemployee.pojo.Paper;
import com.stm.shorttermemployee.service.TrainService;
import com.stm.shorttermemployee.util.Constants;
import com.stm.shorttermemployee.util.MessageTip;

@ManagedBean
@Scope("session")
@Controller
public class HrPaperManageController implements Serializable {
	private static final long serialVersionUID = -6884680818541286629L;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TrainService trainService;

	private List<Paper> baseQuestionList = null;
	private PaperListModel questionList = null;
	private int first;
	private Paper selectedPaper = null;
	private boolean editMode = false;
	private boolean addMode = false;
	private Paper editingQuestion = null;
	private List<QuestionView> selectedPaperView = null;
	private String dialogHeader;

	private void refreshQuestionList() {
		baseQuestionList = trainService.getPaperList();
		for (Paper p : baseQuestionList) {
			String[] questionArr = p.getQuestion().split(Constants.SPLIT_QUESTION);
			String questionView = "";
			for (Integer i = 0; i < questionArr.length; i++) {
				questionView += (i.toString() + ". " + questionArr[i]);
			}
			p.setQuestionView(questionView);
		}

		questionList = new PaperListModel(baseQuestionList);
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public Paper getSelectedPaper() {
		return selectedPaper;
	}

	public void setSelectedPaper(Paper selectedPaper) {
		this.selectedPaper = selectedPaper;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public boolean isAddMode() {
		return addMode;
	}

	public void setAddMode(boolean addMode) {
		this.addMode = addMode;
	}

	public Paper getEditingQuestion() {
		return editingQuestion;
	}

	public void setEditingQuestion(Paper editingQuestion) {
		this.editingQuestion = editingQuestion;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void startAdd() {
		dialogHeader = "New Paper";
		selectedPaperView = new ArrayList<QuestionView>();
		selectedPaper = new Paper();
		try {
			initEditField();
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		addMode = true;
	}

	public void startEdit() throws IOException {
		initEditField();
		editMode = true;
	}

	private void initEditField() throws IOException {
		if (selectedPaper != null) {
			editingQuestion = selectedPaper;
		} else {
			editingQuestion = new Paper();
		}
	}

	public void discard() {
		selectedPaperView = null;
		editingQuestion = null;
		editMode = false;
		addMode = false;
	}

	public boolean isBrowseMode() {
		return !addMode && !editMode;
	}

	public void apply() {
		// data format
		String questionStr = "";
		String optionStr = "";
		String rightAnswerStr = "";

		if (selectedPaperView.size() == 0) {
			MessageTip.showMessage(Constants.TIPID, "You should at least add one question!");
			return;
		}
		for (QuestionView view : selectedPaperView) {
			if (view.getQuestion().trim().equals("")) {
				MessageTip.showMessage(Constants.TIPID, "Question name should not be empty!");
				return;
			}
			if (view.getOption1().trim().equals("")) {
				MessageTip.showMessage(Constants.TIPID, "Option1 of " + view.getQuestion() + " should not be empty!");
				return;
			}
			if (view.getOption2().trim().equals("")) {
				MessageTip.showMessage(Constants.TIPID, "Option2 of " + view.getQuestion() + " should not be empty!");
				return;
			}
			if (view.getOption3().trim().equals("")) {
				MessageTip.showMessage(Constants.TIPID, "Option3 of " + view.getQuestion() + " should not be empty!");
				return;
			}
			if (view.getOption4().trim().equals("")) {
				MessageTip.showMessage(Constants.TIPID, "Option4 of " + view.getQuestion() + " should not be empty!");
				return;
			}
			questionStr += (view.getQuestion() + Constants.SPLIT_QUESTION);
			String smallOption = view.getOption1() + Constants.SPLIT_SMALLOPTION + view.getOption2() + Constants.SPLIT_SMALLOPTION + view.getOption3()
					+ Constants.SPLIT_SMALLOPTION + view.getOption4();
			optionStr += (smallOption + Constants.SPLIT_BIGOPTION);

			String smallRightAnswer = "";
			if (view.getAnswer() == null || view.getAnswer().length == 0) {
				MessageTip.showMessage(Constants.TIPID, "Question's answer should not be empty!");
				return;
			}
			for (String op : view.getAnswer()) {
				smallRightAnswer += (op + Constants.ANSWER_SMALL_SPLIT);
			}
			smallRightAnswer = smallRightAnswer.substring(0, smallRightAnswer.length() - 1);
			rightAnswerStr += (smallRightAnswer + Constants.ANSWER_BIG_SPLIT);
		}

		questionStr = questionStr.substring(0, questionStr.length() - 1);
		optionStr = optionStr.substring(0, optionStr.length() - 1);
		rightAnswerStr = rightAnswerStr.substring(0, rightAnswerStr.length() - 1);

		if (addMode) {
			Paper question = new Paper();
			question.setName(selectedPaper.getName());
			question.setQuestion(questionStr);
			question.setOptions(optionStr);
			question.setAnswers(rightAnswerStr);
			trainService.addPaper(question);
			baseQuestionList.add(question);
			MessageTip.showMessage(Constants.TIPID, "Add successfully!");
		}
		if (editMode) {
			selectedPaper.setQuestion(questionStr);
			selectedPaper.setOptions(optionStr);
			selectedPaper.setAnswers(rightAnswerStr);
			trainService.modifyPaper(selectedPaper);
			MessageTip.showMessage(Constants.TIPID, "Modify successfully!");
		}

		selectedPaperView = null;
		editingQuestion = null;
		editMode = false;
		addMode = false;
		refreshQuestionList();
	}

	public List<Paper> getBaseQuestionList() {
		return baseQuestionList;
	}

	public void setBaseQuestionList(List<Paper> baseQuestionList) {
		this.baseQuestionList = baseQuestionList;
	}

	public PaperListModel getQuestionList() {
		if (baseQuestionList == null) {
			refreshQuestionList();
		}
		return questionList;
	}

	public void setQuestionList(PaperListModel questionList) {
		this.questionList = questionList;
	}

	public void deletePaper() {
		// make sure this selected user can be deleted.

	}

	public void reload() {
		innerDataClear();
	}

	private void innerDataClear() {
		baseQuestionList = null;
		selectedPaper = null;
		first = 0;
	}

	public void onRowSelect(SelectEvent event) {
		selectedPaper = (Paper) event.getObject();
		selectedPaperView = new ArrayList<QuestionView>();
		String[] questionArr = selectedPaper.getQuestion().split(Constants.SPLIT_QUESTION);
		String[] optionsArr = selectedPaper.getOptions().split(Constants.SPLIT_BIGOPTION);
		String[] answerArr = selectedPaper.getAnswers().split(Constants.ANSWER_BIG_SPLIT);

		for (int i = 0; i < questionArr.length; i++) {
			QuestionView t = new QuestionView();
			t.setQuestionIndex(i);
			t.setQuestion(questionArr[i]);
			String[] smallOptions = (optionsArr[i]).split("\\" + Constants.SPLIT_SMALLOPTION);
			t.setOption1(smallOptions[0]);
			t.setOption2(smallOptions[1]);
			t.setOption3(smallOptions[2]);
			t.setOption4(smallOptions[3]);
			String[] answerArray = answerArr[i].split(Constants.ANSWER_SMALL_SPLIT);
			t.setAnswer(answerArray);
			selectedPaperView.add(t);
		}
		editMode = true;
		dialogHeader = "Editing Paper";
	}

	public List<QuestionView> getSelectedPaperView() {
		return selectedPaperView;
	}

	public void setSelectedPaperView(List<QuestionView> selectedPaperView) {
		this.selectedPaperView = selectedPaperView;
	}

	public void addQuestion() {
		if (selectedPaperView == null) {
			selectedPaperView = new ArrayList<QuestionView>();
		}
		QuestionView nq = new QuestionView();
		nq.setQuestionIndex(selectedPaperView.size());
		selectedPaperView.add(nq);
	}

	public void deleteQuestion() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String index = params.get("questionIndex");
		if (index != null) {
			int realIndex = Integer.parseInt(index.trim());
			selectedPaperView.remove(realIndex);
			int newIndex = 0;
			for (QuestionView q : selectedPaperView) {
				q.setQuestionIndex(newIndex++);
			}
		}
	}

	public String getDialogHeader() {
		return dialogHeader;
	}

	public void setDialogHeader(String dialogHeader) {
		this.dialogHeader = dialogHeader;
	}
}
