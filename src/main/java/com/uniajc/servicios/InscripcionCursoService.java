package com.uniajc.servicios;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.uniajc.dao.EstudianteDao;
import com.uniajc.dao.GrupoDao;
import com.uniajc.dao.InscripcionCursoDao;
import com.uniajc.modelo.InscripcionCurso;

public class InscripcionCursoService {

    private static final Set<String> ESTADOS_VALIDOS = Set.of("ACTIVO", "APROBADO", "REPROBADO", "CANCELADO");

    private final InscripcionCursoDao inscripcionDao;
    private final EstudianteDao estudianteDao;
    private final GrupoDao grupoDao;

    public InscripcionCursoService() {
        this.inscripcionDao = new InscripcionCursoDao();
        this.estudianteDao = new EstudianteDao();
        this.grupoDao = new GrupoDao();
    }

    public InscripcionCursoService(InscripcionCursoDao inscripcionDao, EstudianteDao estudianteDao, GrupoDao grupoDao) {
        this.inscripcionDao = inscripcionDao;
        this.estudianteDao = estudianteDao;
        this.grupoDao = grupoDao;
    }

    public void registrarInscripcion(InscripcionCurso inscripcion) {
        validarInscripcion(inscripcion);

        Optional<InscripcionCurso> existente = inscripcionDao.buscarPorEstudianteYGrupo(
                inscripcion.getIdEstudiante(), inscripcion.getIdGrupo());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("El estudiante ya está inscrito en este grupo.");
        }

        inscripcionDao.guardar(inscripcion);
    }

    public List<InscripcionCurso> obtenerTodasLasInscripciones() {
        return inscripcionDao.obtenerTodos();
    }

    public Optional<InscripcionCurso> buscarInscripcionPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0.");
        }
        return inscripcionDao.buscarPorId(id);
    }

    public List<InscripcionCurso> buscarInscripcionesPorEstudiante(int idEstudiante) {
        if (idEstudiante <= 0) {
            throw new IllegalArgumentException("El ID de estudiante debe ser mayor a 0.");
        }
        return inscripcionDao.buscarPorEstudiante(idEstudiante);
    }

    public void actualizarInscripcion(InscripcionCurso inscripcion) {
        if (inscripcion == null || inscripcion.getId() <= 0) {
            throw new IllegalArgumentException("Inscripción inválida.");
        }

        Optional<InscripcionCurso> existente = inscripcionDao.buscarPorId(inscripcion.getId());
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Inscripción no encontrada con ID: " + inscripcion.getId());
        }

        validarInscripcion(inscripcion);

        Optional<InscripcionCurso> duplicado = inscripcionDao.buscarPorEstudianteYGrupo(
                inscripcion.getIdEstudiante(), inscripcion.getIdGrupo());
        if (duplicado.isPresent() && duplicado.get().getId() != inscripcion.getId()) {
            throw new IllegalArgumentException("El estudiante ya está inscrito en este grupo.");
        }

        inscripcionDao.actualizar(inscripcion);
    }

    public void eliminarInscripcion(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0.");
        }

        Optional<InscripcionCurso> existente = inscripcionDao.buscarPorId(id);
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Inscripción no encontrada con ID: " + id);
        }

        inscripcionDao.eliminar(id);
    }

    private void validarInscripcion(InscripcionCurso inscripcion) {
        if (inscripcion == null) {
            throw new IllegalArgumentException("La inscripción no puede ser nula.");
        }

        if (estudianteDao.buscarPorId(inscripcion.getIdEstudiante()).isEmpty()) {
            throw new IllegalArgumentException("El estudiante con ID " + inscripcion.getIdEstudiante() + " no existe.");
        }

        if (grupoDao.buscarPorId(inscripcion.getIdGrupo()).isEmpty()) {
            throw new IllegalArgumentException("El grupo con ID " + inscripcion.getIdGrupo() + " no existe.");
        }

        if (inscripcion.getFinalGrade() != null) {
            BigDecimal nota = inscripcion.getFinalGrade();
            if (nota.compareTo(BigDecimal.ZERO) < 0 || nota.compareTo(new BigDecimal("5.0")) > 0) {
                throw new IllegalArgumentException("La nota final debe estar entre 0.0 y 5.0.");
            }
        }

        if (inscripcion.getStatus() != null && !ESTADOS_VALIDOS.contains(inscripcion.getStatus().toUpperCase())) {
            throw new IllegalArgumentException("Estado inválido. Opciones válidas: " + ESTADOS_VALIDOS);
        }
    }
}

