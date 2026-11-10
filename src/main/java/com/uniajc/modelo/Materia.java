package com.uniajc.modelo;

import java.util.Objects;

public class Materia {
    private int id;
    private String nombre;
    private int creditos;

    public Materia() {
    }

    public Materia(String nombre, int creditos) {
        this.nombre = nombre;
        this.creditos = creditos;
    }

    public Materia(int id, String nombre, int creditos) {
        this.id = id;
        this.nombre = nombre;
        this.creditos = creditos;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    // Compatibility aliases
    public int getIdMateria() {
        return id;
    }

    public void setIdMateria(int idMateria) {
        this.id = idMateria;
    }

    public String getNombreMateria() {
        return nombre;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombre = nombreMateria;
    }

    public String getName() {
        return nombre;
    }

    public void setName(String name) {
        this.nombre = name;
    }

    public int getCredits() {
        return creditos;
    }

    public void setCredits(int credits) {
        this.creditos = credits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Materia materia = (Materia) o;
        return id == materia.id && Objects.equals(nombre, materia.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre);
    }

    @Override
    public String toString() {
        return "Materia{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", creditos=" + creditos +
                '}';
    }
}
