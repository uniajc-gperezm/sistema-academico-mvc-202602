package com.uniajc.servicios;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.uniajc.dao.EstudianteDao;
import com.uniajc.dao.GrupoDao;
import com.uniajc.dao.InscripcionDao;
import com.uniajc.modelo.Inscripcion;

public class InscripcionService {

    private static final List<String> ESTADOS_VALIDOS = Arrays.asList("ACTIVO", "APROBADO", "REPROBADO", "CANCELADO");

    private final InscripcionDao inscripcionDao;
    private final EstudianteDao estudianteDao;
    private final GrupoDao grupoDao;

    public InscripcionService() {
        this.inscripcionDao = new InscripcionDao();
        this.estudianteDao = new EstudianteDao();
        this.grupoDao = new GrupoDao();
    }

    public InscripcionService(InscripcionDao inscripcionDao, EstudianteDao estudianteDao, GrupoDao grupoDao) {
        this.inscripcionDao = inscripcionDao;
        this.estudianteDao = estudianteDao;
        this.grupoDao = grupoDao;
    }

    public void registrarInscripcion(Inscripcion inscripcion) {
        validarInscripcion(inscripcion);

        Optional<Inscripcion> existente = inscripcionDao.buscarPorEstudianteYGrupo(
                inscripcion.getIdEstudiante(), inscripcion.getIdGrupo());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("El estudiante ya está inscrito en este grupo.");
        }

        inscripcionDao.guardar(inscripcion);
    }

    public List<Inscripcion> obtenerTodasLasInscripciones() {
        return inscripcionDao.obtenerTodos();
    }

    public Optional<Inscripcion> buscarInscripcionPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a cero.");
        }
        return inscripcionDao.buscarPorId(id);
    }

    public List<Inscripcion> buscarInscripcionesPorEstudiante(int idEstudiante) {
        if (idEstudiante <= 0) {
            throw new IllegalArgumentException("ID de estudiante inválido.");
        }
        return inscripcionDao.buscarPorEstudiante(idEstudiante);
    }

    public List<Inscripcion> buscarInscripcionesPorGrupo(int idGrupo) {
        if (idGrupo <= 0) {
            throw new IllegalArgumentException("ID de grupo inválido.");
        }
        return inscripcionDao.buscarPorGrupo(idGrupo);
    }

    public void actualizarInscripcion(Inscripcion inscripcion) {
        if (inscripcion == null || inscripcion.getId() <= 0) {
            throw new IllegalArgumentException("Inscripción inválida para actualizar.");
        }

        Optional<Inscripcion> actualOpt = inscripcionDao.buscarPorId(inscripcion.getId());
        if (actualOpt.isEmpty()) {
            throw new IllegalArgumentException("Inscripción no encontrada con ID: " + inscripcion.getId());
        }

        validarInscripcion(inscripcion);

        Optional<Inscripcion> porPar = inscripcionDao.buscarPorEstudianteYGrupo(
                inscripcion.getIdEstudiante(), inscripcion.getIdGrupo());
        if (porPar.isPresent() && porPar.get().getId() != inscripcion.getId()) {
            throw new IllegalArgumentException("Ya existe otra inscripción para este estudiante en dicho grupo.");
        }

        inscripcionDao.actualizar(inscripcion);
    }

    public void registrarNota(int idInscripcion, double nota) {
        if (idInscripcion <= 0) {
            throw new IllegalArgumentException("ID de inscripción inválido.");
        }

        if (nota < 0.0 || nota > 5.0) {
            throw new IllegalArgumentException("La nota debe estar entre 0.0 y 5.0.");
        }

        Optional<Inscripcion> inscripcionOpt = inscripcionDao.buscarPorId(idInscripcion);
        if (inscripcionOpt.isEmpty()) {
            throw new IllegalArgumentException("Inscripción no encontrada con ID: " + idInscripcion);
        }

        Inscripcion inscripcion = inscripcionOpt.get();
        inscripcion.setNotaFinal(nota);
        if (nota >= 3.0) {
            inscripcion.setEstado("APROBADO");
        } else {
            inscripcion.setEstado("REPROBADO");
        }

        inscripcionDao.actualizar(inscripcion);
    }

    public void eliminarInscripcion(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }

        Optional<Inscripcion> existente = inscripcionDao.buscarPorId(id);
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Inscripción no encontrada con ID: " + id);
        }

        inscripcionDao.eliminar(id);
    }

    private void validarInscripcion(Inscripcion inscripcion) {
        if (inscripcion == null) {
            throw new IllegalArgumentException("Los datos de la inscripción no pueden ser nulos.");
        }

        if (inscripcion.getIdEstudiante() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un estudiante válido.");
        }
        if (estudianteDao.buscarPorId(inscripcion.getIdEstudiante()).isEmpty()) {
            throw new IllegalArgumentException("El estudiante con ID " + inscripcion.getIdEstudiante() + " no existe.");
        }

        if (inscripcion.getIdGrupo() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un grupo válido.");
        }
        if (grupoDao.buscarPorId(inscripcion.getIdGrupo()).isEmpty()) {
            throw new IllegalArgumentException("El grupo con ID " + inscripcion.getIdGrupo() + " no existe.");
        }

        if (inscripcion.getNotaFinal() != null) {
            if (inscripcion.getNotaFinal() < 0.0 || inscripcion.getNotaFinal() > 5.0) {
                throw new IllegalArgumentException("La nota final debe estar entre 0.0 y 5.0.");
            }
        }

        if (inscripcion.getEstado() != null && !inscripcion.getEstado().trim().isEmpty()) {
            String estadoUpper = inscripcion.getEstado().trim().toUpperCase();
            if (!ESTADOS_VALIDOS.contains(estadoUpper)) {
                throw new IllegalArgumentException("Estado no válido. Valores permitidos: " + ESTADOS_VALIDOS);
            }
            inscripcion.setEstado(estadoUpper);
        } else {
            inscripcion.setEstado("ACTIVO");
        }
    }
}
