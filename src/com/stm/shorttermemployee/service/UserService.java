package com.stm.shorttermemployee.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stm.shorttermemployee.dao.ApplyRegularDAO;
import com.stm.shorttermemployee.dao.LeaderApplySTEDAO;
import com.stm.shorttermemployee.dao.LeaderEvaluationDAO;
import com.stm.shorttermemployee.dao.PaymentDAO;
import com.stm.shorttermemployee.dao.STEWorkTimeDAO;
import com.stm.shorttermemployee.dao.SteLeaveDAO;
import com.stm.shorttermemployee.dao.SteTrainDAO;
import com.stm.shorttermemployee.dao.UserDAO;
import com.stm.shorttermemployee.dao.UserSTEDAO;
import com.stm.shorttermemployee.pojo.LeaderApplySTE;
import com.stm.shorttermemployee.pojo.User;
import com.stm.shorttermemployee.pojo.UserSTE;
import com.stm.shorttermemployee.util.Constants;

@Component
public class UserService {

	@Autowired
	private UserDAO userdao;

	@Autowired
	private UserSTEDAO stedao;

	@Autowired
	private LeaderApplySTEDAO leaderapplydao;

	@Autowired
	private ApplyRegularDAO applyregulardao;

	@Autowired
	private LeaderEvaluationDAO leaderevaluationdao;

	@Autowired
	private PaymentDAO paymentdao;

	@Autowired
	private SteLeaveDAO steleavedao;

	@Autowired
	private SteTrainDAO stetraindao;

	@Autowired
	private STEWorkTimeDAO worktimedao;

	@Transactional
	public User login(String email, String password) {
		List<User> result = userdao.getByEmailAndPwd(email, password);
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	@Transactional
	public void addNewUser(User u) {
		userdao.create(u);
		if (u.getRolename().equals(Constants.ROLENAME_STE)) {
			UserSTE ste = new UserSTE();
			ste.setId(u.getId());
			ste.setStatus(Constants.STE_IDEL);
			stedao.create(ste);
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<User> getUserList() {
		return (List<User>) userdao.retrieveAllDesc(User.class, "id");
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<User> getSTEList() {
		return (List<User>) userdao.retrieveByField(User.class, "rolename", Constants.ROLENAME_STE);
	}

	@SuppressWarnings("rawtypes")
	@Transactional
	public boolean existEmail(String email) {
		List userlist = userdao.retrieveByField(User.class, "email", email);
		if (userlist == null || userlist.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Transactional
	public void ModifyUser(User modifyUser) {
		userdao.update(modifyUser);
		if (modifyUser.getRolename().equals(Constants.ROLENAME_STE)) {
			UserSTE oldSte = (UserSTE) stedao.retrieve(UserSTE.class, modifyUser.getId());
			if (oldSte == null) {
				UserSTE ste = new UserSTE();
				ste.setId(modifyUser.getId());
				ste.setStatus(Constants.STE_IDEL);
				stedao.create(ste);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public boolean existSameEmail(User u) {
		List<User> userlist = userdao.retrieveByField(User.class, "email", u.getEmail());
		if (userlist == null || userlist.size() == 0) {
			return false;
		} else {
			if (userlist.get(0).getId().equals(u.getId())) {
				return false;
			} else {
				return true;
			}
		}
	}

	@Transactional
	public void deleteUser(User u) {
		userdao.delete(u);

		//
		if (u.getRolename().equals(Constants.ROLENAME_STE)) {
			stedao.deleteById(UserSTE.class, u.getId());
			applyregulardao.deleteBySteId(u.getId());
			leaderevaluationdao.deleteBySteId(u.getId());
			paymentdao.deleteBySteId(u.getId());
			steleavedao.deleteBySteId(u.getId());
			stetraindao.deleteBySteId(u.getId());
			worktimedao.deleteBySteId(u.getId());
		} else if (u.getRolename().equals(Constants.ROLENAME_LEADER)) {
			leaderapplydao.deleteBySteId(u.getId());
			leaderevaluationdao.deleteByLeaderId(u.getId());
		} else if (u.getRolename().equals(Constants.ROLENAME_HR)) {
			paymentdao.deleteByHrId(u.getId());
		}

	}

	@SuppressWarnings("rawtypes")
	@Transactional
	public boolean existIDNo(String idno) {
		List userlist = userdao.retrieveByField(User.class, "idno", idno);
		if (userlist == null || userlist.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public boolean existSameIDNo(User u) {
		List<User> userlist = userdao.retrieveByField(User.class, "idno", u.getIdno());
		if (userlist == null || userlist.size() == 0) {
			return false;
		} else {
			if (userlist.get(0).getId().equals(u.getId())) {
				return false;
			} else {
				return true;
			}
		}
	}

	@Transactional
	public void assignSTEUser(List<Long> steidList, LeaderApplySTE apply) {
		User leader = (User) userdao.retrieve(User.class, apply.getLeaderid());
		for (Long id : steidList) {
			User user = (User) userdao.retrieve(User.class, id);
			user.setDepartmentid(leader.getDepartmentid());
			userdao.update(user);

			UserSTE ste = (UserSTE) stedao.retrieve(UserSTE.class, id);
			ste.setStatus(Constants.STE_PREPARE_WORK);
			stedao.update(ste);
		}
		LeaderApplySTE app = (LeaderApplySTE) leaderapplydao.retrieve(LeaderApplySTE.class, apply.getId());
		app.setStatus(Constants.LEADERAPPLY_PASS);
		leaderapplydao.update(app);
	}

	@Transactional
	public User getUserById(Long id) {
		return (User) userdao.retrieve(User.class, id);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<User> getSTEAtworkList() {
		List<UserSTE> stelist = stedao.retrieveByField(UserSTE.class, "status", Constants.STE_WORK);
		List<User> result = new ArrayList<User>();
		for (UserSTE ste : stelist) {
			User s = (User) userdao.retrieve(User.class, ste.getId());
			result.add(s);
		}
		return result;
	}
}
