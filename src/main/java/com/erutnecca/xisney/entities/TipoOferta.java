package com.erutnecca.xisney.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TipoOferta {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idTipoOferta;

	private String nombre, descripcion;
	private Double cantidad, pocentaje;
	private Integer dias;

	public Integer getIdTipoOferta() {
		return idTipoOferta;
	}

	public void setIdTipoOferta(Integer idTipoOferta) {
		this.idTipoOferta = idTipoOferta;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	public Double getPocentaje() {
		return pocentaje;
	}

	public void setPocentaje(Double pocentaje) {
		this.pocentaje = pocentaje;
	}

	public Integer getDias() {
		return dias;
	}

	public void setDias(Integer dias) {
		this.dias = dias;
	}

}
