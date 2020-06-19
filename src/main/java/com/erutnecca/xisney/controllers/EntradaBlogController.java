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

import com.erutnecca.xisney.controllers.util.Fecha;
import com.erutnecca.xisney.entities.EntradaBlog;
import com.erutnecca.xisney.repositories.EntradaBlogRepository;

@Controller
@RequestMapping(path = "/blog")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EntradaBlogController {
	@Autowired
	private EntradaBlogRepository entradaBlogRepository;

	// Crea una entrada de blog
	@PostMapping(path = "/add")
	public @ResponseBody ResponseEntity<String> addEntradaBlog(@RequestParam String titulo,
			@RequestParam String contenido) {

		System.out.println("Crear entrada blog");
		
		EntradaBlog entradaBlog = new EntradaBlog();

		entradaBlog.setIdEntradaBlog(0);
		String fecha = Fecha.actualPrecisa();

		if (!titulo.isEmpty()) {
			entradaBlog.setTitulo(titulo);
		} else {
			return ResponseEntity.badRequest().body("El título no es válido");
		}

		if (!contenido.isEmpty()) {
			entradaBlog.setContenido(contenido);
		} else {
			return ResponseEntity.badRequest().body("El contenido no es válido");
		}

		if (fecha != null) {
			entradaBlog.setFecha(fecha);
		} else {
			return ResponseEntity.badRequest().body("La fecha no se ha generado correctamente");
		}

		entradaBlogRepository.save(entradaBlog);

		return new ResponseEntity<>("Entrada de blog creada correctamente", HttpStatus.OK);
	}

	// Obtiene una entrada del blog por ID
	@GetMapping(path = "/get")
	public @ResponseBody Optional<EntradaBlog> getEntradaBlog(@RequestParam int id) {
		System.out.println("Get entrada blog " + id);
		return entradaBlogRepository.findById(id);
	}

	// Obtiene todos las entradas del blog
	@GetMapping(path = "/all")
	public @ResponseBody Iterable<EntradaBlog> getEntradasBlog() {
		System.out.println("Get todas entradas blog");
		return entradaBlogRepository.findAll();
	}

	// Modifica la entrada del blog según su ID
	@PostMapping(path = "/modificar")
	public @ResponseBody ResponseEntity<String> modificarEntradaBlog(@RequestParam Integer id,
			@RequestParam String titulo, @RequestParam String contenido) {
		
		System.out.println("Modificar entrada blog " + id);

		EntradaBlog entradaBlog = entradaBlogRepository.findById(id).orElse(null);

		if (entradaBlog == null) {
			return ResponseEntity.badRequest().body("No se encuentra la entrada del blog con ID " + id.toString());
		}

		if (!titulo.isEmpty()) {
			entradaBlog.setTitulo(titulo);
		} else {
			return ResponseEntity.badRequest().body("El título no es válido");
		}

		if (!contenido.isEmpty()) {
			entradaBlog.setContenido(contenido);
		} else {
			return ResponseEntity.badRequest().body("El contenido no es válido");
		}

		entradaBlogRepository.save(entradaBlog);

		return new ResponseEntity<>("Entrada de blog modificada correctamente", HttpStatus.OK);
	}

	// Elimina la entrada del blog según ID
	@DeleteMapping(path = "/delete")
	public @ResponseBody ResponseEntity<String> deleteEntradaBlog(@RequestParam Integer id) {
		System.out.println("Borrar entrada blog " + id);
		
		EntradaBlog entradaBlog = entradaBlogRepository.findById(id).orElse(null);

		if (entradaBlog == null) {
			return ResponseEntity.badRequest().body("No existe la entrada de blog con ID " + id);
		}

		entradaBlogRepository.delete(entradaBlog);
		return ResponseEntity.badRequest().body("Entrada de blog eliminada con éxito");
	}

}
