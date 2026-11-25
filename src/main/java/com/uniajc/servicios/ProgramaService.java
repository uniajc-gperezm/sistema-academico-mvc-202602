package com.uniajc.servicios;

import java.util.List;
import java.util.Optional;

import com.uniajc.dao.ProgramaDao;
import com.uniajc.modelo.Programa;

public class ProgramaService {

    private final ProgramaDao programaDao;

    public ProgramaService() {
        this.programaDao = new ProgramaDao();
    }

    public ProgramaService(ProgramaDao programaDao) {
        this.programaDao = new ProgramaDao();
    }

    /**
     * Registra un nuevo programa académico validando reglas de negocio.
     * 
     * @param programa El objeto Programa a registrar
     * @throws IllegalArgumentException Si los campos obligatorios están vacíos, la duración es inválida o el código ya existe
     */
    public void registrarPrograma(Programa programa) {
        if (programa == null) {
            throw new IllegalArgumentException("El programa no puede ser nulo.");
        }
        if (programa.getCodigo() == null || programa.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El código del programa es obligatorio.");
        }
        if (programa.getNombre() == null || programa.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del programa es obligatorio.");
        }
        if (programa.getFacultad() == null || programa.getFacultad().trim().isEmpty()) {
            throw new IllegalArgumentException("La facultad es obligatoria.");
        }
        if (programa.getDuracionSemestres() < 1 || programa.getDuracionSemestres() > 12) {
            throw new IllegalArgumentException("La duración en semestres debe estar entre 1 y 12.");
        }

        Optional<Programa> existente = programaDao.buscarPorCodigo(programa.getCodigo().trim());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un programa registrado con el código: " + programa.getCodigo());
        }

        programaDao.guardar(programa);
    }

    /**
     * Actualiza un programa existente.
     * 
     * @param programa El objeto Programa con los datos actualizados
     * @throws IllegalArgumentException Si el programa no existe o los datos son inválidos
     */
    public void actualizarPrograma(Programa programa) {
        if (programa == null || programa.getId() <= 0) {
            throw new IllegalArgumentException("ID de programa inválido para actualizar.");
        }
        if (programa.getCodigo() == null || programa.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El código del programa es obligatorio.");
        }
        if (programa.getNombre() == null || programa.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del programa es obligatorio.");
        }
        if (programa.getFacultad() == null || programa.getFacultad().trim().isEmpty()) {
            throw new IllegalArgumentException("La facultad es obligatoria.");
        }
        if (programa.getDuracionSemestres() < 1 || programa.getDuracionSemestres() > 12) {
            throw new IllegalArgumentException("La duración en semestres debe estar entre 1 y 12.");
        }

        Optional<Programa> existente = programaDao.buscarPorCodigo(programa.getCodigo().trim());
        if (existente.isPresent() && existente.get().getId() != programa.getId()) {
            throw new IllegalArgumentException("El código " + programa.getCodigo() + " ya está asignado a otro programa.");
        }

        programaDao.actualizar(programa);
    }

    public void eliminarPrograma(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de programa inválido.");
        }
        programaDao.eliminar(id);
    }

    public Optional<Programa> buscarPorId(int id) {
        if (id <= 0) {
            return Optional.empty();
        }
        return programaDao.buscarPorId(id);
    }

    public Optional<Programa> buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return Optional.empty();
        }
        return programaDao.buscarPorCodigo(codigo.trim());
    }

    public List<Programa> obtenerTodos() {
        return programaDao.obtenerTodos();
    }
}