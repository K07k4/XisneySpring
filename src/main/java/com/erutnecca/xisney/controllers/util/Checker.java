package com.erutnecca.xisney.controllers.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Checker {
	public static boolean validar(Pattern pattern, String str) {
		Matcher matcher = pattern.matcher(str);
		return (matcher.find() && !str.isEmpty());
	}
	
	// Comprueba que el email sea valido
	public static boolean emailValido(String emailStr) {
		return Checker.validar(Checker.EMAIL_ADDRESS_REGEX, emailStr);
	}
	
	
	// Pattern del email
	public static final Pattern EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);
	
	// Pattern del dni
	public static final Pattern DNI_ADDRESS_REGEX = Pattern.compile(
			"((([X-Z])|([LM])){1}([-]?)((\\d){7})([-]?)([A-Z]{1}))|((\\d{8})([-]?)([A-Z]))", Pattern.CASE_INSENSITIVE);

	// Pattern de fecha
	public static final Pattern FECHA_ADDRESS_REGEX = Pattern
			.compile("^(19|20)\\d\\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])$", Pattern.CASE_INSENSITIVE);

	// Pattern generico
	public static final Pattern NOMBRE_ADDRESS_REGEX = Pattern.compile(
			"^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+$",
			Pattern.CASE_INSENSITIVE);

	// Pattern de contraseña (Sin ',\,=, ,%) para proteger la base de datos
	public static final Pattern PASS_ADDRESS_REGEX = Pattern.compile("^((?!'|\\|=| |%).)*$", Pattern.CASE_INSENSITIVE);
	
	// Comprueba que el dni sea valido
		public static boolean dniValido(String dniStr) {
			boolean flag = false;

			if (Checker.validar(Checker.DNI_ADDRESS_REGEX, dniStr)) {
				String letra = "";
				String[] asignacionLetra = { "T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X", "B", "N", "J", "Z", "S",
						"Q", "V", "H", "L", "C", "K", "E" };
				int numeros = Integer.valueOf(dniStr.substring(0, 8));

				int resto = numeros % 23;
				letra = asignacionLetra[resto];

				if (letra.equals(dniStr.substring(8))) {
					flag = true;
				}
			}
			return flag;
		}

		// Comprueba que tenga caracteres validos
		public static boolean fechaValida(String fechaStr) {
			return Checker.validar(Checker.FECHA_ADDRESS_REGEX, fechaStr);
		}

		// Comprueba que tenga caracteres validos
		public static boolean nombreValido(String nombreStr) {
			return Checker.validar(Checker.NOMBRE_ADDRESS_REGEX, nombreStr);
		}

		// Comprueba que tenga caracteres validos
		public static boolean passValida(String passStr) {
			if (passStr.length() >= 6) {
				return Checker.validar(Checker.PASS_ADDRESS_REGEX, passStr);
			} else {
				return false;
			}
		}
}
