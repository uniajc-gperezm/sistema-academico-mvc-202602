package com.uniajc.servicios;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.uniajc.dao.EstudianteDao;
import com.uniajc.modelo.Estudiante;

public class EstudianteService {

    private static final Pattern NOMBRE_PATTERN = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúñÑ\\s]{2,100}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final EstudianteDao estudianteDao;

    public EstudianteService() {
        this.estudianteDao = new EstudianteDao();
    }

    public EstudianteService(EstudianteDao estudianteDao) {
        this.estudianteDao = estudianteDao;
    }

    public void registrarEstudiante(Estudiante estudiante) {
        validarEstudiante(estudiante);

        Optional<Estudiante> existente = estudianteDao.buscarPorEmail(estudiante.getEmail());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un estudiante con el correo: " + estudiante.getEmail());
        }

        estudianteDao.guardar(estudiante);
    }

    public List<Estudiante> obtenerTodosLosEstudiantes() {
        return estudianteDao.obtenerTodos();
    }

    public List<Estudiante> obtenerTodasLasEntidades() {
        return obtenerTodosLosEstudiantes();
    }

    public Optional<Estudiante> buscarEstudiantePorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a cero.");
        }
        return estudianteDao.buscarPorId(id);
    }

    public Optional<Estudiante> buscarPorId(int id) {
        return buscarEstudiantePorId(id);
    }

    public void actualizarEstudiante(Estudiante estudiante) {
        if (estudiante == null || estudiante.getId() <= 0) {
            throw new IllegalArgumentException("Estudiante inválido para actualizar.");
        }

        Optional<Estudiante> actualOpt = estudianteDao.buscarPorId(estudiante.getId());
        if (actualOpt.isEmpty()) {
            throw new IllegalArgumentException("Estudiante no encontrado con ID: " + estudiante.getId());
        }

        validarEstudiante(estudiante);

        // Si cambió el correo, verificar que no esté ocupado por otro estudiante
        Optional<Estudiante> porEmail = estudianteDao.buscarPorEmail(estudiante.getEmail());
        if (porEmail.isPresent() && porEmail.get().getId() != estudiante.getId()) {
            throw new IllegalArgumentException("El correo ya está registrado para otro estudiante.");
        }

        estudianteDao.actualizar(estudiante);
    }

    public void eliminarEstudiante(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }

        Optional<Estudiante> existente = estudianteDao.buscarPorId(id);
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Estudiante no encontrado con ID: " + id);
        }

        estudianteDao.eliminar(id);
    }

    private void validarEstudiante(Estudiante estudiante) {
        if (estudiante == null) {
            throw new IllegalArgumentException("Los datos del estudiante no pueden ser nulos.");
        }

        if (estudiante.getNombre() == null || estudiante.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (!NOMBRE_PATTERN.matcher(estudiante.getNombre().trim()).matches()) {
            throw new IllegalArgumentException("El nombre solo debe contener letras (mínimo 2 caracteres).");
        }

        if (estudiante.getApellido() == null || estudiante.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido es obligatorio.");
        }
        if (!NOMBRE_PATTERN.matcher(estudiante.getApellido().trim()).matches()) {
            throw new IllegalArgumentException("El apellido solo debe contener letras (mínimo 2 caracteres).");
        }

        if (estudiante.getEmail() == null || estudiante.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio.");
        }
        if (!EMAIL_PATTERN.matcher(estudiante.getEmail().trim()).matches()) {
            throw new IllegalArgumentException("El formato del correo electrónico es inválido.");
        }
    }
}
