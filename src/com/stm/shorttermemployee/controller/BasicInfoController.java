package com.stm.shorttermemployee.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.stm.shorttermemployee.pojo.Department;
import com.stm.shorttermemployee.pojo.User;
import com.stm.shorttermemployee.service.UserSTEService;
import com.stm.shorttermemployee.service.UserService;
import com.stm.shorttermemployee.util.Constants;
import com.stm.shorttermemployee.util.Encryption;
import com.stm.shorttermemployee.util.LoginSession;
import com.stm.shorttermemployee.util.MessageTip;

@ManagedBean
@Scope("session")
@Controller
public class BasicInfoController implements Serializable {
	private static final long serialVersionUID = 9204795764413486648L;
	private User edituser = null;
	private List<String> genderList = new ArrayList<String>();

	private boolean passwordEdit = false;
	private String curPassword;
	private String password;
	private String passwordConfirm;

	@Autowired
	private UserService userService;

	@Autowired
	private UserSTEService steService;

	public User getEdituser() {
		edituser = LoginSession.getCurrentUser();
		Department curDepart = steService.getDepartment(edituser);
		if (curDepart != null) {
			edituser.setDepartmentName(curDepart.getName());
		}
		return edituser;
	}

	public void setEdituser(User edituser) {
		this.edituser = edituser;
	}

	public List<String> getGenderList() {
		initGenderList();
		return genderList;
	}

	public void setGenderList(List<String> genderList) {
		this.genderList = genderList;
	}

	public boolean isPasswordEdit() {
		return passwordEdit;
	}

	public void setPasswordEdit(boolean passwordEdit) {
		this.passwordEdit = passwordEdit;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private void initGenderList() {
		if (genderList.size() == 0) {
			genderList.add(Constants.GENDER_MALE);
			genderList.add(Constants.GENDER_FEMALE);
		}
	}

	public void submitBasicInfo() {
		// convert date type
		userService.ModifyUser(edituser);
		MessageTip.showMessage(Constants.TIPID, "Save successfully!");
	}

	public void startPasswordEdit() {
		passwordEdit = true;
	}

	public void applyPassword() {
		if (Encryption.makePasswordHash(curPassword).equals(edituser.getPwd()) == false) {
			MessageTip.showMessage(Constants.TIPID, "Current password is wrong!");
			return;
		}
		if (password.equals("")) {
			MessageTip.showMessage(Constants.TIPID, "Password is required!");
			return;
		}
		if (!password.equals(passwordConfirm)) {
			MessageTip.showMessage(Constants.TIPID, "Password Confirm Not Equal!");
		} else {
			edituser.setPwd(Encryption.makePasswordHash(password));
			passwordEdit = false;
		}
	}

	public void discardPassword() {
		passwordEdit = false;
	}

	public String getCurPassword() {
		return curPassword;
	}

	public void setCurPassword(String curPassword) {
		this.curPassword = curPassword;
	}
}
