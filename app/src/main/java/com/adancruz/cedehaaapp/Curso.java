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
    private String estado;

    public Curso(String titulo, int numImagen, String descripcionBreve, String descripcionGeneral,
                 String fechaInicio, int totalEstudiantes, int limiteEstudiantes, String estado) {
        this.titulo = titulo;
        this.numImagen = numImagen;
        this.descripcionBreve = descripcionBreve;
        this.descripcionGeneral = descripcionGeneral;
        this.fechaInicio = fechaInicio;
        this.totalEstudiantes = totalEstudiantes;
        this.limiteEstudiantes = limiteEstudiantes;
        this.estado = estado;
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

    public String getFechaInicio(boolean formato) {
        if (formato) {
            return formato(fechaInicio);
        } else {
            return fechaInicio;
        }
    }

    public String formato(String fechaInicio){
        char[] fecha = fechaInicio.toCharArray();
        fechaInicio = "";
        for (int i = 8; i <= 9; i++) {
            fechaInicio += fecha[i];
        }
        fechaInicio += "/";
        String mes = "";
        for (int i = 5; i <= 6; i++) {
            mes += fecha[i];
        }
        fechaInicio += sacarMes(mes);
        fechaInicio += "/";
        for (int i = 0; i <= 3; i++) {
            fechaInicio += fecha[i];
        }
        return fechaInicio;
    }

    public String sacarMes(String mes) {
        switch (mes) {
            case "01": mes = "Enero"; break;
            case "02": mes = "Febrero"; break;
            case "03": mes = "Marzo"; break;
            case "04": mes = "Abril"; break;
            case "05": mes = "Mayo"; break;
            case "06": mes = "Junio"; break;
            case "07": mes = "Julio"; break;
            case "08": mes = "Agosto"; break;
            case "09": mes = "Septiembre"; break;
            case "10": mes = "Octubre"; break;
            case "11": mes = "Noviembre"; break;
            case "12": mes = "Diciembre"; break;
        }
        return mes;
    }

    public int getTotalEstudiantes() {
        return totalEstudiantes;
    }

    public int getLimiteEstudiantes() {
        return limiteEstudiantes;
    }

    public String getEstado() {
        return estado;
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

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
