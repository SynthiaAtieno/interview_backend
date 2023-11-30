package com.example.test.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Utils {
	private Utils() {

	}

	public static Date getCurrentDate() {
		return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

	}
	public static Date getDateFromString(String date){
		 return Date.from(LocalDate.parse(date).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

}
