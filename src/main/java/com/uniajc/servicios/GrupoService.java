package com.uniajc.servicios;

import java.util.List;
import java.util.Optional;

import com.uniajc.dao.GrupoDao;
import com.uniajc.modelo.Grupo;

/** Capa de negocio de grupos: validaciones de integridad. */
public class GrupoService {

    private final GrupoDao grupoDao;

    public GrupoService(GrupoDao grupoDao) {
        this.grupoDao = grupoDao;
    }

    public void registrarGrupo(Grupo g) {
        validarGrupo(g);
        grupoDao.guardar(g);
    }

    public List<Grupo> obtenerTodosLosGrupos() {
        return grupoDao.obtenerTodos();
    }

    public Optional<Grupo> buscarGrupoPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un numero positivo.");
        }
        return grupoDao.buscarPorId(id);
    }

    public void actualizarGrupo(Grupo g) {
        if (g == null || g.getId_grupo() <= 0) {
            throw new IllegalArgumentException("Grupo invalido.");
        }
        if (grupoDao.buscarPorId(g.getId_grupo()).isEmpty()) {
            throw new IllegalArgumentException("Grupo no encontrado.");
        }
        validarGrupo(g);
        grupoDao.actualizar(g);
    }

    public void eliminarGrupo(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID invalido.");
        }
        if (grupoDao.buscarPorId(id).isEmpty()) {
            throw new IllegalArgumentException("Grupo no encontrado.");
        }
        grupoDao.eliminar(id);
    }

    private void validarGrupo(Grupo g) {
        if (g == null) {
            throw new IllegalArgumentException("Grupo no puede ser nulo.");
        }
        if (g.getId_materia() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar una materia.");
        }
        if (g.getId_docente() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un docente.");
        }
        if (g.getAula() == null || g.getAula().trim().isEmpty()) {
            throw new IllegalArgumentException("El aula es obligatoria.");
        }
        if (g.getHorario() == null || g.getHorario().trim().isEmpty()) {
            throw new IllegalArgumentException("El horario es obligatorio.");
        }
    }
}
