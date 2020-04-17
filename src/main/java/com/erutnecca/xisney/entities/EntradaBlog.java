package com.erutnecca.xisney.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class EntradaBlog {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idEntradaBlog;

	private String titulo, contenido;
	private String fecha;

	public Integer getIdEntradaBlog() {
		return idEntradaBlog;
	}

	public void setIdEntradaBlog(Integer idEntradaBlog) {
		this.idEntradaBlog = idEntradaBlog;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

}
