package com.uniajc.servicios;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.uniajc.dao.EstudianteDao;
import com.uniajc.modelo.Estudiante;

public class EstudianteService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private static final Pattern DOCUMENTO_PATTERN =
            Pattern.compile("^[0-9]{5,20}$");

    private static final Pattern TELEFONO_PATTERN =
            Pattern.compile("^[0-9]{7,15}$");

    private final EstudianteDao estudianteDao;

    public EstudianteService() {
        this.estudianteDao = new EstudianteDao();
    }

    public EstudianteService(EstudianteDao estudianteDao) {
        this.estudianteDao = estudianteDao;
    }

    public void registrarEstudiante(Estudiante estudiante) {

        validarEstudiante(estudiante);

        Optional<Estudiante> existenteEmail =
                estudianteDao.buscarPorEmail(estudiante.getEmail());

        if (existenteEmail.isPresent()) {
            throw new IllegalArgumentException(
                    "Ya existe un estudiante registrado con el correo: "
                            + estudiante.getEmail());
        }

        Optional<Estudiante> existenteDocumento =
                estudianteDao.buscarPorDocumento(
                        estudiante.getDocumento());

        if (existenteDocumento.isPresent()) {
            throw new IllegalArgumentException(
                    "Ya existe un estudiante registrado con el documento: "
                            + estudiante.getDocumento());
        }

        estudianteDao.guardar(estudiante);
    }

    public List<Estudiante> obtenerTodosLosEstudiantes() {
        return estudianteDao.obtenerTodos();
    }

    public Optional<Estudiante> buscarEstudiantePorId(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "El ID debe ser un número mayor a 0.");
        }

        return estudianteDao.buscarPorId(id);
    }

    public Optional<Estudiante> buscarEstudiantePorDocumento(
            String documento) {

        if (documento == null || documento.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El documento es obligatorio.");
        }

        return estudianteDao.buscarPorDocumento(documento);
    }

    public List<Estudiante> buscarEstudiantesPorNombre(
            String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            return obtenerTodosLosEstudiantes();
        }

        return estudianteDao.buscarPorNombre(nombre.trim());
    }

    public void actualizarEstudiante(Estudiante estudiante) {

        if (estudiante == null || estudiante.getId() <= 0) {
            throw new IllegalArgumentException(
                    "Estudiante inválido.");
        }

        Optional<Estudiante> existente =
                estudianteDao.buscarPorId(estudiante.getId());

        if (existente.isEmpty()) {
            throw new IllegalArgumentException(
                    "Estudiante no encontrado con ID: "
                            + estudiante.getId());
        }

        validarEstudiante(estudiante);

        Optional<Estudiante> estudianteConMismoEmail =
                estudianteDao.buscarPorEmail(
                        estudiante.getEmail());

        if (estudianteConMismoEmail.isPresent()
                && estudianteConMismoEmail.get().getId()
                != estudiante.getId()) {

            throw new IllegalArgumentException(
                    "Ya existe otro estudiante con el correo: "
                            + estudiante.getEmail());
        }

        Optional<Estudiante> estudianteConMismoDocumento =
                estudianteDao.buscarPorDocumento(
                        estudiante.getDocumento());

        if (estudianteConMismoDocumento.isPresent()
                && estudianteConMismoDocumento.get().getId()
                != estudiante.getId()) {

            throw new IllegalArgumentException(
                    "Ya existe otro estudiante con el documento: "
                            + estudiante.getDocumento());
        }

        estudianteDao.actualizar(estudiante);
    }

    public void eliminarEstudiante(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "El ID debe ser mayor a 0.");
        }

        Optional<Estudiante> existente =
                estudianteDao.buscarPorId(id);

        if (existente.isEmpty()) {
            throw new IllegalArgumentException(
                    "Estudiante no encontrado con ID: "
                            + id);
        }

        estudianteDao.eliminar(id);
    }

    private void validarEstudiante(Estudiante estudiante) {

        if (estudiante == null) {
            throw new IllegalArgumentException(
                    "El estudiante no puede ser nulo.");
        }

        if (estudiante.getName() == null
                || estudiante.getName().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El nombre es obligatorio.");
        }

        if (estudiante.getLastname() == null
                || estudiante.getLastname().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El apellido es obligatorio.");
        }

        if (estudiante.getEmail() == null
                || estudiante.getEmail().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El correo es obligatorio.");
        }

        if (!EMAIL_PATTERN.matcher(
                estudiante.getEmail().trim()).matches()) {

            throw new IllegalArgumentException(
                    "El formato del correo electrónico es inválido.");
        }

        if (estudiante.getTipoDocumento() == null
                || estudiante.getTipoDocumento().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El tipo de documento es obligatorio.");
        }

        if (estudiante.getDocumento() == null
                || estudiante.getDocumento().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El documento es obligatorio.");
        }

        if (!DOCUMENTO_PATTERN.matcher(
                estudiante.getDocumento().trim()).matches()) {

            throw new IllegalArgumentException(
                    "El documento debe contener entre 5 y 20 dígitos numéricos.");
        }

        if (estudiante.getTelefono() == null
                || estudiante.getTelefono().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El teléfono es obligatorio.");
        }

        if (!TELEFONO_PATTERN.matcher(
                estudiante.getTelefono().trim()).matches()) {

            throw new IllegalArgumentException(
                    "El teléfono debe contener entre 7 y 15 dígitos.");
        }

        if (estudiante.getSemestreActual() < 1) {

            throw new IllegalArgumentException(
                    "El semestre actual debe ser mayor o igual a 1.");
        }

        if (estudiante.getEstado() == null
                || estudiante.getEstado().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El estado es obligatorio.");
        }
    }
}