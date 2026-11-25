package com.uniajc.servicios;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import com.uniajc.dao.CalificacionDao;
import com.uniajc.dao.InscripcionCursoDao;
import com.uniajc.modelo.Calificacion;
import com.uniajc.modelo.InscripcionCurso;

public class CalificacionService {

    private final CalificacionDao calificacionDao;
    private final InscripcionCursoDao inscripcionDao;

    public CalificacionService() {
        this.calificacionDao = new CalificacionDao();
        this.inscripcionDao = new InscripcionCursoDao();
    }

    public CalificacionService(CalificacionDao calificacionDao, InscripcionCursoDao inscripcionDao) {
        this.calificacionDao = calificacionDao;
        this.inscripcionDao = inscripcionDao;
    }

    public void registrarCalificacion(Calificacion calificacion) {
        validarCalificacion(calificacion);

        // Validar que la sumatoria de porcentajes no supere 100%
        BigDecimal acumulado = calificacionDao.obtenerPorcentajeAcumulado(calificacion.getInscripcionId(), 0);
        BigDecimal nuevoTotal = acumulado.add(calificacion.getPorcentaje());
        if (nuevoTotal.compareTo(new BigDecimal("100.00")) > 0) {
            throw new IllegalArgumentException(
                String.format("El porcentaje ingresado (%.1f%%) excede el máximo permitido. Acumulado actual: %.1f%% (disponible: %.1f%%).",
                    calificacion.getPorcentaje(), acumulado, new BigDecimal("100.00").subtract(acumulado))
            );
        }

        calificacionDao.guardar(calificacion);
        recalcularNotaFinalInscripcion(calificacion.getInscripcionId());
    }

    public void actualizarCalificacion(Calificacion calificacion) {
        if (calificacion == null || calificacion.getId() <= 0) {
            throw new IllegalArgumentException("Calificación inválida.");
        }

        Optional<Calificacion> existente = calificacionDao.buscarPorId(calificacion.getId());
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la calificación con ID: " + calificacion.getId());
        }

        validarCalificacion(calificacion);

        // Validar porcentaje excluyendo la versión anterior de este registro
        BigDecimal acumulado = calificacionDao.obtenerPorcentajeAcumulado(calificacion.getInscripcionId(), calificacion.getId());
        BigDecimal nuevoTotal = acumulado.add(calificacion.getPorcentaje());
        if (nuevoTotal.compareTo(new BigDecimal("100.00")) > 0) {
            throw new IllegalArgumentException(
                String.format("El porcentaje ingresado (%.1f%%) excede el máximo permitido. Acumulado de otros cortes: %.1f%% (disponible: %.1f%%).",
                    calificacion.getPorcentaje(), acumulado, new BigDecimal("100.00").subtract(acumulado))
            );
        }

        calificacionDao.actualizar(calificacion);
        recalcularNotaFinalInscripcion(calificacion.getInscripcionId());
    }

    public void eliminarCalificacion(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0.");
        }

        Optional<Calificacion> existente = calificacionDao.buscarPorId(id);
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Calificación no encontrada con ID: " + id);
        }

        int inscripcionId = existente.get().getInscripcionId();
        calificacionDao.eliminar(id);
        recalcularNotaFinalInscripcion(inscripcionId);
    }

    public List<Calificacion> obtenerCalificacionesPorInscripcion(int inscripcionId) {
        if (inscripcionId <= 0) {
            throw new IllegalArgumentException("El ID de inscripción debe ser mayor a 0.");
        }
        return calificacionDao.obtenerPorInscripcion(inscripcionId);
    }

    public Optional<Calificacion> buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0.");
        }
        return calificacionDao.buscarPorId(id);
    }

    public List<Calificacion> obtenerTodas() {
        return calificacionDao.obtenerTodos();
    }

    public BigDecimal calcularPorcentajeAcumulado(int inscripcionId) {
        return calificacionDao.obtenerPorcentajeAcumulado(inscripcionId, 0);
    }

    /**
     * Recalcula la nota ponderada final de la inscripción a partir de sus calificaciones.
     * Actualiza el estado a APROBADO / REPROBADO si el porcentaje llega al 100%, o ACTIVO si está en curso.
     */
    public BigDecimal recalcularNotaFinalInscripcion(int inscripcionId) {
        List<Calificacion> lista = calificacionDao.obtenerPorInscripcion(inscripcionId);

        BigDecimal sumaPonderada = BigDecimal.ZERO;
        BigDecimal sumaPorcentajes = BigDecimal.ZERO;

        for (Calificacion c : lista) {
            if (c.getNota() != null && c.getPorcentaje() != null) {
                BigDecimal aporte = c.getNota().multiply(c.getPorcentaje()).divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
                sumaPonderada = sumaPonderada.add(aporte);
                sumaPorcentajes = sumaPorcentajes.add(c.getPorcentaje());
            }
        }

        BigDecimal notaFinal = sumaPonderada.setScale(2, RoundingMode.HALF_UP);

        String nuevoEstado = "ACTIVO";
        if (sumaPorcentajes.compareTo(new BigDecimal("100.00")) >= 0) {
            nuevoEstado = (notaFinal.compareTo(new BigDecimal("3.00")) >= 0) ? "APROBADO" : "REPROBADO";
        }

        try {
            inscripcionDao.actualizarNotaFinalYEstado(inscripcionId, notaFinal, nuevoEstado);
        } catch (Exception e) {
            System.err.println("Advertencia al sincronizar nota final en inscripciones: " + e.getMessage());
        }

        return notaFinal;
    }

    private void validarCalificacion(Calificacion calificacion) {
        if (calificacion == null) {
            throw new IllegalArgumentException("La calificación no puede ser nula.");
        }
        if (calificacion.getInscripcionId() <= 0) {
            throw new IllegalArgumentException("Debe asociar la calificación a una inscripción válida.");
        }
        if (calificacion.getCorte() == null || calificacion.getCorte().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del corte o evaluación es obligatorio.");
        }
        if (calificacion.getPorcentaje() == null) {
            throw new IllegalArgumentException("El porcentaje es obligatorio.");
        }
        if (calificacion.getPorcentaje().compareTo(BigDecimal.ZERO) <= 0 ||
            calificacion.getPorcentaje().compareTo(new BigDecimal("100.00")) > 0) {
            throw new IllegalArgumentException("El porcentaje debe ser mayor a 0% y menor o igual a 100%.");
        }
        if (calificacion.getNota() == null) {
            throw new IllegalArgumentException("La nota es obligatoria.");
        }
        if (calificacion.getNota().compareTo(BigDecimal.ZERO) < 0 ||
            calificacion.getNota().compareTo(new BigDecimal("5.00")) > 0) {
            throw new IllegalArgumentException("La nota debe estar en el rango de 0.0 a 5.0.");
        }

        Optional<InscripcionCurso> optInscripcion = inscripcionDao.buscarPorId(calificacion.getInscripcionId());
        if (optInscripcion.isEmpty()) {
            throw new IllegalArgumentException("La inscripción con ID " + calificacion.getInscripcionId() + " no existe.");
        }
    }
}

