package com.erutnecca.xisney.controllers;

import java.util.List;
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

import com.erutnecca.xisney.entities.Articulo;
import com.erutnecca.xisney.repositories.ArticuloRepository;
import com.erutnecca.xisney.repositories.TipoArticuloRepository;

@Controller
@RequestMapping(path = "/articulo")
public class ArticuloController {

	@Autowired
	ArticuloRepository articuloRepository;
	TipoArticuloRepository tipoArticuloRepository;

	ArticuloController(ArticuloRepository articuloRepository, TipoArticuloRepository tipoArticuloRepository) {
		this.articuloRepository = articuloRepository;
		this.tipoArticuloRepository = tipoArticuloRepository;
	}

	// Crea un artículo
	@PostMapping(path = "/add")
	public @ResponseBody ResponseEntity<String> addArticulo(@RequestParam Integer idTipoArticulo,
			@RequestParam String nombre, @RequestParam Double precio, @RequestParam String descripcion,
			@RequestParam Integer stock) {

		Articulo articulo = new Articulo();
		articulo.setIdArticulo(0);

		if (nombre.isEmpty() || descripcion.isEmpty()) {
			return ResponseEntity.badRequest().body("No puede haber campos vacíos");
		}

		if (tipoArticuloRepository.findById(idTipoArticulo).orElse(null) == null) {
			return ResponseEntity.badRequest().body("No existe tipo de articulo con ID " + idTipoArticulo);
		}

		if (precio <= 0) {
			return ResponseEntity.badRequest().body("El precio debe ser mayor que 0");
		}

		if (stock < 0) {
			stock = 0;
		}

		articulo.setNombre(nombre);
		articulo.setDescripcion(descripcion);
		articulo.setPrecio(precio);
		articulo.setIdTipoArticulo(idTipoArticulo);
		articulo.setStock(stock);
		articulo.setNumeroPuntuaciones(0);
		articulo.setPuntuacionMedia(0.0);

		articuloRepository.save(articulo);

		return new ResponseEntity<>("Artículo creado correctamente", HttpStatus.OK);
	}

	// Devuelve todos los artículos
	@GetMapping(path = "/all")
	public @ResponseBody Iterable<Articulo> getAllArticulos(String nombre) {
		return articuloRepository.findAll();
	}

	// Devuelve un artículo según ID
	@GetMapping(path = "/get")
	public @ResponseBody Optional<Articulo> getArticulo(Integer id) {
		return articuloRepository.findById(id);
	}

	@PostMapping(path = "/modificar")
	public @ResponseBody ResponseEntity<String> modificarArticulo(@RequestParam Integer idArticulo,
			@RequestParam Integer idTipoArticulo, @RequestParam String nombre, @RequestParam Double precio,
			@RequestParam String descripcion, @RequestParam Integer stock) {

		Articulo articulo = articuloRepository.findById(idArticulo).orElse(null);

		if (articulo == null) {
			return ResponseEntity.badRequest().body("No se encuentra el artículo con ID " + idArticulo);
		}

		if (nombre.isEmpty() || descripcion.isEmpty()) {
			return ResponseEntity.badRequest().body("No puede haber campos vacíos");
		}

		if (tipoArticuloRepository.findById(idTipoArticulo).orElse(null) == null) {
			return ResponseEntity.badRequest().body("No existe tipo de articulo con ID " + idTipoArticulo);
		}

		if (precio <= 0) {
			return ResponseEntity.badRequest().body("El precio debe ser mayor que 0");
		}

		if (stock < 0) {
			stock = 0;
		}

		articulo.setNombre(nombre);
		articulo.setDescripcion(descripcion);
		articulo.setPrecio(precio);
		articulo.setIdTipoArticulo(idTipoArticulo);
		articulo.setStock(stock);

		articuloRepository.save(articulo);

		return new ResponseEntity<>("Artículo modificado correctamente", HttpStatus.OK);
	}

	@GetMapping(path = "/getArticulosPorTipo")
	public @ResponseBody List<Articulo> getArticulosPorTipo(Integer idTipoArticulo) {
		return articuloRepository.findByIdTipoArticulo(idTipoArticulo);
	}

	// Elimina el artículo según ID
	@DeleteMapping(path = "/delete")
	public @ResponseBody ResponseEntity<String> deleteArticulo(@RequestParam Integer id) {
		Articulo articulo = articuloRepository.findById(id).orElse(null);

		if (articulo == null) {
			return ResponseEntity.badRequest().body("No existe el artículo con ID " + id);
		}

		articuloRepository.delete(articulo);
		return ResponseEntity.badRequest().body("Artículo eliminado con éxito");
	}

}
