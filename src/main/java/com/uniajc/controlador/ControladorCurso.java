package com.uniajc.controlador;

import com.uniajc.modelo.Curso;
import com.uniajc.servicios.CursoService;
import java.util.List;

/** Orquesta las acciones de cursos entre la vista y el servicio. */
public class ControladorCurso {
    private final CursoService service;
    public ControladorCurso() { this(new CursoService()); }
    public ControladorCurso(CursoService service) { this.service = service; }
    public List<Curso> listar() { return service.listar(); }
    public boolean crear(Curso curso) { return service.crear(curso); }
    public boolean actualizar(Curso curso) { return service.actualizar(curso); }
    public boolean eliminar(long id) { return service.eliminar(id); }
}
