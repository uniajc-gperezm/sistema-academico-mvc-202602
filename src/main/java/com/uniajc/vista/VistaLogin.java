package com.uniajc.vista;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;
import com.uniajc.controlador.ControladorLogin;
import com.uniajc.modelo.Usuario;

public class VistaLogin extends JFrame {

    // Degradado del fondo exterior
    private static final Color COLOR_FONDO_INICIO = new Color(28, 15, 62);
    private static final Color COLOR_FONDO_FIN = new Color(110, 24, 92);

    // Degradado suave del formulario (lila claro a rosado/coral muy tenue, sin
    // blanco cegador)
    private static final Color COLOR_TARJETA_INICIO = new Color(245, 239, 255);
    private static final Color COLOR_TARJETA_FIN = new Color(255, 241, 247);

    private static final Color COLOR_INPUT_FONDO = new Color(255, 252, 255);
    private static final Color COLOR_TEXTO_PRINCIPAL = new Color(24, 16, 46);
    private static final Color COLOR_TEXTO_SECUNDARIO = new Color(102, 88, 138);

    // Degradado del botón principal (magenta vibrante a coral/naranja estilo GTA
    // VI)
    private static final Color COLOR_PRIMARIO_INICIO = new Color(236, 72, 153);
    private static final Color COLOR_PRIMARIO_FIN = new Color(249, 115, 22);
    private static final Color COLOR_PRIMARIO_HOVER_INICIO = new Color(219, 39, 119);
    private static final Color COLOR_PRIMARIO_HOVER_FIN = new Color(234, 88, 12);

    private static final Color COLOR_SECUNDARIO_FONDO = new Color(255, 252, 255);
    private static final Color COLOR_SECUNDARIO_HOVER = new Color(252, 231, 243);
    private static final Color COLOR_BORDE = new Color(216, 198, 232);
    private static final Color COLOR_ERROR = new Color(225, 29, 72);
    private static final Color COLOR_EXITO = new Color(16, 185, 129);

    private static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 21);
    private static final Font FUENTE_SUBTITULO = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font FUENTE_ETIQUETA = new Font("SansSerif", Font.BOLD, 12);
    private static final Font FUENTE_INPUT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FUENTE_BOTON = new Font("SansSerif", Font.BOLD, 14);
    private static final Font FUENTE_BOTON_SECUNDARIO = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font FUENTE_MENSAJE = new Font("SansSerif", Font.PLAIN, 12);

    private final ControladorLogin controladorLogin;

    private JTextField txtUsername;
    private JPasswordField txtContrasena;
    private JToggleButton btnToggleContrasena;
    private JButton btnIngresar;
    private JButton btnCancelar;
    private JLabel lblMensaje;
    private char echoCharOriginal;

    private Icon iconoOjoAbierto;
    private Icon iconoOjoCerrado;

    public VistaLogin() {
        this(new ControladorLogin());
    }

    public VistaLogin(ControladorLogin controladorLogin) {
        this.controladorLogin = controladorLogin;
        inicializarUI();
    }

    public static void aplicarTemaFlatLaf() {
        try {
            FlatLightLaf.setup();
            UIManager.put("Component.arc", 10);
            UIManager.put("TextComponent.arc", 10);
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.focusColor", new Color(236, 72, 153));
            UIManager.put("Component.focusedBorderColor", new Color(236, 72, 153));
        } catch (Exception ex) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Usa el Look and Feel por defecto si falla
            }
        }
    }

    private void inicializarUI() {
        setTitle("Sistema de Gestión Académica - Inicio de Sesión");
        setSize(450, 460);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        iconoOjoAbierto = new IconoOjo(18, 18, false, COLOR_TEXTO_SECUNDARIO);
        iconoOjoCerrado = new IconoOjo(18, 18, true, COLOR_PRIMARIO_INICIO);

        // Panel de fondo con degradado
        JPanel panelFondo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(
                        0, 0, COLOR_FONDO_INICIO,
                        getWidth(), getHeight(), COLOR_FONDO_FIN);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        panelFondo.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        // Tarjeta del formulario con degradado suave
        JPanel tarjeta = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(
                        0, 0, COLOR_TARJETA_INICIO,
                        getWidth(), getHeight(), COLOR_TARJETA_FIN);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();

                setOpaque(false);
                super.paintComponent(g);
            }
        };
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setOpaque(false);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 1),
                BorderFactory.createEmptyBorder(24, 24, 24, 24)));

        // Encabezado
        JLabel lblTitulo = new JLabel("Sistema Académico UNIAJC", SwingConstants.CENTER);
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(COLOR_TEXTO_PRINCIPAL);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Inicia sesión con tu cuenta institucional", SwingConstants.CENTER);
        lblSubtitulo.setFont(FUENTE_SUBTITULO);
        lblSubtitulo.setForeground(COLOR_TEXTO_SECUNDARIO);
        lblSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(lblSubtitulo);
        tarjeta.add(Box.createVerticalStrut(20));

        // Campo usuario
        JLabel lblUsuario = crearEtiqueta("Nombre de Usuario");
        txtUsername = new JTextField();
        txtUsername.setFont(FUENTE_INPUT);
        txtUsername.setForeground(COLOR_TEXTO_PRINCIPAL);
        txtUsername.setBackground(COLOR_INPUT_FONDO);
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtUsername.setPreferredSize(new Dimension(340, 40));
        txtUsername.putClientProperty("JTextField.placeholderText", "Ingresa tu usuario");

        tarjeta.add(lblUsuario);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(txtUsername);
        tarjeta.add(Box.createVerticalStrut(14));

        // Campo contraseña + botón con ícono de ojo
        JLabel lblContrasena = crearEtiqueta("Contraseña");

        JPanel panelContrasena = new JPanel(new BorderLayout(8, 0));
        panelContrasena.setOpaque(false);
        panelContrasena.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelContrasena.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panelContrasena.setPreferredSize(new Dimension(340, 40));

        txtContrasena = new JPasswordField();
        txtContrasena.setFont(FUENTE_INPUT);
        txtContrasena.setForeground(COLOR_TEXTO_PRINCIPAL);
        txtContrasena.setBackground(COLOR_INPUT_FONDO);
        txtContrasena.putClientProperty("JTextField.placeholderText", "Ingresa tu contraseña");
        echoCharOriginal = txtContrasena.getEchoChar();

        btnToggleContrasena = new JToggleButton(iconoOjoAbierto);
        btnToggleContrasena.setBackground(COLOR_INPUT_FONDO);
        btnToggleContrasena.setFocusPainted(false);
        btnToggleContrasena.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnToggleContrasena.setPreferredSize(new Dimension(42, 40));
        btnToggleContrasena.setToolTipText("Mostrar contraseña");
        btnToggleContrasena.addActionListener(e -> alternarVisibilidadContrasena());

        panelContrasena.add(txtContrasena, BorderLayout.CENTER);
        panelContrasena.add(btnToggleContrasena, BorderLayout.EAST);

        tarjeta.add(lblContrasena);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(panelContrasena);
        tarjeta.add(Box.createVerticalStrut(8));

        // Mensaje de estado con altura fija
        lblMensaje = new JLabel(" ", SwingConstants.CENTER);
        lblMensaje.setFont(FUENTE_MENSAJE);
        lblMensaje.setForeground(COLOR_ERROR);
        lblMensaje.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblMensaje.setPreferredSize(new Dimension(340, 20));
        lblMensaje.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        tarjeta.add(lblMensaje);
        tarjeta.add(Box.createVerticalStrut(8));

        // Botón principal con degradado
        btnIngresar = new JButton("Iniciar Sesión") {
            private boolean hover = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hover = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hover = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                Color c1 = hover ? COLOR_PRIMARIO_HOVER_INICIO : COLOR_PRIMARIO_INICIO;
                Color c2 = hover ? COLOR_PRIMARIO_HOVER_FIN : COLOR_PRIMARIO_FIN;
                if (!isEnabled()) {
                    c1 = new Color(180, 140, 165);
                    c2 = new Color(190, 150, 130);
                }

                GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), 0, c2);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();

                setContentAreaFilled(false);
                super.paintComponent(g);
            }
        };
        btnIngresar.setFont(FUENTE_BOTON);
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setBorderPainted(false);
        btnIngresar.setContentAreaFilled(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIngresar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnIngresar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnIngresar.setPreferredSize(new Dimension(340, 42));

        // Botón cancelar
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(FUENTE_BOTON_SECUNDARIO);
        btnCancelar.setBackground(COLOR_SECUNDARIO_FONDO);
        btnCancelar.setForeground(COLOR_PRIMARIO_HOVER_INICIO);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCancelar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnCancelar.setPreferredSize(new Dimension(340, 38));

        btnCancelar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btnCancelar.isEnabled()) {
                    btnCancelar.setBackground(COLOR_SECUNDARIO_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnCancelar.setBackground(COLOR_SECUNDARIO_FONDO);
            }
        });

        tarjeta.add(btnIngresar);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(btnCancelar);

        panelFondo.add(tarjeta, BorderLayout.CENTER);
        add(panelFondo);

        // Eventos
        btnIngresar.addActionListener(e -> capturarCredenciales());
        btnCancelar.addActionListener(e -> dispose());
        txtUsername.addActionListener(e -> txtContrasena.requestFocusInWindow());
        txtContrasena.addActionListener(e -> capturarCredenciales());
    }

    private void alternarVisibilidadContrasena() {
        boolean mostrar = btnToggleContrasena.isSelected();
        txtContrasena.setEchoChar(mostrar ? (char) 0 : echoCharOriginal);
        btnToggleContrasena.setIcon(mostrar ? iconoOjoCerrado : iconoOjoAbierto);
        btnToggleContrasena.setToolTipText(mostrar ? "Ocultar contraseña" : "Mostrar contraseña");
        txtContrasena.requestFocusInWindow();
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FUENTE_ETIQUETA);
        label.setForeground(COLOR_TEXTO_PRINCIPAL);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    public void capturarCredenciales() {
        String username = txtUsername.getText().trim();
        String password = new String(txtContrasena.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            mostrarError("Por favor, ingrese usuario y contraseña.");
            return;
        }

        try {
            btnIngresar.setEnabled(false);
            btnCancelar.setEnabled(false);
            btnIngresar.setText("Verificando...");

            Usuario usuario = controladorLogin.iniciarSesion(username, password);

            mostrarExito("¡Bienvenido, " + usuario.getNombre() + " (" + usuario.getRol() + ")!");

            SwingUtilities.invokeLater(() -> {
                if ("ESTUDIANTE".equalsIgnoreCase(usuario.getRol())) {
                    VistaPortalEstudianteSwing portalEstudiante = new VistaPortalEstudianteSwing(usuario);
                    portalEstudiante.setVisible(true);
                } else {
                    VistaPrincipalSwing vistaPrincipal = new VistaPrincipalSwing();
                    vistaPrincipal.setVisible(true);
                }
                dispose();
            });

        } catch (IllegalArgumentException | IllegalStateException ex) {
            mostrarError(ex.getMessage());
        } catch (Exception ex) {
            mostrarError("Error al iniciar sesión: " + ex.getMessage());
        } finally {
            btnIngresar.setEnabled(true);
            btnCancelar.setEnabled(true);
            btnIngresar.setText("Iniciar Sesión");
        }
    }

    public void mostrarError(String mensaje) {
        lblMensaje.setForeground(COLOR_ERROR);
        lblMensaje.setText(mensaje);
    }

    public void mostrarExito(String mensaje) {
        lblMensaje.setForeground(COLOR_EXITO);
        lblMensaje.setText(mensaje);
    }

    public void limpiarCampos() {
        if (txtUsername != null) {
            txtUsername.setText("");
        }
        if (txtContrasena != null) {
            txtContrasena.setText("");
            txtContrasena.setEchoChar(echoCharOriginal);
        }
        if (btnToggleContrasena != null) {
            btnToggleContrasena.setSelected(false);
            btnToggleContrasena.setIcon(iconoOjoAbierto);
            btnToggleContrasena.setToolTipText("Mostrar contraseña");
        }
        if (lblMensaje != null) {
            lblMensaje.setText(" ");
        }
    }

    public void mostrarVentana() {
        SwingUtilities.invokeLater(() -> {
            limpiarCampos();
            setVisible(true);
            if (txtUsername != null) {
                txtUsername.requestFocusInWindow();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            aplicarTemaFlatLaf();
            new VistaLogin().mostrarVentana();
        });
    }

    private static class IconoOjo implements Icon {
        private final int width;
        private final int height;
        private final boolean tachado;
        private final Color color;

        IconoOjo(int width, int height, boolean tachado, Color color) {
            this.width = width;
            this.height = height;
            this.tachado = tachado;
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            int left = x + 1;
            int right = x + width - 1;
            int centerY = y + height / 2;
            int curvaY = Math.max(2, height / 4);

            g2.drawArc(left, centerY - curvaY, right - left, curvaY * 2, 0, 180);
            g2.drawArc(left, centerY - curvaY, right - left, curvaY * 2, 180, 180);

            int radioPupila = 4;
            g2.fillOval(x + (width - radioPupila) / 2, y + (height - radioPupila) / 2, radioPupila, radioPupila);

            if (tachado) {
                g2.drawLine(left + 1, y + height - 2, right - 1, y + 2);
            }

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return width;
        }

        @Override
        public int getIconHeight() {
            return height;
        }
    }
}