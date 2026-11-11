package com.uniajc.dao;

import java.util.List;
import java.util.Optional;

import com.uniajc.modelo.Estudiante;

/**
 * Contrato de persistencia de estudiantes.
 *
 * Define las operaciones que la capa de servicio necesita, sin acoplarla a JDBC.
 * Esto permite sustituir la implementacion real ({@link EstudianteDao}) por un
 * doble en memoria durante las pruebas unitarias.
 */
public interface EstudianteRepositorio {

    void guardar(Estudiante e);

    List<Estudiante> obtenerTodos();

    Optional<Estudiante> buscarPorId(int id);

    void actualizar(Estudiante e);

    void eliminar(int id);
}
