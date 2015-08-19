package com.stm.shorttermemployee.model;

import com.stm.shorttermemployee.pojo.STEWorktime;
import com.stm.shorttermemployee.pojo.User;
import com.stm.shorttermemployee.pojo.UserSTE;

public class WorkTimeView {
	private STEWorktime worktime;
	private User user;
	private UserSTE ste;

	public STEWorktime getWorktime() {
		return worktime;
	}

	public void setWorktime(STEWorktime worktime) {
		this.worktime = worktime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserSTE getSte() {
		return ste;
	}

	public void setSte(UserSTE ste) {
		this.ste = ste;
	}

	public WorkTimeView(User user, UserSTE ste, STEWorktime worktime) {
		this.user = user;
		this.ste = ste;
		this.worktime = worktime;
	}

}
