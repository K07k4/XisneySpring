package com.erutnecca.xisney.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.erutnecca.xisney.controllers.util.Checker;
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

		String response = "DNI: " + dni + " => " + Checker.dniValido(dni) + "\n" + "Nombre: " + nombre + " => "
				+ Checker.nombreValido(nombre) + "\n" + "Apellidos: " + apellidos + " => " + Checker.nombreValido(apellidos) + "\n"
				+ "Email: " + email + " => " + Checker.emailValido(email) + "\n" + "Fecha de nacimiento: " + fechaNacimiento
				+ " => " + Checker.fechaValida(fechaNacimiento) + "\n" + "Pass: " + pass + " => " + Checker.passValida(pass);

		// Comprueba si existe el email en la base de datos
		if (usuarioRepository.findByEmail(email) != null) {
			return ResponseEntity.badRequest().body("El email " + email + " ya está registrado");
		}

		// Comprueba si existe el dni en la base de datos
		if (usuarioRepository.findByDni(dni) != null) {
			return ResponseEntity.badRequest().body("El dni " + dni + " ya está registrado");
		}

		try {
			if (Checker.emailValido(email) && Checker.passValida(pass) && Checker.dniValido(dni) && Checker.nombreValido(nombre)
					&& Checker.nombreValido(apellidos) && Checker.fechaValida(fechaNacimiento)) {
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

		usuario = usuarioRepository.findByDni(dni);
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

	// Modifica todos los campos del usuario. Se entiende que si no cambia, se recibe el mismo
	@PostMapping(path = "/modificar")
	public @ResponseBody ResponseEntity<String> modificarUsuario(@RequestParam Integer id, @RequestParam String nombre,
			@RequestParam String apellidos, @RequestParam String email, @RequestParam String pass,
			@RequestParam String dni, @RequestParam String fechaNacimiento) {
		Usuario usuario = usuarioRepository.findById(id).orElse(null);

		if (usuario == null) {
			return ResponseEntity.badRequest().body("No se encuentra el usuario con ID " + id);
		}

		Usuario usuarioDummy = usuarioRepository.findByEmail(email);
		try {
			if (usuarioDummy.getIdUsuario() != id) {
				return ResponseEntity.badRequest().body("El email ya está registrado");
			} else if (Checker.emailValido(email)) {
				usuario.setEmail(email);
			} else {
				return ResponseEntity.badRequest().body("El email no es válido");
			}
		} catch (Exception e) {
			if (Checker.emailValido(email)) {
				usuario.setEmail(email);
			} else {
				return ResponseEntity.badRequest().body("El email no es válido");
			}
		}

		usuarioDummy = usuarioRepository.findByDni(dni);
		try {
			if (usuarioDummy.getIdUsuario() != id) {
				return ResponseEntity.badRequest().body("El dni ya está registrado");
			} else if (Checker.dniValido(dni)) {

				usuario.setDni(dni);
			} else {
				return ResponseEntity.badRequest().body("El dni no es válido");
			}
		} catch (Exception e) {
			if (Checker.dniValido(dni)) {
				usuario.setDni(dni);
			} else {
				return ResponseEntity.badRequest().body("El dni no es válido");
			}
		}

		if (!Checker.passValida(pass)) {
			return ResponseEntity.badRequest().body("La contraseña no es válida");
		} else {
			usuario.setPass(pass);
		}

		if (!Checker.nombreValido(nombre)) {
			return ResponseEntity.badRequest().body("El nombre no es válido");
		} else {
			usuario.setNombre(nombre);
		}

		if (!Checker.nombreValido(apellidos)) {
			return ResponseEntity.badRequest().body("Los apellidos no son válidos");
		} else {
			usuario.setApellidos(apellidos);
		}

		if (!Checker.fechaValida(fechaNacimiento)) {
			return ResponseEntity.badRequest().body("La fecha no es válida");
		} else {
			usuario.setFechaNacimiento(fechaNacimiento);
		}

		usuarioRepository.save(usuario);
		return new ResponseEntity<>("Información cambiada con éxito\n" + usuario.toString(), HttpStatus.OK);

	}

	// Elimina el usuario
	@DeleteMapping(path = "/delete")
	public @ResponseBody ResponseEntity<String> deleteUsuario(@RequestParam Integer id) {
		Usuario usuario = usuarioRepository.findById(id).orElse(null);

		try {
		usuarioRepository.delete(usuario);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("No se ha podido encontrar el usuario");
		}
		return ResponseEntity.badRequest().body("Usuario eliminado con éxito");
	}
	

}