package com.uniajc.modelo;

/** Entidad sencilla que transporta los datos de un estudiante entre capas. */
public class Estudiante {
    private long id;
    private String name;
    private String lastname;
    private String email;
    private Long nit;
    private Long numcelular;

    public Estudiante() {}

    public Estudiante(String name, String lastname, String email, long nit, long numcelular) {
        this(name, lastname, email, Long.valueOf(nit), Long.valueOf(numcelular));
    }

    /** Crea un estudiante incluyendo NIT y celular, que pueden quedar vacíos. */
    public Estudiante(String name, String lastname, String email, Long nit, Long numcelular) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.nit = nit;
        this.numcelular = numcelular;
    }

    /** Crea un estudiante con los campos que utiliza el CRUD de esta aplicación. */
    public Estudiante(String name, String lastname, String email) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
    }

    public Estudiante(long id, String name, String lastname, String email, long nit, long numcelular) {
        this(id, name, lastname, email, Long.valueOf(nit), Long.valueOf(numcelular));
    }

    /** Reconstruye un estudiante de la base, conservando NIT/celular nulos. */
    public Estudiante(long id, String name, String lastname, String email, Long nit, Long numcelular) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.nit = nit;
        this.numcelular = numcelular;
    }

    public Estudiante(long id, String name, String lastname, String email) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getNit() { return nit; }
    public void setNit(long nit) { this.nit = nit; }
    public void setNit(Long nit) { this.nit = nit; }

    public Long getNumcelular() { return numcelular; }
    public void setNumcelular(long numcelular) { this.numcelular = numcelular; }
    public void setNumcelular(Long numcelular) { this.numcelular = numcelular; }
}
