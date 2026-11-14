package com.uniajc.servicios;

import java.util.List;
import java.util.Optional;

import com.uniajc.dao.MateriaRepositorio;
import com.uniajc.modelo.Materia;

/** Capa de negocio de materias: validaciones de integridad. */
public class MateriaService {

    private final MateriaRepositorio materiaDao;

    public MateriaService(MateriaRepositorio materiaDao) {
        this.materiaDao = materiaDao;
    }

    public void registrarMateria(Materia m) {
        validarMateria(m);
        materiaDao.guardar(m);
    }

    public List<Materia> obtenerTodasLasMaterias() {
        return materiaDao.obtenerTodos();
    }

    public Optional<Materia> buscarMateriaPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un numero positivo.");
        }
        return materiaDao.buscarPorId(id);
    }

    public void actualizarMateria(Materia m) {
        if (m == null || m.getId_materia() <= 0) {
            throw new IllegalArgumentException("Materia invalida.");
        }
        if (materiaDao.buscarPorId(m.getId_materia()).isEmpty()) {
            throw new IllegalArgumentException("Materia no encontrada.");
        }
        validarMateria(m);
        materiaDao.actualizar(m);
    }

    public void eliminarMateria(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID invalido.");
        }
        if (materiaDao.buscarPorId(id).isEmpty()) {
            throw new IllegalArgumentException("Materia no encontrada.");
        }
        materiaDao.eliminar(id);
    }

    private void validarMateria(Materia m) {
        if (m == null) {
            throw new IllegalArgumentException("Materia no puede ser nula.");
        }
        if (m.getNombre_materia() == null || m.getNombre_materia().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la materia es obligatorio.");
        }
        if (m.getCreditos() < 1 || m.getCreditos() > 10) {
            throw new IllegalArgumentException("Los creditos deben estar entre 1 y 10.");
        }
    }
}
