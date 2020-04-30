package com.erutnecca.xisney.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.erutnecca.xisney.entities.Articulo;
import com.erutnecca.xisney.entities.Puntuacion;
import com.erutnecca.xisney.entities.Usuario;
import com.erutnecca.xisney.repositories.ArticuloRepository;
import com.erutnecca.xisney.repositories.PuntuacionRepository;
import com.erutnecca.xisney.repositories.UsuarioRepository;

@Controller
@RequestMapping(path = "/puntuacion")
public class PuntuacionController {
	@Autowired
	private PuntuacionRepository puntuacionRepository;
	private ArticuloRepository articuloRepository;
	private UsuarioRepository usuarioRepository;
	
	PuntuacionController(PuntuacionRepository puntuacionRepository, ArticuloRepository articuloRepository, UsuarioRepository usuarioRepository) {
		this.puntuacionRepository = puntuacionRepository;
		this.articuloRepository = articuloRepository;
		this.usuarioRepository = usuarioRepository;
	}

	@PostMapping(path = "/puntuar")
	public @ResponseBody ResponseEntity<String> puntuar(@RequestParam("idArticulo") int idArticulo,
			@RequestParam("idUsuario") int idUsuario, @RequestParam("puntuacion") Double puntuacion) {
		boolean puntuado = false;
		
		if(puntuacion < 0.0 || puntuacion > 5.0) {
			return ResponseEntity.badRequest().body("La puntuacion debe ser mínimo 0 y máximo 5");
		}
		
		
		Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
		
		if(usuario == null) {
			return ResponseEntity.badRequest().body("No se encuentra el usuario con ID " + idUsuario);
		}
		
		
		Articulo articulo = articuloRepository.findById(idArticulo).orElse(null);
		
		if(articulo == null) {
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
		

		double puntuacionMedia = articulo.getPuntuacionMedia();
		
		
		List<Puntuacion> listPuntuaciones = puntuacionRepository.findByIdArticulo(idArticulo);
		
		
		double suma = puntuacion;
		int numeroPuntuaciones = articulo.getNumeroPuntuaciones();
		
		
		for (int i = 0; i < numeroPuntuaciones; i++) {
			if(listPuntuaciones.get(i).getIdUsuario() != idUsuario) {
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
