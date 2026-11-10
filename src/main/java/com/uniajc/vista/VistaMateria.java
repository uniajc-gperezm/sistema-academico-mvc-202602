package com.uniajc.vista;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.uniajc.controlador.ControladorMateria;
import com.uniajc.modelo.Materia;

public class VistaMateria {

    private final ControladorMateria controlador;
    private final Scanner scanner;

    public VistaMateria() {
        this.controlador = new ControladorMateria();
        this.scanner = new Scanner(System.in);
    }

    public VistaMateria(ControladorMateria controlador, Scanner scanner) {
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
                    listarMaterias();
                    break;
                case "2":
                    buscarPorId();
                    break;
                case "3":
                    registrarMateria();
                    break;
                case "4":
                    actualizarMateria();
                    break;
                case "5":
                    eliminarMateria();
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
        System.out.println("         GESTIÓN DE MATERIAS (CONSOLA)  ");
        System.out.println("========================================");
        System.out.println("1. Listar todas las materias");
        System.out.println("2. Buscar materia por ID");
        System.out.println("3. Registrar nueva materia");
        System.out.println("4. Actualizar materia");
        System.out.println("5. Eliminar materia");
        System.out.println("0. Volver");
        System.out.println("========================================");
    }

    public void listarMaterias() {
        System.out.println("\n--- LISTA DE MATERIAS ---");
        List<Materia> lista = controlador.listarMaterias();
        if (lista.isEmpty()) {
            System.out.println("No hay materias registradas.");
            return;
        }

        System.out.printf("%-5s | %-35s | %-10s%n", "ID", "Nombre de la Materia", "Créditos");
        System.out.println("------------------------------------------------------------------");
        for (Materia m : lista) {
            System.out.printf("%-5d | %-35s | %-10d%n",
                    m.getId(), m.getNombre(), m.getCreditos());
        }
    }

    public void buscarPorId() {
        System.out.print("\nIngrese el ID de la materia a buscar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Optional<Materia> opt = controlador.buscarPorId(id);
            if (opt.isPresent()) {
                Materia m = opt.get();
                System.out.println("Materia encontrada:");
                System.out.println("ID: " + m.getId());
                System.out.println("Nombre: " + m.getNombre());
                System.out.println("Créditos: " + m.getCreditos());
            } else {
                System.out.println("No se encontró ninguna materia con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID debe ser un número entero.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void registrarMateria() {
        System.out.println("\n--- REGISTRAR MATERIA ---");
        System.out.print("Nombre de la materia: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Créditos (1 - 10): ");
        try {
            int creditos = Integer.parseInt(scanner.nextLine().trim());
            controlador.registrarMateria(nombre, creditos);
            System.out.println("¡Materia registrada exitosamente!");
        } catch (NumberFormatException e) {
            System.out.println("Error: Los créditos deben ser un número entero.");
        } catch (Exception e) {
            System.out.println("Error al registrar: " + e.getMessage());
        }
    }

    public void actualizarMateria() {
        System.out.println("\n--- ACTUALIZAR MATERIA ---");
        System.out.print("Ingrese el ID de la materia a actualizar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Optional<Materia> opt = controlador.buscarPorId(id);
            if (opt.isEmpty()) {
                System.out.println("No existe materia con ID: " + id);
                return;
            }

            Materia actual = opt.get();
            System.out.println("Datos actuales -> Nombre: " + actual.getNombre() + ", Créditos: " + actual.getCreditos());

            System.out.print("Nuevo Nombre (deje en blanco para conservar actual): ");
            String nombre = scanner.nextLine().trim();
            if (nombre.isEmpty()) nombre = actual.getNombre();

            System.out.print("Nuevos Créditos (deje en blanco para conservar actual): ");
            String creditosStr = scanner.nextLine().trim();
            int creditos = creditosStr.isEmpty() ? actual.getCreditos() : Integer.parseInt(creditosStr);

            controlador.actualizarMateria(id, nombre, creditos);
            System.out.println("¡Materia actualizada exitosamente!");
        } catch (NumberFormatException e) {
            System.out.println("Error: Formato numérico incorrecto.");
        } catch (Exception e) {
            System.out.println("Error al actualizar: " + e.getMessage());
        }
    }

    public void eliminarMateria() {
        System.out.println("\n--- ELIMINAR MATERIA ---");
        System.out.print("Ingrese el ID de la materia a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("¿Está seguro de eliminar la materia con ID " + id + "? (s/n): ");
            String confirmacion = scanner.nextLine().trim();
            if (confirmacion.equalsIgnoreCase("s")) {
                controlador.eliminarMateria(id);
                System.out.println("¡Materia eliminada exitosamente!");
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
