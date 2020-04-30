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
import com.erutnecca.xisney.entities.Puntuacion;
import com.erutnecca.xisney.entities.Usuario;
import com.erutnecca.xisney.repositories.ArticuloRepository;
import com.erutnecca.xisney.repositories.PuntuacionRepository;
import com.erutnecca.xisney.repositories.TipoArticuloRepository;
import com.erutnecca.xisney.repositories.UsuarioRepository;

@Controller
@RequestMapping(path = "/articulo")
public class ArticuloController {

	@Autowired
	ArticuloRepository articuloRepository;
	TipoArticuloRepository tipoArticuloRepository;
	PuntuacionRepository puntuacionRepository;
	UsuarioRepository usuarioRepository;

	ArticuloController(ArticuloRepository articuloRepository, TipoArticuloRepository tipoArticuloRepository,
			PuntuacionRepository puntuacionRepository, UsuarioRepository usuarioRepository) {
		this.articuloRepository = articuloRepository;
		this.tipoArticuloRepository = tipoArticuloRepository;
		this.puntuacionRepository = puntuacionRepository;
		this.usuarioRepository = usuarioRepository;
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

	@PostMapping(path = "/puntuar")
	public @ResponseBody ResponseEntity<String> puntuar(@RequestParam("idArticulo") int idArticulo,
			@RequestParam("idUsuario") int idUsuario, @RequestParam("puntuacion") Double puntuacion) {
		boolean puntuado = false;

		if (puntuacion < 0.0 || puntuacion > 5.0) {
			return ResponseEntity.badRequest().body("La puntuacion debe ser mínimo 0 y máximo 5");
		}

		Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

		if (usuario == null) {
			return ResponseEntity.badRequest().body("No se encuentra el usuario con ID " + idUsuario);
		}

		Articulo articulo = articuloRepository.findById(idArticulo).orElse(null);

		if (articulo == null) {
			return ResponseEntity.badRequest().body("No se encuentra el artículo con ID " + idArticulo);
		}

		Puntuacion puntuacionItem = new Puntuacion();

		puntuacionItem.setIdPuntuacion(0);
		puntuacionItem.setIdArticulo(idArticulo);
		puntuacionItem.setIdUsuario(idUsuario);
		puntuacionItem.setPuntuacion(puntuacion);

		Puntuacion puntuacionExistente = puntuacionRepository.findByIdUsuarioAndIdArticulo(idUsuario, idArticulo);

		if (puntuacionExistente != null) {
			puntuado = true;

			puntuacionItem.setIdPuntuacion(puntuacionExistente.getIdPuntuacion());
		}

		puntuacionRepository.save(puntuacionItem);

		List<Puntuacion> listPuntuaciones = puntuacionRepository.findByIdArticulo(idArticulo);

		double suma = puntuacion;
		int numeroPuntuaciones = articulo.getNumeroPuntuaciones();

		for (int i = 0; i < numeroPuntuaciones; i++) {
			if (listPuntuaciones.get(i).getIdUsuario() != idUsuario) {
				suma += listPuntuaciones.get(i).getPuntuacion();
			}
		}

		// Si el usuario ya ha puntuado esta receta no se sumará al número total
		if (!puntuado) {
			numeroPuntuaciones++;
		}

		articulo.setNumeroPuntuaciones(numeroPuntuaciones);

		double resultado = suma / numeroPuntuaciones;

		articulo.setPuntuacionMedia(resultado);

		articuloRepository.save(articulo);

		return new ResponseEntity<>("Puntuación realizada", HttpStatus.OK);
	}

}
