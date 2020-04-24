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

import com.erutnecca.xisney.entities.TipoArticulo;
import com.erutnecca.xisney.repositories.TipoArticuloRepository;

@Controller
@RequestMapping(path = "/tipoArticulo")
public class TipoArticuloController {

	@Autowired
	TipoArticuloRepository tipoArticuloRepository;

	// Crea un tipo de oferta
	@PostMapping(path = "/add")
	public @ResponseBody ResponseEntity<String> addTipoArticulo(@RequestParam String nombre) {

		if (nombre.isEmpty()) {
			return ResponseEntity.badRequest().body("El nombre no puede ir vacío");
		}

		TipoArticulo tipoArticulo = tipoArticuloRepository.findByNombre(nombre).orElse(null);

		if (tipoArticulo != null) {
			return ResponseEntity.badRequest().body("Ya existe un tipo de artículo con este nombre");
		}

		tipoArticulo = new TipoArticulo();

		tipoArticulo.setIdTipoArticulo(0);
		tipoArticulo.setNombre(nombre);

		tipoArticuloRepository.save(tipoArticulo);

		return new ResponseEntity<>("Tipo de articulo creado correctamente", HttpStatus.OK);
	}

	@GetMapping(path = "/all")
	public @ResponseBody Iterable<TipoArticulo> getAllTiposArticulos(String nombre) {
		return tipoArticuloRepository.findAll();
	}

	@GetMapping(path = "/getPorNombre")
	public @ResponseBody Optional<TipoArticulo> getTipoArticuloNombre(String nombre) {
		return tipoArticuloRepository.findByNombre(nombre);
	}

	@GetMapping(path = "/getPorId")
	public @ResponseBody Optional<TipoArticulo> getTipoArticuloId(Integer idTipoArticulo) {
		return tipoArticuloRepository.findById(idTipoArticulo);
	}

	// Elimina el tipo de artículo según ID
	@DeleteMapping(path = "/delete")
	public @ResponseBody ResponseEntity<String> deleteTipoArticulo(@RequestParam Integer id) {
		TipoArticulo tipoArticulo = tipoArticuloRepository.findById(id).orElse(null);

		if (tipoArticulo == null) {
			return ResponseEntity.badRequest().body("No existe el tipo de artículo con ID " + id);
		}

		tipoArticuloRepository.delete(tipoArticulo);
		return ResponseEntity.badRequest().body("Tipo de artículo eliminado con éxito");
	}

	// TODO: Obtener todos los artículos del tipo de artículo
}
