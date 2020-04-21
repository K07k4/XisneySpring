package com.erutnecca.xisney.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.erutnecca.xisney.entities.EntradaBlog;
import com.erutnecca.xisney.repositories.EntradaBlogRepository;

@Controller
@RequestMapping(path = "/blog")
public class BlogController {
	@Autowired
	private EntradaBlogRepository entradaBlogRepository;

	
	@PostMapping(path = "/add")
	public @ResponseBody ResponseEntity<String> addEntradaBlog(@RequestParam String titulo, @RequestParam String contenido) {

		EntradaBlog entradaBlog = new EntradaBlog();

		
		
		
		return new ResponseEntity<>("Entrada de blog creada correctamente", HttpStatus.OK);
	}

	
}
