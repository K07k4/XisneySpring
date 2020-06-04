package com.erutnecca.xisney.controllers;

import com.erutnecca.xisney.controllers.util.Checker;
import com.erutnecca.xisney.entities.Parque;
import com.erutnecca.xisney.entities.Restaurante;
import com.erutnecca.xisney.repositories.ParqueRepository;
import com.erutnecca.xisney.repositories.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path = "/restaurante")
@CrossOrigin(origins = "*", allowedHeaders = "*")
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
			@RequestParam String nombre, @RequestParam String descripcion, @RequestParam String inicioTramo1,
			@RequestParam String finTramo1, @RequestParam(required = false) String inicioTramo2,
			@RequestParam(required = false) String finTramo2) {

		System.out.println("Crear restaurante");

		Restaurante restaurante = new Restaurante();
		Parque parque = parqueRepository.findById(idParque).orElse(null);

		// Comprueba si existe el parque
		if (parque == null) {
			return ResponseEntity.badRequest().body("No existe el parque con ID " + idParque.toString());
		}

		restaurante.setIdParque(idParque);
		restaurante.setNombre(nombre);
		restaurante.setDescripcion(descripcion);

		if (Checker.horaValida(inicioTramo1) && Checker.horaValida(finTramo1)) {
			restaurante.setInicioTramo1(inicioTramo1);
			restaurante.setFinTramo1(finTramo1);
		} else {
			return ResponseEntity.badRequest().body("El formato de hora el primer tramo es inválido (HH:MM)");
		}

		if (!inicioTramo2.isEmpty() || !finTramo2.isEmpty()) {
			if (Checker.horaValida(inicioTramo2) && Checker.horaValida(finTramo2)) {
				restaurante.setInicioTramo2(inicioTramo2);
				restaurante.setFinTramo2(finTramo2);
			} else {
				return ResponseEntity.badRequest().body("El formato de hora del segundo tramo es inválido (HH:MM)");
			}
		}

		restauranteRepository.save(restaurante);
		return new ResponseEntity<>("Restaurante creado correctamente", HttpStatus.OK);

	}

	// [GET] obtener restaurante
	@GetMapping(path = "/get")
	public @ResponseBody Optional<Restaurante> getRestaurante(@RequestParam int id) {
		System.out.println("Get restaurante " + id);
		return restauranteRepository.findById(id);
	}

	// [GET] obtener todos
	@GetMapping(path = "/all")
	public @ResponseBody Iterable<Restaurante> getAllRestaurantes() {
		System.out.println("Get all restaurantes");
		return restauranteRepository.findAll();
	}

	// [DELETE] eliminar restaurante
	@DeleteMapping(path = "/delete")
	public @ResponseBody ResponseEntity<String> deleteRestaurante(@RequestParam Integer id) {
		System.out.println("Borrar restaurante " + id);
		Restaurante restaurante = restauranteRepository.findById(id).orElse(null);

		if (restaurante == null) {
			return ResponseEntity.badRequest().body("No se encuentra el restaurante con ID " + id.toString());
		}

		restauranteRepository.delete(restaurante);
		return new ResponseEntity<>("Se ha eliminado el restaurante correctamente", HttpStatus.OK);
	}
}