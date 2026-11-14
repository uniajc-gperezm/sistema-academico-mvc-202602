package com.uniajc.controlador;

import com.uniajc.modelo.Estudiante;
import com.uniajc.servicios.EstudianteService;
import java.util.List;

/** Conecta las acciones de la vista con las operaciones de persistencia del DAO. */
public class ControladorEstudiante {
    private final EstudianteService estudianteService;

    public ControladorEstudiante() {
        this(new EstudianteService());
    }

    public ControladorEstudiante(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    // READ: solicita al DAO la lista que se mostrará en la tabla.
    public List<Estudiante> listarEstudiantes() {
        return estudianteService.listar();
    }

    // CREATE: entrega al DAO el estudiante capturado en el formulario.
    public boolean crearEstudiante(Estudiante estudiante) {
        return estudianteService.crear(estudiante);
    }

    // UPDATE: solicita al DAO guardar los cambios del estudiante seleccionado.
    public boolean actualizarEstudiante(Estudiante estudiante) {
        return estudianteService.actualizar(estudiante);
    }

    // DELETE: solicita al DAO borrar el registro identificado por su ID.
    public boolean eliminarEstudiante(long id) {
        return estudianteService.eliminar(id);
    }
}
