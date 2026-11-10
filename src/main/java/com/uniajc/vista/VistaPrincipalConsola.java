package com.uniajc.vista;

import java.util.Scanner;

public class VistaPrincipalConsola {

    private final Scanner scanner;
    private final VistaEstudiante vistaEstudiante;
    private final VistaDocente vistaDocente;
    private final VistaMateria vistaMateria;
    private final VistaGrupo vistaGrupo;
    private final VistaInscripcion vistaInscripcion;

    public VistaPrincipalConsola() {
        this.scanner = new Scanner(System.in);
        this.vistaEstudiante = new VistaEstudiante(new com.uniajc.controlador.ControladorEstudiante(), scanner);
        this.vistaDocente = new VistaDocente(new com.uniajc.controlador.ControladorDocente(), scanner);
        this.vistaMateria = new VistaMateria(new com.uniajc.controlador.ControladorMateria(), scanner);
        this.vistaGrupo = new VistaGrupo(new com.uniajc.controlador.ControladorGrupo(), scanner);
        this.vistaInscripcion = new VistaInscripcion(new com.uniajc.controlador.ControladorInscripcion(), scanner);
    }

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n=======================================================");
            System.out.println("  SISTEMA DE GESTIÓN ACADÉMICA - UNIAJC (MVC CONSOLA)  ");
            System.out.println("=======================================================");
            System.out.println("1. Módulo de Estudiantes");
            System.out.println("2. Módulo de Docentes");
            System.out.println("3. Módulo de Materias");
            System.out.println("4. Módulo de Grupos");
            System.out.println("5. Módulo de Inscripciones y Calificaciones");
            System.out.println("0. Salir del Sistema");
            System.out.println("=======================================================");
            System.out.print("Seleccione una opción: ");

            if (!scanner.hasNextLine()) break;
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    vistaEstudiante.iniciar();
                    break;
                case "2":
                    vistaDocente.iniciar();
                    break;
                case "3":
                    vistaMateria.iniciar();
                    break;
                case "4":
                    vistaGrupo.iniciar();
                    break;
                case "5":
                    vistaInscripcion.iniciar();
                    break;
                case "0":
                    salir = true;
                    System.out.println("\n¡Gracias por usar el Sistema Académico UNIAJC! Hasta pronto.");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }
}
