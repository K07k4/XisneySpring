package com.erutnecca.xisney.controllers.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Fecha {

	private static String fecha(String formato) {
		DateFormat dateFormat = new SimpleDateFormat(formato);
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static String actualPrecisa() {
		return fecha("yyyy-MM-dd HH:mm:ss");
	}

	public static String actualMuyPrecisa() {
		return fecha("yyyy-MM-dd HH:mm:ss.SSS");
	}

	public static String actual() {
		return fecha("yyyy-MM-dd");
	}

	public static Date stringFecha(String strFecha) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.parse(strFecha);
	}

	public static Integer diferenciaDias(Date fechaFin, Date fechaInicio) {
		long diff = fechaInicio.getTime() - fechaFin.getTime();
		return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
	}

	public static long actualMilisegundos() {
		Date date = new Date();
		return date.getTime();
	}

}
