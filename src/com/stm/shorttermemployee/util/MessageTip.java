package com.stm.shorttermemployee.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class MessageTip {
	public static void showMessage(String id,String msgContent){
		FacesMessage msg = new FacesMessage(msgContent);
		FacesContext.getCurrentInstance().addMessage(id, msg);
	}

	public static void dismissMessage(String id){
	}
}
