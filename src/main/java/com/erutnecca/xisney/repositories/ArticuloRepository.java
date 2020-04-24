package com.erutnecca.xisney.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.erutnecca.xisney.entities.Articulo;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ArticuloRepository extends CrudRepository<Articulo, Integer> {
	List<Articulo> findByIdTipoArticulo(Integer idTipoArticulo);

}