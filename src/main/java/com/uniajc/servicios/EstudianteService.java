package com.uniajc.servicios;

import java.util.List;

import com.uniajc.modelo.Estudiante;
import com.uniajc.modelo.EstudianteDAO;

/** Valida los datos del estudiante antes de enviarlos al DAO. */
public class EstudianteService {
    private final EstudianteDAO dao;

    public EstudianteService() {
        this(new EstudianteDAO());
    }

    public EstudianteService(EstudianteDAO dao) {
        this.dao = dao;
    }

    public List<Estudiante> listar() {
        return dao.listarTodos();
    }

    public boolean crear(Estudiante estudiante) {
        validar(estudiante);
        return dao.insertar(estudiante);
    }

    public boolean actualizar(Estudiante estudiante) {
        validar(estudiante);
        if (estudiante.getId() <= 0) throw new IllegalArgumentException("El ID del estudiante no es válido.");
        return dao.actualizar(estudiante);
    }

    public boolean eliminar(long id) {
        if (id <= 0) throw new IllegalArgumentException("El ID del estudiante no es válido.");
        return dao.eliminar(id);
    }

    private void validar(Estudiante estudiante) {
        if (estudiante == null) throw new IllegalArgumentException("El estudiante es obligatorio.");
        exigirTexto(estudiante.getName(), "nombre");
        exigirTexto(estudiante.getLastname(), "apellido");
        exigirTexto(estudiante.getEmail(), "correo");
        validarNumero(estudiante.getNit(), "NIT");
        validarNumero(estudiante.getNumcelular(), "celular");
    }

    private void exigirTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) throw new IllegalArgumentException("El " + campo + " es obligatorio.");
    }

    private void validarNumero(Long valor, String campo) {
        if (valor != null && valor < 0) throw new IllegalArgumentException("El " + campo + " no puede ser negativo.");
    }
}
