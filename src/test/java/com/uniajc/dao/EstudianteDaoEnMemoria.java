package com.uniajc.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.uniajc.modelo.Estudiante;

/**
 * Doble de prueba en memoria de {@link EstudianteRepositorio}.
 * Permite probar la capa de servicio sin depender de una base de datos real.
 */
public class EstudianteDaoEnMemoria implements EstudianteRepositorio {

    private final Map<Integer, Estudiante> datos = new LinkedHashMap<>();
    private int siguienteId = 1;

    @Override
    public void guardar(Estudiante e) {
        if (e.getId_estudiante() <= 0) {
            e.setId_estudiante(siguienteId++);
        } else {
            siguienteId = Math.max(siguienteId, e.getId_estudiante() + 1);
        }
        datos.put(e.getId_estudiante(), e);
    }

    @Override
    public List<Estudiante> obtenerTodos() {
        return new ArrayList<>(datos.values());
    }

    @Override
    public Optional<Estudiante> buscarPorId(int id) {
        return Optional.ofNullable(datos.get(id));
    }

    @Override
    public void actualizar(Estudiante e) {
        if (!datos.containsKey(e.getId_estudiante())) {
            throw new RuntimeException("Estudiante no encontrado con ID: " + e.getId_estudiante());
        }
        datos.put(e.getId_estudiante(), e);
    }

    @Override
    public void eliminar(int id) {
        if (datos.remove(id) == null) {
            throw new RuntimeException("Estudiante no encontrado con ID: " + id);
        }
    }

    /** Utilidad de prueba: cuantos registros hay almacenados. */
    public int contar() {
        return datos.size();
    }
}
