package com.uniajc.modelo;

import java.time.LocalDate;

public class PeriodoAcademico {
    private int id;
    private String codigo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean activo;

    public PeriodoAcademico() {}

    public PeriodoAcademico(int id, String codigo, LocalDate fechaInicio, LocalDate fechaFin, boolean activo) {
        this.id = id;
        this.codigo = codigo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.activo = activo;
    }

    public PeriodoAcademico(String codigo, LocalDate fechaInicio, LocalDate fechaFin, boolean activo) {
        this.codigo = codigo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isActivo() {
        return activo;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "PeriodoAcademico{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", activo=" + activo +
                '}';
    }
}
