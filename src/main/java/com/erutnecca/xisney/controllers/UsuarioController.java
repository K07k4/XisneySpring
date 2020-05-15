package com.erutnecca.xisney.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sun.mail.smtp.SMTPTransport;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

import com.erutnecca.xisney.controllers.util.Checker;
import com.erutnecca.xisney.entities.Usuario;
import com.erutnecca.xisney.repositories.UsuarioRepository;

@Controller
@RequestMapping(path = "/usuario")
@CrossOrigin(origins = "*", allowedHeaders = "*")
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
				+ Checker.nombreValido(nombre) + "\n" + "Apellidos: " + apellidos + " => "
				+ Checker.nombreValido(apellidos) + "\n" + "Email: " + email + " => " + Checker.emailValido(email)
				+ "\n" + "Fecha de nacimiento: " + fechaNacimiento + " => " + Checker.fechaValida(fechaNacimiento)
				+ "\n" + "Pass: " + pass + " => " + Checker.passValida(pass);

		// Comprueba si existe el email en la base de datos
		if (usuarioRepository.findByEmail(email) != null) {
			return ResponseEntity.badRequest().body("El email " + email + " ya está registrado");
		}

		// Comprueba si existe el dni en la base de datos
		if (usuarioRepository.findByDni(dni) != null) {
			return ResponseEntity.badRequest().body("El dni " + dni + " ya está registrado");
		}

		try {
			if (Checker.emailValido(email) && Checker.passValida(pass) && Checker.dniValido(dni)
					&& Checker.nombreValido(nombre) && Checker.nombreValido(apellidos)
					&& Checker.fechaValida(fechaNacimiento)) {
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

	// Modifica todos los campos del usuario. Se entiende que si no cambia, se
	// recibe el mismo
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
		return new ResponseEntity<>("Usuario eliminado con éxito", HttpStatus.OK);
	}

	// Recuperar contraseña
	@PostMapping(path = "/recuperarPass")
	public @ResponseBody ResponseEntity<String> recuperarPass(@RequestParam String email) {
		Usuario usuario = usuarioRepository.findByEmail(email);

		if (usuario == null) {
			return ResponseEntity.badRequest().body("No se ha podido encontrar el usuario");
		}

		if (usuario.getActivo() == false) {
			return ResponseEntity.badRequest().body("El usuario está desactivado");
		}

		final String SMTP_SERVER = "smtp.office365.com";
		final String USERNAME = "xisneyteam@outlook.com";
		final String PASSWORD = "cesurmola-";

		final String EMAIL_FROM = "xisneyteam@outlook.com";
		final String EMAIL_TO = usuario.getEmail();
		final String EMAIL_TO_CC = "";

		final String EMAIL_SUBJECT = "Recuperación de contraseña";
		final String EMAIL_TEXT = "Buenas, " + usuario.getNombre() + "\n\nSu contraseña es: " + usuario.getPass()
				+ "\n\n\nXisney Team";

		Properties prop = System.getProperties();
		prop.put("mail.smtp.host", SMTP_SERVER); // optional, defined in SMTPTransport
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.port", "587"); // default port 25
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.auth", "true");

		Session session = Session.getInstance(prop, null);
		Message msg = new MimeMessage(session);

		try {

			// from
			msg.setFrom(new InternetAddress(EMAIL_FROM));

			// to
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL_TO, false));

			// cc
			msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(EMAIL_TO_CC, false));

			// subject
			msg.setSubject(EMAIL_SUBJECT);

			// content
			msg.setText(EMAIL_TEXT);

			msg.setSentDate(new Date());

			// Get SMTPTransport
			SMTPTransport t = (SMTPTransport) session.getTransport("smtp");

			// connect
			t.connect(SMTP_SERVER, USERNAME, PASSWORD);

			// send
			t.sendMessage(msg, msg.getAllRecipients());

			System.out.println("Response: " + t.getLastServerResponse());

			t.close();


		} catch (MessagingException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("No se ha podido enviar el email");
		}

		return new ResponseEntity<>("Email con contraseña enviado con éxito", HttpStatus.OK);
	}

	@PostMapping(path = "/login")
	public @ResponseBody ResponseEntity<Integer> login(@RequestParam String email, @RequestParam String pass) {
		Usuario usuario = usuarioRepository.findByEmail(email);

		if (usuario == null) {
			return ResponseEntity.status(403).body(-1);
		}

		if (usuario.getActivo() == false) {
			return ResponseEntity.status(404).body(-1);
		}

		if (usuario.getPass().equals(pass)) {
			return new ResponseEntity<>(usuario.getIdUsuario(), HttpStatus.OK);
		} else {
			return ResponseEntity.status(406).body(-1);
		}

	}

}