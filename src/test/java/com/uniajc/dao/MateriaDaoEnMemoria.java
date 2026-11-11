package com.uniajc.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.uniajc.modelo.Materia;

/** Doble de prueba en memoria de {@link MateriaRepositorio}. */
public class MateriaDaoEnMemoria implements MateriaRepositorio {

    private final Map<Integer, Materia> datos = new LinkedHashMap<>();
    private int siguienteId = 1;

    @Override
    public void guardar(Materia m) {
        if (m.getId_materia() <= 0) {
            m.setId_materia(siguienteId++);
        } else {
            siguienteId = Math.max(siguienteId, m.getId_materia() + 1);
        }
        datos.put(m.getId_materia(), m);
    }

    @Override
    public List<Materia> obtenerTodos() {
        return new ArrayList<>(datos.values());
    }

    @Override
    public Optional<Materia> buscarPorId(int id) {
        return Optional.ofNullable(datos.get(id));
    }

    @Override
    public void actualizar(Materia m) {
        if (!datos.containsKey(m.getId_materia())) {
            throw new RuntimeException("Materia no encontrada con ID: " + m.getId_materia());
        }
        datos.put(m.getId_materia(), m);
    }

    @Override
    public void eliminar(int id) {
        if (datos.remove(id) == null) {
            throw new RuntimeException("Materia no encontrada con ID: " + id);
        }
    }

    public int contar() {
        return datos.size();
    }
}
