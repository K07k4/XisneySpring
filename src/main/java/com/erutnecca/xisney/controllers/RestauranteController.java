package com.erutnecca.xisney.controllers;

import com.erutnecca.xisney.controllers.util.Fecha;
import com.erutnecca.xisney.entities.Parque;
import com.erutnecca.xisney.entities.Restaurante;
import com.erutnecca.xisney.repositories.ParqueRepository;
import com.erutnecca.xisney.repositories.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping(path = "/restaurante")
public class RestauranteController {
	@Autowired
	private final RestauranteRepository restauranteRepository;
	private final ParqueRepository parqueRepository;

	RestauranteController(RestauranteRepository restauranteRepository, ParqueRepository parqueRepository) {
		this.restauranteRepository = restauranteRepository;
		this.parqueRepository = parqueRepository;
	}

	// [POST] crear restaurante
	@PostMapping(path = "/add")
	public @ResponseBody ResponseEntity<String> addRestaurante(@RequestParam Integer idParque,
			@RequestParam String nombre, @RequestParam String descripcion, @RequestParam String fechaInicio,
			@RequestParam String fechaFin) {

		Restaurante restaurante = new Restaurante();
		Parque parque = parqueRepository.findById(idParque).orElse(null);

		// Comprueba si existe el parque
		if (parque == null) {
			return ResponseEntity.badRequest().body("No existe el parque con ID " + idParque.toString());
		}

		restaurante.setIdParque(idParque);
		restaurante.setNombre(nombre);
		restaurante.setDescripcion(descripcion);

		Date fecha1;
		Date fecha2;

		// Se comprueban que las fechas son correctas
		try {
			fecha1 = Fecha.stringFecha(fechaInicio);
			fecha2 = Fecha.stringFecha(fechaFin);
		} catch (ParseException e) {
			return ResponseEntity.badRequest().body("Las fechas no son correctas");
		}

		restaurante.setFechaInicio(fechaInicio);
		restaurante.setFechaFin(fechaFin);

		// Se calcula la diferencia de días
		int diferenciaDias = Fecha.diferenciaDias(fecha1, fecha2);

		// Si es menor de 0 significa que la fecha final es anterior a la de inicio
		if (diferenciaDias < 0) {
			return ResponseEntity.badRequest().body("La fecha final es anterior a la de inicio");
		}

		restauranteRepository.save(restaurante);
		return new ResponseEntity<>("Restaurante creado correctamente", HttpStatus.OK);

	}

	// [GET] obtener restaurante
	@GetMapping(path = "/get")
	public @ResponseBody Optional<Restaurante> getRestaurante(@RequestParam int id) {
		return restauranteRepository.findById(id);
	}

	// [GET] obtener todos
	@GetMapping(path = "/all")
	public @ResponseBody Iterable<Restaurante> getAllRestaurantes() {
		return restauranteRepository.findAll();
	}

	// [DELETE] eliminar restaurante
	@DeleteMapping(path = "/delete")
	public @ResponseBody ResponseEntity<String> deleteRestaurante(@RequestParam Integer id) {
		Restaurante restaurante = restauranteRepository.findById(id).orElse(null);

		if (restaurante == null) {
			return ResponseEntity.badRequest().body("No se encuentra el restaurante con ID " + id.toString());
		}

		restauranteRepository.delete(restaurante);
		return new ResponseEntity<>("Se ha eliminado el restaurante correctamente", HttpStatus.OK);
	}
}