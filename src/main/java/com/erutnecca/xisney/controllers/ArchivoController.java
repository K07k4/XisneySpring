package com.erutnecca.xisney.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import javax.servlet.ServletContext;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(path = "/archivo")
public class ArchivoController {

	// Sube una imagen. El tamaño máximo de transferencia está establecido en
	// src/main/resources/application.properties
	@PostMapping(path = "/upload/{tipo}/{id}")
	public @ResponseBody ResponseEntity<String> uploadImagen(@RequestParam("file") MultipartFile multipartFile,
			@PathVariable("tipo") String tipo, @PathVariable("id") String id) {

		String path = "D:/xisney/" + tipo + "/" + id + ".jpg";

		
		// Si no es formato .jpg/.jpeg no permitirá almacenarse
		if (!multipartFile.getContentType().equals("image/jpeg")) {
			return ResponseEntity.badRequest()
					.body("Sólo se permite formato jpg/jpeg, no el formato " + multipartFile.getContentType());
		}

		// Sea jpg o jpeg se guardará como jpg, por mantener la nomenclatura
		// El nombre de la imagen corresponde al id, así ahorramos un campo en la base
		// de datos


		// La almacena en la carpeta
		// En caso que se quiera cambiar la imagen, se sobreescribirá. Por lo que
		// siempre habrá únicamente 1 imagen por id en tipo
		try {
			multipartFile.transferTo(new File(path));
			return new ResponseEntity<>("Imagen almacenada en: " + path, HttpStatus.OK);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Lio también");
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("No se encuentra la ruta " + path);
		}

	}

	@RequestMapping(value = "/imagen/{tipo}/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
	public @ResponseBody byte[] getImagen(@PathVariable("tipo") String tipo, @PathVariable("id") String id) {

		String path = "D:/xisney/" + tipo + "/" + id + ".jpg";
		File file = null;

		file = new File(path); // Se busca la imagen en el servidor de archivos

		byte[] b = null;
		try {
			b = Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}
	
	@DeleteMapping(path = "/delete/{tipo}/{id}")
	public @ResponseBody ResponseEntity<String> deleteImagen(@PathVariable("tipo") String tipo, @PathVariable("id") String id) {
		
		String path = "D:/xisney/" + tipo + "/" + id + ".jpg";
		
	    File fileToDelete = new File(path);
	    boolean success = fileToDelete.delete();
		
	    if(success) {
	    	return new ResponseEntity<>("Imagen eliminada correctamente\n" + path, HttpStatus.OK);
	    } else {
	    	return ResponseEntity.badRequest().body("No se encuentra ninguna imagen en la ruta\n" + path);
	    }
	    
	}

}
