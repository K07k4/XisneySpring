package com.erutnecca.xisney.controllers;

import java.text.ParseException;
import java.util.Date;
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
import com.erutnecca.xisney.entities.EntradaParque;
import com.erutnecca.xisney.entities.Parque;
import com.erutnecca.xisney.entities.TipoOferta;
import com.erutnecca.xisney.entities.Usuario;
import com.erutnecca.xisney.repositories.EntradaParqueRepository;
import com.erutnecca.xisney.repositories.ParqueRepository;
import com.erutnecca.xisney.repositories.TipoOfertaRepository;
import com.erutnecca.xisney.repositories.UsuarioRepository;

@Controller
@RequestMapping(path = "/entrada")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EntradaParqueController {
	@Autowired
	private EntradaParqueRepository entradaParqueRepository;
	private TipoOfertaRepository tipoOfertaRepository;
	private UsuarioRepository usuarioRepository;
	private ParqueRepository parqueRepository;

	EntradaParqueController(EntradaParqueRepository entradaParqueRepository, TipoOfertaRepository tipoOfertaRepository,
			UsuarioRepository usuarioRepository, ParqueRepository parqueRepository) {
		this.entradaParqueRepository = entradaParqueRepository;
		this.tipoOfertaRepository = tipoOfertaRepository;
		this.usuarioRepository = usuarioRepository;
		this.parqueRepository = parqueRepository;
	}

	// Crea/Saca una entrada
	@PostMapping(path = "/add")
	public @ResponseBody ResponseEntity<String> addEntradaParque(@RequestParam Integer idUsuario,
			@RequestParam Integer idTipoOferta, @RequestParam Integer idParque, @RequestParam String fechaInicio,
			@RequestParam String fechaFin) {

		EntradaParque entradaParque = new EntradaParque();
		TipoOferta tipoOferta = tipoOfertaRepository.findById(idTipoOferta).orElse(null);
		Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
		Parque parque = parqueRepository.findById(idParque).orElse(null);

		// Comprueba si existen el tipo de oferta, usuario y parque
		if (tipoOferta == null) {
			return ResponseEntity.badRequest().body("No existe el tipo de oferta con ID " + idTipoOferta.toString());
		}

		entradaParque.setIdTipoOferta(idTipoOferta);

		if (usuario == null) {
			return ResponseEntity.badRequest().body("No existe el usuario con ID " + idUsuario.toString());
		}

		entradaParque.setIdUsuario(idUsuario);

		if (parque == null) {
			return ResponseEntity.badRequest().body("No existe el parque con ID " + idParque.toString());
		}

		entradaParque.setIdParque(idParque);

		entradaParque.setIdEntradaParque(0);

		Date fecha1;
		Date fecha2;

		// Se comprueban que las fechas son correctas
		try {
			fecha1 = Fecha.stringFecha(fechaInicio);
			fecha2 = Fecha.stringFecha(fechaFin);
		} catch (ParseException e) {
			return ResponseEntity.badRequest().body("Las fechas no son correctas");
		}

		entradaParque.setFechaInicio(fechaInicio);
		entradaParque.setFechaFin(fechaFin);

		// Se calcula la diferencia de días
		int diferenciaDias = Fecha.diferenciaDias(fecha1, fecha2);

		// Si es menor de 0 significa que la fecha final es anterior a la de inicio
		if (diferenciaDias < 0) {
			return ResponseEntity.badRequest().body("La fecha final es anterior a la de inicio");
		}

		Double precio = 0.0;

		if (tipoOferta.getDias() == 0) { // Si los días dentro del tipo de oferta es 0, significa que se puede asignar
											// un número ilimitado de días
			precio = tipoOferta.getCantidad() * diferenciaDias * (tipoOferta.getPorcentaje() / 100);

		} else if (tipoOferta.getDias() == diferenciaDias) { // Si no fuese 0, el número de días del tipo de oferta debe
																// coincidir con la diferencia de días
			precio = tipoOferta.getCantidad() * tipoOferta.getDias() * (tipoOferta.getPorcentaje() / 100);
		} else { // Si no se cumplen esas condiciones significa que la oferta no es aplicable a
					// ese número de días
			return ResponseEntity.badRequest()
					.body("La oferta debe aplicarse a " + tipoOferta.getDias() + " día/s, no a " + diferenciaDias);
		}

		entradaParque.setPrecioFinal(precio);
		entradaParque.setConsumido(false);
		entradaParque.setFechaCompra(Fecha.actualPrecisa());
		entradaParqueRepository.save(entradaParque);

		return new ResponseEntity<>("Entrada de parque creada correctamente", HttpStatus.OK);
	}

	// Obtiene una entrada por ID
	@GetMapping(path = "/get")
	public @ResponseBody Optional<EntradaParque> getEntradaParque(@RequestParam int id) {
		return entradaParqueRepository.findById(id);
	}

	// Obtiene todas las entradas
	@GetMapping(path = "/all")
	public @ResponseBody Iterable<EntradaParque> getAllEntradasParque() {
		return entradaParqueRepository.findAll();
	}

	// Obtiene todas las entradas
	@GetMapping(path = "/getDeUsuario")
	public @ResponseBody Iterable<EntradaParque> getEntradasDeUsuario(@RequestParam int id) {
		return entradaParqueRepository.findByIdUsuario(id);
	}

	// Busca un rango de fecha y devolverá una lista de entradas que coincidan
	// mínimo un día
	@GetMapping(path = "/getConRango")
	public @ResponseBody Iterable<EntradaParque> getEntradaConRango(@RequestParam String fechaInicio,
			@RequestParam String fechaFin) {
		try {
			@SuppressWarnings("unused")
			Date fecha1 = Fecha.stringFecha(fechaInicio);
			@SuppressWarnings("unused")
			Date fecha2 = Fecha.stringFecha(fechaFin);

			return entradaParqueRepository.findByDateRange(fechaInicio, fechaFin);
		} catch (ParseException e) {
			return null;
		}
	}

	// Consume la entrada, pasando de false a true
	@PostMapping(path = "/consumir")
	public @ResponseBody ResponseEntity<String> consumirEntrada(@RequestParam Integer id) {
		EntradaParque entradaParque = entradaParqueRepository.findById(id).orElse(null);

		if (entradaParque == null) {
			return ResponseEntity.badRequest().body("No existe la entrada con ID " + id.toString());
		}

		if (entradaParque.getConsumido() == true) {
			return ResponseEntity.badRequest().body("La entrada ya se ha utilizado");
		}

		entradaParque.setConsumido(true);
		entradaParqueRepository.save(entradaParque);
		return new ResponseEntity<>("Se ha consumido la entrada correctamente", HttpStatus.OK);
	}

	// Elimina la entrada segun su ID
	@DeleteMapping(path = "/delete")
	public @ResponseBody ResponseEntity<String> deleteEntrada(@RequestParam Integer id) {

		EntradaParque entradaParque = entradaParqueRepository.findById(id).orElse(null);

		if (entradaParque == null) {
			return ResponseEntity.badRequest().body("No se encuentra la entrada con ID " + id.toString());
		}

		entradaParqueRepository.delete(entradaParque);
		return new ResponseEntity<>("Se ha eliminado la entrada correctamente", HttpStatus.OK);
	}

}
