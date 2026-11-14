package com.uniajc.controlador;

import com.uniajc.modelo.Grupo;
import com.uniajc.servicios.GrupoService;
import java.util.List;

/** Orquesta las acciones de grupos entre la vista y el servicio. */
public class ControladorGrupo {
    private final GrupoService service;
    public ControladorGrupo() { this(new GrupoService()); }
    public ControladorGrupo(GrupoService service) { this.service = service; }
    public List<Grupo> listar() { return service.listar(); }
    public boolean crear(Grupo grupo) { return service.crear(grupo); }
    public boolean actualizar(Grupo grupo) { return service.actualizar(grupo); }
    public boolean eliminar(long id) { return service.eliminar(id); }
}
