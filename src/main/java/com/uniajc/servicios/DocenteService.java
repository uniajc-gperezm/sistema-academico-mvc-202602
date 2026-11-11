package com.uniajc.servicios;

import java.util.List;
import java.util.Optional;

import com.uniajc.dao.DocenteDao;
import com.uniajc.modelo.Docente;

/** Capa de negocio de docentes: validaciones de integridad. */
public class DocenteService {

    private final DocenteDao docenteDao;

    public DocenteService(DocenteDao docenteDao) {
        this.docenteDao = docenteDao;
    }

    public void registrarDocente(Docente d) {
        validarDocente(d);
        docenteDao.guardar(d);
    }

    public List<Docente> obtenerTodosLosDocentes() {
        return docenteDao.obtenerTodos();
    }

    public Optional<Docente> buscarDocentePorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un numero positivo.");
        }
        return docenteDao.buscarPorId(id);
    }

    public void actualizarDocente(Docente d) {
        if (d == null || d.getId_docente() <= 0) {
            throw new IllegalArgumentException("Docente invalido.");
        }
        if (docenteDao.buscarPorId(d.getId_docente()).isEmpty()) {
            throw new IllegalArgumentException("Docente no encontrado.");
        }
        validarDocente(d);
        docenteDao.actualizar(d);
    }

    public void eliminarDocente(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID invalido.");
        }
        if (docenteDao.buscarPorId(id).isEmpty()) {
            throw new IllegalArgumentException("Docente no encontrado.");
        }
        docenteDao.eliminar(id);
    }

    private void validarDocente(Docente d) {
        if (d == null) {
            throw new IllegalArgumentException("Docente no puede ser nulo.");
        }
        if (isBlank(d.getNombre())) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (isBlank(d.getEspecialidad())) {
            throw new IllegalArgumentException("La especialidad es obligatoria.");
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
