package com.uniajc.modelo;

public class Curso {
    private long id;
    private String facultad;

    public Curso() {}

    public Curso(String facultad) {
        this.facultad = facultad;
    }

    public Curso(long id, String facultad) {
        this.id = id;
        this.facultad = facultad;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getFacultad() { return facultad; }
    public void setFacultad(String facultad) { this.facultad = facultad; }
}
