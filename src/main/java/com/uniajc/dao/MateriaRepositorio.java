package com.uniajc.dao;

import java.util.List;
import java.util.Optional;

import com.uniajc.modelo.Materia;

/** Contrato de persistencia de materias. Implementado por {@link MateriaDao}. */
public interface MateriaRepositorio {

    void guardar(Materia m);

    List<Materia> obtenerTodos();

    Optional<Materia> buscarPorId(int id);

    void actualizar(Materia m);

    void eliminar(int id);
}
