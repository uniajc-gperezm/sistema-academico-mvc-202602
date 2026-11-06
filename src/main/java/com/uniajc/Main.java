package com.uniajc;

import java.util.Scanner;

import javax.swing.SwingUtilities;

import com.uniajc.vista.VistaEstudiante;
import com.uniajc.vista.VistaEstudianteFX;
import com.uniajc.vista.VistaEstudianteSwing;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Seleccione la interfaz:");
        System.out.println("1. Consola (Scanner)");
        System.out.println("2. Swing");
        System.out.println("3. JavaFX");
        System.out.print("Opción: ");
        String opcion = sc.nextLine().trim();

        switch (opcion) {
            case "2" -> SwingUtilities.invokeLater(() -> new VistaEstudianteSwing().setVisible(true));
            case "3" -> Application.launch(VistaEstudianteFX.class, args);
            default -> new VistaEstudiante().iniciar();
        }
    }
}