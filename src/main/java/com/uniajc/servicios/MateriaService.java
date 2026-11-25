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
        if (materia.getCodigo() != null && !materia.getCodigo().trim().isEmpty()) {
            Optional<Materia> existente = materiaDao.buscarPorCodigo(materia.getCodigo());
            if (existente.isPresent() && existente.get().getId() != materia.getId()) {
                throw new IllegalArgumentException("El código de la materia ya existe.");
            }
        }
        materiaDao.guardar(materia);
    }

    public List<Materia> obtenerTodasLasMaterias() {
        return materiaDao.obtenerTodos();
    }

    public Optional<Materia> buscarMateriaPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0.");
        }
        return materiaDao.buscarPorId(id);
    }

    public List<Materia> buscarMateriasPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return obtenerTodasLasMaterias();
        }
        return materiaDao.buscarPorNombre(nombre.trim());
    }

    public void actualizarMateria(Materia materia) {
        if (materia == null || materia.getId() <= 0) {
            throw new IllegalArgumentException("Materia inválida.");
        }

        Optional<Materia> existente = materiaDao.buscarPorId(materia.getId());
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Materia no encontrada con ID: " + materia.getId());
        }

        validarMateria(materia);
        if (materia.getCodigo() != null && !materia.getCodigo().trim().isEmpty()) {
            Optional<Materia> conCodigo = materiaDao.buscarPorCodigo(materia.getCodigo());
            if (conCodigo.isPresent() && conCodigo.get().getId() != materia.getId()) {
                throw new IllegalArgumentException("El código de la materia ya existe.");
            }
        }
        materiaDao.actualizar(materia);
    }

    public void eliminarMateria(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0.");
        }

        Optional<Materia> existente = materiaDao.buscarPorId(id);
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Materia no encontrada con ID: " + id);
        }

        materiaDao.eliminar(id);
    }

    private void validarMateria(Materia materia) {
        if (materia == null) {
            throw new IllegalArgumentException("La materia no puede ser nula.");
        }

        if (materia.getName() == null || materia.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la materia es obligatorio.");
        }

        if (materia.getCredits() < 1 || materia.getCredits() > 10) {
            throw new IllegalArgumentException("Los créditos deben estar entre 1 y 10.");
        }

        if (materia.getCodigo() != null && !materia.getCodigo().trim().isEmpty()) {
            String codigo = materia.getCodigo().trim();
            if (!codigo.matches("^[A-Z0-9-]+$")) {
                throw new IllegalArgumentException("El código debe contener solo letras mayúsculas, números y guiones.");
            }
        }

        if (materia.getHorasSemanales() < 1 || materia.getHorasSemanales() > 40) {
            throw new IllegalArgumentException("Las horas semanales deben estar entre 1 y 40.");
        }
    }
}

