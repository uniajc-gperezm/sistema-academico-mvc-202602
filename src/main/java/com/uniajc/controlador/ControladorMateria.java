package com.uniajc.controlador;

import com.uniajc.modelo.Materia;
import com.uniajc.servicios.MateriaService;
import java.util.List;

/** Orquesta las acciones de materias entre la vista y el servicio. */
public class ControladorMateria {
    private final MateriaService service;
    public ControladorMateria() { this(new MateriaService()); }
    public ControladorMateria(MateriaService service) { this.service = service; }
    public List<Materia> listar() { return service.listar(); }
    public boolean crear(Materia materia) { return service.crear(materia); }
    public boolean actualizar(Materia materia) { return service.actualizar(materia); }
    public boolean eliminar(int id) { return service.eliminar(id); }
}
