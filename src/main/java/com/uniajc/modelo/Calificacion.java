package com.uniajc.modelo;

import java.math.BigDecimal;

public class Calificacion {
    private int id;
    private int inscripcionId;
    private String corte;
    private BigDecimal porcentaje;
    private BigDecimal nota;
    private String observacion;

    public Calificacion() {}

    public Calificacion(int id, int inscripcionId, String corte, BigDecimal porcentaje, BigDecimal nota, String observacion) {
        this.id = id;
        this.inscripcionId = inscripcionId;
        this.corte = corte;
        this.porcentaje = porcentaje;
        this.nota = nota;
        this.observacion = observacion;
    }

    public Calificacion(int inscripcionId, String corte, BigDecimal porcentaje, BigDecimal nota, String observacion) {
        this.inscripcionId = inscripcionId;
        this.corte = corte;
        this.porcentaje = porcentaje;
        this.nota = nota;
        this.observacion = observacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInscripcionId() {
        return inscripcionId;
    }

    public void setInscripcionId(int inscripcionId) {
        this.inscripcionId = inscripcionId;
    }

    public String getCorte() {
        return corte;
    }

    public void setCorte(String corte) {
        this.corte = corte;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public BigDecimal getNota() {
        return nota;
    }

    public void setNota(BigDecimal nota) {
        this.nota = nota;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @Override
    public String toString() {
        return "Calificacion{" +
                "id=" + id +
                ", inscripcionId=" + inscripcionId +
                ", corte='" + corte + '\'' +
                ", porcentaje=" + porcentaje +
                ", nota=" + nota +
                ", observacion='" + observacion + '\'' +
                '}';
    }
}

