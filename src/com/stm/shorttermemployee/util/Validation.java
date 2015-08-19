package com.stm.shorttermemployee.util;

public class Validation {
	public static final String MATCH_MAIL = "([a-zA-Z0-9][a-zA-Z0-9_.+\\-]*)@(([a-zA-Z0-9][a-zA-Z0-9_\\-]+\\.)+[a-zA-Z]{2,6})";

	public static boolean emailValidate(String email) {
		return email.matches(MATCH_MAIL);
	}

}
