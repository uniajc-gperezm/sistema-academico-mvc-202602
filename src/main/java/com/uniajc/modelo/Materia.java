package com.uniajc.modelo;

import java.util.ArrayList;
import java.util.List;

public class Materia {
    private int idMateria;
    private String nombreMateria;
    private int creditos;

    public Materia() {
    }
    private List<Grupo> grupos = new ArrayList<>();

    public List<Grupo> getGrupos() {
    return grupos;
    }

    public void setGrupos(List<Grupo> grupos) {
    this.grupos = grupos;
    }
    public Materia(int idMateria, String nombreMateria, int creditos) {
        this.idMateria = idMateria;
        this.nombreMateria = nombreMateria;
        this.creditos = creditos;
    }

    public int getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(int idMateria) {
        this.idMateria = idMateria;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }
}