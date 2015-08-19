package com.stm.shorttermemployee.util;

import javax.faces.context.FacesContext;

import com.stm.shorttermemployee.pojo.User;

public class LoginSession {

	public static User getCurrentUser() {
		FacesContext context = FacesContext.getCurrentInstance();
		User cur = (User) context.getExternalContext().getSessionMap().get(Constants.SESSION_USER);
		return cur;
	}

	public static void clearSession(){
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().clear();
	}

	public static void put(String key, Object value) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put(key, value);
	}

	public static void logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	}
}
