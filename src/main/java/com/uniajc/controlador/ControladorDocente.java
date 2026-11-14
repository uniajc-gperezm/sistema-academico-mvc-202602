package com.uniajc.controlador;

import com.uniajc.modelo.Docente;
import com.uniajc.servicios.DocenteService;
import java.util.List;

/** Orquesta las acciones de docentes entre la vista y el servicio. */
public class ControladorDocente {
    private final DocenteService service;
    public ControladorDocente() { this(new DocenteService()); }
    public ControladorDocente(DocenteService service) { this.service = service; }
    public List<Docente> listar() { return service.listar(); }
    public boolean crear(Docente docente) { return service.crear(docente); }
    public boolean actualizar(Docente docente) { return service.actualizar(docente); }
    public boolean eliminar(long id) { return service.eliminar(id); }
}
