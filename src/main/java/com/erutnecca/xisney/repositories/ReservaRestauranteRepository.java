package com.erutnecca.xisney.repositories;

import com.erutnecca.xisney.entities.ReservaRestaurante;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ReservaRestauranteRepository extends CrudRepository<ReservaRestaurante, Integer> {
    List<ReservaRestaurante> findByIdRestaurante(int id);

    List<ReservaRestaurante> findByIdUsuario(int id);
}