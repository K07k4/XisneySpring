package com.erutnecca.xisney.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ArticuloRecibo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idArticuloRecibo;

	private Integer idArticulo, idRecibo;
	private Integer cantidad;
	private Double precioUnitario;

	public Integer getIdArticuloRecibo() {
		return idArticuloRecibo;
	}

	public void setIdArticuloRecibo(Integer idArticuloRecibo) {
		this.idArticuloRecibo = idArticuloRecibo;
	}

	public Integer getIdArticulo() {
		return idArticulo;
	}

	public void setIdArticulo(Integer idArticulo) {
		this.idArticulo = idArticulo;
	}

	public Integer getIdRecibo() {
		return idRecibo;
	}

	public void setIdRecibo(Integer idRecibo) {
		this.idRecibo = idRecibo;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(Double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

}