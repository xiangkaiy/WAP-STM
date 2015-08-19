package com.stm.shorttermemployee.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.stm.shorttermemployee.model.UserListModel;
import com.stm.shorttermemployee.pojo.Department;
import com.stm.shorttermemployee.pojo.User;
import com.stm.shorttermemployee.service.DepartmentService;
import com.stm.shorttermemployee.service.UserService;
import com.stm.shorttermemployee.util.Constants;
import com.stm.shorttermemployee.util.Encryption;
import com.stm.shorttermemployee.util.MessageTip;
import com.stm.shorttermemployee.util.Validation;

@ManagedBean
@Scope("session")
@Controller
public class AdminUserManageController implements Serializable {
	private static final long serialVersionUID = 3953404436775842677L;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;

	@Autowired
	private DepartmentService departService;

	private List<User> baseUserList = null;
	private UserListModel userList = null;
	private int first;
	private User selectedUser = null;
	private boolean editMode = false;
	private boolean addMode = false;
	private User editingUser = null;

	// about password
	private boolean passwordEdit = false;
	private String password;
	private String passwordConfirm;

	private List<String> roleList = new ArrayList<String>();
	private List<String> genderList = new ArrayList<String>();
	private List<Department> departmentList = null;
	private boolean showDepartmentList = false;

	public List<Department> getDepartmentList() {
		if (departmentList == null) {
			departmentList = departService.getAllDepartment();
		}
		return departmentList;
	}

	public void setDepartmentList(List<Department> departmentList) {
		this.departmentList = departmentList;
	}

	private void initRoleList() {
		if (roleList.size() == 0) {
			roleList.add(Constants.ROLENAME_ADMIN);
			roleList.add(Constants.ROLENAME_HR);
			roleList.add(Constants.ROLENAME_LEADER);
			roleList.add(Constants.ROLENAME_STE);
		}
	}

	private void initGenderList() {
		if (genderList.size() == 0) {
			genderList.add(Constants.GENDER_MALE);
			genderList.add(Constants.GENDER_FEMALE);
		}
	}

	private void refreshUserList() {
		baseUserList = userService.getUserList();
		for (User u : baseUserList) {
			Department d = departService.getDepart(u.getDepartmentid());
			if (d == null) {
				u.setDepartmentName("");
			} else {
				u.setDepartmentName(d.getName());
			}
		}
		userList = new UserListModel(baseUserList);
	}

	public UserListModel getUserList() {
		if (baseUserList == null) {
			refreshUserList();
		}
		return userList;
	}

	public void setUserList(UserListModel userList) {
		this.userList = userList;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
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

	public boolean isPasswordEdit() {
		return passwordEdit;
	}

	public void setPasswordEdit(boolean passwordEdit) {
		this.passwordEdit = passwordEdit;
	}

	public User getEditingUser() {
		return editingUser;
	}

	public void setEditingUser(User editingUser) {
		this.editingUser = editingUser;
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

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<String> getRoleList() {
		initRoleList();
		return roleList;
	}

	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}

	public List<String> getGenderList() {
		initGenderList();
		return genderList;
	}

	public void setGenderList(List<String> genderList) {
		this.genderList = genderList;
	}

	public void startAdd() {
		selectedUser = null;
		try {
			initEditField();
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		addMode = true;
	}

	public void startPasswordEdit() {
		passwordEdit = true;
	}

	public void startEdit() throws IOException {
		initEditField();
		editMode = true;
	}

	private void initEditField() throws IOException {

		if (selectedUser != null) {
			editingUser = new User(selectedUser);
		} else {
			editingUser = new User();
		}
		if (editingUser.getRolename().equals(Constants.ROLENAME_LEADER)) {
			setShowDepartmentList(true);
		} else {
			setShowDepartmentList(false);
		}
		password = "";
		passwordConfirm = "";
	}

	public void discard() {
		editingUser = null;
		editMode = false;
		addMode = false;
		passwordEdit = false;
	}

	public boolean isBrowseMode() {
		return !addMode && !editMode;
	}

	public void apply() {
		// data format
		// data check
		if (editingUser.getEmail() == null || editingUser.getEmail().trim().equals("")) {
			MessageTip.showMessage(Constants.TIPID, "Email can't be empty!");
			return;
		} else {
			if (Validation.emailValidate(editingUser.getEmail()) == true) {
				editingUser.setEmail(editingUser.getEmail().trim());
			} else {
				MessageTip.showMessage(Constants.TIPID, "Invalid Email!");
				return;
			}
		}

		if (editingUser.getIdno() == null || editingUser.getIdno().trim().equals("")) {
			MessageTip.showMessage(Constants.TIPID, "IDNo. can't be empty!");
			return;
		} else {
			editingUser.setIdno(editingUser.getIdno().trim());
		}

		if (editingUser.getRolename() == null || editingUser.getRolename().trim().equals("")) {
			MessageTip.showMessage(Constants.TIPID, "Role can't be empty!");
			return;
		} else {
			editingUser.setRolename(editingUser.getRolename().trim());
		}

		if (editingUser.getUsername() == null || editingUser.getUsername().trim().equals("")) {
			MessageTip.showMessage(Constants.TIPID, "Username can't be empty!");
			return;
		} else {
			editingUser.setUsername(editingUser.getUsername().trim());
		}

		if (editingUser.getGender() == null || editingUser.getGender().trim().equals("")) {
			MessageTip.showMessage(Constants.TIPID, "Gender can't be empty!");
			return;
		} else {
			editingUser.setGender(editingUser.getGender().trim());
		}

		if (editingUser.getPwd() == null || editingUser.getPwd().equals("")) {
			MessageTip.showMessage(Constants.TIPID, "Password can't be empty!");
			return;
		}

		if (addMode) {
			// add
			// check if there is an exist email address
			if (userService.existEmail(editingUser.getEmail().trim())) {
				MessageTip.showMessage(Constants.TIPID, "Email address already exist!");
				return;
			} else if (userService.existIDNo(editingUser.getIdno().trim())) {
				MessageTip.showMessage(Constants.TIPID, "ID number already exist!");
				return;
			} else {
				userService.addNewUser(editingUser);
				MessageTip.showMessage(Constants.TIPID, "Add successfully!");
				refreshUserList();
			}

		} else if (editMode) {
			if (userService.existSameEmail(editingUser)) {
				MessageTip.showMessage(Constants.TIPID, "Exist email address!");
				return;
			} else if (userService.existSameIDNo(editingUser)) {
				MessageTip.showMessage(Constants.TIPID, "Exist ID number!");
				return;
			} else if (selectedUser.getRolename().equals(Constants.ROLENAME_ADMIN) && (editingUser.getRolename().equals(Constants.ROLENAME_ADMIN) == false)) {
				MessageTip.showMessage(Constants.TIPID, "You can't change an administrator's role!");
				return;
			} else {
				userService.ModifyUser(editingUser);
				refreshUserList();
				MessageTip.showMessage(Constants.TIPID, "Modify successfully!");
			}
		}
		editingUser = null;
		editMode = false;
		addMode = false;
	}

	public void applyPassword() {
		if (password.equals("")) {
			MessageTip.showMessage(Constants.TIPID, "Password is required!");
			return;
		}
		if (!password.equals(passwordConfirm)) {
			MessageTip.showMessage(Constants.TIPID, "Password Confirm Not Equal!");
		} else {
			editingUser.setPwd(Encryption.makePasswordHash(password));
			passwordEdit = false;
		}
	}

	public void discardPassword() {
		passwordEdit = false;
	}

	public void deleteUser() {
		// make sure this selected user can be deleted.
		boolean canBeDeleted = true;
		if (selectedUser.getRolename().equals(Constants.ROLENAME_ADMIN)) {
			canBeDeleted = false;
			MessageTip.showMessage(Constants.TIPID, "Can not delete an administrator!");
			return;
		}

		// delete this user
		if (canBeDeleted) {
			userService.deleteUser(selectedUser);
			refreshUserList();
			MessageTip.showMessage(Constants.TIPID, "Delete successfully!");
			return;
		}
	}

	public void reload() {
		innerDataClear();
	}

	private void innerDataClear() {
		baseUserList = null;
		selectedUser = null;
		first = 0;
	}

	public void onRowSelect(SelectEvent event) {
		selectedUser = (User) event.getObject();
	}

	public void roleSelectChanged(ValueChangeEvent event) {
		String newRoleName = (String) event.getNewValue();
		if (newRoleName.equals(Constants.ROLENAME_LEADER)) {
			setShowDepartmentList(true);
		} else {
			setShowDepartmentList(false);
		}
	}

	public boolean isShowDepartmentList() {
		return showDepartmentList;
	}

	public void setShowDepartmentList(boolean showDepartmentList) {
		this.showDepartmentList = showDepartmentList;
	}
}
