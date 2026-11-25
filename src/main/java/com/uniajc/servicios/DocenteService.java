package com.uniajc.servicios;

import java.util.List;
import java.util.Optional;

import com.uniajc.dao.DocenteDao;
import com.uniajc.modelo.Docente;

public class DocenteService {

    private final DocenteDao docenteDao;

    public DocenteService() {
        this.docenteDao = new DocenteDao();
    }

    public DocenteService(DocenteDao docenteDao) {
        this.docenteDao = docenteDao;
    }

    public void registrarDocente(Docente docente) {
        validarDocente(docente);
        docenteDao.guardar(docente);
    }

    public List<Docente> obtenerTodosLosDocentes() {
        return docenteDao.obtenerTodos();
    }

    public Optional<Docente> buscarDocentePorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0.");
        }
        return docenteDao.buscarPorId(id);
    }

    public List<Docente> buscarDocentesPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return obtenerTodosLosDocentes();
        }
        return docenteDao.buscarPorNombre(nombre.trim());
    }

    public void actualizarDocente(Docente docente) {
        if (docente == null || docente.getIdDocente() <= 0) {
            throw new IllegalArgumentException("Docente inválido.");
        }

        Optional<Docente> existente = docenteDao.buscarPorId(docente.getIdDocente());
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Docente no encontrado con ID: " + docente.getIdDocente());
        }

        validarDocente(docente);
        docenteDao.actualizar(docente);
    }

    public void eliminarDocente(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0.");
        }

        Optional<Docente> existente = docenteDao.buscarPorId(id);
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Docente no encontrado con ID: " + id);
        }

        docenteDao.eliminar(id);
    }

    private void validarDocente(Docente docente) {
        if (docente == null) {
            throw new IllegalArgumentException("El docente no puede ser nulo.");
        }
        if (docente.getNombre() == null || docente.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del docente es obligatorio.");
        }
        if (docente.getEspecialidad() == null || docente.getEspecialidad().trim().isEmpty()) {
            throw new IllegalArgumentException("La especialidad es obligatoria.");
        }
        if (docente.getDocumento() == null || docente.getDocumento().trim().isEmpty()) {
            throw new IllegalArgumentException("El documento es obligatorio.");
        }
    }
}