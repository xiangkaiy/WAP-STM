package com.stm.shorttermemployee.controller;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.PieChartModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.stm.shorttermemployee.model.UserListModel;
import com.stm.shorttermemployee.pojo.Department;
import com.stm.shorttermemployee.pojo.Payment;
import com.stm.shorttermemployee.pojo.Property;
import com.stm.shorttermemployee.pojo.User;
import com.stm.shorttermemployee.pojo.UserSTE;
import com.stm.shorttermemployee.service.DepartmentService;
import com.stm.shorttermemployee.service.PaymentService;
import com.stm.shorttermemployee.service.UserSTEService;
import com.stm.shorttermemployee.service.UserService;
import com.stm.shorttermemployee.service.WorkTimeService;
import com.stm.shorttermemployee.util.CalculateEngine;
import com.stm.shorttermemployee.util.Constants;
import com.stm.shorttermemployee.util.Encryption;
import com.stm.shorttermemployee.util.LoginSession;
import com.stm.shorttermemployee.util.MessageTip;
import com.stm.shorttermemployee.util.Validation;

@ManagedBean
@Scope("session")
@Controller
public class HrSTEManageController implements Serializable {
	private static final long serialVersionUID = -240539202393846779L;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private List<User> filteredEmployee;

	@Autowired
	private UserService userService;

	@Autowired
	private PaymentService payService;

	@Autowired
	private WorkTimeService worktimeService;

	@Autowired
	private UserSTEService steService;

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

	private Payment selectedPayment;

	private PieChartModel timePieModel = new PieChartModel();;
	private Float totalPayAmount;
	private Payment newPay;
	private String newPayEndtime;
	private Integer totalTimeHour;
	private Float totalFine;
	private Float finalTotalPayAmount;
	private List<Department> departmentList = null;

	private Float intervalSalary;
	private Float intervalFine;
	private Date intervalStime;
	private Date intervalEtime;
	private boolean showChart = false;
	private String departList;
	private String departSalaryList;
	private String departFineList;
	private List<Department> allDeparts;
	private Map<String, Float> departSalary = new HashMap<String, Float>();
	private Map<String, Float> departFine = new HashMap<String, Float>();
	private Long currentWorkTime;
	private Long currentLeaveTime;

	public List<User> getFilteredEmployee() {
		return filteredEmployee;
	}

	public void setFilteredEmployee(List<User> filteredEmployee) {
		this.filteredEmployee = filteredEmployee;
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
		baseUserList = userService.getSTEAtworkList();
		for (User u : baseUserList) {
			Department selectedDepart = steService.getDepartment(u);
			u.setDepartmentName(selectedDepart.getName());
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
			MessageTip.showMessage("tip", "Email can't be empty!");
			return;
		} else {
			if (Validation.emailValidate(editingUser.getEmail()) == true) {
				editingUser.setEmail(editingUser.getEmail().trim());
			} else {
				MessageTip.showMessage("tip", "Invalid Email!");
				return;
			}

		}

		if (editingUser.getIdno() == null || editingUser.getIdno().trim().equals("")) {
			MessageTip.showMessage("tip", "IDNo. can't be empty!");
			return;
		} else {
			editingUser.setIdno(editingUser.getIdno().trim());
		}

		if (editingUser.getRolename() == null || editingUser.getRolename().trim().equals("")) {
			MessageTip.showMessage("tip", "Role can't be empty!");
			return;
		} else {
			editingUser.setRolename(editingUser.getRolename().trim());
		}

		if (editingUser.getUsername() == null || editingUser.getUsername().trim().equals("")) {
			MessageTip.showMessage("tip", "Username can't be empty!");
			return;
		} else {
			editingUser.setUsername(editingUser.getUsername().trim());
		}

		if (editingUser.getGender() == null || editingUser.getGender().trim().equals("")) {
			MessageTip.showMessage("tip", "Gender can't be empty!");
			return;
		} else {
			editingUser.setGender(editingUser.getGender().trim());
		}

		if (editingUser.getPwd() == null || editingUser.getPwd().equals("")) {
			MessageTip.showMessage("tip", "Password can't be empty!");
			return;
		}

		if (addMode) {
			// add
			// check if there is an exist email address
			if (userService.existEmail(editingUser.getEmail().trim())) {
				MessageTip.showMessage("tip", "Email address already exist!");
				return;
			} else if (userService.existIDNo(editingUser.getIdno().trim())) {
				MessageTip.showMessage("tip", "ID number already exist!");
				return;
			} else {
				userService.addNewUser(editingUser);
				MessageTip.showMessage("tip", "Add successfully!");
				refreshUserList();
				// baseUserList.add(editingUser);
			}

		} else if (editMode) {
			if (userService.existSameEmail(editingUser)) {
				MessageTip.showMessage("tip", "Exist email address!");
				return;
			} else if (userService.existSameIDNo(editingUser)) {
				MessageTip.showMessage("tip", "Exist ID number!");
				return;
			} else if (selectedUser.getRolename().equals(Constants.ROLENAME_ADMIN) && (editingUser.getRolename().equals(Constants.ROLENAME_ADMIN) == false)) {
				MessageTip.showMessage("tip", "You can't change an administrator's role!");
				return;
			} else {
				userService.ModifyUser(editingUser);
				// User localUser = new User(editingUser);
				// replaceUserData(localUser);
				refreshUserList();
				MessageTip.showMessage("tip", "Modify successfully!");
			}
		}
		editingUser = null;
		editMode = false;
		addMode = false;
	}

	public void applyPassword() {
		if (password.equals("")) {
			MessageTip.showMessage("tip", "Password is required!");
			return;
		}
		if (!password.equals(passwordConfirm)) {
			MessageTip.showMessage("tip", "Password Confirm Not Equal!");
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
			MessageTip.showMessage("tip", "Can not delete an administrator!");
			return;
		}

		// delete this user
		if (canBeDeleted) {
			userService.deleteUser(selectedUser);
			refreshUserList();
			MessageTip.showMessage("tip", "Delete successfully!");
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

		selectedPayment = payService.getLatestPay(selectedUser);
		Date startdate = null;
		Date enddate = null;
		if (selectedPayment.getStartdate() == null) {
			enddate = new Date();
		} else {
			startdate = selectedPayment.getEnddate();
			enddate = new Date();
		}
		currentWorkTime = worktimeService.getWorktimeInterval(selectedUser, startdate, enddate).getTotaltime() / 60;
		currentLeaveTime = worktimeService.getLeavetimeInterval(selectedUser, startdate, enddate) / 60;

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		newPayEndtime = format.format(now);

		if (selectedPayment.getStartdate() == null) {
			newPay = worktimeService.getWorktimeInterval(selectedUser, null, now);
		} else {
			newPay = worktimeService.getWorktimeInterval(selectedUser, selectedPayment.getEnddate(), now);
		}
		UserSTE selectedSte = steService.getUserSTE(selectedUser.getId());
		totalPayAmount = CalculateEngine.calculatePayment(newPay.getTotaltime(), Float.parseFloat(selectedSte.getExpectedsalary()));
		totalTimeHour = (int) (newPay.getTotaltime() / 60);

		Property finePro = payService.getFineUnit();
		Float fineUnit = (finePro == null ? 0 : Float.parseFloat(finePro.getValue()));
		totalFine = (fineUnit * worktimeService.getLateAndLeaveEarlyCount(selectedUser, newPay.getStartdate(), newPay.getEnddate()));
		if ((totalPayAmount - totalFine) > 0) {
			finalTotalPayAmount = totalPayAmount - totalFine;
		} else {
			finalTotalPayAmount = 0.0f;
		}
	}

	public Payment getSelectedPayment() {
		return selectedPayment;
	}

	public void setSelectedPayment(Payment selectedPayment) {
		this.selectedPayment = selectedPayment;
	}

	public void pay() {
		User hr = LoginSession.getCurrentUser();
		if (finalTotalPayAmount > 0) {
			payService.payToSTE(hr, selectedUser, new Timestamp(new Date().getTime()), finalTotalPayAmount, totalFine, newPay.getStartdate(),
					newPay.getEnddate(), newPay.getTotaltime());
			MessageTip.showMessage(Constants.TIPID, "Pay " + finalTotalPayAmount + " $ successfully!");
		} else {
			MessageTip.showMessage(Constants.TIPID, "The total salary is 0! There is no need to pay.");
		}
	}

	public PieChartModel getTimePieModel() {
		return timePieModel;
	}

	public void setTimePieModel(PieChartModel timePieModel) {
		this.timePieModel = timePieModel;
	}

	public Float getTotalPayAmount() {
		return totalPayAmount;
	}

	public void setTotalPayAmount(Float totalPayAmount) {
		this.totalPayAmount = totalPayAmount;
	}

	public Payment getNewPay() {
		return newPay;
	}

	public void setNewPay(Payment newPay) {
		this.newPay = newPay;
	}

	public String getNewPayEndtime() {
		return newPayEndtime;
	}

	public void setNewPayEndtime(String newPayEndtime) {
		this.newPayEndtime = newPayEndtime;
	}

	public Integer getTotalTimeHour() {
		return totalTimeHour;
	}

	public void setTotalTimeHour(Integer totalTimeHour) {
		this.totalTimeHour = totalTimeHour;
	}

	public Float getTotalFine() {
		return totalFine;
	}

	public void setTotalFine(Float totalFine) {
		this.totalFine = totalFine;
	}

	public Float getFinalTotalPayAmount() {
		return finalTotalPayAmount;
	}

	public void setFinalTotalPayAmount(Float finalTotalPayAmount) {
		this.finalTotalPayAmount = finalTotalPayAmount;
	}

	public List<Department> getDepartmentList() {
		if (departmentList == null) {
			departmentList = departService.getAllDepartment();
		}
		return departmentList;
	}

	public void setDepartmentList(List<Department> departmentList) {
		this.departmentList = departmentList;
	}

	public Float getIntervalSalary() {

		return intervalSalary;
	}

	public void setIntervalSalary(Float intervalSalary) {
		this.intervalSalary = intervalSalary;
	}

	public Float getIntervalFine() {
		return intervalFine;
	}

	public void setIntervalFine(Float intervalFine) {
		this.intervalFine = intervalFine;
	}

	public Date getIntervalStime() {
		return intervalStime;
	}

	public void setIntervalStime(Date intervalStime) {
		this.intervalStime = intervalStime;
	}

	public Date getIntervalEtime() {
		return intervalEtime;
	}

	public void setIntervalEtime(Date intervalEtime) {
		this.intervalEtime = intervalEtime;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void searchSalary() {
		if (allDeparts == null) {
			allDeparts = departService.getAllDepartment();
		}
		showChart = true;
		Long hrId = LoginSession.getCurrentUser().getId();
		Timestamp startstamp = null;
		Timestamp endstamp = null;
		if (intervalStime != null) {
			startstamp = new Timestamp(intervalStime.getTime());
		}
		if (intervalEtime != null) {
			endstamp = new Timestamp(intervalEtime.getTime());
		}
		List<Payment> intervalPayment = payService.getTotalSalaryByTimeAndHr(startstamp, endstamp, hrId);
		intervalFine = 0.0f;
		intervalSalary = 0.0f;
		Iterator itSalary = departSalary.entrySet().iterator();
		Iterator itFine = departFine.entrySet().iterator();
		while (itSalary.hasNext()) {
			Map.Entry<String, Float> entry = (Entry<String, Float>) itSalary.next();
			String key = entry.getKey();
			departSalary.put(key, 0.0f);
		}

		while (itFine.hasNext()) {
			Map.Entry<String, Float> entry = (Entry<String, Float>) itFine.next();
			String key = entry.getKey();
			departFine.put(key, 0.0f);
		}

		if (intervalPayment != null) {
			for (Payment p : intervalPayment) {
				intervalFine += Float.parseFloat(p.getFine());
				intervalSalary += p.getMoney();
				Long curSteid = p.getSteid();
				Long departId = userService.getUserById(curSteid).getDepartmentid();
				Department curDepart = departService.getDepart(departId);
				Float curDepartSalary = departSalary.get(curDepart.getName());
				Float curDepartFine = departFine.get(curDepart.getName());
				departSalary.put(curDepart.getName(), curDepartSalary + intervalSalary);
				departFine.put(curDepart.getName(), curDepartFine + intervalFine);
			}
		}

		departSalaryList = "";
		departFineList = "";
		for (Department t : allDeparts) {
			String departName = t.getName();
			departSalaryList += (departSalary.get(departName).toString() + "+");
			departFineList += (departFine.get(departName).toString() + "+");
		}
		departSalaryList = departSalaryList.substring(0, departSalaryList.length() - 1);
		departFineList = departFineList.substring(0, departFineList.length() - 1);

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("hrSTEManage.jsf");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isShowChart() {
		return showChart;
	}

	public void setShowChart(boolean showChart) {
		this.showChart = showChart;
	}

	public String getDepartList() {
		if (departList == null) {
			departList = "";
			allDeparts = departService.getAllDepartment();
			for (Department d : allDeparts) {
				departList += (d.getName() + "+");
				departSalary.put(d.getName(), 0.0f);
				departFine.put(d.getName(), 0.0f);
			}
			departList = departList.substring(0, departList.length() - 1);

		}
		return departList;
	}

	public void setDepartList(String departList) {
		this.departList = departList;
	}

	public String getDepartSalaryList() {
		return departSalaryList;
	}

	public void setDepartSalaryList(String departSalaryList) {
		this.departSalaryList = departSalaryList;
	}

	public String getDepartFineList() {
		return departFineList;
	}

	public void setDepartFineList(String departFineList) {
		this.departFineList = departFineList;
	}

	public Long getCurrentWorkTime() {
		return currentWorkTime;
	}

	public void setCurrentWorkTime(Long currentWorkTime) {
		this.currentWorkTime = currentWorkTime;
	}

	public Long getCurrentLeaveTime() {
		return currentLeaveTime;
	}

	public void setCurrentLeaveTime(Long currentLeaveTime) {
		this.currentLeaveTime = currentLeaveTime;
	}

}
