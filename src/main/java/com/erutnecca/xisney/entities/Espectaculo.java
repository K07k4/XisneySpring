package com.erutnecca.xisney.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Espectaculo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idEspectaculo;

	private Integer idParque;
	private String nombre, descripcion;
	private String fechaInicio, fechaFin;

	public Integer getIdEspectaculo() {
		return idEspectaculo;
	}

	public void setIdEspectaculo(Integer idEspectaculo) {
		this.idEspectaculo = idEspectaculo;
	}

	public Integer getIdParque() {
		return idParque;
	}

	public void setIdParque(Integer idParque) {
		this.idParque = idParque;
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

}
