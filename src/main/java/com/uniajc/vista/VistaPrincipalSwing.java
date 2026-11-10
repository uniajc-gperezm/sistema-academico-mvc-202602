package com.uniajc.vista;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class VistaPrincipalSwing extends JFrame {

    private static final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    private static final Color COLOR_OSCURO = new Color(31, 97, 141);
    private static final Color COLOR_SECUNDARIO = new Color(236, 240, 241);
    private static final Color COLOR_TEXTO = Color.WHITE;

    private CardLayout cardLayout;
    private JPanel panelContenedor;

    private VistaEstudianteSwing vistaEstudianteSwing;
    private VistaDocenteSwing vistaDocenteSwing;
    private VistaMateriaSwing vistaMateriaSwing;
    private VistaGrupoSwing vistaGrupoSwing;
    private VistaInscripcionSwing vistaInscripcionSwing;

    private JButton botonNavEstudiantes;
    private JButton botonNavDocentes;
    private JButton botonNavMaterias;
    private JButton botonNavGrupos;
    private JButton botonNavInscripciones;

    public VistaPrincipalSwing() {
        super("UNIAJC - Sistema de Gestión Académica (MVC)");
        configurarLookAndFeel();
        initComponents();
    }

    private void configurarLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setMinimumSize(new Dimension(850, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header superior
        add(crearHeader(), BorderLayout.NORTH);

        // Sidebar o barra de navegación
        add(crearBarraNavegacion(), BorderLayout.WEST);

        // Contenedor principal con CardLayout
        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);

        vistaEstudianteSwing = new VistaEstudianteSwing();
        vistaDocenteSwing = new VistaDocenteSwing();
        vistaMateriaSwing = new VistaMateriaSwing();
        vistaGrupoSwing = new VistaGrupoSwing();
        vistaInscripcionSwing = new VistaInscripcionSwing();

        panelContenedor.add(vistaEstudianteSwing, "ESTUDIANTES");
        panelContenedor.add(vistaDocenteSwing, "DOCENTES");
        panelContenedor.add(vistaMateriaSwing, "MATERIAS");
        panelContenedor.add(vistaGrupoSwing, "GRUPOS");
        panelContenedor.add(vistaInscripcionSwing, "INSCRIPCIONES");

        add(panelContenedor, BorderLayout.CENTER);

        // Footer inferior
        add(crearFooter(), BorderLayout.SOUTH);

        mostrarModulo("ESTUDIANTES", botonNavEstudiantes);
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_PRIMARIO);
        header.setPreferredSize(new Dimension(getWidth(), 65));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titulo = new JLabel("UNIAJC - SISTEMA DE GESTIÓN ACADÉMICA");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(COLOR_TEXTO);

        JLabel subtitulo = new JLabel("Arquitectura Modelo - Vista - Controlador");
        subtitulo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        subtitulo.setForeground(COLOR_SECUNDARIO);

        JPanel panelTexto = new JPanel(new GridLayout(2, 1));
        panelTexto.setOpaque(false);
        panelTexto.add(titulo);
        panelTexto.add(subtitulo);

        header.add(panelTexto, BorderLayout.WEST);
        return header;
    }

    private JPanel crearBarraNavegacion() {
        JPanel nav = new JPanel(new GridLayout(5, 1, 5, 5));
        nav.setBackground(COLOR_OSCURO);
        nav.setPreferredSize(new Dimension(180, getHeight()));
        nav.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        botonNavEstudiantes = crearBotonNav("Estudiantes");
        botonNavDocentes = crearBotonNav("Docentes");
        botonNavMaterias = crearBotonNav("Materias");
        botonNavGrupos = crearBotonNav("Grupos");
        botonNavInscripciones = crearBotonNav("Inscripciones");

        botonNavEstudiantes.addActionListener(e -> {
            vistaEstudianteSwing.cargarEstudiantes();
            mostrarModulo("ESTUDIANTES", botonNavEstudiantes);
        });

        botonNavDocentes.addActionListener(e -> {
            vistaDocenteSwing.cargarDocentes();
            mostrarModulo("DOCENTES", botonNavDocentes);
        });

        botonNavMaterias.addActionListener(e -> {
            vistaMateriaSwing.cargarMaterias();
            mostrarModulo("MATERIAS", botonNavMaterias);
        });

        botonNavGrupos.addActionListener(e -> {
            vistaGrupoSwing.cargarGrupos();
            mostrarModulo("GRUPOS", botonNavGrupos);
        });

        botonNavInscripciones.addActionListener(e -> {
            vistaInscripcionSwing.cargarInscripciones();
            mostrarModulo("INSCRIPCIONES", botonNavInscripciones);
        });

        nav.add(botonNavEstudiantes);
        nav.add(botonNavDocentes);
        nav.add(botonNavMaterias);
        nav.add(botonNavGrupos);
        nav.add(botonNavInscripciones);

        return nav;
    }

    private JButton crearBotonNav(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setBackground(COLOR_PRIMARIO);
        boton.setForeground(COLOR_TEXTO);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return boton;
    }

    private void mostrarModulo(String nombreCard, JButton botonActivo) {
        cardLayout.show(panelContenedor, nombreCard);

        JButton[] botones = {botonNavEstudiantes, botonNavDocentes, botonNavMaterias, botonNavGrupos, botonNavInscripciones};
        for (JButton b : botones) {
            if (b != null) {
                b.setBackground(COLOR_PRIMARIO);
            }
        }
        if (botonActivo != null) {
            botonActivo.setBackground(new Color(21, 67, 96));
        }
    }

    private JPanel crearFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 6));
        footer.setBackground(COLOR_SECUNDARIO);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(189, 195, 199)));

        JLabel labelEstado = new JLabel("Estado: Conectado | Práctica Académica UNIAJC");
        labelEstado.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        labelEstado.setForeground(new Color(127, 140, 141));

        footer.add(labelEstado);
        return footer;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VistaPrincipalSwing ventana = new VistaPrincipalSwing();
            ventana.setVisible(true);
        });
    }
}
