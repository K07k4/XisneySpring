package com.erutnecca.xisney.repositories;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import com.erutnecca.xisney.entities.Usuario;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {
	//@Query(value = "SELECT * FROM Usuario WHERE email = ?1", nativeQuery = true)
	
	ArrayList<Usuario> findByEmail(String email);
	ArrayList<Usuario> findByDni(String dni);
	ArrayList<Usuario> findByEmailAndDni(String email, String dni);

	
}