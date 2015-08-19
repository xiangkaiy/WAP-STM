package com.stm.shorttermemployee.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.stm.shorttermemployee.model.QuestionView;
import com.stm.shorttermemployee.pojo.Paper;
import com.stm.shorttermemployee.pojo.SteTrain;
import com.stm.shorttermemployee.pojo.Train;
import com.stm.shorttermemployee.pojo.User;
import com.stm.shorttermemployee.service.TrainService;
import com.stm.shorttermemployee.util.CalculateEngine;
import com.stm.shorttermemployee.util.Constants;
import com.stm.shorttermemployee.util.LoginSession;
import com.stm.shorttermemployee.util.MessageTip;

@ManagedBean
@Scope("session")
@Controller
public class SteTrainController implements Serializable {
	private static final long serialVersionUID = -1253198336619348180L;
	private List<Train> allTrains = null;
	private Train[] selectedTrains = null;
	private Train[] deletedTrains = null;
	private List<Train> choosedTrains = null;
	private Train selectedDoingTrain;
	private List<QuestionView> questionList = new ArrayList<QuestionView>();
	private int selectTrainScore = 0;
	private String correctAnswers;
	private String trainResult;
	// private List<List<Train>> allKindsTrain = null;
	private List<Train> finishedTrain = null;
	private List<Train> unfinishedTrain = null;

	@Autowired
	private TrainService trainService;

	private void init() {
		User cur = LoginSession.getCurrentUser();
		choosedTrains = trainService.getChoosedTrains(cur);
	}

	public List<Train> getAllTrains() {
		if (allTrains == null) {
			init();
		}
		return allTrains;
	}

	public void setAllTrains(List<Train> allTrains) {
		this.allTrains = allTrains;
	}

	public Train[] getSelectedTrains() {
		return selectedTrains;
	}

	public void setSelectedTrains(Train[] selectedTrains) {
		this.selectedTrains = selectedTrains;
	}

	public Train[] getDeletedTrains() {
		return deletedTrains;
	}

	public void setDeletedTrains(Train[] deletedTrains) {
		this.deletedTrains = deletedTrains;
	}

	private void updateChoosedTrains() {
		User cur = LoginSession.getCurrentUser();
		choosedTrains = trainService.getTrainListByDepart(cur.getDepartmentid());
		unfinishedTrain = new ArrayList<Train>();
		finishedTrain = new ArrayList<Train>();

		if (choosedTrains != null) {
			for (Train t : choosedTrains) {
				SteTrain stetrain = trainService.getSteTrain(t.getId(), cur.getId());
				if (stetrain == null) {
					t.setStatus(Constants.TRAIN_UNFINISHED);

					unfinishedTrain.add(t);
				} else {
					String status = stetrain.getStatus();
					t.setStatus(status);
					long curScore = trainService.getScoreBySteAndTrain(cur.getId(), t);
					t.setScore(curScore);
					finishedTrain.add(t);
				}
			}
		}
	}

	public List<Train> getChoosedTrains() {
		// updateChoosedTrains();
		return choosedTrains;
	}

	public void setChoosedTrains(List<Train> choosedTrains) {
		this.choosedTrains = choosedTrains;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void submitSelectedTrains() {
		if (selectedTrains.length == 0) {
			MessageTip.showMessage(Constants.TIPID, "You should choose one train at least!");
			return;
		} else {
			List<Train> trainList = Arrays.asList(selectedTrains);
			trainService.selectTrains(LoginSession.getCurrentUser(), trainList);
			MessageTip.showMessage(Constants.TIPID, "Submit successfully!");
			init();
		}

	}

	public void dropTrains() {
		if (deletedTrains.length == 0) {
			MessageTip.showMessage(Constants.TIPID, "You should choose one train at least!");
			return;
		} else {
			List<Train> trainList = Arrays.asList(deletedTrains);
			trainService.dropTrains(LoginSession.getCurrentUser(), trainList);
			MessageTip.showMessage(Constants.TIPID, "Drop successfully!");
			init();
		}

	}

	public Train getSelectedDoingTrain() {
		return selectedDoingTrain;
	}

	public void setSelectedDoingTrain(Train selectedDoingTrain) {
		this.selectedDoingTrain = selectedDoingTrain;
	}

	public void onRowSelect(SelectEvent event) {
		Train trainDoing = (Train) event.getObject();
		Paper paperDoing = trainService.getPaperByTrain(trainDoing);
		String[] questionArr = paperDoing.getQuestion().split(Constants.SPLIT_QUESTION);
		String[] optionsArr = paperDoing.getOptions().split(Constants.SPLIT_BIGOPTION);

		questionList.clear();
		for (int i = 0; i < questionArr.length; i++) {
			QuestionView t = new QuestionView();
			t.setQuestionIndex(i + 1);
			t.setQuestion(questionArr[i]);
			String[] smallOptions = (optionsArr[i]).split("\\" + Constants.SPLIT_SMALLOPTION);
			t.setOption1(smallOptions[0]);
			t.setOption2(smallOptions[1]);
			t.setOption3(smallOptions[2]);
			t.setOption4(smallOptions[3]);
			questionList.add(t);
		}

		Long steId = LoginSession.getCurrentUser().getId();
		SteTrain selectedSteTrain = trainService.getSteTrain(trainDoing.getId(), steId);
		if (selectedSteTrain != null) {
			if (selectedSteTrain.getStatus().equals(Constants.TRAIN_PASSED) || selectedSteTrain.getStatus().equals(Constants.TRAIN_FAILED)) {
				String[] answers = selectedSteTrain.getAnswer().split(Constants.ANSWER_BIG_SPLIT);
				for (int i = 0; i < questionList.size(); i++) {
					String[] smallAnswers = answers[i].split(Constants.ANSWER_SMALL_SPLIT);
					questionList.get(i).setAnswer(smallAnswers);
				}
				correctAnswers = paperDoing.getAnswers();
				correctAnswers = CalculateEngine.mapAnswerToAlpha(correctAnswers);
				correctAnswers = correctAnswers.replace(Constants.ANSWER_BIG_SPLIT, ",");

				List<String> correctAnswerList = Arrays.asList(paperDoing.getAnswers().split(Constants.ANSWER_BIG_SPLIT));
				List<String> answerList = Arrays.asList(selectedSteTrain.getAnswer().split(Constants.ANSWER_BIG_SPLIT));
				selectTrainScore = CalculateEngine.getTrainScore(correctAnswerList, answerList);

				trainResult = selectedSteTrain.getStatus();
			}
		}

	}

	public List<QuestionView> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<QuestionView> questionList) {
		this.questionList = questionList;
	}

	public void submitPaperAnswer() {
		if (selectedDoingTrain != null) {
			List<String> answers = new ArrayList<String>();
			for (QuestionView view : questionList) {
				if (view.getAnswer() == null || view.getAnswer().length == 0) {
					MessageTip.showMessage(Constants.TIPID, "The answer for question:" + view.getQuestion() + " should not be empty!");
					return;
				} else {
					String smallAnswer = "";
					for (String a : view.getAnswer()) {
						smallAnswer += (a + Constants.ANSWER_SMALL_SPLIT);
					}
					smallAnswer = smallAnswer.substring(0, smallAnswer.length() - 1);
					answers.add(smallAnswer);
				}
			}
			// submit answers
			List<String> correctAnswer = Arrays.asList(trainService.getPaperByTrain(selectedDoingTrain).getAnswers().split(Constants.ANSWER_BIG_SPLIT));

			int score = CalculateEngine.getTrainScore(correctAnswer, answers);
			String status = trainService.submitAnswer(LoginSession.getCurrentUser(), selectedDoingTrain, answers, score);
			MessageTip.showMessage(Constants.TIPID, "Submit successfully!");
			updateChoosedTrains();
			selectedDoingTrain.setStatus(status);
			selectTrainScore = score;
			trainResult = status;
			Paper paperDoing = trainService.getPaperByTrain(selectedDoingTrain);
			correctAnswers = paperDoing.getAnswers();
			correctAnswers = CalculateEngine.mapAnswerToAlpha(correctAnswers);
			correctAnswers = correctAnswers.replace(Constants.ANSWER_BIG_SPLIT, ",");
		}
	}

	public int getSelectTrainScore() {
		return selectTrainScore;
	}

	public void setSelectTrainScore(int selectTrainScore) {
		this.selectTrainScore = selectTrainScore;
	}

	public String getCorrectAnswers() {
		return correctAnswers;
	}

	public void setCorrectAnswers(String correctAnswers) {
		this.correctAnswers = correctAnswers;
	}

	public String getTrainResult() {
		return trainResult;
	}

	public void setTrainResult(String trainResult) {
		this.trainResult = trainResult;
	}

	public List<Train> getFinishedTrain() {
		if (finishedTrain == null) {
			updateChoosedTrains();
		}
		return finishedTrain;
	}

	public void setFinishedTrain(List<Train> finishedTrain) {
		this.finishedTrain = finishedTrain;
	}

	public List<Train> getUnfinishedTrain() {
		if (unfinishedTrain == null) {
			updateChoosedTrains();
		}
		return unfinishedTrain;
	}

	public void setUnfinishedTrain(List<Train> unfinishedTrain) {
		this.unfinishedTrain = unfinishedTrain;
	}

}
