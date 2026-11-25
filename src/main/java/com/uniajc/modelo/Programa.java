package com.uniajc.modelo;

public class Programa {
    private int id;
    private String codigo;
    private String nombre;
    private String facultad;
    private int duracionSemestres;

    public Programa() {}

    public Programa(int id, String codigo, String nombre, String facultad, int duracionSemestres) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.facultad = facultad;
        this.duracionSemestres = duracionSemestres;
    }

    public Programa(String codigo, String nombre, String facultad, int duracionSemestres) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.facultad = facultad;
        this.duracionSemestres = duracionSemestres;
    }

    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getFacultad() { return facultad; }
    public void setFacultad(String facultad) { this.facultad = facultad; }

    public int getDuracionSemestres() { return duracionSemestres; }
    public void setDuracionSemestres(int duracionSemestres) { this.duracionSemestres = duracionSemestres; }
}