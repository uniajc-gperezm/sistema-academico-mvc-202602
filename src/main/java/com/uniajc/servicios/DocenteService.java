package com.uniajc.servicios;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.uniajc.dao.DocenteDao;
import com.uniajc.modelo.Docente;

public class DocenteService {

    private static final Pattern NOMBRE_PATTERN = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúñÑ\\s]{2,100}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final DocenteDao docenteDao;

    public DocenteService() {
        this.docenteDao = new DocenteDao();
    }

    public DocenteService(DocenteDao docenteDao) {
        this.docenteDao = docenteDao;
    }

    public void registrarDocente(Docente docente) {
        validarDocente(docente);

        Optional<Docente> existente = docenteDao.buscarPorEmail(docente.getEmail());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un docente con el correo: " + docente.getEmail());
        }

        docenteDao.guardar(docente);
    }

    public List<Docente> obtenerTodosLosDocentes() {
        return docenteDao.obtenerTodos();
    }

    public Optional<Docente> buscarDocentePorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a cero.");
        }
        return docenteDao.buscarPorId(id);
    }

    public void actualizarDocente(Docente docente) {
        if (docente == null || docente.getId() <= 0) {
            throw new IllegalArgumentException("Docente inválido para actualizar.");
        }

        Optional<Docente> actualOpt = docenteDao.buscarPorId(docente.getId());
        if (actualOpt.isEmpty()) {
            throw new IllegalArgumentException("Docente no encontrado con ID: " + docente.getId());
        }

        validarDocente(docente);

        Optional<Docente> porEmail = docenteDao.buscarPorEmail(docente.getEmail());
        if (porEmail.isPresent() && porEmail.get().getId() != docente.getId()) {
            throw new IllegalArgumentException("El correo ya está registrado para otro docente.");
        }

        docenteDao.actualizar(docente);
    }

    public void eliminarDocente(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }

        Optional<Docente> existente = docenteDao.buscarPorId(id);
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Docente no encontrado con ID: " + id);
        }

        docenteDao.eliminar(id);
    }

    private void validarDocente(Docente docente) {
        if (docente == null) {
            throw new IllegalArgumentException("Los datos del docente no pueden ser nulos.");
        }

        if (docente.getNombre() == null || docente.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del docente es obligatorio.");
        }
        if (!NOMBRE_PATTERN.matcher(docente.getNombre().trim()).matches()) {
            throw new IllegalArgumentException("El nombre del docente solo debe contener letras (mínimo 2 caracteres).");
        }

        if (docente.getEspecialidad() == null || docente.getEspecialidad().trim().isEmpty()) {
            throw new IllegalArgumentException("La especialidad es obligatoria.");
        }
        if (docente.getEspecialidad().trim().length() < 2) {
            throw new IllegalArgumentException("La especialidad debe tener al menos 2 caracteres.");
        }

        if (docente.getEmail() == null || docente.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio.");
        }
        if (!EMAIL_PATTERN.matcher(docente.getEmail().trim()).matches()) {
            throw new IllegalArgumentException("El formato del correo electrónico es inválido.");
        }
    }
}
