package com.stm.shorttermemployee.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.stm.shorttermemployee.model.LeaderApplyListModel;
import com.stm.shorttermemployee.model.LeaderApplyView;
import com.stm.shorttermemployee.pojo.Department;
import com.stm.shorttermemployee.pojo.LeaderApplySTE;
import com.stm.shorttermemployee.pojo.UserSTE;
import com.stm.shorttermemployee.service.DepartmentService;
import com.stm.shorttermemployee.service.LeaderService;
import com.stm.shorttermemployee.service.UserSTEService;
import com.stm.shorttermemployee.service.UserService;
import com.stm.shorttermemployee.util.CalculateEngine;
import com.stm.shorttermemployee.util.Constants;
import com.stm.shorttermemployee.util.MessageTip;

//@ManagedBean
//@SessionScoped
//@Component
@ManagedBean
@Scope("session")
@Controller
public class HRApplyFromLeaderController implements Serializable {
	private static final long serialVersionUID = 8517491319765589408L;
	private LeaderApplyListModel applyList = null;
	private List<LeaderApplyView> baseApplyList = null;
	private LeaderApplyView selectedApply = null;
	private int first;

	private List<UserSTE> availableSTE = null;

	private List<UserSTE> selectedSTE = null;
	@Autowired
	private LeaderService leaderService;

	@Autowired
	private UserSTEService usersteService;

	@Autowired
	private UserService userService;

	@Autowired
	private DepartmentService departService;

	private boolean showAvailableSTE = false;

	private String allDepartment = null;
	private String departApplyAmount = null;
	private List<Department> allDeparts;
	private Map<String, Long> departApplyAmountMap = new HashMap<String, Long>();

	public LeaderApplyListModel getApplyList() {
		if (applyList == null) {
			refreshApplyList();
		}
		return applyList;
	}

	public void setApplyList(LeaderApplyListModel applyList) {
		this.applyList = applyList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<LeaderApplyView> getBaseApplyList() {
		return baseApplyList;
	}

	public void setBaseApplyList(List<LeaderApplyView> baseApplyList) {
		this.baseApplyList = baseApplyList;
	}

	private void refreshApplyList() {
		baseApplyList = leaderService.getApplyListView();

		applyList = new LeaderApplyListModel(baseApplyList);
	}

	public LeaderApplyView getSelectedApply() {
		return selectedApply;
	}

	public void setSelectedApply(LeaderApplyView selectedApply) {
		showAvailableSTE = true;
		this.selectedApply = selectedApply;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public List<UserSTE> getAvailableSTE() {

		searchSTE();

		return availableSTE;
	}

	public void setAvailableSTE(List<UserSTE> availableSTE) {
		this.availableSTE = availableSTE;
	}

	public boolean isShowAvailableSTE() {
		return showAvailableSTE;
	}

	public void setShowAvailableSTE(boolean showAvailableSTE) {
		this.showAvailableSTE = showAvailableSTE;
	}

	public void reload() {
		innerDataClear();
	}

	private void innerDataClear() {
		baseApplyList = null;
		selectedApply = null;
		first = 0;
	}

	public void searchSTE() {
		List<UserSTE> allIdleste = usersteService.getUserSTEList();
		for (UserSTE s : allIdleste) {
			String name = userService.getUserById(s.getId()).getUsername();
			s.setName(name);
		}
		availableSTE = CalculateEngine.filtratedByLeaderApply(allIdleste, selectedApply);
	}

	public void assignSTE() {
		if (selectedSTE == null || selectedSTE.size() == 0) {
			MessageTip.showMessage(Constants.TIPID, "You should at least choose one short-term employee!");
			return;
		}
		if (selectedSTE.size() != selectedApply.getApply().getSteamount()) {
			MessageTip.showMessage(Constants.TIPID, selectedApply.getDepart().getName() + " need " + selectedApply.getApply().getSteamount()
					+ " short-term employees, but the number you choosed is " + selectedSTE.size());
			return;
		}
		List<Long> idList = new LinkedList<Long>();
		for (UserSTE a : selectedSTE) {
			idList.add(a.getId());
		}
		userService.assignSTEUser(idList, selectedApply.getApply());
		MessageTip.showMessage(Constants.TIPID, "Assign Successfully!");
		for (LeaderApplyView app : baseApplyList) {
			if (app.getApply().getId().equals(selectedApply.getApply().getId())) {
				app.getApply().setStatus(Constants.LEADERAPPLY_PASS);
			}
		}
		applyList = new LeaderApplyListModel(baseApplyList);
		selectedApply = null;
		selectedSTE = null;
		showAvailableSTE = false;
	}

	public List<UserSTE> getSelectedSTE() {
		return selectedSTE;
	}

	public void setSelectedSTE(List<UserSTE> selectedSTE) {
		this.selectedSTE = selectedSTE;
	}

	public void discard() {
		showAvailableSTE = false;
		selectedApply = null;

	}

	private void getAllDepart() {
		allDeparts = departService.getAllDepartment();
	}

	public String getAllDepartment() {
		if (allDepartment == null) {
			allDepartment = "";

			getAllDepart();
			for (Department d : allDeparts) {
				allDepartment += (d.getName() + "+");
			}
			allDepartment = allDepartment.substring(0, allDepartment.length() - 1);
		}
		return allDepartment;
	}

	public void setAllDepartment(String allDepartment) {
		this.allDepartment = allDepartment;
	}

	public String getDepartApplyAmount() {
		if (departApplyAmount == null) {
			List<LeaderApplySTE> allApply = leaderService.getAllLeaderApply();
			for (Department d : allDeparts) {
				departApplyAmountMap.put(d.getName(), 0L);
			}

			for (LeaderApplySTE app : allApply) {
				// get corresponding department
				Long leaderId = app.getLeaderid();
				Long curDepartId = userService.getUserById(leaderId).getDepartmentid();
				Department curDepart = departService.getDepart(curDepartId);
				Long curDepartAmount = departApplyAmountMap.get(curDepart.getName());
				departApplyAmountMap.put(curDepart.getName(), curDepartAmount + app.getSteamount());
			}
			getAllDepart();
			departApplyAmount = "";
			for (Department d : allDeparts) {
				departApplyAmount += (departApplyAmountMap.get(d.getName()).toString() + "+");
			}
			departApplyAmount = departApplyAmount.substring(0, departApplyAmount.length() - 1);
		}
		return departApplyAmount;
	}

	public void setDepartApplyAmount(String departApplyAmount) {
		this.departApplyAmount = departApplyAmount;
	}

}
