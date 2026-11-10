package com.uniajc.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.uniajc.dao.DocenteDao;
import com.uniajc.modelo.Docente;
import com.uniajc.servicios.DocenteService;

public class VistaDocenteSwing extends JPanel implements ActionListener {

    private static final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    private static final Color COLOR_SECUNDARIO = new Color(236, 240, 241);
    private static final Color COLOR_ACENTO = new Color(231, 76, 60);
    private static final Color COLOR_EXITO = new Color(39, 174, 96);

    private static final String[] COLUMNAS = {"ID", "Nombre", "Especialidad", "Correo Electrónico"};

    private final DocenteService docenteService;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField textoBusqueda;
    private JButton botonBuscar;
    private JButton botonRegistrar;
    private JButton botonEditar;
    private JButton botonEliminar;
    private JButton botonActualizar;

    private JDialog dialogoFormulario;
    private JTextField campoNombre;
    private JTextField campoEspecialidad;
    private JTextField campoEmail;
    private JButton botonGuardar;
    private boolean modoEdicion;
    private Docente docenteSeleccionado;

    public VistaDocenteSwing() {
        this.docenteService = new DocenteService(new DocenteDao());
        initComponents();
        cargarDocentes();
    }

    public VistaDocenteSwing(DocenteService docenteService) {
        this.docenteService = docenteService;
        initComponents();
        cargarDocentes();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_SECUNDARIO);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(crearPanelBusqueda(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(COLOR_SECUNDARIO);

        JLabel labelBusqueda = new JLabel("Buscar Docente:");
        labelBusqueda.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelBusqueda.setForeground(COLOR_PRIMARIO);

        textoBusqueda = new JTextField(25);
        textoBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textoBusqueda.setPreferredSize(new Dimension(220, 30));

        botonBuscar = crearBoton("Buscar", COLOR_PRIMARIO);
        botonBuscar.addActionListener(e -> buscarDocentes());

        panel.add(labelBusqueda);
        panel.add(textoBusqueda);
        panel.add(botonBuscar);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_SECUNDARIO);

        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(25);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setGridColor(new Color(189, 195, 199));
        tabla.setBackground(Color.WHITE);
        tabla.setSelectionBackground(COLOR_PRIMARIO);

        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(COLOR_PRIMARIO);
        tabla.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO, 2));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(COLOR_SECUNDARIO);

        botonRegistrar = crearBoton("Registrar", COLOR_EXITO);
        botonRegistrar.addActionListener(e -> mostrarDialogoRegistro(false));

        botonEditar = crearBoton("Editar", COLOR_PRIMARIO);
        botonEditar.addActionListener(e -> {
            if (tabla.getSelectedRow() >= 0) {
                mostrarDialogoRegistro(true);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un docente para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        botonEliminar = crearBoton("Eliminar", COLOR_ACENTO);
        botonEliminar.addActionListener(e -> eliminarDocente());

        botonActualizar = crearBoton("Actualizar", new Color(149, 165, 166));
        botonActualizar.addActionListener(e -> cargarDocentes());

        panel.add(botonRegistrar);
        panel.add(botonEditar);
        panel.add(botonEliminar);
        panel.add(botonActualizar);

        return panel;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setPreferredSize(new Dimension(110, 35));
        return boton;
    }

    public void cargarDocentes() {
        modeloTabla.setRowCount(0);
        try {
            List<Docente> docentes = docenteService.obtenerTodosLosDocentes();
            for (Docente d : docentes) {
                modeloTabla.addRow(new Object[]{d.getId(), d.getNombre(), d.getEspecialidad(), d.getEmail()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarDocentes() {
        modeloTabla.setRowCount(0);
        String busqueda = textoBusqueda.getText().trim().toLowerCase();

        if (busqueda.isEmpty()) {
            cargarDocentes();
            return;
        }

        try {
            List<Docente> docentes = docenteService.obtenerTodosLosDocentes();
            for (Docente d : docentes) {
                boolean coincide = String.valueOf(d.getId()).contains(busqueda) ||
                        d.getNombre().toLowerCase().contains(busqueda) ||
                        d.getEspecialidad().toLowerCase().contains(busqueda) ||
                        d.getEmail().toLowerCase().contains(busqueda);
                if (coincide) {
                    modeloTabla.addRow(new Object[]{d.getId(), d.getNombre(), d.getEspecialidad(), d.getEmail()});
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoRegistro(boolean esEdicion) {
        modoEdicion = esEdicion;

        if (esEdicion && tabla.getSelectedRow() >= 0) {
            int id = (int) modeloTabla.getValueAt(tabla.getSelectedRow(), 0);
            try {
                docenteSeleccionado = docenteService.buscarDocentePorId(id).orElse(null);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            docenteSeleccionado = new Docente();
        }

        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        dialogoFormulario = new JDialog(parentFrame, esEdicion ? "Editar Docente" : "Registrar Docente", true);
        dialogoFormulario.setSize(450, 300);
        dialogoFormulario.setLocationRelativeTo(this);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel labelTitulo = new JLabel(esEdicion ? "Editar Docente" : "Registrar Docente");
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelTitulo.setForeground(COLOR_PRIMARIO);
        panelFormulario.add(labelTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        JLabel labelNombre = new JLabel("Nombre:");
        labelNombre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0;
        panelFormulario.add(labelNombre, gbc);

        campoNombre = new JTextField(20);
        campoNombre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1;
        panelFormulario.add(campoNombre, gbc);

        gbc.gridy++;
        JLabel labelEspecialidad = new JLabel("Especialidad:");
        labelEspecialidad.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0;
        panelFormulario.add(labelEspecialidad, gbc);

        campoEspecialidad = new JTextField(20);
        campoEspecialidad.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1;
        panelFormulario.add(campoEspecialidad, gbc);

        gbc.gridy++;
        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0;
        panelFormulario.add(labelEmail, gbc);

        campoEmail = new JTextField(20);
        campoEmail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1;
        panelFormulario.add(campoEmail, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.gridx = 0;

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setBackground(Color.WHITE);

        botonGuardar = crearBoton("Guardar", COLOR_EXITO);
        botonGuardar.addActionListener(e -> guardarDocente());

        JButton botonCancelar = crearBoton("Cancelar", new Color(149, 165, 166));
        botonCancelar.addActionListener(e -> dialogoFormulario.dispose());

        panelBotones.add(botonGuardar);
        panelBotones.add(botonCancelar);
        panelFormulario.add(panelBotones, gbc);

        if (esEdicion && docenteSeleccionado != null) {
            campoNombre.setText(docenteSeleccionado.getNombre());
            campoEspecialidad.setText(docenteSeleccionado.getEspecialidad());
            campoEmail.setText(docenteSeleccionado.getEmail());
        }

        dialogoFormulario.add(panelFormulario);
        dialogoFormulario.setVisible(true);
    }

    private void guardarDocente() {
        try {
            String nombre = campoNombre.getText().trim();
            String especialidad = campoEspecialidad.getText().trim();
            String email = campoEmail.getText().trim();

            docenteSeleccionado.setNombre(nombre);
            docenteSeleccionado.setEspecialidad(especialidad);
            docenteSeleccionado.setEmail(email);

            if (modoEdicion) {
                docenteService.actualizarDocente(docenteSeleccionado);
                JOptionPane.showMessageDialog(dialogoFormulario, "Docente actualizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                docenteService.registrarDocente(docenteSeleccionado);
                JOptionPane.showMessageDialog(dialogoFormulario, "Docente registrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }

            dialogoFormulario.dispose();
            cargarDocentes();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(dialogoFormulario, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialogoFormulario, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarDocente() {
        if (tabla.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un docente de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el docente seleccionado?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = (int) modeloTabla.getValueAt(tabla.getSelectedRow(), 0);
                docenteService.eliminarDocente(id);
                JOptionPane.showMessageDialog(this, "Docente eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDocentes();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}
