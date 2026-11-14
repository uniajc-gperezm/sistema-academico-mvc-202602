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

    // ---- Rutinas almacenadas replicadas en memoria ----
    // Reproducen el comportamiento de database/procedimientos.sql para poder
    // probar el servicio sin conexion a PostgreSQL.

    /**
     * Equivalente a sp_asignar_nota: actualiza la nota y recalcula el estado.
     * Una inscripcion CANCELADA conserva su estado.
     */
    @Override
    public void asignarNota(int idInscripcion, float nota) {
        if (nota < 0 || nota > 5) {
            throw new RuntimeException("La nota debe estar entre 0 y 5 (recibida: " + nota + ")");
        }
        InscripcionCurso i = datos.get(idInscripcion);
        if (i == null) {
            throw new RuntimeException("No existe la inscripcion con ID " + idInscripcion);
        }
        i.setNota_final(nota);
        if (!"CANCELADO".equals(i.getEstado())) {
            i.setEstado(nota >= 3.0f ? "APROBADO" : "REPROBADO");
        }
    }

    /** Equivalente a fn_promedio_grupo: 0 si el grupo no tiene inscripciones. */
    @Override
    public float promedioGrupo(int idGrupo) {
        float suma = 0f;
        int cantidad = 0;
        for (InscripcionCurso i : datos.values()) {
            if (i.getId_grupo() == idGrupo) {
                suma += i.getNota_final();
                cantidad++;
            }
        }
        return cantidad == 0 ? 0f : suma / cantidad;
    }

    /** Equivalente a fn_estado_por_nota. */
    @Override
    public String estadoPorNota(float nota) {
        return nota >= 3.0f ? "APROBADO" : "REPROBADO";
    }

    public int contar() {
        return datos.size();
    }
}
