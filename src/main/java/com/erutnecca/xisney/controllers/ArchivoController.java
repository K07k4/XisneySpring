package com.erutnecca.xisney.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.erutnecca.xisney.controllers.util.Checker;

@Controller
@RequestMapping(path = "/archivo")
public class ArchivoController {

	// Sube una imagen. El tamaño máximo de transferencia está establecido en
	// src/main/resources/application.properties
	@PostMapping(path = "/upload/image/{tipo}/{id}")
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

	// Obtiene una imagen según el tipo y el id
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

	// Elimina una imagen según el tipo y el id
	@DeleteMapping(path = "/delete/{tipo}/{id}")
	public @ResponseBody ResponseEntity<String> deleteImagen(@PathVariable("tipo") String tipo,
			@PathVariable("id") String id) {

		String path = "D:/xisney/" + tipo + "/" + id + ".jpg";

		File fileToDelete = new File(path);
		boolean success = fileToDelete.delete();

		if (success) {
			return new ResponseEntity<>("Imagen eliminada correctamente\n" + path, HttpStatus.OK);
		} else {
			return ResponseEntity.badRequest().body("No se encuentra ninguna imagen en la ruta\n" + path);
		}

	}

	// Sube el pdf de un cv
	@PostMapping(path = "/upload/cv")
	public @ResponseBody ResponseEntity<String> uploadCV(@RequestParam("file") MultipartFile multipartFile,
			@RequestParam("email") String email) {

		// Se almacenan los cv en esta carpeta
		String path = "D:/xisney/cv/";

		if (!Checker.emailValido(email)) {
			return ResponseEntity.badRequest().body("El email no es válido");
		}

		// Si no es formato .jpg/.jpeg no permitirá almacenarse
		if (!multipartFile.getContentType().equals("application/pdf")) {
			return ResponseEntity.badRequest()
					.body("Sólo se permite formato pdf, no el formato " + multipartFile.getContentType());
		}

		// El mismo email solo puede almacenar un cv. Si sube otro, se sobreescribe.
		try {
			multipartFile.transferTo(new File(path + email + ".pdf"));
			return new ResponseEntity<>("CV almacenado en: " + path, HttpStatus.OK);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Lio también");
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("No se encuentra la ruta " + path);
		}

	}

}
