package com.uniajc.vista;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.uniajc.controlador.ControladorGrupo;
import com.uniajc.modelo.Grupo;

public class VistaGrupo {

    private final ControladorGrupo controlador;
    private final Scanner scanner;

    public VistaGrupo() {
        this.controlador = new ControladorGrupo();
        this.scanner = new Scanner(System.in);
    }

    public VistaGrupo(ControladorGrupo controlador, Scanner scanner) {
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
                    listarGrupos();
                    break;
                case "2":
                    buscarPorId();
                    break;
                case "3":
                    registrarGrupo();
                    break;
                case "4":
                    actualizarGrupo();
                    break;
                case "5":
                    eliminarGrupo();
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
        System.out.println("          GESTIÓN DE GRUPOS (CONSOLA)   ");
        System.out.println("========================================");
        System.out.println("1. Listar todos los grupos");
        System.out.println("2. Buscar grupo por ID");
        System.out.println("3. Registrar nuevo grupo");
        System.out.println("4. Actualizar grupo");
        System.out.println("5. Eliminar grupo");
        System.out.println("0. Volver");
        System.out.println("========================================");
    }

    public void listarGrupos() {
        System.out.println("\n--- LISTA DE GRUPOS ---");
        List<Grupo> lista = controlador.listarGrupos();
        if (lista.isEmpty()) {
            System.out.println("No hay grupos registrados.");
            return;
        }

        System.out.printf("%-5s | %-10s | %-10s | %-25s | %-25s | %-15s | %-20s%n",
                "ID", "ID Mat", "ID Doc", "Materia", "Docente", "Aula", "Horario");
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        for (Grupo g : lista) {
            String matNom = (g.getMateriaNombre() != null) ? g.getMateriaNombre() : "N/A";
            String docNom = (g.getDocenteNombre() != null) ? g.getDocenteNombre() : "N/A";
            System.out.printf("%-5d | %-10d | %-10d | %-25s | %-25s | %-15s | %-20s%n",
                    g.getId(), g.getIdMateria(), g.getIdDocente(), matNom, docNom, g.getAula(), g.getHorario());
        }
    }

    public void buscarPorId() {
        System.out.print("\nIngrese el ID del grupo a buscar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Optional<Grupo> opt = controlador.buscarPorId(id);
            if (opt.isPresent()) {
                Grupo g = opt.get();
                System.out.println("Grupo encontrado:");
                System.out.println("ID: " + g.getId());
                System.out.println("ID Materia: " + g.getIdMateria() + " (" + g.getMateriaNombre() + ")");
                System.out.println("ID Docente: " + g.getIdDocente() + " (" + g.getDocenteNombre() + ")");
                System.out.println("Aula: " + g.getAula());
                System.out.println("Horario: " + g.getHorario());
            } else {
                System.out.println("No se encontró ningún grupo con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID debe ser un número entero.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void registrarGrupo() {
        System.out.println("\n--- REGISTRAR GRUPO ---");
        try {
            System.out.print("ID de la materia: ");
            int idMateria = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("ID del docente: ");
            int idDocente = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Aula: ");
            String aula = scanner.nextLine().trim();
            System.out.print("Horario (ej. Lun-Mie 08:00-10:00): ");
            String horario = scanner.nextLine().trim();

            controlador.registrarGrupo(idMateria, idDocente, aula, horario);
            System.out.println("¡Grupo registrado exitosamente!");
        } catch (NumberFormatException e) {
            System.out.println("Error: Los IDs deben ser números enteros.");
        } catch (Exception e) {
            System.out.println("Error al registrar: " + e.getMessage());
        }
    }

    public void actualizarGrupo() {
        System.out.println("\n--- ACTUALIZAR GRUPO ---");
        System.out.print("Ingrese el ID del grupo a actualizar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Optional<Grupo> opt = controlador.buscarPorId(id);
            if (opt.isEmpty()) {
                System.out.println("No existe grupo con ID: " + id);
                return;
            }

            Grupo actual = opt.get();
            System.out.println("Datos actuales -> ID Materia: " + actual.getIdMateria() + ", ID Docente: " + actual.getIdDocente() + ", Aula: " + actual.getAula() + ", Horario: " + actual.getHorario());

            System.out.print("Nuevo ID Materia (deje en blanco para conservar actual): ");
            String matStr = scanner.nextLine().trim();
            int idMateria = matStr.isEmpty() ? actual.getIdMateria() : Integer.parseInt(matStr);

            System.out.print("Nuevo ID Docente (deje en blanco para conservar actual): ");
            String docStr = scanner.nextLine().trim();
            int idDocente = docStr.isEmpty() ? actual.getIdDocente() : Integer.parseInt(docStr);

            System.out.print("Nueva Aula (deje en blanco para conservar actual): ");
            String aula = scanner.nextLine().trim();
            if (aula.isEmpty()) aula = actual.getAula();

            System.out.print("Nuevo Horario (deje en blanco para conservar actual): ");
            String horario = scanner.nextLine().trim();
            if (horario.isEmpty()) horario = actual.getHorario();

            controlador.actualizarGrupo(id, idMateria, idDocente, aula, horario);
            System.out.println("¡Grupo actualizado exitosamente!");
        } catch (NumberFormatException e) {
            System.out.println("Error: Formato numérico incorrecto.");
        } catch (Exception e) {
            System.out.println("Error al actualizar: " + e.getMessage());
        }
    }

    public void eliminarGrupo() {
        System.out.println("\n--- ELIMINAR GRUPO ---");
        System.out.print("Ingrese el ID del grupo a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("¿Está seguro de eliminar el grupo con ID " + id + "? (s/n): ");
            String confirmacion = scanner.nextLine().trim();
            if (confirmacion.equalsIgnoreCase("s")) {
                controlador.eliminarGrupo(id);
                System.out.println("¡Grupo eliminado exitosamente!");
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
