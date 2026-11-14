package com.uniajc.servicios;

import com.uniajc.modelo.Curso;
import com.uniajc.modelo.CursoDAO;
import java.util.List;

/** Valida los datos de un curso antes de delegar en el DAO. */
public class CursoService {
    private final CursoDAO dao;

    public CursoService() { this(new CursoDAO()); }
    public CursoService(CursoDAO dao) { this.dao = dao; }

    public List<Curso> listar() { return dao.listarTodos(); }
    public boolean crear(Curso curso) { validar(curso); return dao.insertar(curso); }
    public boolean actualizar(Curso curso) {
        validar(curso);
        if (curso.getId() <= 0) throw new IllegalArgumentException("El ID del curso no es válido.");
        return dao.actualizar(curso);
    }
    public boolean eliminar(long id) {
        if (id <= 0) throw new IllegalArgumentException("El ID del curso no es válido.");
        return dao.eliminar(id);
    }

    private void validar(Curso curso) {
        if (curso == null) throw new IllegalArgumentException("El curso es obligatorio.");
        if (curso.getFacultad() == null || curso.getFacultad().isBlank()) {
            throw new IllegalArgumentException("La facultad del curso es obligatoria.");
        }
    }
}
