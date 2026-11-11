package com.uniajc.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.uniajc.modelo.InscripcionCurso;

/** Doble de prueba en memoria de {@link InscripcionRepositorio}. */
public class InscripcionDaoEnMemoria implements InscripcionRepositorio {

    private final Map<Integer, InscripcionCurso> datos = new LinkedHashMap<>();
    private int siguienteId = 1;

    @Override
    public void guardar(InscripcionCurso i) {
        if (i.getId_inscripcion() <= 0) {
            i.setId_inscripcion(siguienteId++);
        } else {
            siguienteId = Math.max(siguienteId, i.getId_inscripcion() + 1);
        }
        datos.put(i.getId_inscripcion(), i);
    }

    @Override
    public List<InscripcionCurso> obtenerTodos() {
        return new ArrayList<>(datos.values());
    }

    @Override
    public Optional<InscripcionCurso> buscarPorId(int id) {
        return Optional.ofNullable(datos.get(id));
    }

    @Override
    public boolean existeInscripcion(int idEstudiante, int idGrupo) {
        for (InscripcionCurso i : datos.values()) {
            if (i.getId_estudiante() == idEstudiante && i.getId_grupo() == idGrupo) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void actualizar(InscripcionCurso i) {
        if (!datos.containsKey(i.getId_inscripcion())) {
            throw new RuntimeException("Inscripcion no encontrada con ID: " + i.getId_inscripcion());
        }
        datos.put(i.getId_inscripcion(), i);
    }

    @Override
    public void eliminar(int id) {
        if (datos.remove(id) == null) {
            throw new RuntimeException("Inscripcion no encontrada con ID: " + id);
        }
    }

    public int contar() {
        return datos.size();
    }
}
