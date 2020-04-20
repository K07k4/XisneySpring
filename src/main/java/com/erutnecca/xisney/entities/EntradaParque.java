package com.erutnecca.xisney.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class EntradaParque {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idEntradaParque;

	private Integer idUsuario, idTipoOferta, idParque;
	private Double precioFinal;
	private String fechaInicio, fechaFin, fechaCompra;
	private Boolean consumido;

	public Integer getIdEntradaParque() {
		return idEntradaParque;
	}

	public void setIdEntradaParque(Integer idEntradaParque) {
		this.idEntradaParque = idEntradaParque;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Integer getIdTipoOferta() {
		return idTipoOferta;
	}

	public void setIdTipoOferta(Integer idTipoOferta) {
		this.idTipoOferta = idTipoOferta;
	}

	public Integer getIdParque() {
		return idParque;
	}

	public void setIdParque(Integer idParque) {
		this.idParque = idParque;
	}

	public Double getPrecioFinal() {
		return precioFinal;
	}

	public void setPrecioFinal(Double precioFinal) {
		this.precioFinal = precioFinal;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
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
