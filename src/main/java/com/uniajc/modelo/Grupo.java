package com.uniajc.modelo;

public class Grupo {
    private int id;
    private int idMateria;
    private int idDocente;
    private String classroom;
    private String schedule;
    private int cupoMaximo;
    private String periodo;

    public Grupo() {}

    public Grupo(int id, int idMateria, int idDocente, String classroom, String schedule) {
        this.id = id;
        this.idMateria = idMateria;
        this.idDocente = idDocente;
        this.classroom = classroom;
        this.schedule = schedule;
    }

    public Grupo(int id, int idMateria, int idDocente, String classroom, String schedule, int cupoMaximo, String periodo) {
        this.id = id;
        this.idMateria = idMateria;
        this.idDocente = idDocente;
        this.classroom = classroom;
        this.schedule = schedule;
        this.cupoMaximo = cupoMaximo;
        this.periodo = periodo;
    }

    public Grupo(int idMateria, int idDocente, String classroom, String schedule) {
        this.idMateria = idMateria;
        this.idDocente = idDocente;
        this.classroom = classroom;
        this.schedule = schedule;
    }

    public Grupo(int idMateria, int idDocente, String classroom, String schedule, int cupoMaximo, String periodo) {
        this.idMateria = idMateria;
        this.idDocente = idDocente;
        this.classroom = classroom;
        this.schedule = schedule;
        this.cupoMaximo = cupoMaximo;
        this.periodo = periodo;
    }

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

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    @Override
    public String toString() {
        return "Grupo{" +
                "id=" + id +
                ", idMateria=" + idMateria +
                ", idDocente=" + idDocente +
                ", classroom='" + classroom + '\'' +
                ", schedule='" + schedule + '\'' +
                ", cupoMaximo=" + cupoMaximo +
                ", periodo='" + periodo + '\'' +
                '}';
    }
}

