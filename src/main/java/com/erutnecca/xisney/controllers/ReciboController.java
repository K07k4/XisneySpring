package com.erutnecca.xisney.controllers;

import java.util.ArrayList;
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

import com.erutnecca.xisney.controllers.classes.JsonArticuloRecibo;
import com.erutnecca.xisney.controllers.util.Fecha;
import com.erutnecca.xisney.entities.Articulo;
import com.erutnecca.xisney.entities.ArticuloRecibo;
import com.erutnecca.xisney.entities.Recibo;
import com.erutnecca.xisney.entities.Usuario;
import com.erutnecca.xisney.repositories.ArticuloReciboRepository;
import com.erutnecca.xisney.repositories.ArticuloRepository;
import com.erutnecca.xisney.repositories.ReciboRepository;
import com.erutnecca.xisney.repositories.UsuarioRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(path = "/recibo")
public class ReciboController {
	@Autowired
	private final ArticuloReciboRepository articuloReciboRepository;
	private final ReciboRepository reciboRepository;
	private final ArticuloRepository articuloRepository;
	private final UsuarioRepository usuarioRepository;

	ReciboController(ArticuloReciboRepository articuloReciboRepository, ReciboRepository reciboRepository,
			ArticuloRepository articuloRepository, UsuarioRepository usuarioRepository) {
		this.articuloReciboRepository = articuloReciboRepository;
		this.reciboRepository = reciboRepository;
		this.articuloRepository = articuloRepository;
		this.usuarioRepository = usuarioRepository;
	}

	// Crea un recibo recibiendo el id de usuario y el objeto strArticulosRecibo,
	// que es un JSON, una lista que almacena el id del articulo y la cantidad de
	// cada uno
	@PostMapping(path = "/create")
	public @ResponseBody ResponseEntity<String> createRecibo(@RequestParam Integer idUsuario,
			@RequestParam String strArticulosRecibo) {
		Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

		if (usuario == null) {
			return ResponseEntity.badRequest().body("No existe el usuario con ID " + idUsuario);
		}

		List<JsonArticuloRecibo> jsonArticulosRecibo = null;
		ObjectMapper mapper = new ObjectMapper();

		// Mapea el JSON a la clase JsonArticuloRecibo
		try {
			jsonArticulosRecibo = mapper.readValue(strArticulosRecibo, new TypeReference<List<JsonArticuloRecibo>>() {
			});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("El JSON es inválido");
		}

		Recibo recibo = new Recibo();

		String fecha = Fecha.actualPrecisa();

		recibo.setIdRecibo(0);
		recibo.setIdUsuario(idUsuario);
		recibo.setPrecioTotal(0.0);
		recibo.setFechaCompra(fecha);

		// Guarda el recibo
		reciboRepository.save(recibo);

		// Recupera el recibo con el id asignado por la base de datos
		recibo = reciboRepository.findByIdUsuarioAndPrecioTotalAndFechaCompra(idUsuario, 0.0, fecha);

		double precioFinal = 0.0;

		// Lista de articulos que se van a comprar
		List<Articulo> articuloList = new ArrayList<>();

		List<Integer> cantidadesList = new ArrayList<>();

		// Busca cada artículo, comprueba que exista y suma el precio por su cantidad
		for (JsonArticuloRecibo jsonArticuloRecibo : jsonArticulosRecibo) {
			Articulo articulo = articuloRepository.findById(jsonArticuloRecibo.getIdArticulo()).orElse(null);

			if (jsonArticuloRecibo.getCantidad() <= 0) {
				reciboRepository.delete(recibo);
				return ResponseEntity.badRequest().body("La cantidad de cada artículo debe ser mayor que cero");
			}

			if (articulo == null) {
				reciboRepository.delete(recibo);
				return ResponseEntity.badRequest()
						.body("El artículo con ID [" + jsonArticuloRecibo.getIdArticulo() + "] no existe");
			}

			if (articulo.getStock() < jsonArticuloRecibo.getCantidad()) {
				reciboRepository.delete(recibo);
				return ResponseEntity.badRequest()
						.body("No hay suficiente stock del articulo con ID " + articulo.getIdArticulo());
			}

			ArticuloRecibo articuloRecibo = new ArticuloRecibo();

			articuloRecibo.setIdArticuloRecibo(0);
			articuloRecibo.setIdArticulo(jsonArticuloRecibo.getIdArticulo());
			articuloRecibo.setIdRecibo(recibo.getIdRecibo());
			articuloRecibo.setPrecioUnitario(articulo.getPrecio());
			articuloRecibo.setCantidad(jsonArticuloRecibo.getCantidad());

			articuloReciboRepository.save(articuloRecibo);

			articuloList.add(articulo);
			cantidadesList.add(articuloRecibo.getCantidad());

			precioFinal += jsonArticuloRecibo.getCantidad() * articulo.getPrecio();
		}

		for (int i = 0; i < articuloList.size(); i++) {
			articuloList.get(i).setStock(articuloList.get(i).getStock() - cantidadesList.get(i));
			articuloRepository.save(articuloList.get(i));
		}

		recibo.setPrecioTotal(precioFinal);

		reciboRepository.save(recibo);

		return new ResponseEntity<>("Recibo creado correctamente", HttpStatus.OK);
	}

	// Obtiene un recibo por ID
	@GetMapping(path = "/get")
	public @ResponseBody Optional<Recibo> getRecibo(@RequestParam int id) {
		return reciboRepository.findById(id);
	}

	// Obtiene todos los recibos
	@GetMapping(path = "/all")
	public @ResponseBody Iterable<Recibo> getAllRecibos() {
		return reciboRepository.findAll();
	}

	// Obtiene todos los recibos del usuario
	@GetMapping(path = "/deUsuario")
	public @ResponseBody Iterable<Recibo> getRecibosUsuario(@RequestParam int id) {
		return reciboRepository.findAll();
	}

	// Elimina el recibo según ID
	// Elimina los ArticuloRecibo en cascada
	@DeleteMapping(path = "/delete")
	public @ResponseBody ResponseEntity<String> deleteRecibo(@RequestParam Integer id) {
		Recibo recibo = reciboRepository.findById(id).orElse(null);

		if (recibo == null) {
			return ResponseEntity.badRequest().body("No se encuentra el recibo con ID " + id.toString());
		}

		reciboRepository.delete(recibo);
		return ResponseEntity.badRequest().body("Recibo eliminado con éxito");
	}

}
