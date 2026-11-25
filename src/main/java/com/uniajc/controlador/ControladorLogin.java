package com.uniajc.controlador;

import com.uniajc.modelo.Usuario;
import com.uniajc.servicios.UsuarioService;

public class ControladorLogin {

    private final UsuarioService usuarioService;

    public ControladorLogin() {
        this.usuarioService = new UsuarioService();
    }

    public ControladorLogin(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Intenta autenticar un usuario con sus credenciales.
     *
     * @param username Nombre de usuario
     * @param password Contraseña
     * @return El Usuario autenticado si las credenciales son válidas
     */
    public Usuario iniciarSesion(String username, String password) {
        return usuarioService.autenticar(username, password);
    }
}

