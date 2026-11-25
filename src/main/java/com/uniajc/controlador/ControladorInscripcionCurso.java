package com.uniajc.controlador;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.uniajc.modelo.InscripcionCurso;
import com.uniajc.servicios.InscripcionCursoService;

public class ControladorInscripcionCurso {

    private final InscripcionCursoService inscripcionService;

    public ControladorInscripcionCurso() {
        this.inscripcionService = new InscripcionCursoService();
    }

    public ControladorInscripcionCurso(InscripcionCursoService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    public void registrarInscripcion(int idEstudiante, int idGrupo, BigDecimal notaFinal, String estado) {
        InscripcionCurso inscripcion = new InscripcionCurso(idEstudiante, idGrupo, notaFinal, estado);
        inscripcionService.registrarInscripcion(inscripcion);
    }

    public List<InscripcionCurso> listarInscripciones() {
        return inscripcionService.obtenerTodasLasInscripciones();
    }

    public Optional<InscripcionCurso> buscarInscripcionPorId(int id) {
        return inscripcionService.buscarInscripcionPorId(id);
    }

    public List<InscripcionCurso> buscarPorEstudiante(int idEstudiante) {
        return inscripcionService.buscarInscripcionesPorEstudiante(idEstudiante);
    }

    public void actualizarInscripcion(int id, int idEstudiante, int idGrupo, BigDecimal notaFinal, String estado) {
        InscripcionCurso inscripcion = new InscripcionCurso(id, idEstudiante, idGrupo, notaFinal, estado);
        inscripcionService.actualizarInscripcion(inscripcion);
    }

    public void eliminarInscripcion(int id) {
        inscripcionService.eliminarInscripcion(id);
    }
}

