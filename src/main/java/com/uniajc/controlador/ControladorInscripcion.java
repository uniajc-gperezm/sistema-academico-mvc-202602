package com.uniajc.controlador;

import com.uniajc.modelo.Inscripcion;
import com.uniajc.servicios.InscripcionService;
import java.util.List;

/** Orquesta las acciones de inscripciones entre la vista y el servicio. */
public class ControladorInscripcion {
    private final InscripcionService service;
    public ControladorInscripcion() { this(new InscripcionService()); }
    public ControladorInscripcion(InscripcionService service) { this.service = service; }
    public List<Inscripcion> listar() { return service.listar(); }
    public boolean crear(Inscripcion inscripcion) { return service.crear(inscripcion); }
    public boolean actualizar(Inscripcion inscripcion) { return service.actualizar(inscripcion); }
    public boolean eliminar(int id) { return service.eliminar(id); }
}
