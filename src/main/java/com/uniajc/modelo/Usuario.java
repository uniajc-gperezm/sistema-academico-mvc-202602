package com.uniajc.modelo;

/** Datos de una cuenta; el hash solo se usa dentro del proceso de autenticación. */
public record Usuario(long id, String username, String nombre, String rol, boolean activo,
                      String hashContrasena) {
    public Usuario sinHash() {
        return new Usuario(id, username, nombre, rol, activo, null);
    }
}
