package com.uniajc.modelo;

import java.math.BigDecimal;

public class InscripcionCurso {
    private int id;
    private int idEstudiante;
    private int idGrupo;
    private BigDecimal finalGrade;
    private String status;

    public InscripcionCurso() {}

    public InscripcionCurso(int id, int idEstudiante, int idGrupo, BigDecimal finalGrade, String status) {
        this.id = id;
        this.idEstudiante = idEstudiante;
        this.idGrupo = idGrupo;
        this.finalGrade = finalGrade;
        this.status = status;
    }

    public InscripcionCurso(int idEstudiante, int idGrupo, BigDecimal finalGrade, String status) {
        this.idEstudiante = idEstudiante;
        this.idGrupo = idGrupo;
        this.finalGrade = finalGrade;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public BigDecimal getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(BigDecimal finalGrade) {
        this.finalGrade = finalGrade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "InscripcionCurso{" +
                "id=" + id +
                ", idEstudiante=" + idEstudiante +
                ", idGrupo=" + idGrupo +
                ", finalGrade=" + finalGrade +
                ", status='" + status + '\'' +
                '}';
    }
}

