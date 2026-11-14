package com.uniajc.vista;

import com.uniajc.controlador.ControladorAcceso;
import com.uniajc.modelo.Usuario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/** Pantalla de bienvenida y acceso previo al módulo académico. */
public class VistaAcceso {
    private static final Color NEGRO = new Color(17, 17, 17);
    private static final Color GRIS = new Color(110, 110, 110);
    private static final Color BORDE = new Color(220, 220, 220);

    private final JFrame ventana = new JFrame("UNIAJC | Sistema académico");
    private final JTextField usuario = new JTextField();
    private final JPasswordField contrasena = new JPasswordField();
    private final ControladorAcceso controlador = new ControladorAcceso();

    public VistaAcceso() {
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(900, 570);
        ventana.setMinimumSize(new Dimension(780, 500));
        ventana.setLocationRelativeTo(null);

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(Color.WHITE);
        raiz.add(crearPresentacion(), BorderLayout.CENTER);
        raiz.add(crearFormulario(), BorderLayout.EAST);
        ventana.setContentPane(raiz);
    }

    public void mostrar() {
        ventana.setVisible(true);
    }

    private JPanel crearPresentacion() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(NEGRO);
        panel.setBorder(BorderFactory.createEmptyBorder(42, 48, 42, 42));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        JLabel marca = texto("UNIAJC  /  ACADÉMICO", 13, Font.BOLD, Color.WHITE);
        c.gridy = 0;
        c.insets = new Insets(0, 0, 70, 0);
        panel.add(marca, c);

        JLabel sobrelinea = texto("SISTEMA DE GESTIÓN", 12, Font.BOLD, new Color(180, 180, 180));
        c.gridy++;
        c.insets = new Insets(0, 0, 12, 0);
        panel.add(sobrelinea, c);

        JLabel titulo = texto("<html>Tu vida<br>académica,<br>en un solo lugar.</html>",
                38, Font.BOLD, Color.WHITE);
        c.gridy++;
        c.insets = new Insets(0, 0, 22, 0);
        panel.add(titulo, c);

        JLabel detalle = texto("<html>Consulta y administra la información<br>de estudiantes de forma sencilla.</html>", 15,
                Font.PLAIN, new Color(195, 195, 195));
        c.gridy++;
        c.insets = new Insets(0, 0, 0, 0);
        panel.add(detalle, c);

        JLabel pie = texto("PLATAFORMA ACADÉMICA  •  UNIAJC", 10, Font.BOLD, new Color(150, 150, 150));
        c.gridy = 4;
        c.anchor = GridBagConstraints.SOUTHWEST;
        c.weighty = 1;
        panel.add(pie, c);
        return panel;
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(370, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(42, 38, 36, 38));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;

        JLabel etiqueta = texto("BIENVENIDO", 11, Font.BOLD, GRIS);
        c.gridy = 0;
        c.insets = new Insets(0, 0, 10, 0);
        panel.add(etiqueta, c);
        JLabel titulo = texto("Iniciar sesión", 27, Font.BOLD, NEGRO);
        c.gridy++;
        c.insets = new Insets(0, 0, 8, 0);
        panel.add(titulo, c);
        JLabel descripcion = texto("Ingresa tus datos para continuar.", 13, Font.PLAIN, GRIS);
        c.gridy++;
        c.insets = new Insets(0, 0, 28, 0);
        panel.add(descripcion, c);

        c.gridy++;
        c.insets = new Insets(0, 0, 7, 0);
        panel.add(texto("Usuario", 12, Font.BOLD, NEGRO), c);
        prepararCampo(usuario);
        c.gridy++;
        c.insets = new Insets(0, 0, 18, 0);
        panel.add(usuario, c);

        c.gridy++;
        c.insets = new Insets(0, 0, 7, 0);
        panel.add(texto("Contraseña", 12, Font.BOLD, NEGRO), c);
        prepararCampo(contrasena);
        c.gridy++;
        c.insets = new Insets(0, 0, 22, 0);
        panel.add(contrasena, c);

        JButton entrar = new JButton("Entrar al sistema");
        entrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        entrar.setForeground(Color.WHITE);
        entrar.setBackground(NEGRO);
        entrar.setFocusPainted(false);
        entrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        entrar.setBorder(BorderFactory.createEmptyBorder(13, 16, 13, 16));
        entrar.addActionListener(event -> iniciarSesion());
        usuario.addActionListener(event -> iniciarSesion());
        contrasena.addActionListener(event -> iniciarSesion());
        c.gridy++;
        c.insets = new Insets(0, 0, 18, 0);
        panel.add(entrar, c);

        JLabel aviso = texto("<html><center>Ingresa con una cuenta registrada<br>"
                        + "en el sistema académico.</center></html>",
                11, Font.PLAIN, GRIS);
        aviso.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridy++;
        c.insets = new Insets(0, 0, 0, 0);
        panel.add(aviso, c);
        return panel;
    }

    private void iniciarSesion() {
        char[] clave = contrasena.getPassword();
        if (usuario.getText().isBlank() || clave.length == 0) {
            JOptionPane.showMessageDialog(ventana, "Escribe tu usuario y contraseña para continuar.",
                    "Datos requeridos", JOptionPane.INFORMATION_MESSAGE);
            Arrays.fill(clave, '\0');
            contrasena.setText("");
            return;
        }

        try {
            Optional<Usuario> autenticado = controlador.autenticar(usuario.getText(), clave);
            if (autenticado.isEmpty()) {
                JOptionPane.showMessageDialog(ventana, "Usuario o contraseña incorrectos, o cuenta inactiva.",
                        "No se pudo iniciar sesión", JOptionPane.WARNING_MESSAGE);
                contrasena.setText("");
                return;
            }
            VistaAcademica vista = new VistaAcademica(autenticado.get());
            vista.mostrar();
            ventana.dispose();
        } catch (RuntimeException error) {
            JOptionPane.showMessageDialog(ventana,
                    "No se pudo abrir el sistema académico: " + error.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        } finally {
            Arrays.fill(clave, '\0');
            contrasena.setText("");
        }
    }

    private static void prepararCampo(JTextField campo) {
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(250, 42));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE), BorderFactory.createEmptyBorder(0, 11, 0, 11)));
    }

    private static JLabel texto(String valor, int tamano, int estilo, Color color) {
        JLabel etiqueta = new JLabel(valor);
        etiqueta.setFont(new Font("SansSerif", estilo, tamano));
        etiqueta.setForeground(color);
        return etiqueta;
    }
}
