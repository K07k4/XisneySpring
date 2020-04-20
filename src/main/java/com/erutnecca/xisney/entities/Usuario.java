package com.erutnecca.xisney.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//TODO: En la base de datos estan declarados como 3 PK, pero aqui funciona bien (y mejor de hecho) con solo una declaracion
//TODO: Habria que cambiar el resto de clases que tengan una Key relacionada

@Entity
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idUsuario;
	
	// Otras PKs
	private String email;
	private String dni;

	private String pass, nombre, apellidos;
	private String fechaNacimiento;
	private Boolean activo;

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	@Override
	public String toString() {
		return "[Usuario]\nidUsuario=" + idUsuario + "\nemail=" + email + "\npass=" + pass + "\ndni=" + dni
				+ "\nnombre=" + nombre + "\napellidos=" + apellidos + "\nfechaNacimiento=" + fechaNacimiento
				+ "\nactivo=" + activo;
	}

}