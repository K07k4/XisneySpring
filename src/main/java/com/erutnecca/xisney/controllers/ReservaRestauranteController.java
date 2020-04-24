package com.erutnecca.xisney.controllers;

import com.erutnecca.xisney.controllers.util.Fecha;
import com.erutnecca.xisney.entities.ReservaRestaurante;
import com.erutnecca.xisney.entities.Restaurante;
import com.erutnecca.xisney.entities.Usuario;
import com.erutnecca.xisney.repositories.ReservaRestauranteRepository;
import com.erutnecca.xisney.repositories.RestauranteRepository;
import com.erutnecca.xisney.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping(path = "/reservaRestaurante")
public class ReservaRestauranteController {
    @Autowired
    private final UsuarioRepository usuarioRepository;
    private final RestauranteRepository restauranteRepository;
    private final ReservaRestauranteRepository reservaRestauranteRepository;

    public ReservaRestauranteController(UsuarioRepository usuarioRepository,
                                        RestauranteRepository restauranteRepository,
                                        ReservaRestauranteRepository reservaRestauranteRepository) {
        this.usuarioRepository = usuarioRepository;
        this.restauranteRepository = restauranteRepository;
        this.reservaRestauranteRepository = reservaRestauranteRepository;
    }

    // [POST] reservar
    @PostMapping(path = "/add")
    public @ResponseBody
    ResponseEntity<String> addReservaRestaurante(@RequestParam Integer idUsuario,
                                                 @RequestParam Integer idRestaurante,
                                                 @RequestParam String fechaReserva,
                                                 @RequestParam Integer personas) {
        ReservaRestaurante reservaRestaurante = new ReservaRestaurante();
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        Restaurante restaurante = restauranteRepository.findById(idRestaurante).orElse(null);

        // Comprueba que existan usuario y espectáculo
        if (usuario == null) {
            return ResponseEntity.badRequest().body("No existe el usuario con ID " + idUsuario.toString());
        }

        if (restaurante == null) {
            return ResponseEntity.badRequest().body("No existe el restaurante con ID " + idRestaurante.toString());
        }
        reservaRestaurante.setIdRestaurante(idRestaurante);
        reservaRestaurante.setIdUsuario(idUsuario);

        // Comprueba que haya al menos una persona
        if (personas < 1) {
            return ResponseEntity.badRequest().body("Se necesita una persona como minimo");
        }
        reservaRestaurante.setPersonas(personas);

        // Comprueba que fechaReserva sea correcta y que esté en el rango del espectáculo

        Date fechaFin;
        Date fechaInicio;
        try {
            fechaInicio = Fecha.stringFecha(restaurante.getFechaInicio());
            fechaFin = Fecha.stringFecha(restaurante.getFechaFin());
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("Error al parsear la fecha de cierre del restaurante con ID "
                    + idRestaurante.toString());
        }

        Date thisFechaReserva;
        try {
            thisFechaReserva = Fecha.stringFecha(fechaReserva);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("La fecha de reserva no es correcta");
        }

        if (thisFechaReserva.before(fechaInicio) || thisFechaReserva.after(fechaFin)) {
            return ResponseEntity.badRequest().body("El restaurante no abre en la fecha seleccionada");
        }
        reservaRestaurante.setFechaReserva(fechaReserva);
        reservaRestaurante.setFechaCompra(Fecha.actualPrecisa());
        reservaRestaurante.setConsumido(false);
        reservaRestauranteRepository.save(reservaRestaurante);

        return new ResponseEntity<>("Reserva de restaurante creada correctamente", HttpStatus.OK);

    }

    // [GET] obtener reserva
    @GetMapping(path = "/get")
    public @ResponseBody
    Optional<ReservaRestaurante> getReservaRestaurante(@RequestParam int id) {
        return reservaRestauranteRepository.findById(id);
    }

    // [GET] obtener todas reservas de todos restaurantes
    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<ReservaRestaurante> getAllReservasAllRestaurantes() {
        return reservaRestauranteRepository.findAll();
    }

    // [GET] obtener todas las reservas de un restaurante
    @GetMapping(path = "/getDeRestaurante")
    public @ResponseBody
    Iterable<ReservaRestaurante> getAllReservasDeRestaurante(@RequestParam int id) {
        return reservaRestauranteRepository.findByIdRestaurante(id);
    }

    // [GET] obtener todas reservas de un usuario
    @GetMapping(path = "/getDeUsuario")
    public @ResponseBody
    Iterable<ReservaRestaurante> getAllReservasDeUsuario(@RequestParam int id) {
        return reservaRestauranteRepository.findByIdUsuario(id);
    }

    // [POST] consumir reserva
    @PostMapping(path = "/consumir")
    public @ResponseBody
    ResponseEntity<String> consumirReserva(@RequestParam Integer id) {
        ReservaRestaurante reservaRestaurante = reservaRestauranteRepository.findById(id).orElse(null);

        if (reservaRestaurante == null) {
            return ResponseEntity.badRequest().body("No existe la reserva con ID " + id.toString());
        }

        if (reservaRestaurante.getConsumido()) {
            return ResponseEntity.badRequest().body("La reserva ya se ha utilizado");
        }

        reservaRestaurante.setConsumido(true);
        reservaRestauranteRepository.save(reservaRestaurante);
        return new ResponseEntity<>("Se ha consumido la reserva correctamente", HttpStatus.OK);
    }

    // [DELETE] eliminar reserva
    @DeleteMapping(path = "/delete")
    public @ResponseBody
    ResponseEntity<String> deleteReserva(@RequestParam Integer id) {

        ReservaRestaurante reservaRestaurante = reservaRestauranteRepository.findById(id).orElse(null);

        if (reservaRestaurante == null) {
            return ResponseEntity.badRequest().body("No se encuentra la reserva con ID " + id.toString());
        }

        reservaRestauranteRepository.delete(reservaRestaurante);
        return new ResponseEntity<>("Se ha eliminado la reserva correctamente", HttpStatus.OK);
    }
}
