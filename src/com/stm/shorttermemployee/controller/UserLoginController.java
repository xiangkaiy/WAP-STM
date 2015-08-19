package com.stm.shorttermemployee.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.stm.shorttermemployee.pojo.SteTrain;
import com.stm.shorttermemployee.pojo.Train;
import com.stm.shorttermemployee.pojo.User;
import com.stm.shorttermemployee.service.TrainService;
import com.stm.shorttermemployee.service.UserService;
import com.stm.shorttermemployee.util.Constants;
import com.stm.shorttermemployee.util.Encryption;
import com.stm.shorttermemployee.util.LoginSession;
import com.stm.shorttermemployee.util.MessageTip;

@ManagedBean
@Scope("session")
@Controller
public class UserLoginController implements Serializable {
	private static final long serialVersionUID = -6763489747971257464L;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;

	@Autowired
	private TrainService trainService;

	private String email;

	private String password;

	private User curUser;

	private String welcomeMsg;
	private int trainNum;
	private boolean steRole = false;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public User getCurUser() {
		return curUser;
	}

	public void setCurUser(User curUser) {
		this.curUser = curUser;
	}

	public String login() {
		// format input
		email = email.trim();
		String pwd = Encryption.makePasswordHash(password);

		User curUser = userService.login(email, pwd);
		if (curUser == null) {
			MessageTip.showMessage("tip", "Username or password is wrong!");
		} else {
			LoginSession.put(Constants.SESSION_USER, curUser);
			String rolename = curUser.getRolename().trim();
			welcomeMsg = "Welcome " + rolename + ": " + curUser.getUsername();
			if (rolename.equals(Constants.ROLENAME_ADMIN)) {
				return "userpage/adminUserManage.xhtml?faces-redirect=true";
			} else if (rolename.equals(Constants.ROLENAME_HR)) {
				return "userpage/hrSTEManage.xhtml?faces-redirect=true";
			} else if (rolename.equals(Constants.ROLENAME_LEADER)) {
				return "userpage/leaderApply.xhtml?faces-redirect=true";
			} else if (rolename.equals(Constants.ROLENAME_STE)) {
				return "userpage/steWorkTime.xhtml?faces-redirect=true";
			}
			return "";
		}

		return "";
	}

	public void logout() {
		email = password = null;
		LoginSession.clearSession();
		LoginSession.logout();
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(Constants.PROJECT_NAME + "/index.xhtml");
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public boolean isLoggedIn() {
		return curUser != null;
	}

	public String getWelcomeMsg() {
		return welcomeMsg;
	}

	public void setWelcomeMsg(String welcomeMsg) {
		this.welcomeMsg = welcomeMsg;
	}

	public void jumpToTrain() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("steTraindoing.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getTrainNum() {
		User cur = LoginSession.getCurrentUser();
		List<Train> choosedTrain = trainService.getTrainListByDepart(cur.getDepartmentid());
		trainNum = 0;
		if (choosedTrain != null) {
			for (Train t : choosedTrain) {
				if (t.getRequired().equals("true")) {
					SteTrain stetrain = trainService.getSteTrain(t.getId(), cur.getId());
					if (stetrain == null) {
						trainNum++;
					}
				}
			}
		}

		return trainNum;
	}

	public void setTrainNum(int trainNum) {
		this.trainNum = trainNum;
	}

	public boolean isSteRole() {
		User cur = LoginSession.getCurrentUser();
		steRole = cur.getRolename().equals("ste");
		return steRole;
	}

	public void setSteRole(boolean steRole) {
		this.steRole = steRole;
	}

}
