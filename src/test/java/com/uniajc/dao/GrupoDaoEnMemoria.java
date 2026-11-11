package com.uniajc.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.uniajc.modelo.Grupo;

/** Doble de prueba en memoria de {@link GrupoRepositorio}. */
public class GrupoDaoEnMemoria implements GrupoRepositorio {

    private final Map<Integer, Grupo> datos = new LinkedHashMap<>();
    private int siguienteId = 1;

    @Override
    public void guardar(Grupo g) {
        if (g.getId_grupo() <= 0) {
            g.setId_grupo(siguienteId++);
        } else {
            siguienteId = Math.max(siguienteId, g.getId_grupo() + 1);
        }
        datos.put(g.getId_grupo(), g);
    }

    @Override
    public List<Grupo> obtenerTodos() {
        return new ArrayList<>(datos.values());
    }

    @Override
    public Optional<Grupo> buscarPorId(int id) {
        return Optional.ofNullable(datos.get(id));
    }

    @Override
    public void actualizar(Grupo g) {
        if (!datos.containsKey(g.getId_grupo())) {
            throw new RuntimeException("Grupo no encontrado con ID: " + g.getId_grupo());
        }
        datos.put(g.getId_grupo(), g);
    }

    @Override
    public void eliminar(int id) {
        if (datos.remove(id) == null) {
            throw new RuntimeException("Grupo no encontrado con ID: " + id);
        }
    }

    public int contar() {
        return datos.size();
    }
}
