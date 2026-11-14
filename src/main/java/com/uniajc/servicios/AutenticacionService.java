package com.uniajc.servicios;

import com.uniajc.modelo.Usuario;
import com.uniajc.modelo.UsuarioDAO;
import com.uniajc.seguridad.PasswordHasher;
import java.util.Optional;

/** Autentica cuentas y migra sus claves antiguas a PBKDF2-HMAC-SHA512 al iniciar sesión. */
public class AutenticacionService {
    private final UsuarioDAO dao;
    private final PasswordHasher passwordHasher;

    public AutenticacionService() {
        this(new UsuarioDAO(), new PasswordHasher());
    }

    public AutenticacionService(UsuarioDAO dao, PasswordHasher passwordHasher) {
        this.dao = dao;
        this.passwordHasher = passwordHasher;
    }

    public Optional<Usuario> autenticar(String username, char[] contrasena) {
        if (username == null || username.isBlank() || contrasena == null || contrasena.length == 0) {
            return Optional.empty();
        }
        Optional<Usuario> resultado = dao.buscarPorUsername(username.trim());
        if (resultado.isEmpty()) return Optional.empty();

        Usuario usuario = resultado.get();
        if (!usuario.activo() || !passwordHasher.verificar(contrasena, usuario.hashContrasena())) {
            return Optional.empty();
        }
        if (passwordHasher.necesitaRehash(usuario.hashContrasena())) {
            String hashNuevo = passwordHasher.hash(contrasena);
            if (!dao.actualizarHashContrasena(usuario.id(), hashNuevo)) {
                throw new IllegalStateException("No se pudo actualizar el hash de la contraseña.");
            }
        }
        return Optional.of(usuario.sinHash());
    }
}
