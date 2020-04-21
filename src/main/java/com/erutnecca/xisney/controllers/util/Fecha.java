package com.erutnecca.xisney.controllers.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Fecha {
	
	private static String fecha(String formato) {
		DateFormat dateFormat = new SimpleDateFormat(formato);
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	public static String actualPrecisa() {
		return fecha("yyyy-MM-dd HH:mm:ss");
	}

	public static String actual() {
		return fecha("yyyy-MM-dd");
	}

}
