package com.uniajc.servicios;

import java.util.List;
import java.util.Optional;

import com.uniajc.dao.DocenteDao;
import com.uniajc.dao.GrupoDao;
import com.uniajc.dao.MateriaDao;
import com.uniajc.modelo.Grupo;

public class GrupoService {

    private final GrupoDao grupoDao;
    private final MateriaDao materiaDao;
    private final DocenteDao docenteDao;

    public GrupoService() {
        this.grupoDao = new GrupoDao();
        this.materiaDao = new MateriaDao();
        this.docenteDao = new DocenteDao();
    }

    public GrupoService(GrupoDao grupoDao, MateriaDao materiaDao, DocenteDao docenteDao) {
        this.grupoDao = grupoDao;
        this.materiaDao = materiaDao;
        this.docenteDao = docenteDao;
    }

    public void registrarGrupo(Grupo grupo) {
        validarGrupo(grupo);
        grupoDao.guardar(grupo);
    }

    public List<Grupo> obtenerTodosLosGrupos() {
        return grupoDao.obtenerTodos();
    }

    public Optional<Grupo> buscarGrupoPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a cero.");
        }
        return grupoDao.buscarPorId(id);
    }

    public List<Grupo> buscarGruposPorMateria(int idMateria) {
        if (idMateria <= 0) {
            throw new IllegalArgumentException("ID de materia inválido.");
        }
        return grupoDao.buscarPorMateria(idMateria);
    }

    public List<Grupo> buscarGruposPorDocente(int idDocente) {
        if (idDocente <= 0) {
            throw new IllegalArgumentException("ID de docente inválido.");
        }
        return grupoDao.buscarPorDocente(idDocente);
    }

    public void actualizarGrupo(Grupo grupo) {
        if (grupo == null || grupo.getId() <= 0) {
            throw new IllegalArgumentException("Grupo inválido para actualizar.");
        }

        Optional<Grupo> actualOpt = grupoDao.buscarPorId(grupo.getId());
        if (actualOpt.isEmpty()) {
            throw new IllegalArgumentException("Grupo no encontrado con ID: " + grupo.getId());
        }

        validarGrupo(grupo);
        grupoDao.actualizar(grupo);
    }

    public void eliminarGrupo(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }

        Optional<Grupo> existente = grupoDao.buscarPorId(id);
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Grupo no encontrado con ID: " + id);
        }

        grupoDao.eliminar(id);
    }

    private void validarGrupo(Grupo grupo) {
        if (grupo == null) {
            throw new IllegalArgumentException("Los datos del grupo no pueden ser nulos.");
        }

        if (grupo.getIdMateria() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar una materia válida.");
        }
        if (materiaDao.buscarPorId(grupo.getIdMateria()).isEmpty()) {
            throw new IllegalArgumentException("La materia con ID " + grupo.getIdMateria() + " no existe.");
        }

        if (grupo.getIdDocente() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un docente válido.");
        }
        if (docenteDao.buscarPorId(grupo.getIdDocente()).isEmpty()) {
            throw new IllegalArgumentException("El docente con ID " + grupo.getIdDocente() + " no existe.");
        }

        if (grupo.getAula() == null || grupo.getAula().trim().isEmpty()) {
            throw new IllegalArgumentException("El aula es obligatoria.");
        }

        if (grupo.getHorario() == null || grupo.getHorario().trim().isEmpty()) {
            throw new IllegalArgumentException("El horario es obligatorio.");
        }
    }
}
