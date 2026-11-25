package com.uniajc.controlador;

import java.util.List;
import java.util.Optional;

import com.uniajc.modelo.Usuario;
import com.uniajc.servicios.UsuarioService;

public class ControladorUsuario {

    private final UsuarioService usuarioService;

    public ControladorUsuario() {
        this.usuarioService = new UsuarioService();
    }

    public ControladorUsuario(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void registrarUsuario(String username, String password, String nombre, String rol, boolean activo, String email) {
        registrarUsuario(username, password, nombre, rol, activo, email, null);
    }

    public void registrarUsuario(String username, String password, String nombre, String rol, boolean activo, String email, Integer estudianteId) {
        Usuario usuario = new Usuario(username, password, nombre, rol, activo, email, estudianteId);
        usuarioService.registrarUsuario(usuario);
    }

    public void actualizarUsuario(int id, String username, String nuevaPassword, String nombre, String rol, boolean activo, String email) {
        actualizarUsuario(id, username, nuevaPassword, nombre, rol, activo, email, null);
    }

    public void actualizarUsuario(int id, String username, String nuevaPassword, String nombre, String rol, boolean activo, String email, Integer estudianteId) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setUsername(username);
        usuario.setNombre(nombre);
        usuario.setRol(rol);
        usuario.setActivo(activo);
        usuario.setEmail(email);
        usuario.setEstudianteId(estudianteId);
        usuarioService.actualizarUsuario(usuario, nuevaPassword);
    }

    public void eliminarUsuario(int id) {
        usuarioService.eliminarUsuario(id);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioService.obtenerTodos();
    }

    public Optional<Usuario> buscarPorId(int id) {
        return usuarioService.buscarPorId(id);
    }

    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioService.buscarPorUsername(username);
    }
}
