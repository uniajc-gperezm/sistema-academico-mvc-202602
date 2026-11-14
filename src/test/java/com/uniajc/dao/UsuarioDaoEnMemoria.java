package com.uniajc.dao;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import com.uniajc.modelo.Usuario;

/** Doble de prueba en memoria de {@link UsuarioRepositorio}. */
public class UsuarioDaoEnMemoria implements UsuarioRepositorio {

    private final Map<Integer, Usuario> datos = new LinkedHashMap<>();
    private int siguienteId = 1;

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        for (Usuario u : datos.values()) {
            if (u.getUsername() != null && u.getUsername().equalsIgnoreCase(username)) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> buscarPorId(int id) {
        return Optional.ofNullable(datos.get(id));
    }

    @Override
    public void guardar(Usuario u) {
        if (u.getId() <= 0) {
            u.setId(siguienteId++);
        } else {
            siguienteId = Math.max(siguienteId, u.getId() + 1);
        }
        datos.put(u.getId(), u);
    }

    @Override
    public void cambiarPassword(int id, String nuevoPasswordHash) {
        Usuario u = datos.get(id);
        if (u == null) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        u.setPassword(nuevoPasswordHash);
    }

    public int contar() {
        return datos.size();
    }
}
