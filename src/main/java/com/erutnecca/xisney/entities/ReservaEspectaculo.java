package com.erutnecca.xisney.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(ReservaEspectaculoKey.class)
public class ReservaEspectaculo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idReservaEspectaculo;
	@Id
	private Integer idUsuario;
	@Id
	private Integer idEvento;
	@Id
	private String fechaReserva;
	
	private Integer personas;
	private String codigo;
	private String fechaCompra;
	private Boolean consumido;

	public Integer getIdReservaEspectaculo() {
		return idReservaEspectaculo;
	}

	public void setIdReservaEspectaculo(Integer idReservaEspectaculo) {
		this.idReservaEspectaculo = idReservaEspectaculo;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Integer getIdEvento() {
		return idEvento;
	}

	public void setIdEvento(Integer idEvento) {
		this.idEvento = idEvento;
	}

	public Integer getPersonas() {
		return personas;
	}

	public void setPersonas(Integer personas) {
		this.personas = personas;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
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
