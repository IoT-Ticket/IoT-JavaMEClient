package com.iotticket.me.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateParser {
	
	public static Date getISO8601Date(String string) {
		Calendar calendar = Calendar.getInstance();
		int year = Integer.valueOf(string.substring(0, 4)).intValue();
		int month = Integer.valueOf(string.substring(5, 7)).intValue()-1;
		int day = Integer.valueOf(string.substring(8, 10)).intValue();
		int hours = Integer.valueOf(string.substring(11, 13)).intValue();
		int minutes = Integer.valueOf(string.substring(14, 16)).intValue();
		int seconds = Integer.valueOf(string.substring(17, 19)).intValue();
		String timezone = string.substring(19);
		TimeZone zone = TimeZone.getTimeZone(timezone);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, hours);
		calendar.set(Calendar.MINUTE, minutes);
		calendar.set(Calendar.SECOND, seconds);
		calendar.setTimeZone(zone);
		return calendar.getTime();
	}

	public static String toISO8601(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String dt = calendar.get(Calendar.YEAR)
				+ "-" + zeroFilledNumber(calendar.get(Calendar.MONTH)+1, 2)
				+ "-" + zeroFilledNumber(calendar.get(Calendar.DAY_OF_MONTH), 2)
				+ "T"
				+ zeroFilledNumber(calendar.get(Calendar.HOUR_OF_DAY), 2)
				+ ":" + zeroFilledNumber(calendar.get(Calendar.MINUTE), 2)
				+ ":" + zeroFilledNumber(calendar.get(Calendar.SECOND), 2)
				+ calendar.getTimeZone().getID();
		return dt;
	}
	
	private static String zeroFilledNumber(int number, int length) {
		String num = String.valueOf(number);
		while (num.length() < length) {
			num = "0" + num;
		}
		return num;
	}
	
}