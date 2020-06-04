package com.erutnecca.xisney.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Restaurante {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idRestaurante;

	private Integer idParque;
	private String nombre, descripcion;
	private String inicioTramo1, finTramo1, inicioTramo2, finTramo2;

	public Integer getIdRestaurante() {
		return idRestaurante;
	}

	public void setIdRestaurante(Integer idRestaurante) {
		this.idRestaurante = idRestaurante;
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

	public String getInicioTramo1() {
		return inicioTramo1;
	}

	public void setInicioTramo1(String inicioTramo1) {
		this.inicioTramo1 = inicioTramo1;
	}

	public String getFinTramo1() {
		return finTramo1;
	}

	public void setFinTramo1(String finTramo1) {
		this.finTramo1 = finTramo1;
	}

	public String getInicioTramo2() {
		return inicioTramo2;
	}

	public void setInicioTramo2(String inicioTramo2) {
		this.inicioTramo2 = inicioTramo2;
	}

	public String getFinTramo2() {
		return finTramo2;
	}

	public void setFinTramo2(String finTramo2) {
		this.finTramo2 = finTramo2;
	}

}
