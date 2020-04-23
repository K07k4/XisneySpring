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

import com.erutnecca.xisney.entities.Provincia;
import com.erutnecca.xisney.entities.TipoOferta;
import com.erutnecca.xisney.repositories.TipoOfertaRepository;

@Controller
@RequestMapping(path = "/tipoOferta")
public class TipoOfertaController {

	@Autowired
	TipoOfertaRepository tipoOfertaRepository;

	// Crea un tipo de oferta
	@PostMapping(path = "/add")
	public @ResponseBody ResponseEntity<String> addTipoOferta(@RequestParam String nombre,
			@RequestParam String descripcion, @RequestParam Double cantidad, @RequestParam Double porcentaje,
			@RequestParam Integer dias) {

		TipoOferta tipoOferta = new TipoOferta();

		tipoOferta.setIdTipoOferta(0);

		if (nombre.isEmpty() || descripcion.isEmpty()) {
			return ResponseEntity.badRequest().body("No puede haber campos vacíos");
		}

		if (cantidad <= 0) {
			return ResponseEntity.badRequest().body("La cantidad no puede ser 0 o menos");
		}

		if (porcentaje < 0 || porcentaje > 100) {
			return ResponseEntity.badRequest().body("El porcentaje debe estar entre 0% y 100%");
		}

		if (dias <= 0) {
			return ResponseEntity.badRequest().body("El número de días debe ser mínimo 1");
		}

		tipoOferta.setNombre(nombre);
		tipoOferta.setDescripcion(descripcion);
		tipoOferta.setCantidad(cantidad);
		tipoOferta.setPorcentaje(porcentaje);
		tipoOferta.setDias(dias);

		tipoOfertaRepository.save(tipoOferta);

		return new ResponseEntity<>("Tipo de oferta creado correctamente", HttpStatus.OK);
	}

	// Obtiene un tipo de oferta por ID
	@GetMapping(path = "/get")
	public @ResponseBody Optional<TipoOferta> getTipoOferta(@RequestParam int id) {
		return tipoOfertaRepository.findById(id);
	}

	// Obtiene todas los tipo de oferta
	@GetMapping(path = "/all")
	public @ResponseBody Iterable<TipoOferta> getAllTipoOferta() {
		return tipoOfertaRepository.findAll();
	}

	// Modifica un tipo de oferta segun su ID
	@PostMapping(path = "/modificar")
	public @ResponseBody ResponseEntity<String> modificarTipoOferta(@RequestParam Integer idOferta,
			@RequestParam String nombre, @RequestParam String descripcion, @RequestParam Double cantidad,
			@RequestParam Double porcentaje, @RequestParam Integer dias) {

		TipoOferta tipoOferta = new TipoOferta();

		TipoOferta tipoOfertaDummy = tipoOfertaRepository.findById(idOferta).orElse(null);

		if (tipoOfertaDummy == null) {
			return ResponseEntity.badRequest().body("No se ha encontrado el tipo de oferta con ID " + idOferta);
		}

		tipoOferta = tipoOfertaDummy;

		if (nombre.isEmpty() || descripcion.isEmpty()) {
			return ResponseEntity.badRequest().body("No puede haber campos vacíos");
		}

		if (cantidad <= 0) {
			return ResponseEntity.badRequest().body("La cantidad no puede ser 0 o menos");
		}

		if (porcentaje < 0 || porcentaje > 100) {
			return ResponseEntity.badRequest().body("El porcentaje debe estar entre 0% y 100%");
		}

		if (dias <= 0) {
			return ResponseEntity.badRequest().body("El número de días debe ser mínimo 1");
		}

		tipoOferta.setNombre(nombre);
		tipoOferta.setDescripcion(descripcion);
		tipoOferta.setCantidad(cantidad);
		tipoOferta.setPorcentaje(porcentaje);
		tipoOferta.setDias(dias);

		tipoOfertaRepository.save(tipoOferta);

		return new ResponseEntity<>("Tipo de oferta modificado correctamente", HttpStatus.OK);
	}
	
	// Elimina el tipo de oferta segun su ID
	@DeleteMapping(path = "/delete")
	public @ResponseBody ResponseEntity<String> deleteTipoOferta(@RequestParam Integer id) {
		
		TipoOferta tipoOferta = tipoOfertaRepository.findById(id).orElse(null);

		if (tipoOferta == null) {
			return ResponseEntity.badRequest().body("No se encuentra el tipo de oferta con ID " + id.toString());
		}

		tipoOfertaRepository.delete(tipoOferta);
		return ResponseEntity.badRequest().body("Tipo de oferta eliminada con éxito");
	}
}
