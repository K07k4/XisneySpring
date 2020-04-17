package com.erutnecca.xisney.controllers;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.erutnecca.xisney.entities.Usuario;
import com.erutnecca.xisney.repositories.UsuarioRepository;

@Controller
@RequestMapping(path = "/usuario")
public class UsuarioController {
	@Autowired
	private UsuarioRepository usuarioRepository;

	// TODO: Comprobar que el email y el DNI no esten registrados antes de crear el usuario
	
	@PostMapping(path = "/add") // Map ONLY POST Requests
	public @ResponseBody ResponseEntity<String> addUsuario(@RequestParam String email, @RequestParam String pass,
			@RequestParam String dni, @RequestParam String nombre, @RequestParam String apellidos,
			@RequestParam String fechaNacimiento) {

		Usuario usuario = new Usuario();

		String response = "DNI: " + dni + " => " + dniValido(dni) + "\n" + "Nombre: " + nombre + " => "
				+ nombreValido(nombre) + "\n" + "Apellidos: " + apellidos + " => " + nombreValido(apellidos) + "\n"
				+ "Email: " + email + " => " + emailValido(email) + "\n" + "Fecha de nacimiento: " + fechaNacimiento
				+ " => " + fechaValida(fechaNacimiento) + "\n" + "Pass: " + pass + " => " + passValida(pass);

		try {
			if (emailValido(email) && passValida(pass) && dniValido(dni) && nombreValido(nombre)
					&& nombreValido(apellidos) && fechaValida(fechaNacimiento)) {
				usuario.setIdUsuario(0);
				usuario.setEmail(email);
				usuario.setPass(pass);
				usuario.setDni(dni);
				usuario.setNombre(nombre);
				usuario.setApellidos(apellidos);
				usuario.setFechaNacimiento(fechaNacimiento);
				usuario.setActivo(true);

				usuarioRepository.save(usuario);
			} else {
				return ResponseEntity.badRequest().body("No se ha podido crear el usuario\n" + response);

			}

		} catch (Exception e) {
			return new ResponseEntity<>("No se ha podido crear el usuario\n" + response, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("Usuario creado correctamente\n" + usuario.toString(), HttpStatus.OK);
	}

	@GetMapping(path = "/get") // Map ONLY POST Requests
	public @ResponseBody Optional<Usuario> getUsuario(@RequestParam int id) {
		return usuarioRepository.findById(id);
	}

	@GetMapping(path = "/all")
	public @ResponseBody Iterable<Usuario> getUsuarios() {
		return usuarioRepository.findAll();
	}

	@PostMapping(path = "/activo") // Map ONLY POST Requests
	public @ResponseBody ResponseEntity<String> desactivarUsuario(@RequestParam Integer id,
			@RequestParam Boolean estado) {
		Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);

		if (optionalUsuario != null) {
			Usuario usuario = optionalUsuario.get();

			if (usuario.getActivo() == estado) {
				return ResponseEntity.badRequest()
						.body("El usuario ya se encuentra en ese estado\n" + usuario.toString());
			}

			usuario.setActivo(estado);
			usuarioRepository.save(usuario);

			String response = "Usuario desactivado correctamente\n";
			if (estado == true) {
				response = "Usuario activado correctamente\n";
			}
			return new ResponseEntity<>(response + usuario.toString(), HttpStatus.OK);
		} else {
			return ResponseEntity.badRequest().body("No se ha podido encontrar el usuario");
		}

	}

	
	//TODO: Comprobar que el email y el dni no están ya almacenados
	
	@PostMapping(path = "/modificar") // Map ONLY POST Requests
	public @ResponseBody ResponseEntity<String> modificarUsuario(@RequestParam Integer id,
			@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "pass", required = false) String pass,
			@RequestParam(name = "dni", required = false) String dni,
			@RequestParam(name = "nombre", required = false) String nombre,
			@RequestParam(name = "apellidos", required = false) String apellidos,
			@RequestParam(name = "fechaNacimiento", required = false) String fechaNacimiento) {

		Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
		Usuario usuario = new Usuario();

		String response = "DNI: " + dni + " => " + dniValido(dni) + "\n" + "Nombre: " + nombre + " => "
				+ nombreValido(nombre) + "\n" + "Apellidos: " + apellidos + " => " + nombreValido(apellidos) + "\n"
				+ "Email: " + email + " => " + emailValido(email) + "\n" + "Fecha de nacimiento: " + fechaNacimiento
				+ " => " + fechaValida(fechaNacimiento) + "\n" + "Pass: " + pass + " => " + passValida(pass);

		if (optionalUsuario != null) {
			usuario = optionalUsuario.get();
		} else {
			return ResponseEntity.badRequest().body("No se ha podido encontrar el usuario");
		}

		try {
			if (emailValido(email) && passValida(pass) && dniValido(dni) && nombreValido(nombre)
					&& nombreValido(apellidos) && fechaValida(fechaNacimiento)) {
				usuario.setEmail(email);
				usuario.setPass(pass);
				usuario.setDni(dni);
				usuario.setNombre(nombre);
				usuario.setApellidos(apellidos);
				usuario.setFechaNacimiento(fechaNacimiento);

				usuarioRepository.save(usuario);
			} else {
				return ResponseEntity.badRequest().body("No se ha podido modificar el usuario\n" + response);

			}

		} catch (Exception e) {
			return new ResponseEntity<>("No se ha podido modificar el usuario\n" + response, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>("Usuario modificado correctamente\n" + usuario.toString(), HttpStatus.OK);
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

	// Comprueba el string con un pattern
	public static boolean validar(Pattern pattern, String dniStr) {
		Matcher matcher = pattern.matcher(dniStr);
		return matcher.find();
	}

	// Comprueba que el email sea valido
	public static boolean emailValido(String emailStr) {
		return validar(EMAIL_ADDRESS_REGEX, emailStr);
	}

	// Comprueba que el dni sea valido
	public static boolean dniValido(String dniStr) {
		boolean flag = false;

		if (validar(DNI_ADDRESS_REGEX, dniStr)) {
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
		return validar(FECHA_ADDRESS_REGEX, fechaStr);
	}

	// Comprueba que tenga caracteres validos
	public static boolean nombreValido(String nombreStr) {
		return validar(NOMBRE_ADDRESS_REGEX, nombreStr);
	}

	// Comprueba que tenga caracteres validos
	public static boolean passValida(String passStr) {
		return validar(PASS_ADDRESS_REGEX, passStr);
	}

}