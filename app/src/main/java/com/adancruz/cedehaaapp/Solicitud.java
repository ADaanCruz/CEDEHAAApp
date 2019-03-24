package com.adancruz.cedehaaapp;

import java.io.Serializable;

public class Solicitud implements Serializable {

    private String titulo;
    private String fechaInicio;
    private String nombre;
    private String apellidoPaterno;
    private String correo;
    private String telefono;

    public Solicitud(String titulo, String fechaInicio, String nombre, String apellidoPaterno, String correo, String telefono) {
        this.titulo = titulo;
        this.fechaInicio = fechaInicio;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.correo = correo;
        this.telefono = telefono;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String curso) {
        this.titulo = curso;
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

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
