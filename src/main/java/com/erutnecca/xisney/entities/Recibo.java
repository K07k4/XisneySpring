package com.erutnecca.xisney.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Recibo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idRecibo;

	private Integer idUsuario;
	private Double precioTotal;
	private String fechaCompra;

	public Integer getIdRecibo() {
		return idRecibo;
	}

	public void setIdRecibo(Integer idRecibo) {
		this.idRecibo = idRecibo;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Double getPrecioTotal() {
		return precioTotal;
	}

	public void setPrecioTotal(Double precioTotal) {
		this.precioTotal = precioTotal;
	}

	public String getFechaCompra() {
		return fechaCompra;
	}

	public void setFechaCompra(String fechaCompra) {
		this.fechaCompra = fechaCompra;
	}

}
