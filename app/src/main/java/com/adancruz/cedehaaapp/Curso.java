package com.adancruz.cedehaaapp;

import java.io.Serializable;

public class Curso implements Serializable {

    private int imagen;
    private String titulo;
    private String descripcionBreve;
    private String descripcionGeneral;

    public Curso(int imagen, String titulo, String descripcionBreve, String descripcionGeneral) {
        this.imagen = imagen;
        this.titulo = titulo;
        this.descripcionBreve = descripcionBreve;
        this.descripcionGeneral = descripcionGeneral;
    }

    // Getters

    public int getImagen() {
        return imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcionBreve() {
        return descripcionBreve;
    }

    public String getDescripcionGeneral() {
        return descripcionGeneral;
    }

    // Setters

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcionBreve(String descripcionBreve) {
        this.descripcionBreve = descripcionBreve;
    }

    public void setDescripcionGeneral(String descripcionGeneral) {
        this.descripcionGeneral = descripcionGeneral;
    }
}
