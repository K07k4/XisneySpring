package com.erutnecca.xisney.controllers;

import com.erutnecca.xisney.controllers.util.Fecha;
import com.erutnecca.xisney.entities.Espectaculo;
import com.erutnecca.xisney.entities.Parque;
import com.erutnecca.xisney.entities.ReservaEspectaculo;
import com.erutnecca.xisney.entities.Usuario;
import com.erutnecca.xisney.repositories.EspectaculoRepository;
import com.erutnecca.xisney.repositories.ParqueRepository;
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
@RequestMapping(path = "/espectaculo")
public class EspectaculoController {
    @Autowired
    private final EspectaculoRepository espectaculoRepository;
    private final ReservaEspectaculoRepository reservaEspectaculoRepository;
    private final ParqueRepository parqueRepository;
    private final UsuarioRepository usuarioRepository;

    EspectaculoController(EspectaculoRepository espectaculoRepository,
                          ReservaEspectaculoRepository reservaEspectaculoRepository,
                          ParqueRepository parqueRepository, UsuarioRepository usuarioRepository) {
        this.espectaculoRepository = espectaculoRepository;
        this.reservaEspectaculoRepository = reservaEspectaculoRepository;
        this.parqueRepository = parqueRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // [POST] crear espectaculo
    @PostMapping(path = "/add")
    public @ResponseBody
    ResponseEntity<String> addEspectaculo(@RequestParam Integer idParque, @RequestParam String nombre,
                                          @RequestParam String descripcion, @RequestParam String fechaInicio,
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
    public @ResponseBody
    Optional<Espectaculo> getEspectaculo(@RequestParam int id) {
        return espectaculoRepository.findById(id);
    }

    // [GET] obtener todos
    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Espectaculo> getAllEspectaculos() {
        return espectaculoRepository.findAll();
    }

    // [DELETE] eliminar espectaculo
    @DeleteMapping(path = "/delete")
    public @ResponseBody
    ResponseEntity<String> deleteEspectaculo(@RequestParam Integer id) {
        Espectaculo espectaculo = espectaculoRepository.findById(id).orElse(null);

        if (espectaculo == null) {
            return ResponseEntity.badRequest().body("No se encuentra el espectaclo con ID " + id.toString());
        }

        espectaculoRepository.delete(espectaculo);
        return new ResponseEntity<>("Se ha eliminado el espectaculo correctamente", HttpStatus.OK);
    }

    // [POST] reservar
    @PostMapping(path = "/reservar")
    public @ResponseBody
    ResponseEntity<String> reservarEspectaculo(@RequestParam Integer idUsuario,
                                               @RequestParam Integer idEspectaculo,
                                               @RequestParam String fechaReserva,
                                               @RequestParam Integer personas,
                                               @RequestParam String fechaCompra,
                                               @RequestParam boolean consumido) {
        ReservaEspectaculo reservaEspectaculo = new ReservaEspectaculo();
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        Espectaculo espectaculo = espectaculoRepository.findById(idEspectaculo).orElse(null);

        // Comprueba que existan usuario y espectáculo
        if (usuario == null) {
            return ResponseEntity.badRequest().body("No existe el usuario con ID " + idUsuario.toString());
        }

        if (espectaculo == null) {
            return ResponseEntity.badRequest().body("No existe el espectaculo con ID " + idEspectaculo.toString());
        }
        reservaEspectaculo.setIdEvento(idEspectaculo);
        reservaEspectaculo.setIdUsuario(idUsuario);

        // Comprueba que haya al menos una persona
        if (personas < 1) {
            return ResponseEntity.badRequest().body("Se necesita una persona como minimo");
        }
        reservaEspectaculo.setPersonas(personas);

        // Comprueba que fechaReserva sea correcta y que esté en el rango del espectáculo

        Date fechaInicio;
        Date fechaFin;
        try {
            fechaInicio = Fecha.stringFecha(espectaculo.getFechaInicio());
            fechaFin = Fecha.stringFecha(espectaculo.getFechaFin());
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("No se pudieron parsear las fechas del evento con ID "
                    + idEspectaculo.toString());
        }

        Date thisFechaReserva;
        try {
            thisFechaReserva = Fecha.stringFecha(fechaReserva);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("Las fechas no son correctas");
        }

        if (thisFechaReserva.before(fechaFin) || thisFechaReserva.after(fechaFin)) {
            return ResponseEntity.badRequest().body("El espectáculo no se realiza en la fecha seleccionada");
        }
        reservaEspectaculo.setFechaReserva(fechaReserva);
        reservaEspectaculo.setFechaCompra(Fecha.actualPrecisa());
        reservaEspectaculo.setConsumido(false);
        reservaEspectaculoRepository.save(reservaEspectaculo);

        return new ResponseEntity<>("Reserva de espectaculo creada correctamente", HttpStatus.OK);

    }

// [GET] obtener reserva
// [GET] obtener todas reservas de todos espectaculos
// [GET] obtener todas las reservas de un espectaculo
// [GET] obtener todas reservas de un usuario
// [POST] consumir reserva
// [DELETE] eliminar reserva


}
