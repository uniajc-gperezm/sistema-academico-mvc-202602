package com.uniajc;

import javax.swing.SwingUtilities;

import com.uniajc.vista.VistaLoginSwing;

/** Punto de entrada de la aplicación. */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VistaLoginSwing().setVisible(true));
    }
}
