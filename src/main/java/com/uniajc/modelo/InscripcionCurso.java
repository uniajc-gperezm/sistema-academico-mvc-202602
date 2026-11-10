package com.uniajc.modelo;

public class InscripcionCurso extends Inscripcion {

    public InscripcionCurso() {
        super();
    }

    public InscripcionCurso(int idEstudiante, int idGrupo) {
        super(idEstudiante, idGrupo);
    }

    public InscripcionCurso(int idEstudiante, int idGrupo, Double notaFinal, String estado) {
        super(idEstudiante, idGrupo, notaFinal, estado);
    }

    public InscripcionCurso(int id, int idEstudiante, int idGrupo, Double notaFinal, String estado) {
        super(id, idEstudiante, idGrupo, notaFinal, estado);
    }
}
