package com.uniajc.modelo;

public class Grupo {
    private long idGrupo;
    private long idMateria;
    private long idDocente;
    private String aula;
    private String horario;

    public Grupo() {}

    public Grupo(long idMateria, long idDocente, String aula, String horario) {
        this.idMateria = idMateria;
        this.idDocente = idDocente;
        this.aula = aula;
        this.horario = horario;
    }

    public Grupo(long idGrupo, long idMateria, long idDocente, String aula, String horario) {
        this.idGrupo = idGrupo;
        this.idMateria = idMateria;
        this.idDocente = idDocente;
        this.aula = aula;
        this.horario = horario;
    }

    public long getIdGrupo() { return idGrupo; }
    public void setIdGrupo(long idGrupo) { this.idGrupo = idGrupo; }

    public long getIdMateria() { return idMateria; }
    public void setIdMateria(long idMateria) { this.idMateria = idMateria; }

    public long getIdDocente() { return idDocente; }
    public void setIdDocente(long idDocente) { this.idDocente = idDocente; }

    public String getAula() { return aula; }
    public void setAula(String aula) { this.aula = aula; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }
}
