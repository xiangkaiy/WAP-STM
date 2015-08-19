package com.stm.shorttermemployee.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stm.shorttermemployee.dao.ApplyRegularDAO;
import com.stm.shorttermemployee.dao.DepartmentDAO;
import com.stm.shorttermemployee.dao.LeaderEvaluationDAO;
import com.stm.shorttermemployee.dao.SteLeaveDAO;
import com.stm.shorttermemployee.dao.UserDAO;
import com.stm.shorttermemployee.dao.UserSTEDAO;
import com.stm.shorttermemployee.model.ApplyRegularView;
import com.stm.shorttermemployee.model.UserSTEView;
import com.stm.shorttermemployee.pojo.ApplyRegular;
import com.stm.shorttermemployee.pojo.Department;
import com.stm.shorttermemployee.pojo.LeaderEvaluation;
import com.stm.shorttermemployee.pojo.SteLeave;
import com.stm.shorttermemployee.pojo.SteTrain;
import com.stm.shorttermemployee.pojo.User;
import com.stm.shorttermemployee.pojo.UserSTE;
import com.stm.shorttermemployee.util.Constants;

@Component
public class UserSTEService {

	@Autowired
	private UserSTEDAO usersteDAO;

	@Autowired
	private DepartmentDAO departDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private SteLeaveDAO leaveDAO;

	@Autowired
	private ApplyRegularDAO regulardao;

	@Autowired
	private LeaderEvaluationDAO evaluationdao;

	@Autowired
	private TrainService trainService;

	@Autowired
	private WorkTimeService worktimeService;

	@Transactional
	public UserSTE getUserSTE(long id) {
		return (UserSTE) usersteDAO.retrieve(UserSTE.class, id);
	}

	@Transactional
	public void saveOrUpdate(UserSTE o) {
		usersteDAO.saveOrUpdate(o);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<UserSTE> getUserSTEList() {
		return usersteDAO.retrieveByField(UserSTE.class, "status", Constants.STE_IDEL);
	}

	@Transactional
	public List<UserSTEView> getUserSTEByDepart(Department depart) {
		return usersteDAO.getUserSTEViewByDepart(depart);
	}

	@Transactional
	public User getLeader(User ste) {
		User leader = new User();
		long departmentId = ste.getDepartmentid();
		Department depart = (Department) departDAO.retrieve(Department.class, departmentId);
		leader = (User) userDAO.retrieve(User.class, depart.getLeaderid());
		return leader;
	}

	@Transactional
	public Department getDepartment(User ste) {
		long departmentId = ste.getDepartmentid();
		Department depart = (Department) departDAO.retrieve(Department.class, departmentId);
		return depart;
	}

	@Transactional
	public void applyForLeave(SteLeave apply) {
		leaveDAO.create(apply);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<SteLeave> getAllApplyLeave() {
		return leaveDAO.retrieveByField(SteLeave.class, "status", Constants.ASK_FOR_LEAVE_SUBMIT);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<SteLeave> getAllPassedLeave(Long steId) {

		return leaveDAO.retrieveByField(SteLeave.class, "status", Constants.ASK_FOR_LEAVE_PASS);
	}

	@Transactional
	public void passApplyForLeave(SteLeave[] leaveArr) {
		for (SteLeave apply : leaveArr) {
			apply.setStatus(Constants.ASK_FOR_LEAVE_PASS);
			leaveDAO.update(apply);
		}
	}

	@Transactional
	public void refuseApplyForLeave(SteLeave[] leaveArr) {
		for (SteLeave apply : leaveArr) {
			apply.setStatus(Constants.ASK_FOR_LEAVE_REFUSE);
			leaveDAO.update(apply);
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<SteLeave> getApplyLeaveByLeader(Long leaderid) {
		return leaveDAO.retrieveByField(SteLeave.class, "leaderid", leaderid);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<SteLeave> getApplyLeaveBySTE(Long steid) {
		return leaveDAO.retrieveByField(SteLeave.class, "steid", steid);
	}

	@Transactional
	public void submitApplyRegular(ApplyRegular apply) {
		regulardao.create(apply);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<ApplyRegular> getApplyRegularList(Long steid) {
		return regulardao.retrieveByField(ApplyRegular.class, "steid", steid);
	}

	@Transactional
	public ApplyRegular getUnhandledApplyRegular(Long steid) {
		ApplyRegular result = null;
		List<ApplyRegular> list = getApplyRegularList(steid);
		for (ApplyRegular a : list) {
			if (a.getStatus().equals(Constants.APPLY_REGULAR_SUBMIT)) {
				result = a;
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<ApplyRegularView> getApplyRegularView() {
		List<ApplyRegularView> result = new ArrayList<ApplyRegularView>();
		List<ApplyRegular> applyRegular = regulardao.retrieveAll(ApplyRegular.class);
		for (ApplyRegular regular : applyRegular) {
			User u = (User) userDAO.retrieve(User.class, regular.getSteid());
			UserSTE ste = (UserSTE) userDAO.retrieve(UserSTE.class, regular.getSteid());
			ApplyRegularView view = new ApplyRegularView();
			view.setApplyRegular(regular);
			view.setSte(u);
			view.setUserSte(ste);
			result.add(view);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public String getEvaluationScore(Long steId) {
		String result = null;
		List<LeaderEvaluation> list = evaluationdao.retrieveByField(LeaderEvaluation.class, "steid", steId);
		if (list != null && list.size() > 0) {
			result = list.get(0).getEvaluationscore();
		}
		return result;
	}

	@Transactional
	public void passApplyForTurnHR(ApplyRegular apply, User hr) {
		apply.setStatus(Constants.APPLY_REGULAR_PASS);
		apply.setHrid(hr.getId());
		regulardao.update(apply);
	}

	@Transactional
	public void refuseApplyForTurnHR(ApplyRegular apply, User hr) {
		apply.setStatus(Constants.APPLY_REGULAR_REFUSE);
		apply.setHrid(hr.getId());
		regulardao.update(apply);
	}

	@Transactional
	public int calculateTrainScore(User ste) {
		int curTrainScore = 0;
		List<SteTrain> steTrainList = trainService.getTrainListBySte(ste.getId());
		for (SteTrain t : steTrainList) {
			curTrainScore += t.getScore();
		}
		return curTrainScore;
	}

	@Transactional
	public int calculateWorkTimeScore(User ste) {
		int workTimeScore = 0;
		Date now = new Date();
		int lateCount = worktimeService.getLateCount(ste, null, now);
		int leaveEarlyCount = worktimeService.getLeaveEarlyCount(ste, null, now);
		int totalCount = worktimeService.getTotalSignCount(ste, null, now);
		int onScheduleCount = totalCount - lateCount - leaveEarlyCount;
		workTimeScore = (int) (100 * ((float) onScheduleCount / (float) totalCount)) + onScheduleCount;
		return workTimeScore;
	}

	@Transactional
	public int calculateEvaluationScore(User ste) {
		String evaluationScoreStr = getEvaluationScore(ste.getId());
		int curEvaluationScore = 0;
		int workEfficiency = 0;
		int studyAbility = 0;
		int meetingParticipate = 0;
		int teamWork = 0;
		if (evaluationScoreStr != null) {
			String[] evaluationArray = evaluationScoreStr.split(" ");
			workEfficiency = Integer.parseInt(evaluationArray[0]);
			studyAbility = Integer.parseInt(evaluationArray[1]);
			meetingParticipate = Integer.parseInt(evaluationArray[2]);
			teamWork = Integer.parseInt(evaluationArray[3]);
		}
		curEvaluationScore = workEfficiency + studyAbility + meetingParticipate + teamWork;
		return curEvaluationScore;
	}

	// @Transactional
	// public int calculateTotalScore(User ste){
	// int totalScore = 0;
	// int trainScore =
	//
	// return totalScore;
	// }
}
