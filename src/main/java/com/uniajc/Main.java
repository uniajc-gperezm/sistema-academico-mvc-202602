package com.uniajc;

import java.awt.GraphicsEnvironment;
import javax.swing.SwingUtilities;

import com.uniajc.db.DatabaseConexion;
import com.uniajc.vista.VistaPrincipalConsola;
import com.uniajc.vista.VistaPrincipalSwing;

public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("     SISTEMA ACADÉMICO UNIAJC (MVC + DB)");
        System.out.println("==================================================");

        try {
            // Inicializar y verificar conexión a la base de datos
            DatabaseConexion.getConnection();
            DatabaseConexion.initDatabase();
        } catch (Exception e) {
            System.err.println("Advertencia al inicializar base de datos: " + e.getMessage());
        }

        boolean modoConsola = false;

        // Evaluar argumentos de línea de comandos
        if (args != null && args.length > 0) {
            for (String arg : args) {
                if (arg.equalsIgnoreCase("consola") || arg.equalsIgnoreCase("--consola") ||
                        arg.equalsIgnoreCase("-c") || arg.equalsIgnoreCase("scanner")) {
                    modoConsola = true;
                    break;
                }
            }
        }

        // Si el entorno no soporta interfaz gráfica, forzar modo consola
        if (GraphicsEnvironment.isHeadless() || modoConsola) {
            System.out.println("Iniciando interfaz por Consola (Scanner)...");
            VistaPrincipalConsola consola = new VistaPrincipalConsola();
            consola.iniciar();
        } else {
            System.out.println("Iniciando interfaz gráfica Swing...");
            System.out.println("(Nota: para ejecutar la versión en consola use: java -jar app.jar consola)");
            SwingUtilities.invokeLater(() -> {
                try {
                    VistaPrincipalSwing ventana = new VistaPrincipalSwing();
                    ventana.setVisible(true);
                } catch (Exception e) {
                    System.err.println("No se pudo iniciar la interfaz Swing. Cambiando a consola: " + e.getMessage());
                    VistaPrincipalConsola consola = new VistaPrincipalConsola();
                    consola.iniciar();
                }
            });
        }
    }
}
