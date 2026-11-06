package com.uniajc;

import java.util.Scanner;

import javax.swing.SwingUtilities;

import com.uniajc.vista.VistaEstudiante;
import com.uniajc.vista.VistaEstudianteSwing;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Seleccione la interfaz:");
        System.out.println("1. Consola (Scanner)");
        System.out.println("2. Swing");
        System.out.print("Opción: ");
        String opcion = sc.nextLine().trim();

        if (opcion.equals("2")) {
            SwingUtilities.invokeLater(() -> new VistaEstudianteSwing().setVisible(true));
        } else {
            new VistaEstudiante().iniciar();
        }
    }
}