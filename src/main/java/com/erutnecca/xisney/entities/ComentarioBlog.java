package com.erutnecca.xisney.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ComentarioBlog {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idComentarioBlog;
	private Integer idEntradaBlog;
	private Integer idUsuario;

	private String comentario;
	private String fecha;
	private Boolean editado;

	public Integer getIdComentarioBlog() {
		return idComentarioBlog;
	}

	public void setIdComentarioBlog(Integer idComentarioBlog) {
		this.idComentarioBlog = idComentarioBlog;
	}

	public Integer getIdEntradaBlog() {
		return idEntradaBlog;
	}

	public void setIdEntradaBlog(Integer idEntradaBlog) {
		this.idEntradaBlog = idEntradaBlog;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public Boolean getEditado() {
		return editado;
	}

	public void setEditado(Boolean editado) {
		this.editado = editado;
	}

}
