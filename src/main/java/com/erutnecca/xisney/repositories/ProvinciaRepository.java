package com.erutnecca.xisney.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.erutnecca.xisney.entities.Provincia;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ProvinciaRepository extends CrudRepository<Provincia, Integer> {
	List<Provincia> findByIdPais(Integer idPais);
	Provincia findByNombre(String nombre);
	Provincia findByNombreAndIdPais(String nombre, Integer idPais);
	
}