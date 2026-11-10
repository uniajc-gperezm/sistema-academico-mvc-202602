package com.uniajc.servicios;

import java.util.List;
import java.util.Optional;

import com.uniajc.dao.MateriaDao;
import com.uniajc.modelo.Materia;

public class MateriaService {

    private final MateriaDao materiaDao;

    public MateriaService() {
        this.materiaDao = new MateriaDao();
    }

    public MateriaService(MateriaDao materiaDao) {
        this.materiaDao = materiaDao;
    }

    public void registrarMateria(Materia materia) {
        validarMateria(materia);
        materiaDao.guardar(materia);
    }

    public List<Materia> obtenerTodasLasMaterias() {
        return materiaDao.obtenerTodos();
    }

    public Optional<Materia> buscarMateriaPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a cero.");
        }
        return materiaDao.buscarPorId(id);
    }

    public void actualizarMateria(Materia materia) {
        if (materia == null || materia.getId() <= 0) {
            throw new IllegalArgumentException("Materia inválida para actualizar.");
        }

        Optional<Materia> actualOpt = materiaDao.buscarPorId(materia.getId());
        if (actualOpt.isEmpty()) {
            throw new IllegalArgumentException("Materia no encontrada con ID: " + materia.getId());
        }

        validarMateria(materia);
        materiaDao.actualizar(materia);
    }

    public void eliminarMateria(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }

        Optional<Materia> existente = materiaDao.buscarPorId(id);
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Materia no encontrada con ID: " + id);
        }

        materiaDao.eliminar(id);
    }

    private void validarMateria(Materia materia) {
        if (materia == null) {
            throw new IllegalArgumentException("Los datos de la materia no pueden ser nulos.");
        }

        if (materia.getNombre() == null || materia.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la materia es obligatorio.");
        }
        if (materia.getNombre().trim().length() < 2) {
            throw new IllegalArgumentException("El nombre de la materia debe tener al menos 2 caracteres.");
        }

        if (materia.getCreditos() < 1 || materia.getCreditos() > 10) {
            throw new IllegalArgumentException("Los créditos deben estar entre 1 y 10.");
        }
    }
}
