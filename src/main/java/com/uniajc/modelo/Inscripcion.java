package com.uniajc.modelo;

import java.util.Objects;

public class Inscripcion {
    private int id;
    private int idEstudiante;
    private int idGrupo;
    private Double notaFinal;
    private String estado; // 'ACTIVO', 'APROBADO', 'REPROBADO', 'CANCELADO'

    // Optional descriptive fields for UI display
    private String estudianteNombre;
    private String grupoDetalle;

    public Inscripcion() {
        this.estado = "ACTIVO";
    }

    public Inscripcion(int idEstudiante, int idGrupo) {
        this.idEstudiante = idEstudiante;
        this.idGrupo = idGrupo;
        this.notaFinal = null;
        this.estado = "ACTIVO";
    }

    public Inscripcion(int idEstudiante, int idGrupo, Double notaFinal, String estado) {
        this.idEstudiante = idEstudiante;
        this.idGrupo = idGrupo;
        this.notaFinal = notaFinal;
        this.estado = (estado != null) ? estado : "ACTIVO";
    }

    public Inscripcion(int id, int idEstudiante, int idGrupo, Double notaFinal, String estado) {
        this.id = id;
        this.idEstudiante = idEstudiante;
        this.idGrupo = idGrupo;
        this.notaFinal = notaFinal;
        this.estado = (estado != null) ? estado : "ACTIVO";
    }

    // Getters and Setters
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

    public Double getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(Double notaFinal) {
        this.notaFinal = notaFinal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstudianteNombre() {
        return estudianteNombre;
    }

    public void setEstudianteNombre(String estudianteNombre) {
        this.estudianteNombre = estudianteNombre;
    }

    public String getGrupoDetalle() {
        return grupoDetalle;
    }

    public void setGrupoDetalle(String grupoDetalle) {
        this.grupoDetalle = grupoDetalle;
    }

    // Compatibility aliases
    public int getIdInscripcion() {
        return id;
    }

    public void setIdInscripcion(int idInscripcion) {
        this.id = idInscripcion;
    }

    public Double getFinalGrade() {
        return notaFinal;
    }

    public void setFinalGrade(Double finalGrade) {
        this.notaFinal = finalGrade;
    }

    public String getStatus() {
        return estado;
    }

    public void setStatus(String status) {
        this.estado = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inscripcion that = (Inscripcion) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Inscripcion{" +
                "id=" + id +
                ", idEstudiante=" + idEstudiante +
                ", idGrupo=" + idGrupo +
                ", notaFinal=" + notaFinal +
                ", estado='" + estado + '\'' +
                '}';
    }
}
