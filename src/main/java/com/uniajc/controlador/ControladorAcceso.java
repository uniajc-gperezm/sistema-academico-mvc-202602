package com.uniajc.controlador;

import com.uniajc.modelo.Usuario;
import com.uniajc.servicios.AutenticacionService;
import java.util.Optional;

/** Recibe credenciales desde la vista y delega su validación al servicio de autenticación. */
public class ControladorAcceso {
    private final AutenticacionService service;

    public ControladorAcceso() { this(new AutenticacionService()); }
    public ControladorAcceso(AutenticacionService service) { this.service = service; }

    public Optional<Usuario> autenticar(String username, char[] contrasena) {
        return service.autenticar(username, contrasena);
    }
}
