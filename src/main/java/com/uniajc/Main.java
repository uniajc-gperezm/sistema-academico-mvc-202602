package com.uniajc;

import com.uniajc.vista.VistaAcceso;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Muestra el acceso primero; la conexión se solicita al abrir el módulo académico.
        SwingUtilities.invokeLater(() -> new VistaAcceso().mostrar());
    }
}
