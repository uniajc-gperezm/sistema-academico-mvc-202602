package com.uniajc.controlador;

import java.util.List;
import java.util.Optional;

import com.uniajc.modelo.Estudiante;
import com.uniajc.servicios.EstudianteService;

public class ControladorEstudiante {

    private final EstudianteService servicio;

    public ControladorEstudiante(EstudianteService servicio) {
        this.servicio = servicio;
    }

    /** Devuelve null si todo salió bien, o el mensaje de error. */
    public String registrar(String nombre, String apellido, String email) {
        try {
            servicio.registrarEstudiante(new Estudiante(0, nombre, apellido, email));
            return null;
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public List<Estudiante> listar() {
        return servicio.obtenerTodosLosEstudiantes();
    }

    public Optional<Estudiante> buscar(int id) {
        return servicio.buscarEstudiantePorId(id);
    }

    public String actualizar(int id, String nombre, String apellido, String email) {
        try {
            servicio.actualizarEstudiante(new Estudiante(id, nombre, apellido, email));
            return null;
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public String eliminar(int id) {
        try {
            servicio.eliminarEstudiante(id);
            return null;
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}