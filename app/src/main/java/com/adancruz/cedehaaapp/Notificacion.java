package com.adancruz.cedehaaapp;

import java.io.Serializable;

public class Notificacion implements Serializable {

    private int imagen;
    private String nombreCurso;
    private String nombreGrupo;
    private String tipoUsuario;
    private String nombreUsuario;
    private String mensaje;
    private String fecha;

    public Notificacion(int imagen, String nombreCurso, String nombreGrupo, String tipoUsuario, String nombreUsuario, String mensaje, String fecha) {
        this.imagen = imagen;
        this.nombreCurso = nombreCurso;
        this.nombreGrupo = nombreGrupo;
        this.tipoUsuario = tipoUsuario;
        this.nombreUsuario = nombreUsuario;
        this.mensaje = mensaje;
        this.fecha = fecha;
    }

    // Getters

    public int getImagen() {
        return imagen;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getFecha() {
        return fecha;
    }

    // Setters

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
