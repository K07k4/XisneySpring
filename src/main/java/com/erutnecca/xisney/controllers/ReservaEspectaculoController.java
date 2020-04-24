package com.erutnecca.xisney.controllers;

import com.erutnecca.xisney.controllers.util.Fecha;
import com.erutnecca.xisney.entities.Espectaculo;
import com.erutnecca.xisney.entities.ReservaEspectaculo;
import com.erutnecca.xisney.entities.Usuario;
import com.erutnecca.xisney.repositories.EspectaculoRepository;
import com.erutnecca.xisney.repositories.ReservaEspectaculoRepository;
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
@RequestMapping(path = "/reservaEspectaculo")
public class ReservaEspectaculoController {
    @Autowired
    private final UsuarioRepository usuarioRepository;
    private final EspectaculoRepository espectaculoRepository;
    private final ReservaEspectaculoRepository reservaEspectaculoRepository;

    public ReservaEspectaculoController(UsuarioRepository usuarioRepository,
                                        EspectaculoRepository espectaculoRepository,
                                        ReservaEspectaculoRepository reservaEspectaculoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.espectaculoRepository = espectaculoRepository;
        this.reservaEspectaculoRepository = reservaEspectaculoRepository;
    }

    // [POST] reservar
    @PostMapping(path = "/add")
    public @ResponseBody
    ResponseEntity<String> addReservaEspectaculo(@RequestParam Integer idUsuario,
                                                 @RequestParam Integer idEvento,
                                                 @RequestParam String fechaReserva,
                                                 @RequestParam Integer personas) {
        ReservaEspectaculo reservaEspectaculo = new ReservaEspectaculo();
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        Espectaculo espectaculo = espectaculoRepository.findById(idEvento).orElse(null);

        // Comprueba que existan usuario y espectáculo
        if (usuario == null) {
            return ResponseEntity.badRequest().body("No existe el usuario con ID " + idUsuario.toString());
        }

        if (espectaculo == null) {
            return ResponseEntity.badRequest().body("No existe el espectaculo con ID " + idEvento.toString());
        }
        reservaEspectaculo.setIdEvento(idEvento);
        reservaEspectaculo.setIdUsuario(idUsuario);

        // Comprueba que haya al menos una persona
        if (personas < 1) {
            return ResponseEntity.badRequest().body("Se necesita una persona como minimo");
        }
        reservaEspectaculo.setPersonas(personas);

        // Comprueba que fechaReserva sea correcta y que esté en el rango del espectáculo

        Date fechaFin;
        Date fechaInicio;
        try {
            fechaInicio = Fecha.stringFecha(espectaculo.getFechaInicio());
            fechaFin = Fecha.stringFecha(espectaculo.getFechaFin());
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("Error al parsear la fecha de fin del evento con ID "
                    + idEvento.toString());
        }

        Date thisFechaReserva;
        try {
            thisFechaReserva = Fecha.stringFecha(fechaReserva);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("La fecha de reserva no es correcta");
        }

        if (thisFechaReserva.before(fechaInicio) || thisFechaReserva.after(fechaFin)) {
            return ResponseEntity.badRequest().body("El espectáculo no se realiza en la fecha seleccionada");
        }
        reservaEspectaculo.setFechaReserva(fechaReserva);
        reservaEspectaculo.setFechaCompra(Fecha.actualPrecisa());
        reservaEspectaculo.setConsumido(false);
        reservaEspectaculoRepository.save(reservaEspectaculo);

        return new ResponseEntity<>("Reserva de espectaculo creada correctamente", HttpStatus.OK);

    }

    // [GET] obtener reserva
    @GetMapping(path = "/get")
    public @ResponseBody
    Optional<ReservaEspectaculo> getReservaEspectaculo(@RequestParam int id) {
        return reservaEspectaculoRepository.findById(id);
    }

    // [GET] obtener todas reservas de todos espectaculos
    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<ReservaEspectaculo> getAllReservasAllEspectaculos() {
        return reservaEspectaculoRepository.findAll();
    }

    // [GET] obtener todas las reservas de un espectaculo
    @GetMapping(path = "/getDeEspectaculo")
    public @ResponseBody
    Iterable<ReservaEspectaculo> getAllReservasDeEspectaculo(@RequestParam int id) {
        return reservaEspectaculoRepository.findByIdEvento(id);
    }

    // [GET] obtener todas reservas de un usuario
    @GetMapping(path = "/getDeUsuario")
    public @ResponseBody
    Iterable<ReservaEspectaculo> getAllReservasDeUsuario(@RequestParam int id) {
        return reservaEspectaculoRepository.findByIdUsuario(id);
    }

    // [POST] consumir reserva
    @PostMapping(path = "/consumir")
    public @ResponseBody
    ResponseEntity<String> consumirReserva(@RequestParam Integer id) {
        ReservaEspectaculo reservaEspectaculo = reservaEspectaculoRepository.findById(id).orElse(null);

        if (reservaEspectaculo == null) {
            return ResponseEntity.badRequest().body("No existe la reserva con ID " + id.toString());
        }

        if (reservaEspectaculo.getConsumido()) {
            return ResponseEntity.badRequest().body("La reserva ya se ha utilizado");
        }

        reservaEspectaculo.setConsumido(true);
        reservaEspectaculoRepository.save(reservaEspectaculo);
        return new ResponseEntity<>("Se ha consumido la reserva correctamente", HttpStatus.OK);
    }

    // [DELETE] eliminar reserva
    @DeleteMapping(path = "/delete")
    public @ResponseBody
    ResponseEntity<String> deleteReserva(@RequestParam Integer id) {

        ReservaEspectaculo reservaEspectaculo = reservaEspectaculoRepository.findById(id).orElse(null);

        if (reservaEspectaculo == null) {
            return ResponseEntity.badRequest().body("No se encuentra la reserva con ID " + id.toString());
        }

        reservaEspectaculoRepository.delete(reservaEspectaculo);
        return new ResponseEntity<>("Se ha eliminado la reserva correctamente", HttpStatus.OK);
    }
}
