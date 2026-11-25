package com.uniajc.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class MenuLateralUNIAJC extends JPanel {

    private static final Color COLOR_HEADER = new Color(232, 163, 23); // Dorado/ámbar tipo referencia
    private static final Color COLOR_FONDO_MENU = new Color(226, 226, 226);
    private static final Color COLOR_HOVER = new Color(210, 210, 210);
    private static final Color COLOR_TEXTO_OSCURO = new Color(25, 25, 25);

    private final Consumer<String> onSeleccionarModulo;
    private final Runnable onCerrarMenu;

    public MenuLateralUNIAJC(Consumer<String> onSeleccionarModulo, Runnable onCerrarMenu) {
        this.onSeleccionarModulo = onSeleccionarModulo;
        this.onCerrarMenu = onCerrarMenu;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(280, 600));
        setBackground(COLOR_FONDO_MENU);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(180, 180, 180)));

        // 1. Encabezado superior con título y botón "X"
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_HEADER);
        panelHeader.setPreferredSize(new Dimension(280, 56));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 10));

        JLabel lblTitulo = new JLabel("Sistema academico UNIAJC");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JButton btnCerrar = new JButton("X");
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBackground(COLOR_HEADER);
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> {
            if (onCerrarMenu != null) {
                onCerrarMenu.run();
            }
        });

        panelHeader.add(lblTitulo, BorderLayout.WEST);
        panelHeader.add(btnCerrar, BorderLayout.EAST);
        add(panelHeader, BorderLayout.NORTH);

        // 2. Cuerpo del menú con opciones académicas
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(COLOR_FONDO_MENU);

        // Barra superior secundaria de accesos rápidos (similar a la referencia)
        JPanel panelAccesosRapidos = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 10));
        panelAccesosRapidos.setBackground(COLOR_FONDO_MENU);
        JLabel lblInicio = new JLabel("Módulos");
        lblInicio.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblInicio.setForeground(COLOR_TEXTO_OSCURO);
        panelAccesosRapidos.add(lblInicio);
        panelContenido.add(panelAccesosRapidos);
        panelContenido.add(new JSeparator());

        String[] opciones = {
            "Estudiantes",
            "Docentes",
            "Programas",
            "Periodos Académicos",
            "Materias",
            "Grupos",
            "Inscripciones",
            "Calificaciones"
        };

        for (String opcion : opciones) {
            panelContenido.add(crearItemMenu(opcion));
            panelContenido.add(new JSeparator());
        }

        JScrollPane scrollPane = new JScrollPane(panelContenido);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel crearItemMenu(String texto) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(COLOR_FONDO_MENU);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        item.setPreferredSize(new Dimension(260, 46));
        item.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblTexto = new JLabel(texto, SwingConstants.LEFT);
        lblTexto.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTexto.setForeground(COLOR_TEXTO_OSCURO);

        JLabel lblFlecha = new JLabel(">");
        lblFlecha.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFlecha.setForeground(COLOR_TEXTO_OSCURO);

        item.add(lblTexto, BorderLayout.WEST);
        item.add(lblFlecha, BorderLayout.EAST);

        // Efecto hover y clic
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                item.setBackground(COLOR_HOVER);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                item.setBackground(COLOR_FONDO_MENU);
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (onSeleccionarModulo != null) {
                    onSeleccionarModulo.accept(texto);
                }
                if (onCerrarMenu != null) {
                    onCerrarMenu.run();
                }
            }
        });

        return item;
    }
}