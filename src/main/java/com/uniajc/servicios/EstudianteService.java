package com.uniajc.servicios;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.uniajc.dao.EstudianteRepositorio;
import com.uniajc.modelo.Estudiante;

/** Capa de negocio de estudiantes: validaciones de integridad. */
public class EstudianteService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private final EstudianteRepositorio estudianteDao;

    public EstudianteService(EstudianteRepositorio estudianteDao) {
        this.estudianteDao = estudianteDao;
    }

    public void registrarEstudiante(Estudiante e) {
        validarEstudiante(e);
        estudianteDao.guardar(e);
    }

    public List<Estudiante> obtenerTodosLosEstudiantes() {
        return estudianteDao.obtenerTodos();
    }

    public Optional<Estudiante> buscarEstudiantePorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un numero positivo.");
        }
        return estudianteDao.buscarPorId(id);
    }

    public void actualizarEstudiante(Estudiante e) {
        if (e == null || e.getId_estudiante() <= 0) {
            throw new IllegalArgumentException("Estudiante invalido.");
        }
        if (estudianteDao.buscarPorId(e.getId_estudiante()).isEmpty()) {
            throw new IllegalArgumentException("Estudiante no encontrado.");
        }
        validarEstudiante(e);
        estudianteDao.actualizar(e);
    }

    public void eliminarEstudiante(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID invalido.");
        }
        if (estudianteDao.buscarPorId(id).isEmpty()) {
            throw new IllegalArgumentException("Estudiante no encontrado.");
        }
        estudianteDao.eliminar(id);
    }

    /** Expone la validacion para pruebas unitarias. */
    public void validarPublico(Estudiante e) {
        validarEstudiante(e);
    }

    private void validarEstudiante(Estudiante e) {
        if (e == null) {
            throw new IllegalArgumentException("Estudiante no puede ser nulo.");
        }
        if (isBlank(e.getNombre())) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (isBlank(e.getApellido())) {
            throw new IllegalArgumentException("El apellido es obligatorio.");
        }
        if (isBlank(e.getEmail())) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }
        if (!EMAIL_PATTERN.matcher(e.getEmail()).matches()) {
            throw new IllegalArgumentException("El formato del email es invalido.");
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
