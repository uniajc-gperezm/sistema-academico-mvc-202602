package com.uniajc.dao;

import java.util.List;
import java.util.Optional;

import com.uniajc.modelo.Docente;

/** Contrato de persistencia de docentes. Implementado por {@link DocenteDao}. */
public interface DocenteRepositorio {

    void guardar(Docente d);

    List<Docente> obtenerTodos();

    Optional<Docente> buscarPorId(int id);

    void actualizar(Docente d);

    void eliminar(int id);
}
