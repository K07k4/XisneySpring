package com.erutnecca.xisney.controllers;

import java.util.List;
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

	// Añade un usuario
	@PostMapping(path = "/add")
	public @ResponseBody ResponseEntity<String> addUsuario(@RequestParam String email, @RequestParam String pass,
			@RequestParam String dni, @RequestParam String nombre, @RequestParam String apellidos,
			@RequestParam String fechaNacimiento) {

		Usuario usuario = new Usuario();

		String response = "DNI: " + dni + " => " + dniValido(dni) + "\n" + "Nombre: " + nombre + " => "
				+ nombreValido(nombre) + "\n" + "Apellidos: " + apellidos + " => " + nombreValido(apellidos) + "\n"
				+ "Email: " + email + " => " + emailValido(email) + "\n" + "Fecha de nacimiento: " + fechaNacimiento
				+ " => " + fechaValida(fechaNacimiento) + "\n" + "Pass: " + pass + " => " + passValida(pass);

		// Comprueba si existe el email en la base de datos
		if(usuarioRepository.findByEmail(email).size() != 0) {			
			return ResponseEntity.badRequest().body("El email " + email + " ya está registrado");
		}
		
		// Comprueba si existe el dni en la base de datos
		if(usuarioRepository.findByDni(dni).size() != 0) {			
			return ResponseEntity.badRequest().body("El dni " + dni + " ya está registrado");
		}
		
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

	// Obtiene un usuario por ID
	@GetMapping(path = "/get")
	public @ResponseBody Optional<Usuario> getUsuario(@RequestParam int id) {
		return usuarioRepository.findById(id);
	}

	// Obtiene todos los usuarios
	@GetMapping(path = "/all")
	public @ResponseBody Iterable<Usuario> getUsuarios() {
		return usuarioRepository.findAll();
	}
	
	// Activa o desactiva un usuario
	@PostMapping(path = "/activo")
	public @ResponseBody ResponseEntity<String> desactivarUsuario(@RequestParam Integer id,
			@RequestParam Boolean estado) {
		Usuario usuario = usuarioRepository.findById(id).orElse(null);

		if (usuario != null) {

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

	// Modifica el email del usuario
	@PostMapping(path = "/modificarEmail")
	public @ResponseBody ResponseEntity<String> modificarEmail(@RequestParam Integer id,
			@RequestParam String email) {
		Usuario usuario = usuarioRepository.findById(id).orElse(null);
		
		if(email.equals(usuario.getEmail())) {
			return ResponseEntity.badRequest().body("El nuevo email es el mismo que el actual");
		}
		
		if(!emailValido(email)) {
			return ResponseEntity.badRequest().body("El email " + email + " no es válido");
		}
				
		if(usuarioRepository.findByEmail(email).size() == 0) {			
			usuario.setEmail(email);
			
			usuarioRepository.save(usuario);
			return new ResponseEntity<>("Email cambiado con éxito a " + email, HttpStatus.OK);
		} else {
			return ResponseEntity.badRequest().body("El email " + email + " ya está registrado");
		}
	}
	
	// Modifica el DNI del usuario
	@PostMapping(path = "/modificarDni")
	public @ResponseBody ResponseEntity<String> modificarDni(@RequestParam Integer id,
			@RequestParam String dni) {
		Usuario usuario = usuarioRepository.findById(id).orElse(null);
		
		if(dni.equals(usuario.getDni())) {
			return ResponseEntity.badRequest().body("El nuevo DNI es el mismo que el actual");
		}
		
		if(!dniValido(dni)) {
			return ResponseEntity.badRequest().body("El DNI " + dni + " no es válido");
		}
				
		if(usuarioRepository.findByDni(dni).size() == 0) {			
			usuario.setDni(dni);
			
			usuarioRepository.save(usuario);
			return new ResponseEntity<>("DNI cambiado con éxito a " + dni, HttpStatus.OK);
		} else {
			return ResponseEntity.badRequest().body("El DNI " + dni + " ya está registrado");
		}
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