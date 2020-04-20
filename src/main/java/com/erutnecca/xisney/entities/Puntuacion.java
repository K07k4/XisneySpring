package com.erutnecca.xisney.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Puntuacion {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idPuntuacion;
	
	// Otras PKs
	private Integer idUsuario;
	private Integer idArticulo;
	
	private Double puntuacion;

	public Integer getIdPuntuacion() {
		return idPuntuacion;
	}

	public void setIdPuntuacion(Integer idPuntuacion) {
		this.idPuntuacion = idPuntuacion;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Integer getIdArticulo() {
		return idArticulo;
	}

	public void setIdArticulo(Integer idArticulo) {
		this.idArticulo = idArticulo;
	}

	public Double getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(Double puntuacion) {
		this.puntuacion = puntuacion;
	}
}
