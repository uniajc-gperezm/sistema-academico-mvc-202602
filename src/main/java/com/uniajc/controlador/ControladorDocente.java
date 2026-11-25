package com.uniajc.controlador;

import java.util.List;
import java.util.Optional;

import com.uniajc.modelo.Docente;
import com.uniajc.servicios.DocenteService;

public class ControladorDocente {

    private final DocenteService docenteService;

    public ControladorDocente() {
        this.docenteService = new DocenteService();
    }

    public ControladorDocente(DocenteService docenteService) {
        this.docenteService = docenteService;
    }

    public void registrarDocente(String nombre, String especialidad, String documento, String email, String telefono, String tituloAcademico, String tipoContrato) {
        Docente docente = new Docente(0, nombre, especialidad, documento, email, telefono, tituloAcademico, tipoContrato);
        docenteService.registrarDocente(docente);
    }

    public List<Docente> listarDocentes() {
        return docenteService.obtenerTodosLosDocentes();
    }

    public Optional<Docente> buscarDocentePorId(int id) {
        return docenteService.buscarDocentePorId(id);
    }

    public List<Docente> buscarDocentesPorNombre(String nombre) {
        return docenteService.buscarDocentesPorNombre(nombre);
    }

    public void actualizarDocente(int id, String nombre, String especialidad, String documento, String email, String telefono, String tituloAcademico, String tipoContrato) {
        Docente docente = new Docente(id, nombre, especialidad, documento, email, telefono, tituloAcademico, tipoContrato);
        docenteService.actualizarDocente(docente);
    }

    public void eliminarDocente(int id) {
        docenteService.eliminarDocente(id);
    }
}