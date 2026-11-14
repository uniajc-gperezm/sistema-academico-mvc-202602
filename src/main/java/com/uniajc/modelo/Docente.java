package com.uniajc.modelo;
public class Docente {
    private long idDocente;
    private String nombre;
    private String especialidad;
    private String area;

    public Docente() {}

    public Docente(String nombre, String especialidad, String area) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.area = area;
    }

    public Docente(long idDocente, String nombre, String especialidad, String area) {
        this.idDocente = idDocente;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.area = area;
    }

    public long getIdDocente() { return idDocente; }
    public void setIdDocente(long idDocente) { this.idDocente = idDocente; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
}


