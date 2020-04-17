package com.erutnecca.xisney.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Articulo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idArticulo;

	private Integer idTipoArticulo;
	private String nombre;
	private Double precio;
	private String descripcion;
	private Integer stock;
	private Double puntuacionMedia;
	private Integer numeroPuntuaciones;

	public Integer getIdArticulo() {
		return idArticulo;
	}

	public void setIdArticulo(Integer idArticulo) {
		this.idArticulo = idArticulo;
	}

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

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Double getPuntuacionMedia() {
		return puntuacionMedia;
	}

	public void setPuntuacionMedia(Double puntuacionMedia) {
		this.puntuacionMedia = puntuacionMedia;
	}

	public Integer getNumeroPuntuaciones() {
		return numeroPuntuaciones;
	}

	public void setNumeroPuntuaciones(Integer numeroPuntuaciones) {
		this.numeroPuntuaciones = numeroPuntuaciones;
	}

}