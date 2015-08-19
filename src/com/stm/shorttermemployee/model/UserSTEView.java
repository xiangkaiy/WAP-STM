package com.stm.shorttermemployee.model;

import com.stm.shorttermemployee.pojo.Department;
import com.stm.shorttermemployee.pojo.User;
import com.stm.shorttermemployee.pojo.UserSTE;

public class UserSTEView {
	private User user;
	private UserSTE ste;
	private Department depart;

	public UserSTEView(UserSTE ste, User user, Department departItem) {
		this.user = user;
		this.ste = ste;
		this.depart = departItem;
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

	public Department getDepart() {
		return depart;
	}

	public void setDepart(Department depart) {
		this.depart = depart;
	}

}
