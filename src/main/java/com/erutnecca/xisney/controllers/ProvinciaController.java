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

import com.erutnecca.xisney.entities.Pais;
import com.erutnecca.xisney.entities.Provincia;
import com.erutnecca.xisney.repositories.PaisRepository;
import com.erutnecca.xisney.repositories.ProvinciaRepository;

@Controller
@RequestMapping(path = "/provincia")
public class ProvinciaController {
	@Autowired
	private ProvinciaRepository provinciaRepository;
	private PaisRepository paisRepository;

	ProvinciaController(ProvinciaRepository provinciaRepository, PaisRepository paisRepository) {
		this.provinciaRepository = provinciaRepository;
		this.paisRepository = paisRepository;
	}

	// Crea una provincia
	@PostMapping(path = "/add")
	public @ResponseBody ResponseEntity<String> addProvincia(@RequestParam Integer idPais,
			@RequestParam String nombre) {

		Provincia provincia = new Provincia();

		provincia.setIdProvincia(0);

		Pais paisDummy = paisRepository.findById(idPais).orElse(null);

		if (paisDummy == null) {
			return ResponseEntity.badRequest().body("No existe ningún país con ID " + idPais);
		}

		provincia.setIdPais(idPais);

		Provincia provinciaDummy = provinciaRepository.findByNombreAndIdPais(nombre, idPais);
		if (provinciaDummy == null) {
			provincia.setNombre(nombre);
		} else {
			return ResponseEntity.badRequest().body("La provincia ya está registrada");
		}

		System.out.println(provincia);
		provinciaRepository.save(provincia);

		return new ResponseEntity<>("Provincia creada correctamente", HttpStatus.OK);
	}

	// Obtiene una provincia por ID
	@GetMapping(path = "/get")
	public @ResponseBody Optional<Provincia> getProvincia(@RequestParam int id) {
		return provinciaRepository.findById(id);
	}

	// Obtiene todas las provincias
	@GetMapping(path = "/all")
	public @ResponseBody Iterable<Provincia> getAllProvinciaes() {
		return provinciaRepository.findAll();
	}

	// Modifica el nombre de la provincia según su ID
	@PostMapping(path = "/modificar")
	public @ResponseBody ResponseEntity<String> modificarProvincia(@RequestParam Integer id,
			@RequestParam String nombre) {

		Provincia provincia = provinciaRepository.findById(id).orElse(null);

		if (provincia == null) {
			return ResponseEntity.badRequest().body("No se encuentra la provincia con ID " + id.toString());
		}

		if (!nombre.isEmpty()) {
			provincia.setNombre(nombre);
		} else {
			return ResponseEntity.badRequest().body("El nombre no es válido");
		}

		provinciaRepository.save(provincia);

		return new ResponseEntity<>("Provincia modificada correctamente", HttpStatus.OK);
	}

	// Elimina la entrada del blog según ID
	@DeleteMapping(path = "/delete")
	public @ResponseBody ResponseEntity<String> deleteProvincia(@RequestParam Integer id) {
		Provincia provincia = provinciaRepository.findById(id).orElse(null);

		if (provincia == null) {
			return ResponseEntity.badRequest().body("No se encuentra la provincia con ID " + id.toString());
		}

		provinciaRepository.delete(provincia);
		return ResponseEntity.badRequest().body("Provincia eliminada con éxito");
	}

}
