package com.stm.shorttermemployee.model;

import com.stm.shorttermemployee.pojo.Department;
import com.stm.shorttermemployee.pojo.LeaderApplySTE;
import com.stm.shorttermemployee.pojo.User;

public class LeaderApplyView {
	private User leader;
	private LeaderApplySTE apply;
	private Department depart;

	public User getLeader() {
		return leader;
	}

	public void setLeader(User leader) {
		this.leader = leader;
	}

	public LeaderApplySTE getApply() {
		return apply;
	}

	public void setApply(LeaderApplySTE apply) {
		this.apply = apply;
	}

	public Department getDepart() {
		return depart;
	}

	public void setDepart(Department depart) {
		this.depart = depart;
	}

	public LeaderApplyView(User leader, LeaderApplySTE apply, Department depart) {
		this.leader = leader;
		this.apply = apply;
		this.depart = depart;
	}

}
