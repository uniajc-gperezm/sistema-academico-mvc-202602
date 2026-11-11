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
}
