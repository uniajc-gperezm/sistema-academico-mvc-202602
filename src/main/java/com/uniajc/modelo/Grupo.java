package com.uniajc.modelo;

import java.util.Objects;

public class Grupo {
    private int id;
    private int idMateria;
    private int idDocente;
    private String aula;
    private String horario;

    // Optional reference descriptions for presentation in views
    private String materiaNombre;
    private String docenteNombre;

    public Grupo() {
    }

    public Grupo(int idMateria, int idDocente, String aula, String horario) {
        this.idMateria = idMateria;
        this.idDocente = idDocente;
        this.aula = aula;
        this.horario = horario;
    }

    public Grupo(int id, int idMateria, int idDocente, String aula, String horario) {
        this.id = id;
        this.idMateria = idMateria;
        this.idDocente = idDocente;
        this.aula = aula;
        this.horario = horario;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(int idMateria) {
        this.idMateria = idMateria;
    }

    public int getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(int idDocente) {
        this.idDocente = idDocente;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getMateriaNombre() {
        return materiaNombre;
    }

    public void setMateriaNombre(String materiaNombre) {
        this.materiaNombre = materiaNombre;
    }

    public String getDocenteNombre() {
        return docenteNombre;
    }

    public void setDocenteNombre(String docenteNombre) {
        this.docenteNombre = docenteNombre;
    }

    // Compatibility aliases
    public int getIdGrupo() {
        return id;
    }

    public void setIdGrupo(int idGrupo) {
        this.id = idGrupo;
    }

    public String getClassroom() {
        return aula;
    }

    public void setClassroom(String classroom) {
        this.aula = classroom;
    }

    public String getSchedule() {
        return horario;
    }

    public void setSchedule(String schedule) {
        this.horario = schedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grupo grupo = (Grupo) o;
        return id == grupo.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Grupo{" +
                "id=" + id +
                ", idMateria=" + idMateria +
                ", idDocente=" + idDocente +
                ", aula='" + aula + '\'' +
                ", horario='" + horario + '\'' +
                '}';
    }
}
