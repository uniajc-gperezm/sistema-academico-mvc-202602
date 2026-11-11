package com.uniajc.modelo;

import java.util.Objects;

/** Entidad Estudiante. Tabla: estudiantes (id, name, lastname, email). */
public class Estudiante {

    private int id_estudiante;
    private String nombre;
    private String apellido;
    private String email;

    public Estudiante() {}

    public Estudiante(int id_estudiante, String nombre, String apellido, String email) {
        this.id_estudiante = id_estudiante;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }

    public int getId_estudiante() { return id_estudiante; }
    public void setId_estudiante(int id_estudiante) { this.id_estudiante = id_estudiante; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() { return nombre + " " + apellido; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Estudiante)) return false;
        return id_estudiante == ((Estudiante) o).id_estudiante;
    }

    @Override
    public int hashCode() { return Objects.hash(id_estudiante); }
}
