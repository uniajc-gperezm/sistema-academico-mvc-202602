package com.uniajc.dao;

import java.util.List;
import java.util.Optional;

import com.uniajc.modelo.InscripcionCurso;

/** Contrato de persistencia de inscripciones. Implementado por {@link InscripcionDao}. */
public interface InscripcionRepositorio {

    void guardar(InscripcionCurso i);

    List<InscripcionCurso> obtenerTodos();

    Optional<InscripcionCurso> buscarPorId(int id);

    /** Indica si ya existe una inscripcion del estudiante en el grupo (evita duplicados). */
    boolean existeInscripcion(int idEstudiante, int idGrupo);

    void actualizar(InscripcionCurso i);

    void eliminar(int id);

    // ---- Rutinas almacenadas (database/procedimientos.sql) ----
    // Estas operaciones se delegan al motor de base de datos en lugar de
    // resolverse con SQL armado en Java.

    /**
     * Asigna la nota final de una inscripcion y recalcula su estado.
     * En PostgreSQL ejecuta el procedimiento almacenado {@code sp_asignar_nota}
     * (se invoca con CALL y no retorna valor: produce su efecto).
     */
    void asignarNota(int idInscripcion, float nota);

    /**
     * Promedio de las notas de un grupo, o 0 si aun no tiene inscripciones.
     * En PostgreSQL invoca la funcion {@code fn_promedio_grupo} con SELECT.
     */
    float promedioGrupo(int idGrupo);

    /**
     * Traduce una nota numerica a su estado academico (APROBADO / REPROBADO).
     * En PostgreSQL invoca la funcion {@code fn_estado_por_nota} con SELECT.
     */
    String estadoPorNota(float nota);
}
