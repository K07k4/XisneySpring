package com.erutnecca.xisney.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TipoArticulo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idTipoArticulo;

	private String nombre;

	public Integer getIdTipoArticulo() {
		return idTipoArticulo;
	}

	public void setIdTipoArticulo(Integer idTipoArticulo) {
		this.idTipoArticulo = idTipoArticulo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
