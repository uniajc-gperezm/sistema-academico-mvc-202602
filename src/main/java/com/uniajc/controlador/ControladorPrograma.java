package com.uniajc.controlador;

import java.util.List;
import java.util.Optional;

import com.uniajc.modelo.Programa;
import com.uniajc.servicios.ProgramaService;

public class ControladorPrograma {

    private final ProgramaService programaService;

    public ControladorPrograma() {
        this.programaService = new ProgramaService();
    }

    public ControladorPrograma(ProgramaService programaService) {
        this.programaService = programaService;
    }

    public void registrarPrograma(String codigo, String nombre, String facultad, int duracionSemestres) {
        Programa programa = new Programa(codigo, nombre, facultad, duracionSemestres);
        programaService.registrarPrograma(programa);
    }

    public List<Programa> listarProgramas() {
        return programaService.obtenerTodos();
    }

    public Optional<Programa> buscarProgramaPorId(int id) {
        return programaService.buscarPorId(id);
    }

    public Optional<Programa> buscarProgramaPorCodigo(String codigo) {
        return programaService.buscarPorCodigo(codigo);
    }

    public void actualizarPrograma(int id, String codigo, String nombre, String facultad, int duracionSemestres) {
        Programa programa = new Programa(id, codigo, nombre, facultad, duracionSemestres);
        programaService.actualizarPrograma(programa);
    }

    public void eliminarPrograma(int id) {
        programaService.eliminarPrograma(id);
    }
}