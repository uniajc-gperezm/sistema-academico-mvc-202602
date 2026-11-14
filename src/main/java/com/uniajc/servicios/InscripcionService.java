package com.uniajc.servicios;

import java.util.List;
import java.util.Optional;

import com.uniajc.dao.InscripcionRepositorio;
import com.uniajc.modelo.InscripcionCurso;

/** Capa de negocio de inscripciones: validaciones de integridad. */
public class InscripcionService {

    private final InscripcionRepositorio inscripcionDao;

    public InscripcionService(InscripcionRepositorio inscripcionDao) {
        this.inscripcionDao = inscripcionDao;
    }

    public void registrarInscripcion(InscripcionCurso i) {
        validarInscripcion(i);
        if (inscripcionDao.existeInscripcion(i.getId_estudiante(), i.getId_grupo())) {
            throw new IllegalArgumentException("El estudiante ya esta inscrito en este grupo.");
        }
        inscripcionDao.guardar(i);
    }

    public List<InscripcionCurso> obtenerTodasLasInscripciones() {
        return inscripcionDao.obtenerTodos();
    }

    public Optional<InscripcionCurso> buscarInscripcionPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un numero positivo.");
        }
        return inscripcionDao.buscarPorId(id);
    }

    public void actualizarInscripcion(InscripcionCurso i) {
        if (i == null || i.getId_inscripcion() <= 0) {
            throw new IllegalArgumentException("Inscripcion invalida.");
        }
        if (inscripcionDao.buscarPorId(i.getId_inscripcion()).isEmpty()) {
            throw new IllegalArgumentException("Inscripcion no encontrada.");
        }
        validarInscripcion(i);
        inscripcionDao.actualizar(i);
    }

    public void eliminarInscripcion(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID invalido.");
        }
        if (inscripcionDao.buscarPorId(id).isEmpty()) {
            throw new IllegalArgumentException("Inscripcion no encontrada.");
        }
        inscripcionDao.eliminar(id);
    }

    // ---- Operaciones delegadas a rutinas almacenadas ----
    // La logica de negocio (validar) se queda en Java; el trabajo sobre los
    // datos se delega al motor de base de datos (database/procedimientos.sql).

    /**
     * Asigna la nota final de una inscripcion existente.
     * Valida en Java y delega el UPDATE al procedimiento almacenado
     * {@code sp_asignar_nota}, que ademas recalcula el estado.
     */
    public void asignarNota(int idInscripcion, float nota) {
        if (idInscripcion <= 0) {
            throw new IllegalArgumentException("El ID de la inscripcion debe ser positivo.");
        }
        if (nota < 0 || nota > 5) {
            throw new IllegalArgumentException("La nota debe estar entre 0 y 5.");
        }
        if (inscripcionDao.buscarPorId(idInscripcion).isEmpty()) {
            throw new IllegalArgumentException("Inscripcion no encontrada.");
        }
        inscripcionDao.asignarNota(idInscripcion, nota);
    }

    /** Promedio de notas de un grupo (0 si el grupo no tiene inscripciones). */
    public float promedioGrupo(int idGrupo) {
        if (idGrupo <= 0) {
            throw new IllegalArgumentException("El ID del grupo debe ser positivo.");
        }
        return inscripcionDao.promedioGrupo(idGrupo);
    }

    /** Estado academico que corresponde a una nota (APROBADO / REPROBADO). */
    public String estadoPorNota(float nota) {
        if (nota < 0 || nota > 5) {
            throw new IllegalArgumentException("La nota debe estar entre 0 y 5.");
        }
        return inscripcionDao.estadoPorNota(nota);
    }

    private void validarInscripcion(InscripcionCurso i) {
        if (i == null) {
            throw new IllegalArgumentException("Inscripcion no puede ser nula.");
        }
        if (i.getId_estudiante() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un estudiante.");
        }
        if (i.getId_grupo() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un grupo.");
        }
        if (i.getNota_final() < 0 || i.getNota_final() > 5) {
            throw new IllegalArgumentException("La nota debe estar entre 0 y 5.");
        }
        String estado = i.getEstado();
        if (estado == null || !(estado.equals("ACTIVO") || estado.equals("APROBADO")
                || estado.equals("REPROBADO") || estado.equals("CANCELADO"))) {
            throw new IllegalArgumentException("Estado invalido.");
        }
    }
}
