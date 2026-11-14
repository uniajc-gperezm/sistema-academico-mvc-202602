package com.uniajc.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.uniajc.modelo.Docente;

/** Doble de prueba en memoria de {@link DocenteRepositorio}. */
public class DocenteDaoEnMemoria implements DocenteRepositorio {

    private final Map<Integer, Docente> datos = new LinkedHashMap<>();
    private int siguienteId = 1;

    @Override
    public void guardar(Docente d) {
        if (d.getId_docente() <= 0) {
            d.setId_docente(siguienteId++);
        } else {
            siguienteId = Math.max(siguienteId, d.getId_docente() + 1);
        }
        datos.put(d.getId_docente(), d);
    }

    @Override
    public List<Docente> obtenerTodos() {
        return new ArrayList<>(datos.values());
    }

    @Override
    public Optional<Docente> buscarPorId(int id) {
        return Optional.ofNullable(datos.get(id));
    }

    @Override
    public void actualizar(Docente d) {
        if (!datos.containsKey(d.getId_docente())) {
            throw new RuntimeException("Docente no encontrado con ID: " + d.getId_docente());
        }
        datos.put(d.getId_docente(), d);
    }

    @Override
    public void eliminar(int id) {
        if (datos.remove(id) == null) {
            throw new RuntimeException("Docente no encontrado con ID: " + id);
        }
    }

    public int contar() {
        return datos.size();
    }
}
