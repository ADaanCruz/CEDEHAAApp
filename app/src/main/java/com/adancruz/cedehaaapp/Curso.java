package com.adancruz.cedehaaapp;

import java.io.Serializable;

public class Curso implements Serializable {

    private String titulo;
    private int numImagen;
    private String descripcionBreve;
    private String descripcionGeneral;
    private String fechaInicio;
    private int totalEstudiantes;
    private int limiteEstudiantes;

    public Curso(String titulo, int numImagen, String descripcionBreve, String descripcionGeneral, String fechaInicio, int totalEstudiantes, int limiteEstudiantes) {
        this.titulo = titulo;
        this.numImagen = numImagen;
        this.descripcionBreve = descripcionBreve;
        this.descripcionGeneral = descripcionGeneral;
        this.fechaInicio = fechaInicio;
        this.totalEstudiantes = totalEstudiantes;
        this.limiteEstudiantes = limiteEstudiantes;
    }

    // Getters

    public String getTitulo() {
        return titulo;
    }

    public int getNumImagen() {
        return numImagen;
    }

    public String getDescripcionBreve() {
        return descripcionBreve;
    }

    public String getDescripcionGeneral() {
        return descripcionGeneral;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public int getTotalEstudiantes() {
        return totalEstudiantes;
    }

    public int getLimiteEstudiantes() {
        return limiteEstudiantes;
    }

    // Setters

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setNumImagen(int imagen) {
        this.numImagen = imagen;
    }

    public void setDescripcionBreve(String descripcionBreve) {
        this.descripcionBreve = descripcionBreve;
    }

    public void setDescripcionGeneral(String descripcionGeneral) {
        this.descripcionGeneral = descripcionGeneral;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setTotalEstudiantes(int totalEstudiantes) {
        this.totalEstudiantes = totalEstudiantes;
    }

    public void setLimiteEstudiantes(int limiteEstudiantes) {
        this.limiteEstudiantes = limiteEstudiantes;
    }
}
