package com.uniajc.modelo;

import java.util.Objects;

/** Entidad Materia. Tabla: materias (id, name, credits). */
public class Materia {

    private int id_materia;
    private String nombre_materia;
    private int creditos;

    public Materia() {}

    public Materia(int id_materia, String nombre_materia, int creditos) {
        this.id_materia = id_materia;
        this.nombre_materia = nombre_materia;
        this.creditos = creditos;
    }

    public int getId_materia() { return id_materia; }
    public void setId_materia(int id_materia) { this.id_materia = id_materia; }

    public String getNombre_materia() { return nombre_materia; }
    public void setNombre_materia(String nombre_materia) { this.nombre_materia = nombre_materia; }

    public int getCreditos() { return creditos; }
    public void setCreditos(int creditos) { this.creditos = creditos; }

    @Override
    public String toString() { return nombre_materia + " (" + creditos + " cr)"; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Materia)) return false;
        return id_materia == ((Materia) o).id_materia;
    }

    @Override
    public int hashCode() { return Objects.hash(id_materia); }
}
