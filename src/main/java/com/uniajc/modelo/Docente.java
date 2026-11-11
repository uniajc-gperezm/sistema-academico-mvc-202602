package com.uniajc.modelo;

import java.util.Objects;

/** Entidad Docente. Tabla: docentes (id_docente, nombre, especialidad). */
public class Docente {

    private int id_docente;
    private String nombre;
    private String especialidad;

    public Docente() {}

    public Docente(int id_docente, String nombre, String especialidad) {
        this.id_docente = id_docente;
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    public int getId_docente() { return id_docente; }
    public void setId_docente(int id_docente) { this.id_docente = id_docente; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    @Override
    public String toString() { return nombre + " (" + especialidad + ")"; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Docente)) return false;
        return id_docente == ((Docente) o).id_docente;
    }

    @Override
    public int hashCode() { return Objects.hash(id_docente); }
}
