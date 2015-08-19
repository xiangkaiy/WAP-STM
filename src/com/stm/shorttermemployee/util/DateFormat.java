package com.stm.shorttermemployee.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateFormat {
	public static Date deleteHMS(Date today) {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		try {
			today = format.parse(format.format(today));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return today;
	}

	public static Date str2Date(String date, TimeZone timezone) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
			sdf.setTimeZone(timezone);
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String date2Str(Date date, TimeZone timezone) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
		sdf.setTimeZone(timezone);
		return sdf.format(date);

	}
}
