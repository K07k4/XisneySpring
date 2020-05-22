package com.erutnecca.xisney.controllers;

import java.util.List;
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

import com.erutnecca.xisney.controllers.util.Fecha;
import com.erutnecca.xisney.entities.ComentarioBlog;
import com.erutnecca.xisney.repositories.ComentarioBlogRepository;
import com.erutnecca.xisney.repositories.EntradaBlogRepository;
import com.erutnecca.xisney.repositories.UsuarioRepository;

@Controller
@RequestMapping(path = "/comentario")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ComentarioBlogController {
	@Autowired
	private ComentarioBlogRepository comentarioBlogRepository;
	private EntradaBlogRepository entradaBlogRepository;
	UsuarioRepository usuarioRepository;

	ComentarioBlogController(ComentarioBlogRepository comentarioBlogRepository,
			EntradaBlogRepository entradaBlogRepository, UsuarioRepository usuarioRepository) {
		this.comentarioBlogRepository = comentarioBlogRepository;
		this.entradaBlogRepository = entradaBlogRepository;
		this.usuarioRepository = usuarioRepository;
	}

	// Crea un comentario en una entrada de blog
	@PostMapping(path = "/add")
	public @ResponseBody ResponseEntity<String> addComentarioBlog(@RequestParam Integer idEntradaBlog,
			@RequestParam Integer idUsuario, @RequestParam String comentario) {

		ComentarioBlog comentarioBlog = new ComentarioBlog();

		comentarioBlog.setIdComentarioBlog(0);
		String fecha = Fecha.actualPrecisa();

		if (entradaBlogRepository.findById(idEntradaBlog).orElse(null) == null) {
			return ResponseEntity.badRequest().body("No existe entrada de blog con ID " + idEntradaBlog);
		}

		if (usuarioRepository.findById(idUsuario).orElse(null) == null) {
			return ResponseEntity.badRequest().body("No existe usuario ID " + idUsuario);
		}

		if (!comentario.isEmpty()) {
			comentarioBlog.setComentario(comentario);
		} else {
			return ResponseEntity.badRequest().body("El comentario no es válido");
		}

		if (fecha != null) {
			comentarioBlog.setFecha(fecha);
		} else {
			return ResponseEntity.badRequest().body("La fecha no se ha generado correctamente");
		}

		comentarioBlog.setIdEntradaBlog(idEntradaBlog);
		comentarioBlog.setIdUsuario(idUsuario);
		comentarioBlog.setComentario(comentario);
		comentarioBlog.setFecha(fecha);
		comentarioBlog.setEditado(false);

		comentarioBlogRepository.save(comentarioBlog);

		return new ResponseEntity<>("Comentario creado correctamente", HttpStatus.OK);
	}

	// Obtiene un comentario por ID
	@GetMapping(path = "/get")
	public @ResponseBody Optional<ComentarioBlog> getComentarioBlog(@RequestParam int id) {
		return comentarioBlogRepository.findById(id);
	}

	// Obtiene todos los comentarios del blog
	@GetMapping(path = "/all")
	public @ResponseBody Iterable<ComentarioBlog> getEntradasBlog() {
		return comentarioBlogRepository.findAll();
	}

	@GetMapping(path = "/getComentariosDeEntrada")
	public @ResponseBody List<ComentarioBlog> getComentariosDeEntrada(Integer id) {
		return comentarioBlogRepository.findByIdEntradaBlog(id);
	}

	// Modifica el comentario del blog según su ID
	@PostMapping(path = "/modificar")
	public @ResponseBody ResponseEntity<String> modificarEntradaBlog(@RequestParam Integer id,
			@RequestParam String comentario) {

		ComentarioBlog comentarioBlog = comentarioBlogRepository.findById(id).orElse(null);

		String fecha = Fecha.actualPrecisa();

		if (comentarioBlog == null) {
			return ResponseEntity.badRequest().body("No se encuentra el comentario con ID " + id.toString());
		}

		if (!comentario.isEmpty()) {
			comentarioBlog.setComentario(comentario);
		} else {
			return ResponseEntity.badRequest().body("El comentario no es válido");
		}

		comentarioBlog.setEditado(true);
		comentarioBlog.setFecha(fecha);

		comentarioBlogRepository.save(comentarioBlog);

		return new ResponseEntity<>("Comentario modificado correctamente", HttpStatus.OK);
	}

	// Elimina el comentario del blog según ID
	@DeleteMapping(path = "/delete")
	public @ResponseBody ResponseEntity<String> deleteComentarioBlog(@RequestParam Integer id) {
		ComentarioBlog comentarioBlog = comentarioBlogRepository.findById(id).orElse(null);

		if (comentarioBlog == null) {
			return ResponseEntity.badRequest().body("No existe la entrada de blog con ID " + id);
		}

		comentarioBlogRepository.delete(comentarioBlog);
		return ResponseEntity.badRequest().body("Entrada de blog eliminada con éxito");
	}

}
