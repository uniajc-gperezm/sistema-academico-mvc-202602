package com.uniajc.servicios;

import com.uniajc.modelo.Grupo;
import com.uniajc.modelo.GrupoDAO;
import java.util.List;

/** Valida las referencias y los datos de programación de grupos. */
public class GrupoService {
    private final GrupoDAO dao;

    public GrupoService() { this(new GrupoDAO()); }
    public GrupoService(GrupoDAO dao) { this.dao = dao; }

    public List<Grupo> listar() { return dao.listarTodos(); }
    public boolean crear(Grupo grupo) { validar(grupo); return dao.insertar(grupo); }
    public boolean actualizar(Grupo grupo) {
        validar(grupo);
        if (grupo.getIdGrupo() <= 0) throw new IllegalArgumentException("El ID del grupo no es válido.");
        return dao.actualizar(grupo);
    }
    public boolean eliminar(long id) {
        if (id <= 0) throw new IllegalArgumentException("El ID del grupo no es válido.");
        return dao.eliminar(id);
    }

    private void validar(Grupo grupo) {
        if (grupo == null) throw new IllegalArgumentException("El grupo es obligatorio.");
        if (grupo.getIdMateria() <= 0) throw new IllegalArgumentException("Selecciona un curso válido para el grupo.");
        if (grupo.getIdDocente() <= 0) throw new IllegalArgumentException("Selecciona un docente válido para el grupo.");
        if (grupo.getAula() == null || grupo.getAula().isBlank()) throw new IllegalArgumentException("El aula es obligatoria.");
        if (grupo.getHorario() == null || grupo.getHorario().isBlank()) throw new IllegalArgumentException("El horario es obligatorio.");
    }
}
