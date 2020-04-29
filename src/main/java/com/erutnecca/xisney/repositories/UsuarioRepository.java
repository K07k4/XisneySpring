package com.erutnecca.xisney.repositories;

import org.springframework.data.repository.CrudRepository;

import com.erutnecca.xisney.entities.Usuario;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {
	//@Query(value = "SELECT * FROM Usuario WHERE email = ?1", nativeQuery = true)
	
	Usuario findByEmail(String email);
	Usuario findByDni(String dni);
	Usuario findByEmailAndDni(String email, String dni);
	Usuario findByEmailAndPass(String email, String pass);


	
}