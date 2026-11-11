package com.uniajc.modelo;

import java.util.Objects;

/** Entidad Usuario. Tabla: users (id, username, password, nombre, rol, activo, fecha_creacion). */
public class Usuario {
    private int id;
    private String username;
    private String password;
    private String nombre;
    private Rol rol;
    private boolean activo;
    private java.sql.Timestamp fechaCreacion;

    public Usuario() {
    }

    public Usuario(int id, String username, String password, String nombre, Rol rol, boolean activo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.rol = rol;
        this.activo = activo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public java.sql.Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(java.sql.Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    @Override
    public String toString() { return username + " (" + (rol != null ? rol.name() : "") + ")"; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        return id == ((Usuario) o).id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
