package com.uniajc.controlador;

import java.util.List;
import java.util.Optional;

import com.uniajc.modelo.Inscripcion;
import com.uniajc.servicios.InscripcionService;

public class ControladorInscripcion {

    private final InscripcionService inscripcionService;

    public ControladorInscripcion() {
        this.inscripcionService = new InscripcionService();
    }

    public ControladorInscripcion(InscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    public void registrarInscripcion(int idEstudiante, int idGrupo) {
        Inscripcion inscripcion = new Inscripcion(idEstudiante, idGrupo);
        inscripcionService.registrarInscripcion(inscripcion);
    }

    public void registrarInscripcion(Inscripcion inscripcion) {
        inscripcionService.registrarInscripcion(inscripcion);
    }

    public List<Inscripcion> listarInscripciones() {
        return inscripcionService.obtenerTodasLasInscripciones();
    }

    public Optional<Inscripcion> buscarPorId(int id) {
        return inscripcionService.buscarInscripcionPorId(id);
    }

    public List<Inscripcion> buscarPorEstudiante(int idEstudiante) {
        return inscripcionService.buscarInscripcionesPorEstudiante(idEstudiante);
    }

    public List<Inscripcion> buscarPorGrupo(int idGrupo) {
        return inscripcionService.buscarInscripcionesPorGrupo(idGrupo);
    }

    public void actualizarInscripcion(int id, int idEstudiante, int idGrupo, Double notaFinal, String estado) {
        Inscripcion inscripcion = new Inscripcion(id, idEstudiante, idGrupo, notaFinal, estado);
        inscripcionService.actualizarInscripcion(inscripcion);
    }

    public void actualizarInscripcion(Inscripcion inscripcion) {
        inscripcionService.actualizarInscripcion(inscripcion);
    }

    public void registrarNota(int idInscripcion, double nota) {
        inscripcionService.registrarNota(idInscripcion, nota);
    }

    public void eliminarInscripcion(int id) {
        inscripcionService.eliminarInscripcion(id);
    }

    public InscripcionService getService() {
        return inscripcionService;
    }
}
