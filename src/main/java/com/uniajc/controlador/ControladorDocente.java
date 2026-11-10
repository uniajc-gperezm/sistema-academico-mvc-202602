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

    public void registrarDocente(String nombre, String especialidad, String email) {
        Docente docente = new Docente(nombre, especialidad, email);
        docenteService.registrarDocente(docente);
    }

    public void registrarDocente(Docente docente) {
        docenteService.registrarDocente(docente);
    }

    public List<Docente> listarDocentes() {
        return docenteService.obtenerTodosLosDocentes();
    }

    public Optional<Docente> buscarPorId(int id) {
        return docenteService.buscarDocentePorId(id);
    }

    public void actualizarDocente(int id, String nombre, String especialidad, String email) {
        Docente docente = new Docente(id, nombre, especialidad, email);
        docenteService.actualizarDocente(docente);
    }

    public void actualizarDocente(Docente docente) {
        docenteService.actualizarDocente(docente);
    }

    public void eliminarDocente(int id) {
        docenteService.eliminarDocente(id);
    }

    public DocenteService getService() {
        return docenteService;
    }
}
