package com.uniajc.modelo;

public class Materia {
    private int id;
    private String name;
    private int credits;
    private String codigo;
    private int horasSemanales;

    public Materia() {}

    public Materia(int id, String name, int credits) {
        this.id = id;
        this.name = name;
        this.credits = credits;
    }

    public Materia(int id, String name, int credits, String codigo, int horasSemanales) {
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.codigo = codigo;
        this.horasSemanales = horasSemanales;
    }

    public Materia(String name, int credits) {
        this.name = name;
        this.credits = credits;
    }

    public Materia(String name, int credits, String codigo, int horasSemanales) {
        this.name = name;
        this.credits = credits;
        this.codigo = codigo;
        this.horasSemanales = horasSemanales;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getHorasSemanales() {
        return horasSemanales;
    }

    public void setHorasSemanales(int horasSemanales) {
        this.horasSemanales = horasSemanales;
    }

    @Override
    public String toString() {
        return "Materia{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", credits=" + credits +
                ", codigo='" + codigo + '\'' +
                ", horasSemanales=" + horasSemanales +
                '}';
    }
}

