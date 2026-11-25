package com.uniajc.modelo;

import java.sql.Timestamp;

public class Usuario {
    private int id;
    private String username;
    private String password;
    private String nombre;
    private String rol;
    private boolean activo;
    private Timestamp fechaCreacion;
    private String email;
    private Timestamp ultimoAcceso;
    private Integer estudianteId;

    public Usuario() {
        this.activo = true;
    }

    public Usuario(int id, String username, String password, String nombre, String rol, boolean activo, Timestamp fechaCreacion) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.rol = rol;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
    }

    public Usuario(int id, String username, String password, String nombre, String rol, boolean activo, Timestamp fechaCreacion, String email, Timestamp ultimoAcceso, Integer estudianteId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.rol = rol;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
        this.email = email;
        this.ultimoAcceso = ultimoAcceso;
        this.estudianteId = estudianteId;
    }

    public Usuario(String username, String password, String nombre, String rol, boolean activo) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.rol = rol;
        this.activo = activo;
    }

    public Usuario(String username, String password, String nombre, String rol, boolean activo, String email) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.rol = rol;
        this.activo = activo;
        this.email = email;
    }

    public Usuario(String username, String password, String nombre, String rol, boolean activo, String email, Integer estudianteId) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.rol = rol;
        this.activo = activo;
        this.email = email;
        this.estudianteId = estudianteId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(Timestamp ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public Integer getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Integer estudianteId) {
        this.estudianteId = estudianteId;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nombre='" + nombre + '\'' +
                ", rol='" + rol + '\'' +
                ", activo=" + activo +
                ", fechaCreacion=" + fechaCreacion +
                ", email='" + email + '\'' +
                ", ultimoAcceso=" + ultimoAcceso +
                ", estudianteId=" + estudianteId +
                '}';
    }
}
