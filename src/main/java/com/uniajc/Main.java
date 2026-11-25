package com.uniajc;

import java.awt.Font;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import com.uniajc.db.DatabaseConexion;
import com.uniajc.vista.VistaLogin;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando Sistema de Gestión Académica UNIAJC...");

        try (java.sql.Connection conn = DatabaseConexion.getConnection()) {
            System.out.println("Conexión a la base de datos verificada correctamente.");
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo conectar a la base de datos: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        SwingUtilities.invokeLater(() -> {
            configurarLookAndFeel();

            VistaLogin vistaLogin = new VistaLogin();
            vistaLogin.mostrarVentana();
        });
    }

    private static void configurarLookAndFeel() {
        try {
            UIManager.put("Label.font", new FontUIResource("Segoe UI", Font.PLAIN, 12));
            UIManager.put("Button.font", new FontUIResource("Segoe UI", Font.BOLD, 12));
            UIManager.put("Table.font", new FontUIResource("Segoe UI", Font.PLAIN, 11));
        } catch (Exception ignored) {}
    }
}