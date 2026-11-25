package com.uniajc.modelo;

public class Estudiante {

    private int id;
    private String name;
    private String lastname;
    private String email;

    // Nuevos campos
    private String tipoDocumento;
    private String documento;
    private String telefono;
    private int semestreActual;
    private String estado;

    public Estudiante() {
    }

    public Estudiante(int id, String name, String lastname, String email,
                      String tipoDocumento, String documento,
                      String telefono, int semestreActual, String estado) {

        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
        this.telefono = telefono;
        this.semestreActual = semestreActual;
        this.estado = estado;
    }

    public Estudiante(String name, String lastname, String email,
                      String tipoDocumento, String documento,
                      String telefono, int semestreActual, String estado) {

        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
        this.telefono = telefono;
        this.semestreActual = semestreActual;
        this.estado = estado;
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getSemestreActual() {
        return semestreActual;
    }

    public void setSemestreActual(int semestreActual) {
        this.semestreActual = semestreActual;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                ", documento='" + documento + '\'' +
                ", telefono='" + telefono + '\'' +
                ", semestreActual=" + semestreActual +
                ", estado='" + estado + '\'' +
                '}';
    }
}