package com.uniajc.modelo;

import java.util.Objects;

public class Docente {
    private int id;
    private String nombre;
    private String especialidad;
    private String email;

    public Docente() {
    }

    public Docente(String nombre, String especialidad, String email) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.email = email;
    }

    public Docente(int id, String nombre, String especialidad, String email) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.email = email;
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

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Compatibility aliases
    public int getIdDocente() {
        return id;
    }

    public void setIdDocente(int idDocente) {
        this.id = idDocente;
    }

    public String getName() {
        return nombre;
    }

    public void setName(String name) {
        this.nombre = name;
    }

    public String getSpecialty() {
        return especialidad;
    }

    public void setSpecialty(String specialty) {
        this.especialidad = specialty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Docente docente = (Docente) o;
        return id == docente.id && Objects.equals(email, docente.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "Docente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
