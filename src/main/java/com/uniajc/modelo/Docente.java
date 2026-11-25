package com.uniajc.modelo;

public class Docente {
    private int idDocente;
    private String nombre;
    private String especialidad;
    private String documento;
    private String email;
    private String telefono;
    private String tituloAcademico;
    private String tipoContrato;

    // Constructor vacío
    public Docente() {
    }

    // Constructor con todos los atributos
    public Docente(int idDocente, String nombre, String especialidad, String documento, String email, String telefono, String tituloAcademico, String tipoContrato) {
        this.idDocente = idDocente;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.documento = documento;
        this.email = email;
        this.telefono = telefono;
        this.tituloAcademico = tituloAcademico;
        this.tipoContrato = tipoContrato;
    }

    // Getters y Setters
    public int getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(int idDocente) {
        this.idDocente = idDocente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTituloAcademico() {
        return tituloAcademico;
    }

    public void setTituloAcademico(String tituloAcademico) {
        this.tituloAcademico = tituloAcademico;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    @Override
    public String toString() {
        return nombre + " - " + especialidad;
    }
}