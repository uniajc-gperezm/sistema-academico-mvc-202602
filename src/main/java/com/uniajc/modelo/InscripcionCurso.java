package com.uniajc.modelo;

public class InscripcionCurso {
    private int idInscripcion;
    private int idEstudiante;
    private int idGrupo;
    private float notaFinal;
    private String estado;

    public InscripcionCurso() {
    }

    public InscripcionCurso(int idInscripcion, int idEstudiante, int idGrupo, float notaFinal, String estado) {
        this.idInscripcion = idInscripcion;
        this.idEstudiante = idEstudiante;
        this.idGrupo = idGrupo;
        this.notaFinal = notaFinal;
        this.estado = estado;
    }

    public int getIdInscripcion() {
        return idInscripcion;public class InscripcionCurso {
    
}

    }

    public void setIdInscripcion(int idInscripcion) {
        this.idInscripcion = idInscripcion;
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

    public float getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(float notaFinal) {
        this.notaFinal = notaFinal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}