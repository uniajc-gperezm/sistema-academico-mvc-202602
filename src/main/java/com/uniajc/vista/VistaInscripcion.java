package com.uniajc.vista;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.uniajc.controlador.ControladorInscripcion;
import com.uniajc.modelo.Inscripcion;

public class VistaInscripcion {

    private final ControladorInscripcion controlador;
    private final Scanner scanner;

    public VistaInscripcion() {
        this.controlador = new ControladorInscripcion();
        this.scanner = new Scanner(System.in);
    }

    public VistaInscripcion(ControladorInscripcion controlador, Scanner scanner) {
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
                    listarInscripciones();
                    break;
                case "2":
                    buscarPorId();
                    break;
                case "3":
                    registrarInscripcion();
                    break;
                case "4":
                    registrarNota();
                    break;
                case "5":
                    actualizarInscripcion();
                    break;
                case "6":
                    eliminarInscripcion();
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
        System.out.println("      GESTIÓN DE INSCRIPCIONES (CONSOLA)");
        System.out.println("========================================");
        System.out.println("1. Listar todas las inscripciones");
        System.out.println("2. Buscar inscripción por ID");
        System.out.println("3. Inscribir estudiante a grupo");
        System.out.println("4. Registrar / calificar nota final");
        System.out.println("5. Actualizar inscripción");
        System.out.println("6. Eliminar inscripción");
        System.out.println("0. Volver");
        System.out.println("========================================");
    }

    public void listarInscripciones() {
        System.out.println("\n--- LISTA DE INSCRIPCIONES ---");
        List<Inscripcion> lista = controlador.listarInscripciones();
        if (lista.isEmpty()) {
            System.out.println("No hay inscripciones registradas.");
            return;
        }

        System.out.printf("%-5s | %-8s | %-8s | %-25s | %-35s | %-10s | %-12s%n",
                "ID", "ID Est", "ID Grp", "Estudiante", "Grupo / Materia", "Nota", "Estado");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------");
        for (Inscripcion i : lista) {
            String estNom = (i.getEstudianteNombre() != null) ? i.getEstudianteNombre() : "N/A";
            String grpDet = (i.getGrupoDetalle() != null) ? i.getGrupoDetalle() : "N/A";
            String notaStr = (i.getNotaFinal() != null) ? String.format("%.2f", i.getNotaFinal()) : "Sin Calificar";
            System.out.printf("%-5d | %-8d | %-8d | %-25s | %-35s | %-10s | %-12s%n",
                    i.getId(), i.getIdEstudiante(), i.getIdGrupo(), estNom, grpDet, notaStr, i.getEstado());
        }
    }

    public void buscarPorId() {
        System.out.print("\nIngrese el ID de la inscripción a buscar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Optional<Inscripcion> opt = controlador.buscarPorId(id);
            if (opt.isPresent()) {
                Inscripcion i = opt.get();
                System.out.println("Inscripción encontrada:");
                System.out.println("ID: " + i.getId());
                System.out.println("ID Estudiante: " + i.getIdEstudiante() + " (" + i.getEstudianteNombre() + ")");
                System.out.println("ID Grupo: " + i.getIdGrupo() + " (" + i.getGrupoDetalle() + ")");
                System.out.println("Nota Final: " + (i.getNotaFinal() != null ? i.getNotaFinal() : "Sin Calificar"));
                System.out.println("Estado: " + i.getEstado());
            } else {
                System.out.println("No se encontró ninguna inscripción con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID debe ser un número entero.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void registrarInscripcion() {
        System.out.println("\n--- REGISTRAR INSCRIPCIÓN ---");
        try {
            System.out.print("ID del Estudiante: ");
            int idEstudiante = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("ID del Grupo: ");
            int idGrupo = Integer.parseInt(scanner.nextLine().trim());

            controlador.registrarInscripcion(idEstudiante, idGrupo);
            System.out.println("¡Estudiante inscrito exitosamente!");
        } catch (NumberFormatException e) {
            System.out.println("Error: Los IDs deben ser números enteros.");
        } catch (Exception e) {
            System.out.println("Error al inscribir: " + e.getMessage());
        }
    }

    public void registrarNota() {
        System.out.println("\n--- CALIFICAR / REGISTRAR NOTA ---");
        try {
            System.out.print("ID de la inscripción a calificar: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Nota final (0.0 - 5.0): ");
            double nota = Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));

            controlador.registrarNota(id, nota);
            System.out.println("¡Nota registrada exitosamente! Estado actualizado automáticamente.");
        } catch (NumberFormatException e) {
            System.out.println("Error: Formato numérico incorrecto.");
        } catch (Exception e) {
            System.out.println("Error al calificar: " + e.getMessage());
        }
    }

    public void actualizarInscripcion() {
        System.out.println("\n--- ACTUALIZAR INSCRIPCIÓN ---");
        System.out.print("Ingrese el ID de la inscripción a actualizar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Optional<Inscripcion> opt = controlador.buscarPorId(id);
            if (opt.isEmpty()) {
                System.out.println("No existe inscripción con ID: " + id);
                return;
            }

            Inscripcion actual = opt.get();
            System.out.println("Datos actuales -> ID Estudiante: " + actual.getIdEstudiante() + ", ID Grupo: " + actual.getIdGrupo() + ", Nota: " + actual.getNotaFinal() + ", Estado: " + actual.getEstado());

            System.out.print("Nuevo ID Estudiante (deje en blanco para conservar actual): ");
            String estStr = scanner.nextLine().trim();
            int idEstudiante = estStr.isEmpty() ? actual.getIdEstudiante() : Integer.parseInt(estStr);

            System.out.print("Nuevo ID Grupo (deje en blanco para conservar actual): ");
            String grpStr = scanner.nextLine().trim();
            int idGrupo = grpStr.isEmpty() ? actual.getIdGrupo() : Integer.parseInt(grpStr);

            System.out.print("Nueva Nota Final (deje en blanco para conservar actual): ");
            String notaStr = scanner.nextLine().trim();
            Double notaFinal = notaStr.isEmpty() ? actual.getNotaFinal() : Double.parseDouble(notaStr.replace(',', '.'));

            System.out.print("Nuevo Estado (ACTIVO, APROBADO, REPROBADO, CANCELADO) [Enter para conservar]: ");
            String estado = scanner.nextLine().trim();
            if (estado.isEmpty()) estado = actual.getEstado();

            controlador.actualizarInscripcion(id, idEstudiante, idGrupo, notaFinal, estado);
            System.out.println("¡Inscripción actualizada exitosamente!");
        } catch (NumberFormatException e) {
            System.out.println("Error: Formato numérico incorrecto.");
        } catch (Exception e) {
            System.out.println("Error al actualizar: " + e.getMessage());
        }
    }

    public void eliminarInscripcion() {
        System.out.println("\n--- ELIMINAR INSCRIPCIÓN ---");
        System.out.print("Ingrese el ID de la inscripción a eliminar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("¿Está seguro de eliminar la inscripción con ID " + id + "? (s/n): ");
            String confirmacion = scanner.nextLine().trim();
            if (confirmacion.equalsIgnoreCase("s")) {
                controlador.eliminarInscripcion(id);
                System.out.println("¡Inscripción eliminada exitosamente!");
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
