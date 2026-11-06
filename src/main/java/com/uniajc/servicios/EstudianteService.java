package com.uniajc.servicios;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.uniajc.dao.EstudianteDao;
import com.uniajc.modelo.Estudiante;

public class EstudianteService {

    private static final Pattern NOMBRE_PATTERN = Pattern.compile("^[\\p{L}]+( [\\p{L}]+)*$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private final EstudianteDao estudianteDao;

    public EstudianteService(EstudianteDao estudianteDao) {
        this.estudianteDao = estudianteDao;
    }

    public void registrarEstudiante(Estudiante e) {
        validarEstudiante(e);
        if (estudianteDao.buscarPorCampo(e.getEmail().trim()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un estudiante con ese correo.");
        }
        estudianteDao.guardar(e);
    }

    public List<Estudiante> obtenerTodosLosEstudiantes() {
        return estudianteDao.obtenerTodos();
    }

    public Optional<Estudiante> buscarEstudiantePorId(int id) {
        return estudianteDao.buscarPorId(id);
    }

    public void actualizarEstudiante(Estudiante e) {
        if (e == null || e.getId() <= 0) {
            throw new IllegalArgumentException("Estudiante inválido.");
        }
        if (estudianteDao.buscarPorId(e.getId()).isEmpty()) {
            throw new IllegalArgumentException("Estudiante no encontrado.");
        }
        validarEstudiante(e);
        Optional<Estudiante> conMismoCorreo = estudianteDao.buscarPorCampo(e.getEmail().trim());
        if (conMismoCorreo.isPresent() && conMismoCorreo.get().getId() != e.getId()) {
            throw new IllegalArgumentException("Ese correo ya pertenece a otro estudiante.");
        }
        estudianteDao.actualizar(e);
    }

    public void eliminarEstudiante(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        if (estudianteDao.buscarPorId(id).isEmpty()) {
            throw new IllegalArgumentException("Estudiante no encontrado.");
        }
        estudianteDao.eliminar(id);
    }

    private void validarEstudiante(Estudiante e) {
        if (e == null) {
            throw new IllegalArgumentException("El estudiante es obligatorio.");
        }
        validarTexto(e.getNombre(), "nombre");
        validarTexto(e.getApellido(), "apellido");
        if (e.getEmail() == null || e.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }
        if (!EMAIL_PATTERN.matcher(e.getEmail().trim()).matches()) {
            throw new IllegalArgumentException("El formato del email es inválido.");
        }
    }

    private void validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El " + campo + " es obligatorio.");
        }
        String limpio = valor.trim();
        if (limpio.length() < 2 || limpio.length() > 50 || !NOMBRE_PATTERN.matcher(limpio).matches()) {
            throw new IllegalArgumentException("El " + campo + " debe tener entre 2 y 50 letras.");
        }
    }
}