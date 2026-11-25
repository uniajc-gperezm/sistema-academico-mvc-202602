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

    public void registrarGrupo(int idMateria, int idDocente, String classroom, String schedule) {
        Grupo grupo = new Grupo(idMateria, idDocente, classroom, schedule);
        grupo.setCupoMaximo(30);
        grupo.setPeriodo("2026-1");
        grupoService.registrarGrupo(grupo);
    }

    public void registrarGrupo(int idMateria, int idDocente, String classroom, String schedule, int cupoMaximo, String periodo) {
        Grupo grupo = new Grupo(idMateria, idDocente, classroom, schedule, cupoMaximo, periodo);
        grupoService.registrarGrupo(grupo);
    }

    public List<Grupo> listarGrupos() {
        return grupoService.obtenerTodosLosGrupos();
    }

    public Optional<Grupo> buscarGrupoPorId(int id) {
        return grupoService.buscarGrupoPorId(id);
    }

    public List<Grupo> buscarGruposPorAula(String aula) {
        return grupoService.buscarGruposPorAula(aula);
    }

    public List<Grupo> buscarGruposPorPeriodo(String periodo) {
        return grupoService.buscarGruposPorPeriodo(periodo);
    }

    public void actualizarGrupo(int id, int idMateria, int idDocente, String classroom, String schedule) {
        Grupo grupo = new Grupo(id, idMateria, idDocente, classroom, schedule);
        grupo.setCupoMaximo(30);
        grupo.setPeriodo("2026-1");
        grupoService.actualizarGrupo(grupo);
    }

    public void actualizarGrupo(int id, int idMateria, int idDocente, String classroom, String schedule, int cupoMaximo, String periodo) {
        Grupo grupo = new Grupo(id, idMateria, idDocente, classroom, schedule, cupoMaximo, periodo);
        grupoService.actualizarGrupo(grupo);
    }

    public void eliminarGrupo(int id) {
        grupoService.eliminarGrupo(id);
    }
}

