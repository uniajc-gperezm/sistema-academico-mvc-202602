package com.uniajc.servicios;

import java.util.Optional;

import com.uniajc.dao.UsuarioRepositorio;
import com.uniajc.db.EncriptacionUtil;
import com.uniajc.modelo.Usuario;

/** Capa de negocio de autenticación y usuarios. */
public class UsuarioService {

    private final UsuarioRepositorio usuarioDao;

    public UsuarioService(UsuarioRepositorio usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    /**
     * Autentica un usuario contra la base de datos.
     * Compara el hash MD5 del password ingresado contra el hash almacenado.
     */
    public Optional<Usuario> autenticar(String username, String password) {
        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            return Optional.empty();
        }
        Optional<Usuario> encontrado = usuarioDao.buscarPorUsername(username.trim());
        if (encontrado.isEmpty()) {
            return Optional.empty();
        }
        Usuario u = encontrado.get();
        if (!u.isActivo()) {
            return Optional.empty();
        }
        String hashIngresado = EncriptacionUtil.md5(password);
        if (!u.getPassword().equalsIgnoreCase(hashIngresado)) {
            return Optional.empty();
        }
        return Optional.of(u);
    }

    /** Registra un nuevo usuario con contraseña encriptada. */
    public void registrarUsuario(Usuario u, String passwordPlano) {
        u.setPassword(EncriptacionUtil.md5(passwordPlano));
        usuarioDao.guardar(u);
    }

    /** Cambia la contraseña de un usuario (encriptada). */
    public void cambiarPassword(int idUsuario, String nuevoPasswordPlano) {
        if (nuevoPasswordPlano == null || nuevoPasswordPlano.length() < 4) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 4 caracteres.");
        }
        usuarioDao.cambiarPassword(idUsuario, EncriptacionUtil.md5(nuevoPasswordPlano));
    }
}