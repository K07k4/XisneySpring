package com.erutnecca.xisney.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Aviso {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idAviso;

    private Integer idParque;
    private String fecha;
    private String title;
    private String text;

    public Integer getIdAviso() {
        return idAviso;
    }

    public void setIdAviso(Integer idAviso) {
        this.idAviso = idAviso;
    }

    public Integer getIdParque() {
        return idParque;
    }

    public void setIdParque(Integer idParque) {
        this.idParque = idParque;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
