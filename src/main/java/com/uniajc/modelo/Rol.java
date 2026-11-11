package com.uniajc.modelo;

/**
 * Enum de roles de usuario.
 * Define un conjunto fijo de constantes inmutables para los roles del sistema.
 * La convención es escribir sus constantes en mayúsculas.
 */
public enum Rol {
    ADMIN("Administrador"),
    USUARIO("Usuario");

    private final String etiqueta;

    Rol(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
