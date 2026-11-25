package com.uniajc.vista;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.function.Consumer;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class VistaPrincipalSwing extends JFrame {

    // Degradado principal
    private static final Color COLOR_DEGRADADO_INICIO = new Color(28, 15, 62);
    private static final Color COLOR_DEGRADADO_FIN = new Color(110, 24, 92);

    // Degradado suave para el área central de bienvenida
    private static final Color COLOR_BIENVENIDA_INICIO = new Color(245, 239, 255);
    private static final Color COLOR_BIENVENIDA_FIN = new Color(255, 241, 247);

    // Colores del menú lateral y acentos
    private static final Color COLOR_FONDO_MENU = new Color(248, 244, 254);
    private static final Color COLOR_HOVER = new Color(252, 231, 243);
    private static final Color COLOR_ACTIVO = new Color(244, 214, 236);
    private static final Color COLOR_BORDE_MENU = new Color(216, 198, 232);
    private static final Color COLOR_TEXTO_OSCURO = new Color(24, 16, 46);
    private static final Color COLOR_ACENTO_MAGENTA = new Color(219, 39, 119);
    private static final Color COLOR_CERRAR_SESION = new Color(225, 29, 72);

    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final MenuLateralDrawer menuLateral;

    public VistaPrincipalSwing() {
        setTitle("Sistema academico UNIAJC");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Panel de bienvenida
        JPanel panelBienvenida = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(
                        0, 0, COLOR_BIENVENIDA_INICIO,
                        getWidth(), getHeight(), COLOR_BIENVENIDA_FIN);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };

        JLabel labelBienvenida = new JLabel(
                "<html><center>"
                        + "<h1 style='color:#18102E; margin-bottom:8px;'>Bienvenido al Sistema academico UNIAJC</h1>"
                        + "<p style='color:#DB2777;'>Abre el menú en la esquina superior izquierda para navegar entre módulos</p>"
                        + "</center></html>",
                SwingConstants.CENTER);
        labelBienvenida.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        labelBienvenida.setForeground(COLOR_TEXTO_OSCURO);
        panelBienvenida.add(labelBienvenida, BorderLayout.CENTER);

        // Módulos Swing
        VistaEstudianteSwing vistaEstudianteSwing = new VistaEstudianteSwing();
        VistaDocenteSwing vistaDocenteSwing = new VistaDocenteSwing();
        VistaMateriaSwing vistaMateriaSwing = new VistaMateriaSwing();
        VistaGrupoSwing vistaGrupoSwing = new VistaGrupoSwing();
        VistaInscripcionSwing vistaInscripcionSwing = new VistaInscripcionSwing();
        VistaCalificacionSwing vistaCalificacionSwing = new VistaCalificacionSwing();
        VistaUsuarioSwing vistaUsuarioSwing = new VistaUsuarioSwing();

        mainPanel.add(panelBienvenida, "INICIO");
        mainPanel.add(vistaEstudianteSwing, "ESTUDIANTES");
        mainPanel.add(vistaDocenteSwing, "DOCENTES");
        mainPanel.add(vistaMateriaSwing, "MATERIAS");
        mainPanel.add(vistaGrupoSwing, "GRUPOS");
        mainPanel.add(vistaInscripcionSwing, "INSCRIPCIONES");
        mainPanel.add(vistaCalificacionSwing, "CALIFICACIONES");
        mainPanel.add(vistaUsuarioSwing, "USUARIOS");

        // Menú lateral desplegable
        menuLateral = new MenuLateralDrawer(
                this::navegarAModulo,
                () -> alternarMenuLateral(false));
        menuLateral.setVisible(false);

        add(crearBarraSuperior(), BorderLayout.NORTH);
        add(menuLateral, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel crearBarraSuperior() {
        JPanel topBar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(
                        0, 0, COLOR_DEGRADADO_INICIO,
                        getWidth(), 0, COLOR_DEGRADADO_FIN);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        topBar.setPreferredSize(new Dimension(0, 52));
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));

        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        izquierda.setOpaque(false);

        JButton btnHamburguesa = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.setStroke(new java.awt.BasicStroke(2.2f, java.awt.BasicStroke.CAP_ROUND,
                        java.awt.BasicStroke.JOIN_ROUND));

                int w = getWidth();
                int h = getHeight();
                int anchoLinea = 18;
                int x1 = (w - anchoLinea) / 2;
                int x2 = x1 + anchoLinea;
                int centroY = h / 2;

                g2.drawLine(x1, centroY - 6, x2, centroY - 6);
                g2.drawLine(x1, centroY, x2, centroY);
                g2.drawLine(x1, centroY + 6, x2, centroY + 6);
                g2.dispose();
            }
        };
        btnHamburguesa.setPreferredSize(new Dimension(34, 34));
        btnHamburguesa.setFocusPainted(false);
        btnHamburguesa.setContentAreaFilled(false);
        btnHamburguesa.setBorderPainted(false);
        btnHamburguesa.setOpaque(false);
        btnHamburguesa.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHamburguesa.setToolTipText("Abrir o cerrar menú");
        btnHamburguesa.addActionListener(e -> alternarMenuLateral(!menuLateral.isVisible()));

        JLabel lblTitulo = new JLabel("Sistema academico UNIAJC");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);

        izquierda.add(btnHamburguesa);
        izquierda.add(lblTitulo);

        topBar.add(izquierda, BorderLayout.WEST);
        return topBar;
    }

    private ImageIcon tintarIconoBlanco(Image origen) {
        int w = origen.getWidth(null);
        int h = origen.getHeight(null);
        if (w <= 0 || h <= 0) {
            return new ImageIcon(origen);
        }
        java.awt.image.BufferedImage tinted = new java.awt.image.BufferedImage(
                w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = tinted.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(origen, 0, 0, null);
        g2.setComposite(java.awt.AlphaComposite.SrcIn);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, w, h);
        g2.dispose();
        return new ImageIcon(tinted);
    }

    private void alternarMenuLateral(boolean mostrar) {
        menuLateral.setVisible(mostrar);
        revalidate();
        repaint();
    }

    private void navegarAModulo(String opcion) {
        switch (opcion) {
            case "Inicio" -> cardLayout.show(mainPanel, "INICIO");
            case "Estudiantes" -> cardLayout.show(mainPanel, "ESTUDIANTES");
            case "Docentes" -> cardLayout.show(mainPanel, "DOCENTES");
            case "Materias" -> cardLayout.show(mainPanel, "MATERIAS");
            case "Grupos" -> cardLayout.show(mainPanel, "GRUPOS");
            case "Inscripciones" -> cardLayout.show(mainPanel, "INSCRIPCIONES");
            case "Calificaciones" -> cardLayout.show(mainPanel, "CALIFICACIONES");
            case "Usuarios" -> cardLayout.show(mainPanel, "USUARIOS");
            case "Cerrar sesión" -> cerrarSesion();
            default -> cardLayout.show(mainPanel, "INICIO");
        }
        if (!"Cerrar sesión".equals(opcion)) {
            menuLateral.marcarSeleccionada(opcion);
            alternarMenuLateral(false);
        }
    }

    private ImageIcon obtenerIconoCerrarSesion() {
        URL iconUrl = getClass().getResource("/recursos/cerrar-sesion.png");
        if (iconUrl != null) {
            ImageIcon rawIcon = new ImageIcon(iconUrl);
            Image scaledImage = rawIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        return null;
    }

    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas cerrar sesión?",
                "Confirmar cierre de sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                VistaLogin.aplicarTemaFlatLaf();
                VistaLogin login = new VistaLogin();
                login.mostrarVentana();
            });
        }
    }

    private static class MenuLateralDrawer extends JPanel {

        private final Consumer<String> onSeleccionar;
        private final Runnable onCerrar;
        private java.util.Map<String, JPanel> filas = new java.util.HashMap<>();
        private java.util.Map<String, JLabel> etiquetas = new java.util.HashMap<>();
        private String opcionActiva = "Inicio";

        MenuLateralDrawer(Consumer<String> onSeleccionar, Runnable onCerrar) {
            this.onSeleccionar = onSeleccionar;
            this.onCerrar = onCerrar;
            initComponents();
        }

        private void initComponents() {
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(280, 600));
            setBackground(COLOR_FONDO_MENU);
            setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, COLOR_BORDE_MENU));

            JPanel header = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    GradientPaint gp = new GradientPaint(
                            0, 0, COLOR_DEGRADADO_INICIO,
                            getWidth(), 0, COLOR_DEGRADADO_FIN);
                    g2.setPaint(gp);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.dispose();
                }
            };
            header.setPreferredSize(new Dimension(280, 52));
            header.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 10));

            JLabel lblTitulo = new JLabel("Sistema academico UNIAJC");
            lblTitulo.setForeground(Color.WHITE);
            lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));

            JButton btnCerrar = new JButton("X");
            btnCerrar.setForeground(Color.WHITE);
            btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btnCerrar.setFocusPainted(false);
            btnCerrar.setBorderPainted(false);
            btnCerrar.setContentAreaFilled(false);
            btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnCerrar.addActionListener(e -> {
                if (onCerrar != null) {
                    onCerrar.run();
                }
            });

            header.add(lblTitulo, BorderLayout.WEST);
            header.add(btnCerrar, BorderLayout.EAST);
            add(header, BorderLayout.NORTH);

            JPanel lista = new JPanel();
            lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
            lista.setBackground(COLOR_FONDO_MENU);

            String[] opciones = {
                    "Inicio",
                    "Estudiantes",
                    "Docentes",
                    "Materias",
                    "Grupos",
                    "Inscripciones",
                    "Calificaciones",
                    "Usuarios",
                    "Cerrar sesión"
            };

            for (String opcion : opciones) {
                lista.add(crearFila(opcion));
                JSeparator separador = new JSeparator();
                separador.setForeground(COLOR_BORDE_MENU);
                lista.add(separador);
            }
            lista.add(Box.createVerticalGlue());

            JScrollPane scroll = new JScrollPane(lista);
            scroll.setBorder(null);
            scroll.getVerticalScrollBar().setUnitIncrement(12);
            add(scroll, BorderLayout.CENTER);

            actualizarEstilosFilas();
        }

        void marcarSeleccionada(String opcion) {
            if (!"Cerrar sesión".equals(opcion)) {
                this.opcionActiva = opcion;
                actualizarEstilosFilas();
            }
        }

        private void actualizarEstilosFilas() {
            for (Map.Entry<String, JPanel> entry : filas.entrySet()) {
                String texto = entry.getKey();
                JPanel fila = entry.getValue();
                JLabel lblTexto = etiquetas.get(texto);
                boolean esCerrarSesion = "Cerrar sesión".equals(texto);
                boolean activa = !esCerrarSesion && texto.equals(opcionActiva);

                fila.setBackground(activa ? COLOR_ACTIVO : COLOR_FONDO_MENU);
                if (lblTexto != null) {
                    lblTexto.setFont(new Font(
                            "Segoe UI",
                            (esCerrarSesion || activa) ? Font.BOLD : Font.PLAIN,
                            13));
                    lblTexto.setForeground(
                            esCerrarSesion ? COLOR_CERRAR_SESION
                                    : (activa ? COLOR_ACENTO_MAGENTA : COLOR_TEXTO_OSCURO));
                }
            }
        }

        private JPanel crearFila(String texto) {
            JPanel fila = new JPanel(new BorderLayout());
            fila.setBackground(COLOR_FONDO_MENU);
            fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
            fila.setPreferredSize(new Dimension(260, 46));
            fila.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
            fila.setCursor(new Cursor(Cursor.HAND_CURSOR));

            boolean esCerrarSesion = "Cerrar sesión".equals(texto);

            JLabel lblTexto = new JLabel(texto, SwingConstants.LEFT);
            lblTexto.setFont(new Font("Segoe UI", esCerrarSesion ? Font.BOLD : Font.PLAIN, 13));
            lblTexto.setForeground(esCerrarSesion ? COLOR_CERRAR_SESION : COLOR_TEXTO_OSCURO);

            if (esCerrarSesion) {
                URL iconUrl = getClass().getResource("/recursos/cerrar-sesion.png");
                if (iconUrl != null) {
                    ImageIcon rawIcon = new ImageIcon(iconUrl);
                    Image scaledImage = rawIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                    lblTexto.setIcon(new ImageIcon(scaledImage));
                    lblTexto.setIconTextGap(8);
                }
            }

            JLabel lblFlecha = new JLabel(">");
            lblFlecha.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblFlecha.setForeground(esCerrarSesion ? COLOR_CERRAR_SESION : COLOR_ACENTO_MAGENTA);

            fila.add(lblTexto, BorderLayout.WEST);
            fila.add(lblFlecha, BorderLayout.EAST);

            filas.put(texto, fila);
            etiquetas.put(texto, lblTexto);

            fila.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!texto.equals(opcionActiva)) {
                        fila.setBackground(COLOR_HOVER);
                    }
                    if (!esCerrarSesion) {
                        lblTexto.setForeground(COLOR_ACENTO_MAGENTA);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    actualizarEstilosFilas();
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (onSeleccionar != null) {
                        onSeleccionar.accept(texto);
                    }
                }
            });

            return fila;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VistaLogin.aplicarTemaFlatLaf();
            VistaPrincipalSwing frame = new VistaPrincipalSwing();
            frame.setVisible(true);
        });
    }
}