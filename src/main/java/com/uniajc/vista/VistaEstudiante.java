package com.uniajc.vista;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.uniajc.controlador.ControladorEstudiante;
import com.uniajc.dao.EstudianteDao;
import com.uniajc.modelo.Estudiante;
import com.uniajc.servicios.EstudianteService;

public class VistaEstudiante {

    private final ControladorEstudiante controlador =
        new ControladorEstudiante(new EstudianteService(new EstudianteDao()));
    private final Scanner sc = new Scanner(System.in);

    public void iniciar() {
        int opcion;
        do {
            System.out.println("\n--- ESTUDIANTES ---");
            System.out.println("1. Registrar");
            System.out.println("2. Listar");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Actualizar");
            System.out.println("5. Eliminar");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> registrar();
                case 2 -> listar();
                case 3 -> buscar();
                case 4 -> actualizar();
                case 5 -> eliminar();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private void registrar() {
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Apellido: ");
        String apellido = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        mostrar(controlador.registrar(nombre, apellido, email), "Estudiante registrado.");
    }

    private void listar() {
        List<Estudiante> lista = controlador.listar();
        if (lista.isEmpty()) {
            System.out.println("No hay estudiantes.");
            return;
        }
        for (Estudiante e : lista) {
            System.out.printf("%d | %s %s | %s%n",
                e.getId(), e.getNombre(), e.getApellido(), e.getEmail());
        }
    }

    private void buscar() {
        System.out.print("ID: ");
        Optional<Estudiante> e = controlador.buscar(leerEntero());
        if (e.isPresent()) {
            System.out.printf("%d | %s %s | %s%n",
                e.get().getId(), e.get().getNombre(), e.get().getApellido(), e.get().getEmail());
        } else {
            System.out.println("No encontrado.");
        }
    }

    private void actualizar() {
        System.out.print("ID a actualizar: ");
        int id = leerEntero();
        System.out.print("Nuevo nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Nuevo apellido: ");
        String apellido = sc.nextLine();
        System.out.print("Nuevo email: ");
        String email = sc.nextLine();
        mostrar(controlador.actualizar(id, nombre, apellido, email), "Estudiante actualizado.");
    }

    private void eliminar() {
        System.out.print("ID a eliminar: ");
        mostrar(controlador.eliminar(leerEntero()), "Estudiante eliminado.");
    }

    private void mostrar(String error, String exito) {
        System.out.println(error == null ? exito : "Error: " + error);
    }

    private int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un número válido: ");
            }
        }
    }
}