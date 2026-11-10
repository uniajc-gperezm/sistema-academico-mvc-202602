package com.uniajc.controlador;

import java.util.List;
import java.util.Optional;

import com.uniajc.modelo.Grupo;
import com.uniajc.servicios.GrupoService;

public class ControladorGrupo {

    private final GrupoService grupoService;

    public ControladorGrupo() {
        this.grupoService = new GrupoService();
    }

    public ControladorGrupo(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    public void registrarGrupo(int idMateria, int idDocente, String aula, String horario) {
        Grupo grupo = new Grupo(idMateria, idDocente, aula, horario);
        grupoService.registrarGrupo(grupo);
    }

    public void registrarGrupo(Grupo grupo) {
        grupoService.registrarGrupo(grupo);
    }

    public List<Grupo> listarGrupos() {
        return grupoService.obtenerTodosLosGrupos();
    }

    public Optional<Grupo> buscarPorId(int id) {
        return grupoService.buscarGrupoPorId(id);
    }

    public List<Grupo> buscarPorMateria(int idMateria) {
        return grupoService.buscarGruposPorMateria(idMateria);
    }

    public List<Grupo> buscarPorDocente(int idDocente) {
        return grupoService.buscarGruposPorDocente(idDocente);
    }

    public void actualizarGrupo(int id, int idMateria, int idDocente, String aula, String horario) {
        Grupo grupo = new Grupo(id, idMateria, idDocente, aula, horario);
        grupoService.actualizarGrupo(grupo);
    }

    public void actualizarGrupo(Grupo grupo) {
        grupoService.actualizarGrupo(grupo);
    }

    public void eliminarGrupo(int id) {
        grupoService.eliminarGrupo(id);
    }

    public GrupoService getService() {
        return grupoService;
    }
}
