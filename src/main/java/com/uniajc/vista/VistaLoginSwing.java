package com.uniajc.vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.uniajc.dao.UsuarioDao;
import com.uniajc.modelo.Usuario;
import com.uniajc.servicios.UsuarioService;

/** Ventana de login. Autentica contra la base de datos (tabla users). */
public class VistaLoginSwing extends JFrame {

    private final JTextField campoUsuario = new JTextField();
    private final JPasswordField campoPassword = new JPasswordField();
    private final UsuarioService usuarioService = new UsuarioService(new UsuarioDao());

    public VistaLoginSwing() {
        super("Sistema Académico UNIAJC — Inicio de sesión");
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(460, 320);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void inicializarComponentes() {
        JPanel fondo = new JPanel(new BorderLayout());
        fondo.setBackground(UiUtil.SECUNDARIO);

        JLabel titulo = new JLabel("Sistema Académico UNIAJC", javax.swing.SwingConstants.CENTER);
        titulo.setFont(UiUtil.FUENTE_TITULO);
        titulo.setForeground(UiUtil.PRIMARIO);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        fondo.add(titulo, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(java.awt.Color.WHITE);
        formulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiUtil.PRIMARIO_CLARO, 1),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formulario.add(UiUtil.etiqueta("Usuario:", UiUtil.PRIMARIO), gbc);
        gbc.gridx = 1;
        formulario.add(campoUsuario, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        formulario.add(UiUtil.etiqueta("Contraseña:", UiUtil.PRIMARIO), gbc);
        gbc.gridx = 1;
        formulario.add(campoPassword, gbc);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        botones.setBackground(java.awt.Color.WHITE);

        JButton botonIniciar = UiUtil.boton("Iniciar Sesión", UiUtil.EXITO);
        botonIniciar.addActionListener(e -> intentarLogin());

        JButton botonCancelar = UiUtil.boton("Cancelar", UiUtil.GRIS);
        botonCancelar.addActionListener(e -> System.exit(0));

        botones.add(botonIniciar);
        botones.add(botonCancelar);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        formulario.add(botones, gbc);

        fondo.add(formulario, BorderLayout.CENTER);
        add(fondo);

        campoUsuario.addActionListener(e -> intentarLogin());
        campoPassword.addActionListener(e -> intentarLogin());
    }

    private void intentarLogin() {
        String usuario = campoUsuario.getText().trim();
        String password = new String(campoPassword.getPassword()).trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese usuario y contraseña.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Optional<Usuario> resultado = usuarioService.autenticar(usuario, password);
            if (resultado.isPresent()) {
                Usuario u = resultado.get();
                dispose();
                new VistaPrincipalSwing(u.getNombre(), u.getRol().name()).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales inválidas.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VistaLoginSwing().setVisible(true));
    }
}