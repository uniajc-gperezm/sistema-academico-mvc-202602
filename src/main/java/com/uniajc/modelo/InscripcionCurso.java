package com.uniajc.modelo;

import java.util.Objects;

/** Entidad Inscripcion_Curso. Tabla: inscripciones (id, id_estudiante, id_grupo, final_grade, status). */
public class InscripcionCurso {

    private int id_inscripcion;
    private int id_estudiante;
    private int id_grupo;
    private float nota_final;
    private String estado;

    public InscripcionCurso() {
        this.estado = "ACTIVO";
        this.nota_final = 0f;
    }

    public InscripcionCurso(int id_inscripcion, int id_estudiante, int id_grupo,
                            float nota_final, String estado) {
        this.id_inscripcion = id_inscripcion;
        this.id_estudiante = id_estudiante;
        this.id_grupo = id_grupo;
        this.nota_final = nota_final;
        this.estado = estado;
    }

    public int getId_inscripcion() { return id_inscripcion; }
    public void setId_inscripcion(int id_inscripcion) { this.id_inscripcion = id_inscripcion; }

    public int getId_estudiante() { return id_estudiante; }
    public void setId_estudiante(int id_estudiante) { this.id_estudiante = id_estudiante; }

    public int getId_grupo() { return id_grupo; }
    public void setId_grupo(int id_grupo) { this.id_grupo = id_grupo; }

    public float getNota_final() { return nota_final; }
    public void setNota_final(float nota_final) { this.nota_final = nota_final; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Inscr #" + id_inscripcion + " est=" + id_estudiante + " grp=" + id_grupo
                + " nota=" + nota_final + " " + estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InscripcionCurso)) return false;
        return id_inscripcion == ((InscripcionCurso) o).id_inscripcion;
    }

    @Override
    public int hashCode() { return Objects.hash(id_inscripcion); }
}
