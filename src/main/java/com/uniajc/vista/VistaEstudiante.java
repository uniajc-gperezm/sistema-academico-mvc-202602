package com.uniajc.vista;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.uniajc.controlador.ControladorEstudiante;
import com.uniajc.modelo.Estudiante;

public class VistaEstudiante {

    private final ControladorEstudiante controlador;
    private final Scanner scanner;

    public VistaEstudiante() {
        this.controlador = new ControladorEstudiante();
        this.scanner = new Scanner(System.in);
    }

    public void iniciarMenu() {

        int opcion = -1;

        do {

            System.out.println("\n===== GESTIÓN DE ESTUDIANTES =====");
            System.out.println("1. Listar Estudiantes");
            System.out.println("2. Registrar Estudiante");
            System.out.println("3. Buscar Estudiante por ID");
            System.out.println("4. Buscar Estudiante por Nombre");
            System.out.println("5. Actualizar Estudiante");
            System.out.println("6. Eliminar Estudiante");
            System.out.println("0. Salir");

            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida.");
                continue;
            }

            switch (opcion) {
                case 1 -> listarEstudiantes();
                case 2 -> registrarEstudiante();
                case 3 -> buscarPorId();
                case 4 -> buscarPorNombre();
                case 5 -> actualizarEstudiante();
                case 6 -> eliminarEstudiante();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }

    private void listarEstudiantes() {

        System.out.println("\n--- LISTA DE ESTUDIANTES ---");

        try {

            List<Estudiante> lista =
                    controlador.listarEstudiantes();

            if (lista.isEmpty()) {
                System.out.println("No hay estudiantes registrados.");
                return;
            }

            for (Estudiante e : lista) {

                System.out.println("--------------------------------------");
                System.out.println("ID: " + e.getId());
                System.out.println("Nombre: " + e.getName());
                System.out.println("Apellido: " + e.getLastname());
                System.out.println("Correo: " + e.getEmail());
                System.out.println("Tipo Documento: "
                        + e.getTipoDocumento());
                System.out.println("Documento: "
                        + e.getDocumento());
                System.out.println("Teléfono: "
                        + e.getTelefono());
                System.out.println("Semestre: "
                        + e.getSemestreActual());
                System.out.println("Estado: "
                        + e.getEstado());
            }

        } catch (Exception e) {
            System.out.println("Error: "
                    + e.getMessage());
        }
    }

    private void registrarEstudiante() {

        System.out.println("\n--- REGISTRAR ESTUDIANTE ---");

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();

        System.out.print("Apellido: ");
        String apellido = scanner.nextLine().trim();

        System.out.print("Correo: ");
        String email = scanner.nextLine().trim();

        System.out.print("Tipo Documento (CC/TI/CE): ");
        String tipoDocumento = scanner.nextLine().trim();

        System.out.print("Documento: ");
        String documento = scanner.nextLine().trim();

        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine().trim();

        System.out.print("Semestre Actual: ");
        int semestreActual =
                Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Estado (ACTIVO/INACTIVO): ");
        String estado = scanner.nextLine().trim();

        try {

            controlador.registrarEstudiante(
                    nombre,
                    apellido,
                    email,
                    tipoDocumento,
                    documento,
                    telefono,
                    semestreActual,
                    estado
            );

            System.out.println(
                    "¡Estudiante registrado exitosamente!");

        } catch (Exception e) {

            System.out.println(
                    "Error al registrar: "
                            + e.getMessage());
        }
    }

    private void buscarPorId() {

        System.out.println("\n--- BUSCAR POR ID ---");

        System.out.print("Ingrese ID: ");

        try {

            int id =
                    Integer.parseInt(
                            scanner.nextLine().trim());

            Optional<Estudiante> estudiante =
                    controlador.buscarEstudiantePorId(id);

            if (estudiante.isPresent()) {

                Estudiante e = estudiante.get();

                System.out.println("ID: " + e.getId());
                System.out.println("Nombre: "
                        + e.getName());
                System.out.println("Apellido: "
                        + e.getLastname());
                System.out.println("Correo: "
                        + e.getEmail());
                System.out.println("Tipo Documento: "
                        + e.getTipoDocumento());
                System.out.println("Documento: "
                        + e.getDocumento());
                System.out.println("Teléfono: "
                        + e.getTelefono());
                System.out.println("Semestre: "
                        + e.getSemestreActual());
                System.out.println("Estado: "
                        + e.getEstado());

            } else {

                System.out.println(
                        "No se encontró el estudiante.");
            }

        } catch (Exception e) {

            System.out.println(
                    "Error: "
                            + e.getMessage());
        }
    }

    private void buscarPorNombre() {

        System.out.println("\n--- BUSCAR POR NOMBRE ---");

        System.out.print("Ingrese nombre o apellido: ");

        String nombre =
                scanner.nextLine().trim();

        try {

            List<Estudiante> resultados =
                    controlador.buscarEstudiantesPorNombre(nombre);

            if (resultados.isEmpty()) {

                System.out.println(
                        "No se encontraron resultados.");
                return;
            }

            for (Estudiante e : resultados) {

                System.out.println("--------------------------------------");
                System.out.println("ID: " + e.getId());
                System.out.println("Nombre: " + e.getName());
                System.out.println("Apellido: " + e.getLastname());
                System.out.println("Documento: " + e.getDocumento());
                System.out.println("Estado: " + e.getEstado());
            }

        } catch (Exception e) {

            System.out.println(
                    "Error: "
                            + e.getMessage());
        }
    }

    private void actualizarEstudiante() {

        System.out.println("\n--- ACTUALIZAR ESTUDIANTE ---");

        try {

            System.out.print("ID: ");
            int id =
                    Integer.parseInt(
                            scanner.nextLine().trim());

            System.out.print("Nombre: ");
            String nombre =
                    scanner.nextLine().trim();

            System.out.print("Apellido: ");
            String apellido =
                    scanner.nextLine().trim();

            System.out.print("Correo: ");
            String email =
                    scanner.nextLine().trim();

            System.out.print("Tipo Documento: ");
            String tipoDocumento =
                    scanner.nextLine().trim();

            System.out.print("Documento: ");
            String documento =
                    scanner.nextLine().trim();

            System.out.print("Teléfono: ");
            String telefono =
                    scanner.nextLine().trim();

            System.out.print("Semestre Actual: ");
            int semestreActual =
                    Integer.parseInt(
                            scanner.nextLine().trim());

            System.out.print("Estado: ");
            String estado =
                    scanner.nextLine().trim();

            controlador.actualizarEstudiante(
                    id,
                    nombre,
                    apellido,
                    email,
                    tipoDocumento,
                    documento,
                    telefono,
                    semestreActual,
                    estado
            );

            System.out.println(
                    "¡Estudiante actualizado exitosamente!");

        } catch (Exception e) {

            System.out.println(
                    "Error al actualizar: "
                            + e.getMessage());
        }
    }

    private void eliminarEstudiante() {

        System.out.println("\n--- ELIMINAR ESTUDIANTE ---");

        try {

            System.out.print("ID del estudiante: ");

            int id =
                    Integer.parseInt(
                            scanner.nextLine().trim());

            controlador.eliminarEstudiante(id);

            System.out.println(
                    "¡Estudiante eliminado exitosamente!");

        } catch (Exception e) {

            System.out.println(
                    "Error al eliminar: "
                            + e.getMessage());
        }
    }
}