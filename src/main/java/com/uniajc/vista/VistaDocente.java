package com.uniajc.vista;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.uniajc.controlador.ControladorDocente;
import com.uniajc.modelo.Docente;

public class VistaDocente {

    private final ControladorDocente controlador;
    private final Scanner scanner;

    public VistaDocente() {
        this.controlador = new ControladorDocente();
        this.scanner = new Scanner(System.in);
    }

    public VistaDocente(ControladorDocente controlador, Scanner scanner) {
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
                    listarDocentes();
                    break;
                case "2":
                    buscarPorId();
                    break;
                case "3":
                    registrarDocente();
                    break;
                case "4":
                    actualizarDocente();
                    break;
                case "5":
                    eliminarDocente();
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
        System.out.println("         GESTIÓN DE DOCENTES (CONSOLA)  ");
        System.out.println("========================================");
        System.out.println("1. Listar todos los docentes");
        System.out.println("2. Buscar docente por ID");
        System.out.println("3. Registrar nuevo docente");
        System.out.println("4. Actualizar docente");
        System.out.println("5. Eliminar docente");
        System.out.println("0. Volver");
        System.out.println("========================================");
    }

    public void listarDocentes() {
        System.out.println("\n--- LISTA DE DOCENTES ---");
        List<Docente> lista = controlador.listarDocentes();
        if (lista.isEmpty()) {
            System.out.println("No hay docentes registrados.");
            return;
        }

        System.out.printf("%-5s | %-25s | %-25s | %-30s%n", "ID", "Nombre", "Especialidad", "Email");
        System.out.println("---------------------------------------------------------------------------------------------");
        for (Docente d : lista) {
            System.out.printf("%-5d | %-25s | %-25s | %-30s%n",
                    d.getId(), d.getNombre(), d.getEspecialidad(), d.getEmail());
        }
    }

    public void buscarPorId() {
        System.out.print("\nIngrese el ID del docente a buscar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Optional<Docente> opt = controlador.buscarPorId(id);
            if (opt.isPresent()) {
                Docente d = opt.get();
                System.out.println("Docente encontrado:");
                System.out.println("ID: " + d.getId());
                System.out.println("Nombre: " + d.getNombre());
                System.out.println("Especialidad: " + d.getEspecialidad());
                System.out.println("Email: " + d.getEmail());
            } else {
                System.out.println("No se encontró ningún docente con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID debe ser un número entero.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void registrarDocente() {
        System.out.println("\n--- REGISTRAR DOCENTE ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Especialidad: ");
        String especialidad = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        try {
            controlador.registrarDocente(nombre, especialidad, email);
            System.out.println("¡Docente registrado exitosamente!");
        } catch (Exception e) {
            System.out.println("Error al registrar: " + e.getMessage());
        }
    }

    public void actualizarDocente() {
        System.out.println("\n--- ACTUALIZAR DOCENTE ---");
        System.out.print("Ingrese el ID del docente a actualizar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Optional<Docente> opt = controlador.buscarPorId(id);
            if (opt.isEmpty()) {
                System.out.println("No existe docente con ID: " + id);
                return;
            }

            Docente actual = opt.get();
            System.out.println("Datos actuales -> Nombre: " + actual.getNombre() + ", Especialidad: " + actual.getEspecialidad() + ", Email: " + actual.getEmail());

            System.out.print("Nuevo Nombre (deje en blanco para conservar actual): ");
            String nombre = scanner.nextLine().trim();
            if (nombre.isEmpty()) nombre = actual.getNombre();

            System.out.print("Nueva Especialidad (deje en blanco para conservar actual): ");
            String especialidad = scanner.nextLine().trim();
            if (especialidad.isEmpty()) especialidad = actual.getEspecialidad();

            System.out.print("Nuevo Email (deje en blanco para conservar actual): ");
            String email = scanner.nextLine().trim();
            if (email.isEmpty()) email = actual.getEmail();

            controlador.actualizarDocente(id, nombre, especialidad, email);
            System.out.println("¡Docente actualizado exitosamente!");
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID debe ser un número entero.");
        } catch (Exception e) {
            System.out.println("Error al actualizar: " + e.getMessage());
        }
    }

    public void eliminarDocente() {
        System.out.println("\n--- ELIMINAR DOCENTE ---");
        System.out.print("Ingrese el ID del docente a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("¿Está seguro de eliminar al docente con ID " + id + "? (s/n): ");
            String confirmacion = scanner.nextLine().trim();
            if (confirmacion.equalsIgnoreCase("s")) {
                controlador.eliminarDocente(id);
                System.out.println("¡Docente eliminado exitosamente!");
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
