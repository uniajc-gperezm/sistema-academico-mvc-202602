package com.uniajc.dao;

import java.util.Optional;

import com.uniajc.modelo.Usuario;

/** Contrato de persistencia de usuarios. Implementado por {@link UsuarioDao}. */
public interface UsuarioRepositorio {

    Optional<Usuario> buscarPorUsername(String username);

    Optional<Usuario> buscarPorId(int id);

    void guardar(Usuario u);

    /** Actualiza unicamente la contraseña (ya encriptada) del usuario indicado. */
    void cambiarPassword(int id, String nuevoPasswordHash);
}
