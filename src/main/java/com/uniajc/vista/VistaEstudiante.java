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

    public VistaEstudiante(ControladorEstudiante controlador, Scanner scanner) {
        this.controlador = controlador;
        this.scanner = scanner;
    }

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            System.out.print("Seleccione una opción: ");
            if (!scanner.hasNextLine()) break;
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    listarEstudiantes();
                    break;
                case "2":
                    buscarPorId();
                    break;
                case "3":
                    registrarEstudiante();
                    break;
                case "4":
                    actualizarEstudiante();
                    break;
                case "5":
                    eliminarEstudiante();
                    break;
                case "0":
                    salir = true;
                    System.out.println("Regresando al menú principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.\n");
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("\n========================================");
        System.out.println("       GESTIÓN DE ESTUDIANTES (CONSOLA) ");
        System.out.println("========================================");
        System.out.println("1. Listar todos los estudiantes");
        System.out.println("2. Buscar estudiante por ID");
        System.out.println("3. Registrar nuevo estudiante");
        System.out.println("4. Actualizar estudiante");
        System.out.println("5. Eliminar estudiante");
        System.out.println("0. Volver");
        System.out.println("========================================");
    }

    public void listarEstudiantes() {
        System.out.println("\n--- LISTA DE ESTUDIANTES ---");
        List<Estudiante> lista = controlador.listarEstudiantes();
        if (lista.isEmpty()) {
            System.out.println("No hay estudiantes registrados.");
            return;
        }

        System.out.printf("%-5s | %-20s | %-20s | %-30s%n", "ID", "Nombre", "Apellido", "Email");
        System.out.println("----------------------------------------------------------------------------------");
        for (Estudiante e : lista) {
            System.out.printf("%-5d | %-20s | %-20s | %-30s%n",
                    e.getId(), e.getNombre(), e.getApellido(), e.getEmail());
        }
    }

    public void buscarPorId() {
        System.out.print("\nIngrese el ID del estudiante a buscar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Optional<Estudiante> opt = controlador.buscarPorId(id);
            if (opt.isPresent()) {
                Estudiante e = opt.get();
                System.out.println("Estudiante encontrado:");
                System.out.println("ID: " + e.getId());
                System.out.println("Nombre: " + e.getNombre());
                System.out.println("Apellido: " + e.getApellido());
                System.out.println("Email: " + e.getEmail());
            } else {
                System.out.println("No se encontró ningún estudiante con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID debe ser un número entero.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void registrarEstudiante() {
        System.out.println("\n--- REGISTRAR ESTUDIANTE ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        try {
            controlador.registrarEstudiante(nombre, apellido, email);
            System.out.println("¡Estudiante registrado exitosamente!");
        } catch (Exception e) {
            System.out.println("Error al registrar: " + e.getMessage());
        }
    }

    public void actualizarEstudiante() {
        System.out.println("\n--- ACTUALIZAR ESTUDIANTE ---");
        System.out.print("Ingrese el ID del estudiante a actualizar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Optional<Estudiante> opt = controlador.buscarPorId(id);
            if (opt.isEmpty()) {
                System.out.println("No existe estudiante con ID: " + id);
                return;
            }

            Estudiante actual = opt.get();
            System.out.println("Datos actuales -> Nombre: " + actual.getNombre() + ", Apellido: " + actual.getApellido() + ", Email: " + actual.getEmail());

            System.out.print("Nuevo Nombre (deje en blanco para conservar actual): ");
            String nombre = scanner.nextLine().trim();
            if (nombre.isEmpty()) nombre = actual.getNombre();

            System.out.print("Nuevo Apellido (deje en blanco para conservar actual): ");
            String apellido = scanner.nextLine().trim();
            if (apellido.isEmpty()) apellido = actual.getApellido();

            System.out.print("Nuevo Email (deje en blanco para conservar actual): ");
            String email = scanner.nextLine().trim();
            if (email.isEmpty()) email = actual.getEmail();

            controlador.actualizarEstudiante(id, nombre, apellido, email);
            System.out.println("¡Estudiante actualizado exitosamente!");
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID debe ser un número entero.");
        } catch (Exception e) {
            System.out.println("Error al actualizar: " + e.getMessage());
        }
    }

    public void eliminarEstudiante() {
        System.out.println("\n--- ELIMINAR ESTUDIANTE ---");
        System.out.print("Ingrese el ID del estudiante a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("¿Está seguro de eliminar al estudiante con ID " + id + "? (s/n): ");
            String confirmacion = scanner.nextLine().trim();
            if (confirmacion.equalsIgnoreCase("s")) {
                controlador.eliminarEstudiante(id);
                System.out.println("¡Estudiante eliminado exitosamente!");
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID debe ser un número entero.");
        } catch (Exception e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
    }
}
