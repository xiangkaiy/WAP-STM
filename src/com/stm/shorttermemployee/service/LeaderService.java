package com.stm.shorttermemployee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stm.shorttermemployee.dao.LeaderApplySTEDAO;
import com.stm.shorttermemployee.dao.LeaderEvaluationDAO;
import com.stm.shorttermemployee.model.LeaderApplyView;
import com.stm.shorttermemployee.pojo.LeaderApplySTE;
import com.stm.shorttermemployee.pojo.LeaderEvaluation;
import com.stm.shorttermemployee.pojo.User;

@Component
public class LeaderService {
	@Autowired
	private LeaderApplySTEDAO leaderapplyDAO;

	@Autowired
	private LeaderEvaluationDAO evaluationDAO;

	@Transactional
	public void addApplySTE(LeaderApplySTE model) {
		leaderapplyDAO.create(model);
	}

	@Transactional
	public List<LeaderApplyView> getApplyListView() {
		return leaderapplyDAO.getLeaderApplyListView();
	}

	@Transactional
	public List<LeaderApplyView> getApplyListViewByLeader(User leader) {
		return leaderapplyDAO.getApplyListViewByLeader(leader);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public void submitEvaluation(LeaderEvaluation evaluation) {
		Long curSteId = evaluation.getSteid();
		List<LeaderEvaluation> list = evaluationDAO.retrieveByField(LeaderEvaluation.class, "steid", curSteId);
		LeaderEvaluation existEvaluation = null;
		if (list != null) {
			existEvaluation = list.get(0);
			existEvaluation.setCmt(evaluation.getCmt());
			existEvaluation.setEvaluationscore(evaluation.getEvaluationscore());
			existEvaluation.setGivetime(evaluation.getGivetime());
			evaluationDAO.update(existEvaluation);
		} else {
			evaluationDAO.create(evaluation);
		}

	}

	@Transactional
	public LeaderEvaluation getEvaluationBySteAndLeader(Long steid, Long leaderid) {
		List<LeaderEvaluation> list = evaluationDAO.getBySteAndLeader(steid, leaderid);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public LeaderEvaluation getEvaluationBySte(Long steid) {
		List<LeaderEvaluation> list = evaluationDAO.retrieveByField(LeaderEvaluation.class, "steid", steid);
		LeaderEvaluation result = null;
		if (list != null && list.size() > 0) {
			result = list.get(0);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<LeaderApplySTE> getAllLeaderApply() {
		return leaderapplyDAO.retrieveAll(LeaderApplySTE.class);
	}

}
