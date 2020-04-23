package com.erutnecca.xisney.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.erutnecca.xisney.entities.EntradaParque;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface EntradaParqueRepository extends CrudRepository<EntradaParque, Integer> {
	List<EntradaParque> findByIdUsuario(int id);

	@Query(value = "SELECT * FROM entrada_parque WHERE NOT (fecha_inicio > ?2 OR fecha_fin < ?1)", nativeQuery = true)
	List<EntradaParque> findByDateRange(String fechaInicio, String fechaFin);

}