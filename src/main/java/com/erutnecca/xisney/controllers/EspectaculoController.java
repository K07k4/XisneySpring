package com.erutnecca.xisney.controllers;

import com.erutnecca.xisney.controllers.util.Fecha;
import com.erutnecca.xisney.entities.Espectaculo;
import com.erutnecca.xisney.entities.Parque;
import com.erutnecca.xisney.repositories.EspectaculoRepository;
import com.erutnecca.xisney.repositories.ParqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping(path = "/espectaculo")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EspectaculoController {
	@Autowired
	private final EspectaculoRepository espectaculoRepository;
	private final ParqueRepository parqueRepository;

	EspectaculoController(EspectaculoRepository espectaculoRepository, ParqueRepository parqueRepository) {
		this.espectaculoRepository = espectaculoRepository;
		this.parqueRepository = parqueRepository;
	}

	// [POST] crear espectaculo
	@PostMapping(path = "/add")
	public @ResponseBody ResponseEntity<String> addEspectaculo(@RequestParam Integer idParque,
			@RequestParam String nombre, @RequestParam String descripcion, @RequestParam String fechaInicio,
			@RequestParam String fechaFin) {

		Espectaculo espectaculo = new Espectaculo();
		Parque parque = parqueRepository.findById(idParque).orElse(null);

		// Comprueba si existe el parque
		if (parque == null) {
			return ResponseEntity.badRequest().body("No existe el parque con ID " + idParque.toString());
		}

		espectaculo.setIdParque(idParque);
		espectaculo.setNombre(nombre);
		espectaculo.setDescripcion(descripcion);

		Date fecha1;
		Date fecha2;

		// Se comprueban que las fechas son correctas
		try {
			fecha1 = Fecha.stringFecha(fechaInicio);
			fecha2 = Fecha.stringFecha(fechaFin);
		} catch (ParseException e) {
			return ResponseEntity.badRequest().body("Las fechas no son correctas");
		}

		espectaculo.setFechaInicio(fechaInicio);
		espectaculo.setFechaFin(fechaFin);

		// Se calcula la diferencia de días
		int diferenciaDias = Fecha.diferenciaDias(fecha1, fecha2);

		// Si es menor de 0 significa que la fecha final es anterior a la de inicio
		if (diferenciaDias < 0) {
			return ResponseEntity.badRequest().body("La fecha final es anterior a la de inicio");
		}

		espectaculoRepository.save(espectaculo);
		return new ResponseEntity<>("Espectaculo creado correctamente", HttpStatus.OK);

	}

	// [GET] obtener espectaculo
	@GetMapping(path = "/get")
	public @ResponseBody Optional<Espectaculo> getEspectaculo(@RequestParam int id) {
		return espectaculoRepository.findById(id);
	}

	// [GET] obtener todos
	@GetMapping(path = "/all")
	public @ResponseBody Iterable<Espectaculo> getAllEspectaculos() {
		return espectaculoRepository.findAll();
	}

	// [DELETE] eliminar espectaculo
	@DeleteMapping(path = "/delete")
	public @ResponseBody ResponseEntity<String> deleteEspectaculo(@RequestParam Integer id) {
		Espectaculo espectaculo = espectaculoRepository.findById(id).orElse(null);

		if (espectaculo == null) {
			return ResponseEntity.badRequest().body("No se encuentra el espectaculo con ID " + id.toString());
		}

		espectaculoRepository.delete(espectaculo);
		return new ResponseEntity<>("Se ha eliminado el espectaculo correctamente", HttpStatus.OK);
	}
}