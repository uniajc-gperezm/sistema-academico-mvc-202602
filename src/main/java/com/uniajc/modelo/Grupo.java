package com.uniajc.modelo;

import java.util.Objects;

/** Entidad Grupo. Tabla: grupos (id, id_materia, id_docente, classroom, schedule). */
public class Grupo {

    private int id_grupo;
    private int id_materia;
    private int id_docente;
    private String aula;
    private String horario;

    public Grupo() {}

    public Grupo(int id_grupo, int id_materia, int id_docente, String aula, String horario) {
        this.id_grupo = id_grupo;
        this.id_materia = id_materia;
        this.id_docente = id_docente;
        this.aula = aula;
        this.horario = horario;
    }

    public int getId_grupo() { return id_grupo; }
    public void setId_grupo(int id_grupo) { this.id_grupo = id_grupo; }

    public int getId_materia() { return id_materia; }
    public void setId_materia(int id_materia) { this.id_materia = id_materia; }

    public int getId_docente() { return id_docente; }
    public void setId_docente(int id_docente) { this.id_docente = id_docente; }

    public String getAula() { return aula; }
    public void setAula(String aula) { this.aula = aula; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    @Override
    public String toString() { return "Grupo " + id_grupo + " - " + aula + " (" + horario + ")"; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Grupo)) return false;
        return id_grupo == ((Grupo) o).id_grupo;
    }

    @Override
    public int hashCode() { return Objects.hash(id_grupo); }
}
