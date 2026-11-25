package com.uniajc.controlador;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.uniajc.modelo.Calificacion;
import com.uniajc.servicios.CalificacionService;

public class ControladorCalificacion {

    private final CalificacionService calificacionService;

    public ControladorCalificacion() {
        this.calificacionService = new CalificacionService();
    }

    public ControladorCalificacion(CalificacionService calificacionService) {
        this.calificacionService = calificacionService;
    }

    public void registrarCalificacion(int inscripcionId, String corte, BigDecimal porcentaje, BigDecimal nota, String observacion) {
        Calificacion c = new Calificacion(inscripcionId, corte, porcentaje, nota, observacion);
        calificacionService.registrarCalificacion(c);
    }

    public void actualizarCalificacion(int id, int inscripcionId, String corte, BigDecimal porcentaje, BigDecimal nota, String observacion) {
        Calificacion c = new Calificacion(id, inscripcionId, corte, porcentaje, nota, observacion);
        calificacionService.actualizarCalificacion(c);
    }

    public void eliminarCalificacion(int id) {
        calificacionService.eliminarCalificacion(id);
    }

    public List<Calificacion> listarPorInscripcion(int inscripcionId) {
        return calificacionService.obtenerCalificacionesPorInscripcion(inscripcionId);
    }

    public List<Calificacion> listarTodas() {
        return calificacionService.obtenerTodas();
    }

    public Optional<Calificacion> buscarPorId(int id) {
        return calificacionService.buscarPorId(id);
    }

    public BigDecimal calcularPorcentajeAcumulado(int inscripcionId) {
        return calificacionService.calcularPorcentajeAcumulado(inscripcionId);
    }

    public BigDecimal recalcularNotaFinal(int inscripcionId) {
        return calificacionService.recalcularNotaFinalInscripcion(inscripcionId);
    }
}

