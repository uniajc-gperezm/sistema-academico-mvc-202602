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
            throw new IllegalArgumentException("El ID debe ser mayor a 0.");
        }
        return grupoDao.buscarPorId(id);
    }

    public List<Grupo> buscarGruposPorAula(String aula) {
        if (aula == null || aula.trim().isEmpty()) {
            return obtenerTodosLosGrupos();
        }
        return grupoDao.buscarPorAula(aula.trim());
    }

    public List<Grupo> buscarGruposPorPeriodo(String periodo) {
        if (periodo == null || periodo.trim().isEmpty()) {
            return obtenerTodosLosGrupos();
        }
        return grupoDao.buscarPorPeriodo(periodo.trim());
    }

    public void actualizarGrupo(Grupo grupo) {
        if (grupo == null || grupo.getId() <= 0) {
            throw new IllegalArgumentException("Grupo inválido.");
        }

        Optional<Grupo> existente = grupoDao.buscarPorId(grupo.getId());
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Grupo no encontrado con ID: " + grupo.getId());
        }

        validarGrupo(grupo);
        grupoDao.actualizar(grupo);
    }

    public void eliminarGrupo(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0.");
        }

        Optional<Grupo> existente = grupoDao.buscarPorId(id);
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Grupo no encontrado con ID: " + id);
        }

        grupoDao.eliminar(id);
    }

    private void validarGrupo(Grupo grupo) {
        if (grupo == null) {
            throw new IllegalArgumentException("El grupo no puede ser nulo.");
        }

        if (materiaDao.buscarPorId(grupo.getIdMateria()).isEmpty()) {
            throw new IllegalArgumentException("La materia con ID " + grupo.getIdMateria() + " no existe.");
        }

        if (docenteDao.buscarPorId(grupo.getIdDocente()).isEmpty()) {
            throw new IllegalArgumentException("El docente con ID " + grupo.getIdDocente() + " no existe.");
        }

        if (grupo.getClassroom() == null || grupo.getClassroom().trim().isEmpty()) {
            throw new IllegalArgumentException("El aula es obligatoria.");
        }

        if (grupo.getSchedule() == null || grupo.getSchedule().trim().isEmpty()) {
            throw new IllegalArgumentException("El horario es obligatorio.");
        }

        if (grupo.getCupoMaximo() <= 0) {
            throw new IllegalArgumentException("El cupo máximo debe ser mayor a 0.");
        }

        if (grupo.getPeriodo() == null || grupo.getPeriodo().trim().isEmpty()) {
            throw new IllegalArgumentException("El período académico es obligatorio.");
        }
    }
}

