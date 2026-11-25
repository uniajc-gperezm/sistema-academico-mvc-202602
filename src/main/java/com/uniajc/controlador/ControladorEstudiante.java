package com.uniajc.controlador;

import java.util.List;
import java.util.Optional;

import com.uniajc.modelo.Estudiante;
import com.uniajc.servicios.EstudianteService;

public class ControladorEstudiante {

    private final EstudianteService estudianteService;

    public ControladorEstudiante() {
        this.estudianteService = new EstudianteService();
    }

    public ControladorEstudiante(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    public void registrarEstudiante(
            String nombre,
            String apellido,
            String email,
            String tipoDocumento,
            String documento,
            String telefono,
            int semestreActual,
            String estado) {

        Estudiante estudiante = new Estudiante(
                nombre,
                apellido,
                email,
                tipoDocumento,
                documento,
                telefono,
                semestreActual,
                estado);

        estudianteService.registrarEstudiante(estudiante);
    }

    public List<Estudiante> listarEstudiantes() {
        return estudianteService.obtenerTodosLosEstudiantes();
    }

    public Optional<Estudiante> buscarEstudiantePorId(int id) {
        return estudianteService.buscarEstudiantePorId(id);
    }

    public List<Estudiante> buscarEstudiantesPorNombre(String nombre) {
        return estudianteService.buscarEstudiantesPorNombre(nombre);
    }

    public void actualizarEstudiante(
            int id,
            String nombre,
            String apellido,
            String email,
            String tipoDocumento,
            String documento,
            String telefono,
            int semestreActual,
            String estado) {

        Estudiante estudiante = new Estudiante(
                id,
                nombre,
                apellido,
                email,
                tipoDocumento,
                documento,
                telefono,
                semestreActual,
                estado);

        estudianteService.actualizarEstudiante(estudiante);
    }

    public void eliminarEstudiante(int id) {
        estudianteService.eliminarEstudiante(id);
    }
}