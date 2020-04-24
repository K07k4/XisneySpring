package com.erutnecca.xisney.repositories;

import com.erutnecca.xisney.entities.ReservaEspectaculo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ReservaEspectaculoRepository extends CrudRepository<ReservaEspectaculo, Integer> {
    List<ReservaEspectaculo> findByIdEvento(int id);

    List<ReservaEspectaculo> findByIdUsuario(int id);

}