package com.uniajc.dao;

import java.util.List;
import java.util.Optional;

import com.uniajc.modelo.Grupo;

/** Contrato de persistencia de grupos. Implementado por {@link GrupoDao}. */
public interface GrupoRepositorio {

    void guardar(Grupo g);

    List<Grupo> obtenerTodos();

    Optional<Grupo> buscarPorId(int id);

    void actualizar(Grupo g);

    void eliminar(int id);
}
