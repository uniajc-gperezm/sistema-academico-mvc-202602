package com.uniajc.controlador;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.uniajc.modelo.PeriodoAcademico;
import com.uniajc.servicios.PeriodoAcademicoService;

public class ControladorPeriodoAcademico {

    private final PeriodoAcademicoService periodoAcademicoService;

    public ControladorPeriodoAcademico() {
        this.periodoAcademicoService = new PeriodoAcademicoService();
    }

    public ControladorPeriodoAcademico(PeriodoAcademicoService periodoAcademicoService) {
        this.periodoAcademicoService = periodoAcademicoService;
    }

    public void registrarPeriodo(String codigo, LocalDate fechaInicio, LocalDate fechaFin, boolean activo) {
        PeriodoAcademico periodo = new PeriodoAcademico(codigo, fechaInicio, fechaFin, activo);
        periodoAcademicoService.registrarPeriodo(periodo);
    }

    public List<PeriodoAcademico> listarPeriodos() {
        return periodoAcademicoService.obtenerTodosLosPeriodos();
    }

    public Optional<PeriodoAcademico> buscarPeriodoPorId(int id) {
        return periodoAcademicoService.buscarPeriodoPorId(id);
    }

    public Optional<PeriodoAcademico> obtenerPeriodoActivo() {
        return periodoAcademicoService.obtenerPeriodoActivo();
    }

    public void actualizarPeriodo(int id, String codigo, LocalDate fechaInicio, LocalDate fechaFin, boolean activo) {
        PeriodoAcademico periodo = new PeriodoAcademico(id, codigo, fechaInicio, fechaFin, activo);
        periodoAcademicoService.actualizarPeriodo(periodo);
    }

    public void activarPeriodo(int id) {
        periodoAcademicoService.activarPeriodo(id);
    }

    public void eliminarPeriodo(int id) {
        periodoAcademicoService.eliminarPeriodo(id);
    }
}
