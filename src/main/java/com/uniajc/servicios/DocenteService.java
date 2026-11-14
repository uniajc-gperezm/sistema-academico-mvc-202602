package com.uniajc.servicios;

import com.uniajc.modelo.Docente;
import com.uniajc.modelo.DocenteDAO;
import java.util.List;

/** Aplica validaciones básicas a los datos de docentes. */
public class DocenteService {
    private final DocenteDAO dao;

    public DocenteService() { this(new DocenteDAO()); }
    public DocenteService(DocenteDAO dao) { this.dao = dao; }

    public List<Docente> listar() { return dao.listarTodos(); }
    public boolean crear(Docente docente) { validar(docente); return dao.insertar(docente); }
    public boolean actualizar(Docente docente) {
        validar(docente);
        if (docente.getIdDocente() <= 0) throw new IllegalArgumentException("El ID del docente no es válido.");
        return dao.actualizar(docente);
    }
    public boolean eliminar(long id) {
        if (id <= 0) throw new IllegalArgumentException("El ID del docente no es válido.");
        return dao.eliminar(id);
    }

    private void validar(Docente docente) {
        if (docente == null) throw new IllegalArgumentException("El docente es obligatorio.");
        exigirTexto(docente.getNombre(), "nombre");
        exigirTexto(docente.getEspecialidad(), "especialidad");
        exigirTexto(docente.getArea(), "área");
    }

    private void exigirTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
    }
}
