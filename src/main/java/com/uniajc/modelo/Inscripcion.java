package com.uniajc.modelo;

public class Inscripcion {
    private int idInscripcion;
    private int idEstudiante;
    private int idGrupo;
    private double notaFinal;
    private String estado;

    public Inscripcion() {}

    public Inscripcion(int idEstudiante, int idGrupo, double notaFinal, String estado) {
        this.idEstudiante = idEstudiante;
        this.idGrupo = idGrupo;
        this.notaFinal = notaFinal;
        this.estado = estado;
    }

    public Inscripcion(int idInscripcion, int idEstudiante, int idGrupo, double notaFinal, String estado) {
        this.idInscripcion = idInscripcion;
        this.idEstudiante = idEstudiante;
        this.idGrupo = idGrupo;
        this.notaFinal = notaFinal;
        this.estado = estado;
    }

    public int getIdInscripcion() { return idInscripcion; }
    public void setIdInscripcion(int idInscripcion) { this.idInscripcion = idInscripcion; }

    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }

    public int getIdGrupo() { return idGrupo; }
    public void setIdGrupo(int idGrupo) { this.idGrupo = idGrupo; }

    public double getNotaFinal() { return notaFinal; }
    public void setNotaFinal(double notaFinal) { this.notaFinal = notaFinal; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
