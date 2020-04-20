package com.erutnecca.xisney.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class ReservaRestaurante {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idReservaRestaurante;
	
	// Otras PKs
	private Integer idUsuario;
	private Integer idRestaurante;
	private String fechaReserva;
	
	private Integer personas;
	private String fechaCompra;
	private Boolean consumido;

	public Integer getIdReservaRestaurante() {
		return idReservaRestaurante;
	}

	public void setIdReservaRestaurante(Integer idReservaRestaurante) {
		this.idReservaRestaurante = idReservaRestaurante;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Integer getIdRestaurante() {
		return idRestaurante;
	}

	public void setIdRestaurante(Integer idRestaurante) {
		this.idRestaurante = idRestaurante;
	}

	public Integer getPersonas() {
		return personas;
	}

	public void setPersonas(Integer personas) {
		this.personas = personas;
	}

	public String getFechaReserva() {
		return fechaReserva;
	}

	public void setFechaReserva(String fechaReserva) {
		this.fechaReserva = fechaReserva;
	}

	public String getFechaCompra() {
		return fechaCompra;
	}

	public void setFechaCompra(String fechaCompra) {
		this.fechaCompra = fechaCompra;
	}

	public Boolean getConsumido() {
		return consumido;
	}

	public void setConsumido(Boolean consumido) {
		this.consumido = consumido;
	}

}
