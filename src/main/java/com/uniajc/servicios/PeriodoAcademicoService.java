package com.uniajc.servicios;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.uniajc.dao.PeriodoAcademicoDao;
import com.uniajc.modelo.PeriodoAcademico;

public class PeriodoAcademicoService {

    private final PeriodoAcademicoDao periodoAcademicoDao;

    public PeriodoAcademicoService() {
        this.periodoAcademicoDao = new PeriodoAcademicoDao();
    }

    public PeriodoAcademicoService(PeriodoAcademicoDao periodoAcademicoDao) {
        this.periodoAcademicoDao = periodoAcademicoDao;
    }

    public void registrarPeriodo(PeriodoAcademico periodo) {
        validarPeriodo(periodo);
        Optional<PeriodoAcademico> existente = periodoAcademicoDao.buscarPorCodigo(periodo.getCodigo());
        if (existente.isPresent() && existente.get().getId() != periodo.getId()) {
            throw new IllegalArgumentException("El código del periodo académico ya existe.");
        }
        periodoAcademicoDao.guardar(periodo);
    }

    public List<PeriodoAcademico> obtenerTodosLosPeriodos() {
        return periodoAcademicoDao.obtenerTodos();
    }

    public Optional<PeriodoAcademico> buscarPeriodoPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0.");
        }
        return periodoAcademicoDao.buscarPorId(id);
    }

    public Optional<PeriodoAcademico> obtenerPeriodoActivo() {
        return periodoAcademicoDao.obtenerPeriodoActivo();
    }

    public void actualizarPeriodo(PeriodoAcademico periodo) {
        if (periodo == null || periodo.getId() <= 0) {
            throw new IllegalArgumentException("Periodo académico inválido.");
        }

        Optional<PeriodoAcademico> existente = periodoAcademicoDao.buscarPorId(periodo.getId());
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Periodo académico no encontrado con ID: " + periodo.getId());
        }

        validarPeriodo(periodo);
        Optional<PeriodoAcademico> conCodigo = periodoAcademicoDao.buscarPorCodigo(periodo.getCodigo());
        if (conCodigo.isPresent() && conCodigo.get().getId() != periodo.getId()) {
            throw new IllegalArgumentException("El código del periodo académico ya existe.");
        }
        periodoAcademicoDao.actualizar(periodo);
    }

    public void activarPeriodo(int id) {
        Optional<PeriodoAcademico> periodoActual = periodoAcademicoDao.buscarPorId(id);
        if (periodoActual.isEmpty()) {
            throw new IllegalArgumentException("Periodo académico no encontrado con ID: " + id);
        }

        List<PeriodoAcademico> todos = periodoAcademicoDao.obtenerTodos();
        for (PeriodoAcademico periodo : todos) {
            if (periodo.getId() != id) {
                periodo.setActivo(false);
                periodoAcademicoDao.actualizar(periodo);
            }
        }

        PeriodoAcademico activo = periodoActual.get();
        activo.setActivo(true);
        periodoAcademicoDao.actualizar(activo);
    }

    public void eliminarPeriodo(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0.");
        }

        Optional<PeriodoAcademico> existente = periodoAcademicoDao.buscarPorId(id);
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Periodo académico no encontrado con ID: " + id);
        }

        periodoAcademicoDao.eliminar(id);
    }

    private void validarPeriodo(PeriodoAcademico periodo) {
        if (periodo == null) {
            throw new IllegalArgumentException("El periodo académico no puede ser nulo.");
        }

        if (periodo.getCodigo() == null || periodo.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El código del periodo académico es obligatorio.");
        }

        if (periodo.getFechaInicio() == null || periodo.getFechaFin() == null) {
            throw new IllegalArgumentException("Las fechas del periodo académico son obligatorias.");
        }

        if (!periodo.getFechaInicio().isBefore(periodo.getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin.");
        }

        if (periodo.getFechaInicio().isBefore(LocalDate.of(2000, 1, 1))) {
            throw new IllegalArgumentException("La fecha de inicio no es válida.");
        }
    }
}
