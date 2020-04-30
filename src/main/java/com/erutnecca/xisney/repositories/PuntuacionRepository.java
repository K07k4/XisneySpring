package com.erutnecca.xisney.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.erutnecca.xisney.entities.Puntuacion;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface PuntuacionRepository extends CrudRepository<Puntuacion, Integer> {
	Puntuacion findByIdUsuarioAndIdArticulo(Integer idUsuario, Integer idArticulo);
	List<Puntuacion> findByIdArticulo(Integer idArticulo);
	
}