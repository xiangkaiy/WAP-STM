package com.stm.shorttermemployee.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stm.shorttermemployee.dao.PaperDAO;
import com.stm.shorttermemployee.dao.QuestionDAO;
import com.stm.shorttermemployee.dao.SteTrainDAO;
import com.stm.shorttermemployee.dao.TrainDAO;
import com.stm.shorttermemployee.dao.UserSTEDAO;
import com.stm.shorttermemployee.pojo.Paper;
import com.stm.shorttermemployee.pojo.SteTrain;
import com.stm.shorttermemployee.pojo.Train;
import com.stm.shorttermemployee.pojo.User;
import com.stm.shorttermemployee.pojo.UserSTE;
import com.stm.shorttermemployee.util.Constants;

@Component
public class TrainService {
	@Autowired
	private TrainDAO traindao;

	@Autowired
	private QuestionDAO questiondao;

	@Autowired
	private SteTrainDAO stetraindao;

	@Autowired
	private PaperDAO paperdao;

	@Autowired
	private UserSTEDAO stedao;

	@Transactional
	public void addTrain(List<Train> trainlist) {
		if (trainlist != null) {
			for (Train t : trainlist) {
				traindao.saveOrUpdate(t);
			}
		}
	}

	@Transactional
	public Long addOneTrain(Train train) {
		return traindao.addTrain(train);
	}

	@Transactional
	public void updateTrain(Train train) {
		traindao.update(train);
	}

	@Transactional
	public void saveOrUpdateTrain(Train t) {
		if (t != null) {
			traindao.saveOrUpdate(t);
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Train> getTrainList() {
		return traindao.retrieveAll(Train.class);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Train> getTrainListByDepart(Long departId) {
		return traindao.retrieveByFieldSort(Train.class, "departid", departId, "starttime");
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Paper> getPaperList() {
		return questiondao.retrieveAll(Paper.class);
	}

	@Transactional
	public void addPaper(Paper q) {
		questiondao.create(q);
	}

	@Transactional
	public void modifyPaper(Paper q) {
		questiondao.update(q);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Train> getChoosedTrains(User ste) {
		List<SteTrain> stetrainList = stetraindao.retrieveByField(SteTrain.class, "steid", ste.getId());
		List<Train> result = new ArrayList<Train>();
		for (SteTrain s : stetrainList) {
			Train item = (Train) traindao.retrieve(Train.class, s.getTrainid());
			result.add(item);
		}

		return result;
	}

	@Transactional
	public void selectTrains(User ste, List<Train> trainList) {
		for (Train t : trainList) {
			SteTrain steTrain = new SteTrain();
			steTrain.setSteid(ste.getId());
			steTrain.setTrainid(t.getId());
			steTrain.setStatus(Constants.TRAIN_UNFINISHED);
			stetraindao.create(steTrain);
		}
	}

	@Transactional
	public void dropTrains(User ste, List<Train> trainList) {
		for (Train t : trainList) {
			stetraindao.deleteByTrainIdAndSteId(t.getId(), ste.getId());
		}
	}

	@Transactional
	public Paper getPaperByTrain(Train train) {
		return (Paper) paperdao.retrieve(Paper.class, train.getPaperid());

	}

	@Transactional
	public Train getTrain(Long id) {
		return (Train) traindao.retrieve(Train.class, id);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public String submitAnswer(User ste, Train train, List<String> answers, int score) {
		SteTrain stetrain = new SteTrain();
		stetrain.setSteid(ste.getId());
		stetrain.setTrainid(train.getId());
		stetrain.setFinishtime(new Timestamp(new Date().getTime()));

		String answer = "";
		for (String s : answers) {
			answer += (s + Constants.ANSWER_BIG_SPLIT);
		}
		answer = answer.substring(0, answer.length() - 1);
		stetrain.setAnswer(answer);
		stetrain.setScore((long) score);
		String status = score > 0 ? Constants.TRAIN_PASSED : Constants.TRAIN_FAILED;
		stetrain.setStatus(status);

		stetraindao.create(stetrain);
		// update the status of the ste
		if (status.equals(Constants.TRAIN_PASSED)) {
			// check if ste has finished all the train
			boolean passAllRequired = true;
			List<Train> requiredTrains = traindao.retrieveByField(Train.class, "departid", ste.getDepartmentid());
			for (Train t : requiredTrains) {
				SteTrain curSteTrain = stetraindao.getByTrainIdAndSteId(t.getId(), ste.getId());
				if (t.getRequired().equals("true")) {
					if (curSteTrain == null || (curSteTrain.getStatus().equals(Constants.TRAIN_PASSED) == false)) {
						passAllRequired = false;
						break;
					}
				}
			}
			if (passAllRequired) {
				UserSTE s = (UserSTE) stedao.retrieve(UserSTE.class, ste.getId());
				s.setStatus(Constants.STE_WORK);
				stedao.update(s);
			}
		}

		return status;
	}

	@Transactional
	public SteTrain getSteTrain(Long trainId, Long steId) {
		return stetraindao.getByTrainIdAndSteId(trainId, steId);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<SteTrain> getTrainListBySte(Long steId) {
		List<SteTrain> result = stetraindao.retrieveByField(SteTrain.class, "steid", steId);
		for (SteTrain s : result) {
			Train curTrain = (Train) traindao.retrieve(Train.class, s.getTrainid());
			s.setTrainName(curTrain.getName());
		}
		return result;
	}

	@Transactional
	public long getScoreBySteAndTrain(Long steId, Train train) {
		long score = 0;
		SteTrain steTrain = stetraindao.getByTrainIdAndSteId(train.getId(), steId);
		score = steTrain.getScore();
		return score;
	}
}
