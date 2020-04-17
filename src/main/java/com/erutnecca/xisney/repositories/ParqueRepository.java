package com.erutnecca.xisney.repositories;

import org.springframework.data.repository.CrudRepository;

import com.erutnecca.xisney.entities.Parque;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ParqueRepository extends CrudRepository<Parque, Integer> {

}