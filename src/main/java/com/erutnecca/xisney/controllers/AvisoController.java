package com.erutnecca.xisney.controllers;

import com.erutnecca.xisney.entities.Aviso;
import com.erutnecca.xisney.entities.Parque;
import com.erutnecca.xisney.repositories.AvisoRepository;
import com.erutnecca.xisney.repositories.ParqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path = "/aviso")
public class AvisoController {
    @Autowired
    private AvisoRepository avisoRepository;
    private ParqueRepository parqueRepository;

    AvisoController(AvisoRepository avisoRepository, ParqueRepository parqueRepository) {
        this.avisoRepository = avisoRepository;
        this.parqueRepository = parqueRepository;
    }

    // Crea un aviso
    @PostMapping(path = "/add")
    public @ResponseBody
    ResponseEntity<String> addAviso(@RequestParam Integer idParque,
                                    @RequestParam String fecha,
                                    @RequestParam String title,
                                    @RequestParam String text) {

        Aviso aviso = new Aviso();
        Parque parque = parqueRepository.findById(idParque).orElse(null);

        if (parque == null) {
            return ResponseEntity.badRequest().body("No existe el parque con ID " + idParque.toString());
        }

        aviso.setIdAviso(0);
        aviso.setIdParque(idParque);
        aviso.setFecha(fecha);
        aviso.setTitle(title);
        aviso.setText(text);
        avisoRepository.save(aviso);

        return new ResponseEntity<>("Aviso creado correctamente", HttpStatus.OK);
    }

    @GetMapping(path = "/get")
    public @ResponseBody
    Optional<Aviso> getAviso(@RequestParam int id) {
        return avisoRepository.findById(id);
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Aviso> getAllAvisos() {
        return avisoRepository.findAll();
    }


    @DeleteMapping(path = "/delete")
    public @ResponseBody
    ResponseEntity<String> deleteAviso(@RequestParam Integer id) {

        Aviso aviso = avisoRepository.findById(id).orElse(null);

        if (aviso == null) {
            return ResponseEntity.badRequest().body("No se encuentra el aviso con ID " + id.toString());
        }

        avisoRepository.delete(aviso);
        return new ResponseEntity<>("Se ha eliminado el aviso correctamente", HttpStatus.OK);
    }

}
