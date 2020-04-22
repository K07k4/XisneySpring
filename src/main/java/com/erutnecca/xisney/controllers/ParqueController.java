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

import com.erutnecca.xisney.entities.Parque;
import com.erutnecca.xisney.repositories.ParqueRepository;

@Controller
@RequestMapping(path = "/parque")
public class ParqueController {
	@Autowired
	private ParqueRepository parqueRepository;

	// Crea un parque
	@PostMapping(path = "/add")
	public @ResponseBody ResponseEntity<String> addParque(@RequestParam Integer idPais,
			@RequestParam Integer idProvincia, @RequestParam String nombre, @RequestParam String direccion) {

		Parque parque = new Parque();
		parque.setIdParque(0);

		if (!nombre.isEmpty() || !direccion.isEmpty()) {
			parque.setIdPais(idPais);
			parque.setIdProvincia(idProvincia);
			parque.setNombre(nombre);
			parque.setDireccion(direccion);

			parqueRepository.save(parque);

			return new ResponseEntity<>("Parque creado correctamente", HttpStatus.OK);
		} else {
			return ResponseEntity.badRequest().body("Los campos para crear el parque no son válidos");
		}

	}

	// Obtiene un parque por ID
	@GetMapping(path = "/get")
	public @ResponseBody Optional<Parque> getParque(@RequestParam int id) {
		return parqueRepository.findById(id);
	}

	// Obtiene todos los parques
	@GetMapping(path = "/all")
	public @ResponseBody Iterable<Parque> getParques() {
		return parqueRepository.findAll();
	}

	// Modifica un parque según su ID
	@PostMapping(path = "/modificar")
	public @ResponseBody ResponseEntity<String> modificarParque(@RequestParam Integer idParque,
			@RequestParam Integer idPais, @RequestParam Integer idProvincia, @RequestParam String nombre,
			@RequestParam String direccion) {

		Parque parque = parqueRepository.findById(idParque).orElse(null);

		if (parque == null) {
			return ResponseEntity.badRequest().body("No se encuentra el parque con ID " + idParque);
		}

		if (!nombre.isEmpty() || !direccion.isEmpty()) {
			parque.setIdPais(idPais);
			parque.setIdProvincia(idProvincia);
			parque.setNombre(nombre);
			parque.setDireccion(direccion);

			parqueRepository.save(parque);

			return new ResponseEntity<>("Parque modificado correctamente", HttpStatus.OK);
		} else {
			return ResponseEntity.badRequest().body("Los campos para crear el parque no son válidos");
		}

	}

	// Obtiene los parques de un pais
	@GetMapping(path = "/parquesDePais")
	public @ResponseBody List<Parque> getParquesDePais(@RequestParam int id) {
		return parqueRepository.findByIdPais(id);
	}

	// Obtiene los parques de una provincia
	@GetMapping(path = "/parquesDeProvincia")
	public @ResponseBody List<Parque> getParquesDeProvincia(@RequestParam int id) {
		return parqueRepository.findByIdProvincia(id);
	}
	

	// Elimina el parque
	@DeleteMapping(path = "/delete")
	public @ResponseBody ResponseEntity<String> deleteParque(@RequestParam Integer id) {
		Parque parque = parqueRepository.findById(id).orElse(null);

		try {
			parqueRepository.delete(parque);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("No se ha podido encontrar el parque");
		}
		return ResponseEntity.badRequest().body("Parque eliminado con éxito");
	}

}
