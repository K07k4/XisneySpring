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

import com.erutnecca.xisney.entities.Pais;
import com.erutnecca.xisney.entities.Provincia;
import com.erutnecca.xisney.repositories.PaisRepository;
import com.erutnecca.xisney.repositories.ProvinciaRepository;

@Controller
@RequestMapping(path = "/pais")
public class PaisController {
	@Autowired
	private PaisRepository paisRepository;
	private ProvinciaRepository provinciaRepository;

	PaisController(PaisRepository paisRepository, ProvinciaRepository provinciaRepository) {
		this.paisRepository = paisRepository;
		this.provinciaRepository = provinciaRepository;
	}

	// Crea un pais
	@PostMapping(path = "/add")
	public @ResponseBody ResponseEntity<String> addPais(@RequestParam String nombre) {

		Pais pais = new Pais();

		pais.setIdPais(0);

		Pais paisDummy = paisRepository.findByNombre(nombre);
		if (paisDummy == null) {
			pais.setNombre(nombre);
		} else {
			return ResponseEntity.badRequest().body("El país ya está registrado");
		}

		paisRepository.save(pais);

		return new ResponseEntity<>("País creado correctamente", HttpStatus.OK);
	}

	// Obtiene un pais según el ID
	@GetMapping(path = "/get")
	public @ResponseBody Optional<Pais> getPais(@RequestParam int id) {
		return paisRepository.findById(id);
	}

	// Obtiene todos los paises
	@GetMapping(path = "/all")
	public @ResponseBody Iterable<Pais> getAllPaises() {
		return paisRepository.findAll();
	}

	// Obtiene todas las provincias del país según su ID
	@GetMapping(path = "/getProvincias")
	public @ResponseBody List<Provincia> getProvincias(@RequestParam int id) {
		return provinciaRepository.findByIdPais(id);
	}

	// Modifica el nombre del país según el ID
	@PostMapping(path = "/modificar")
	public @ResponseBody ResponseEntity<String> modificarPais(@RequestParam Integer id, @RequestParam String nombre) {

		Pais pais = paisRepository.findById(id).orElse(null);

		if (pais == null) {
			return ResponseEntity.badRequest().body("No se encuentra el pais con ID " + id.toString());
		}

		if (!nombre.isEmpty()) {
			pais.setNombre(nombre);
		} else {
			return ResponseEntity.badRequest().body("El nombre no es válido");
		}

		paisRepository.save(pais);

		return new ResponseEntity<>("País modificado correctamente", HttpStatus.OK);
	}

	// Elimina el país según el ID y sus provincias en cascada
	@DeleteMapping(path = "/delete")
	public @ResponseBody ResponseEntity<String> deletePais(@RequestParam Integer id) {
		Pais pais = paisRepository.findById(id).orElse(null);

		if (pais == null) {
			return ResponseEntity.badRequest().body("No se encuentra el país con ID " + id.toString());
		}

		paisRepository.delete(pais);
		return ResponseEntity.badRequest().body("País eliminado con éxito");
	}

}
