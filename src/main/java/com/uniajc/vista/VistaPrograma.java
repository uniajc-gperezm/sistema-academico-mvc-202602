package com.uniajc.vista;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.uniajc.controlador.ControladorPrograma;
import com.uniajc.modelo.Programa;

public class VistaPrograma {

    private final ControladorPrograma controlador;
    private final Scanner scanner;

    public VistaPrograma() {
        this.controlador = new ControladorPrograma();
        this.scanner = new Scanner(System.in);
    }

    public void iniciarMenu() {
        int opcion = -1;
        do {
            System.out.println("\n===== GESTIÓN DE PROGRAMAS (CONSOLA) =====");
            System.out.println("1. Listar Programas");
            System.out.println("2. Registrar Programa");
            System.out.println("3. Buscar Programa por ID");
            System.out.println("4. Buscar Programa por Código");
            System.out.println("5. Actualizar Programa");
            System.out.println("6. Eliminar Programa");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida.");
                continue;
            }

            switch (opcion) {
                case 1 -> listarProgramas();
                case 2 -> registrarPrograma();
                case 3 -> buscarPorId();
                case 4 -> buscarPorCodigo();
                case 5 -> actualizarPrograma();
                case 6 -> eliminarPrograma();
                case 0 -> System.out.println("Saliendo del menú de programas...");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private void listarProgramas() {
        System.out.println("\n--- Lista de Programas Académicos ---");
        try {
            List<Programa> lista = controlador.listarProgramas();
            if (lista.isEmpty()) {
                System.out.println("No hay programas registrados.");
                return;
            }
            for (Programa p : lista) {
                System.out.printf("ID: %-4d | Código: %-10s | Nombre: %-25s | Facultad: %-20s | Semestres: %d%n",
                        p.getId(), p.getCodigo(), p.getNombre(), p.getFacultad(), p.getDuracionSemestres());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void registrarPrograma() {
        System.out.println("\n--- Registrar Programa ---");
        System.out.print("Código (ej. ING-SIS): ");
        String codigo = scanner.nextLine().trim();
        System.out.print("Nombre del programa: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Facultad: ");
        String facultad = scanner.nextLine().trim();
        System.out.print("Duración en semestres (1-12): ");

        try {
            int duracion = Integer.parseInt(scanner.nextLine().trim());
            controlador.registrarPrograma(codigo, nombre, facultad, duracion);
            System.out.println("¡Programa registrado exitosamente!");
        } catch (NumberFormatException e) {
            System.out.println("Error: La duración debe ser un número entero válido.");
        } catch (Exception e) {
            System.out.println("Error al registrar: " + e.getMessage());
        }
    }

    private void buscarPorId() {
        System.out.println("\n--- Buscar Programa por ID ---");
        System.out.print("Ingrese ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Optional<Programa> programa = controlador.buscarProgramaPorId(id);
            if (programa.isPresent()) {
                Programa p = programa.get();
                System.out.printf("Encontrado: ID=%d, Código=%s, Nombre=%s, Facultad=%s, Semestres=%d%n",
                        p.getId(), p.getCodigo(), p.getNombre(), p.getFacultad(), p.getDuracionSemestres());
            } else {
                System.out.println("No se encontró ningún programa con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido: debe ser un número entero.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void buscarPorCodigo() {
        System.out.println("\n--- Buscar Programa por Código ---");
        System.out.print("Ingrese el código del programa: ");
        String codigo = scanner.nextLine().trim();
        try {
            Optional<Programa> programa = controlador.buscarProgramaPorCodigo(codigo);
            if (programa.isPresent()) {
                Programa p = programa.get();
                System.out.printf("Encontrado: ID=%d, Código=%s, Nombre=%s, Facultad=%s, Semestres=%d%n",
                        p.getId(), p.getCodigo(), p.getNombre(), p.getFacultad(), p.getDuracionSemestres());
            } else {
                System.out.println("No se encontró ningún programa con el código: " + codigo);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void actualizarPrograma() {
        System.out.println("\n--- Actualizar Programa ---");
        System.out.print("Ingrese ID del programa a actualizar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Nuevo Código: ");
            String codigo = scanner.nextLine().trim();
            System.out.print("Nuevo Nombre: ");
            String nombre = scanner.nextLine().trim();
            System.out.print("Nueva Facultad: ");
            String facultad = scanner.nextLine().trim();
            System.out.print("Nueva Duración en semestres (1-12): ");
            int duracion = Integer.parseInt(scanner.nextLine().trim());

            controlador.actualizarPrograma(id, codigo, nombre, facultad, duracion);
            System.out.println("¡Programa actualizado exitosamente!");
        } catch (NumberFormatException e) {
            System.out.println("Error: Verifique que los campos numéricos (ID, semestres) sean enteros válidos.");
        } catch (Exception e) {
            System.out.println("Error al actualizar: " + e.getMessage());
        }
    }

    private void eliminarPrograma() {
        System.out.println("\n--- Eliminar Programa ---");
        System.out.print("Ingrese ID del programa a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            controlador.eliminarPrograma(id);
            System.out.println("¡Programa eliminado exitosamente!");
        } catch (NumberFormatException e) {
            System.out.println("ID inválido: debe ser un número entero.");
        } catch (Exception e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
    }
}