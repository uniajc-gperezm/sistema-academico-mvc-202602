package com.uniajc.vista;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/** Ventana principal con menú y paneles de gestión en CardLayout. */
public class VistaPrincipalSwing extends JFrame {

    private static final String PANEL_INICIO = "inicio";
    private static final String PANEL_ESTUDIANTE = "estudiante";
    private static final String PANEL_DOCENTE = "docente";
    private static final String PANEL_MATERIA = "materia";
    private static final String PANEL_GRUPO = "grupo";
    private static final String PANEL_INSCRIPCION = "inscripcion";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contenedor = new JPanel(cardLayout);
    private final String nombreUsuario;
    private final String rol;

    public VistaPrincipalSwing(String nombreUsuario, String rol) {
        super("Sistema de Gestión Académica — UNIAJC");
        this.nombreUsuario = nombreUsuario;
        this.rol = rol;
        configurarVentana();
        inicializarMenu();
        inicializarPaneles();
    }

    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
    }

    private void inicializarMenu() {
        JMenuBar barra = new JMenuBar();
        barra.setBackground(UiUtil.PRIMARIO);
        barra.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

        JMenu menuArchivo = menu("Archivo");
        JMenuItem itemInicio = item("Inicio");
        itemInicio.addActionListener(e -> cardLayout.show(contenedor, PANEL_INICIO));
        JMenuItem itemConfiguracion = item("Configuración");
        itemConfiguracion.addActionListener(e -> mostrarConfiguracion());
        JMenuItem itemCerrarSesion = item("Cerrar Sesión");
        itemCerrarSesion.addActionListener(e -> cerrarSesion());
        JMenuItem itemSalir = item("Salir");
        itemSalir.addActionListener(e -> System.exit(0));
        menuArchivo.add(itemInicio);
        menuArchivo.add(itemConfiguracion);
        menuArchivo.addSeparator();
        menuArchivo.add(itemCerrarSesion);
        menuArchivo.add(itemSalir);
        barra.add(menuArchivo);

        barra.add(menuConAccion("Estudiante", PANEL_ESTUDIANTE, "Gestión de Estudiantes"));
        barra.add(menuConAccion("Docente", PANEL_DOCENTE, "Gestión de Docentes"));
        barra.add(menuConAccion("Materia", PANEL_MATERIA, "Gestión de Materias"));
        barra.add(menuConAccion("Grupo", PANEL_GRUPO, "Gestión de Grupos"));
        barra.add(menuConAccion("Inscripción", PANEL_INSCRIPCION, "Gestión de Inscripciones"));

        setJMenuBar(barra);
    }

    private JMenu menu(String texto) {
        JMenu m = new JMenu(texto);
        m.setFont(new Font("Segoe UI", Font.BOLD, 13));
        m.setForeground(Color.WHITE);
        return m;
    }

    private JMenuItem item(String texto) {
        JMenuItem i = new JMenuItem(texto);
        i.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return i;
    }

    private JMenu menuConAccion(String titulo, String nombrePanel, String accionTexto) {
        JMenu m = menu(titulo);
        JMenuItem i = item(accionTexto);
        i.addActionListener(e -> cardLayout.show(contenedor, nombrePanel));
        m.add(i);
        return m;
    }

    private void inicializarPaneles() {
        contenedor.setBackground(UiUtil.SECUNDARIO);
        contenedor.add(crearPanelInicio(), PANEL_INICIO);
        contenedor.add(new PanelEstudiante(), PANEL_ESTUDIANTE);
        contenedor.add(new PanelDocente(), PANEL_DOCENTE);
        contenedor.add(new PanelMateria(), PANEL_MATERIA);
        contenedor.add(new PanelGrupo(), PANEL_GRUPO);
        contenedor.add(new PanelInscripcion(), PANEL_INSCRIPCION);

        add(contenedor, BorderLayout.CENTER);
        cardLayout.show(contenedor, PANEL_INICIO);
    }

    private JPanel crearPanelInicio() {
        JPanel inicio = new JPanel(new GridLayout(3, 1, 0, 6));
        inicio.setBackground(UiUtil.SECUNDARIO);
        inicio.setBorder(BorderFactory.createEmptyBorder(60, 40, 60, 40));

        JLabel titulo = new JLabel("Sistema de Gestión Académica", javax.swing.SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(UiUtil.PRIMARIO);

        JLabel subtitulo = new JLabel("UNIAJC — Universidad Antonio José Camacho",
                javax.swing.SwingConstants.CENTER);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitulo.setForeground(UiUtil.GRIS);

        JLabel bienvenida = new JLabel("Bienvenido: " + nombreUsuario + " (" + rol + ")",
                javax.swing.SwingConstants.CENTER);
        bienvenida.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bienvenida.setForeground(UiUtil.EXITO);

        inicio.add(titulo);
        inicio.add(subtitulo);
        inicio.add(bienvenida);
        return inicio;
    }

    private void mostrarConfiguracion() {
        JDialog dialogo = new JDialog(this, "Configuración", true);
        dialogo.setSize(420, 260);
        dialogo.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(3, 2, 12, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        panel.add(UiUtil.etiqueta("Registros por página:", UiUtil.PRIMARIO));
        JComboBox<String> comboRegistros = new JComboBox<>(new String[]{"5", "10", "20", "50"});
        panel.add(comboRegistros);

        panel.add(UiUtil.etiqueta("Confirmar eliminaciones:", UiUtil.PRIMARIO));
        JComboBox<String> comboConfirmar = new JComboBox<>(new String[]{"sí", "no"});
        panel.add(comboConfirmar);

        JButton guardar = UiUtil.boton("Guardar", UiUtil.EXITO);
        guardar.addActionListener(e -> {
            JOptionPane.showMessageDialog(dialogo, "Configuración guardada.", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            dialogo.dispose();
        });
        JButton cancelar = UiUtil.boton("Cancelar", UiUtil.GRIS);
        cancelar.addActionListener(e -> dialogo.dispose());

        panel.add(guardar);
        panel.add(cancelar);

        dialogo.add(panel);
        dialogo.setVisible(true);
    }

    private void cerrarSesion() {
        dispose();
        new VistaLoginSwing().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VistaPrincipalSwing("Administrador", "ADMIN").setVisible(true));
    }
}
