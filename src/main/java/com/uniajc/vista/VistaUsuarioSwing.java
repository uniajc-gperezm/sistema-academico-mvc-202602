package com.uniajc.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.uniajc.controlador.ControladorUsuario;
import com.uniajc.modelo.Estudiante;
import com.uniajc.modelo.Usuario;
import com.uniajc.servicios.EstudianteService;

public class VistaUsuarioSwing extends JPanel implements ActionListener {

    private static final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    private static final Color COLOR_SECUNDARIO = new Color(236, 240, 241);
    private static final Color COLOR_ACENTO = new Color(231, 76, 60);
    private static final Color COLOR_EXITO = new Color(39, 174, 96);

    private static final String[] COLUMNAS = {
            "ID", "Username", "Nombre Completo", "Rol", "ID Estudiante", "Email", "Estado", "Último Acceso"
    };

    private static final String[] ROLES = {"ADMIN", "ESTUDIANTE", "DOCENTE", "USUARIO"};

    private final ControladorUsuario controladorUsuario;
    private final EstudianteService estudianteService;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;

    private JButton btnNuevo;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnRefrescar;

    public VistaUsuarioSwing() {
        this.controladorUsuario = new ControladorUsuario();
        this.estudianteService = new EstudianteService();
        inicializarUI();
        cargarUsuarios("");
    }

    private void inicializarUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_SECUNDARIO);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 1. Panel Superior (Título y Buscador)
        JPanel panelNorte = new JPanel(new BorderLayout(8, 8));
        panelNorte.setBackground(COLOR_SECUNDARIO);

        JLabel lblTitulo = new JLabel("Administración y Creación de Usuarios");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_PRIMARIO);

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelFiltro.setBackground(COLOR_SECUNDARIO);

        JLabel lblBuscar = new JLabel("Buscar (usuario / nombre / rol):");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtBuscar = new JTextField(20);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                cargarUsuarios(txtBuscar.getText().trim());
            }
        });

        btnRefrescar = crearBoton("Refrescar", COLOR_PRIMARIO);
        btnRefrescar.addActionListener(this);

        panelFiltro.add(lblBuscar);
        panelFiltro.add(txtBuscar);
        panelFiltro.add(btnRefrescar);

        panelNorte.add(lblTitulo, BorderLayout.NORTH);
        panelNorte.add(panelFiltro, BorderLayout.SOUTH);
        add(panelNorte, BorderLayout.NORTH);

        // 2. Panel Central (Tabla de Usuarios)
        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(26);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(COLOR_PRIMARIO);
        tabla.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollTabla = new JScrollPane(tabla);
        add(scrollTabla, BorderLayout.CENTER);

        // 3. Panel Inferior (Botones CRUD)
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        panelSur.setBackground(COLOR_SECUNDARIO);

        btnNuevo = crearBoton("Crear Usuario", COLOR_EXITO);
        btnEditar = crearBoton("Modificar", COLOR_PRIMARIO);
        btnEliminar = crearBoton("Eliminar", COLOR_ACENTO);

        btnNuevo.addActionListener(this);
        btnEditar.addActionListener(this);
        btnEliminar.addActionListener(this);

        panelSur.add(btnNuevo);
        panelSur.add(btnEditar);
        panelSur.add(btnEliminar);
        add(panelSur, BorderLayout.SOUTH);
    }

    public void cargarUsuarios(String filtro) {
        modeloTabla.setRowCount(0);
        try {
            List<Usuario> lista = controladorUsuario.listarUsuarios();
            String f = filtro != null ? filtro.toLowerCase() : "";

            for (Usuario u : lista) {
                boolean coincide = f.isEmpty()
                        || u.getUsername().toLowerCase().contains(f)
                        || (u.getNombre() != null && u.getNombre().toLowerCase().contains(f))
                        || (u.getRol() != null && u.getRol().toLowerCase().contains(f))
                        || (u.getEmail() != null && u.getEmail().toLowerCase().contains(f));

                if (coincide) {
                    modeloTabla.addRow(new Object[]{
                            u.getId(),
                            u.getUsername(),
                            u.getNombre(),
                            u.getRol(),
                            u.getEstudianteId() != null && u.getEstudianteId() > 0 ? "Estudiante #" + u.getEstudianteId() : "-",
                            u.getEmail() != null ? u.getEmail() : "-",
                            u.isActivo() ? "Activo" : "Inactivo",
                            u.getUltimoAcceso() != null ? u.getUltimoAcceso().toString() : "Sin acceso"
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRefrescar) {
            txtBuscar.setText("");
            cargarUsuarios("");
        } else if (e.getSource() == btnNuevo) {
            mostrarDialogoUsuario(null);
        } else if (e.getSource() == btnEditar) {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un usuario de la lista para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) modeloTabla.getValueAt(fila, 0);
            Optional<Usuario> opt = controladorUsuario.buscarPorId(id);
            if (opt.isPresent()) {
                mostrarDialogoUsuario(opt.get());
            } else {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnEliminar) {
            eliminarUsuarioSeleccionado();
        }
    }

    private void eliminarUsuarioSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        String username = (String) modeloTabla.getValueAt(fila, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar al usuario '" + username + "' (ID: " + id + ")?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controladorUsuario.eliminarUsuario(id);
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarUsuarios(txtBuscar.getText().trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar usuario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarDialogoUsuario(Usuario existente) {
        boolean esNuevo = (existente == null);
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                esNuevo ? "Registrar Nuevo Usuario" : "Modificar Usuario", true);
        dialog.setSize(480, 480);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);

        JTextField txtNombre = new JTextField();
        JTextField txtUsername = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JComboBox<String> comboRol = new JComboBox<>(ROLES);
        JComboBox<String> comboEstudiante = new JComboBox<>();
        JTextField txtEmail = new JTextField();
        JCheckBox chkActivo = new JCheckBox("Usuario activo", true);

        // Cargar lista de estudiantes para asociar
        comboEstudiante.addItem("Ninguno (No es estudiante)");
        try {
            List<Estudiante> estudiantes = estudianteService.obtenerTodosLosEstudiantes();
            for (Estudiante est : estudiantes) {
                comboEstudiante.addItem(String.format("ID %d: %s %s (%s)", est.getId(), est.getName(), est.getLastname() != null ? est.getLastname() : "", est.getEmail() != null ? est.getEmail() : ""));
            }
        } catch (Exception ignored) {}

        JLabel lblInfoHash = new JLabel(esNuevo
                ? "🔒 La contraseña se cifrará con SHA-256 automáticamente."
                : "🔒 Deje la contraseña en blanco si no desea modificarla.");
        lblInfoHash.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblInfoHash.setForeground(new Color(100, 110, 120));

        if (!esNuevo) {
            txtNombre.setText(existente.getNombre());
            txtUsername.setText(existente.getUsername());
            comboRol.setSelectedItem(existente.getRol());
            txtEmail.setText(existente.getEmail() != null ? existente.getEmail() : "");
            chkActivo.setSelected(existente.isActivo());

            if (existente.getEstudianteId() != null && existente.getEstudianteId() > 0) {
                for (int i = 0; i < comboEstudiante.getItemCount(); i++) {
                    String item = comboEstudiante.getItemAt(i);
                    if (item.startsWith("ID " + existente.getEstudianteId() + ":")) {
                        comboEstudiante.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }

        // Si se selecciona un estudiante y los campos están vacíos, autocompletar
        comboEstudiante.addActionListener(e -> {
            if (comboEstudiante.getSelectedIndex() > 0 && esNuevo) {
                String item = (String) comboEstudiante.getSelectedItem();
                try {
                    int idEst = Integer.parseInt(item.substring(3, item.indexOf(':')));
                    estudianteService.buscarEstudiantePorId(idEst).ifPresent(est -> {
                        if (txtNombre.getText().trim().isEmpty()) {
                            txtNombre.setText(est.getName() + (est.getLastname() != null ? " " + est.getLastname() : ""));
                        }
                        if (txtEmail.getText().trim().isEmpty() && est.getEmail() != null) {
                            txtEmail.setText(est.getEmail());
                        }
                        if (txtUsername.getText().trim().isEmpty()) {
                            txtUsername.setText(est.getName().toLowerCase().replaceAll("\\s+", ""));
                        }
                        comboRol.setSelectedItem("ESTUDIANTE");
                    });
                } catch (Exception ignored) {}
            }
        });

        // Fila 0: Nombre
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.35;
        panelForm.add(new JLabel("Nombre Completo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.65;
        panelForm.add(txtNombre, gbc);

        // Fila 1: Username
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.35;
        panelForm.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.65;
        panelForm.add(txtUsername, gbc);

        // Fila 2: Password
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.35;
        panelForm.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.65;
        panelForm.add(txtPassword, gbc);

        // Fila 3: Info Hash
        gbc.gridx = 1; gbc.gridy = 3;
        panelForm.add(lblInfoHash, gbc);

        // Fila 4: Rol
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.35;
        panelForm.add(new JLabel("Rol:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.65;
        panelForm.add(comboRol, gbc);

        // Fila 5: Estudiante Enrolado
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.35;
        panelForm.add(new JLabel("Estudiante Asociado:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.65;
        panelForm.add(comboEstudiante, gbc);

        // Fila 6: Email
        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0.35;
        panelForm.add(new JLabel("Correo Electrónico:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.65;
        panelForm.add(txtEmail, gbc);

        // Fila 7: Activo
        gbc.gridx = 1; gbc.gridy = 7;
        panelForm.add(chkActivo, gbc);

        // Botones modal
        JPanel panelBotonesModal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnGuardar = crearBoton("Guardar", COLOR_EXITO);
        JButton btnCancelar = crearBoton("Cancelar", Color.GRAY);

        btnCancelar.addActionListener(e -> dialog.dispose());
        btnGuardar.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText().trim();
                String username = txtUsername.getText().trim();
                String password = new String(txtPassword.getPassword()).trim();
                String rol = (String) comboRol.getSelectedItem();
                String email = txtEmail.getText().trim();
                boolean activo = chkActivo.isSelected();

                Integer idEstudiante = null;
                if (comboEstudiante.getSelectedIndex() > 0) {
                    String item = (String) comboEstudiante.getSelectedItem();
                    idEstudiante = Integer.parseInt(item.substring(3, item.indexOf(':')));
                }

                if (nombre.isEmpty()) throw new IllegalArgumentException("El nombre es obligatorio.");
                if (username.isEmpty()) throw new IllegalArgumentException("El username es obligatorio.");

                if (esNuevo) {
                    if (password.isEmpty()) throw new IllegalArgumentException("La contraseña es obligatoria.");
                    controladorUsuario.registrarUsuario(username, password, nombre, rol, activo, email.isEmpty() ? null : email, idEstudiante);
                    JOptionPane.showMessageDialog(dialog, "Usuario registrado exitosamente con contraseña cifrada (SHA-256).", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    controladorUsuario.actualizarUsuario(existente.getId(), username, password, nombre, rol, activo, email.isEmpty() ? null : email, idEstudiante);
                    JOptionPane.showMessageDialog(dialog, "Usuario actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }

                dialog.dispose();
                cargarUsuarios(txtBuscar.getText().trim());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panelBotonesModal.add(btnGuardar);
        panelBotonesModal.add(btnCancelar);

        dialog.add(panelForm, BorderLayout.CENTER);
        dialog.add(panelBotonesModal, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private JButton crearBoton(String texto, Color fondo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
