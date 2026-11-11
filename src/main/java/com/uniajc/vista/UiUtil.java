package com.uniajc.vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

/** Utilidades de estilo para las vistas Swing. Paleta propia (no la de la guia). */
public final class UiUtil {

    private UiUtil() {}

    // Paleta: indigo / violeta / verde azulado / gris claro
    public static final Color PRIMARIO = new Color(69, 39, 160);      // indigo oscuro
    public static final Color PRIMARIO_CLARO = new Color(103, 58, 183); // violeta
    public static final Color SECUNDARIO = new Color(245, 245, 250);   // fondo
    public static final Color ACENTO = new Color(255, 111, 0);         // naranja
    public static final Color EXITO = new Color(0, 150, 136);          // teal
    public static final Color PELIGRO = new Color(211, 47, 47);        // rojo
    public static final Color TEXTO = new Color(38, 38, 48);
    public static final Color GRIS = new Color(120, 120, 130);

    public static final Font FUENTE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FUENTE_NEGRITA = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 20);

    public static JButton boton(String texto, Color fondo) {
        JButton b = new JButton(texto);
        b.setFont(FUENTE_NEGRITA);
        b.setBackground(fondo);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setPreferredSize(new Dimension(120, 36));
        b.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return b;
    }

    public static JLabel etiqueta(String texto, Color color) {
        JLabel l = new JLabel(texto);
        l.setFont(FUENTE_NEGRITA);
        l.setForeground(color);
        return l;
    }

    public static JTextField campo(int ancho) {
        JTextField t = new JTextField(ancho);
        t.setFont(FUENTE);
        t.setPreferredSize(new Dimension(220, 34));
        return t;
    }

    public static JTextField campo() {
        return campo(18);
    }
}
