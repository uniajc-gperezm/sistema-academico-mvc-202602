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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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

import com.uniajc.dao.PeriodoAcademicoDao;
import com.uniajc.modelo.PeriodoAcademico;
import com.uniajc.servicios.PeriodoAcademicoService;

public class VistaPeriodoSwing extends JPanel implements ActionListener {

    private static final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    private static final Color COLOR_SECUNDARIO = new Color(236, 240, 241);
    private static final Color COLOR_ACENTO = new Color(231, 76, 60);
    private static final Color COLOR_EXITO = new Color(39, 174, 96);

    private static final String[] COLUMNAS = {"ID", "Código", "Fecha Inicio", "Fecha Fin", "Activo"};

    private final PeriodoAcademicoService periodoAcademicoService;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton botonRegistrar;
    private JButton botonEditar;
    private JButton botonEliminar;
    private JButton botonActivar;
    private JButton botonActualizar;

    private JDialog dialogoFormulario;
    private JTextField campoCodigo;
    private JTextField campoFechaInicio;
    private JTextField campoFechaFin;
    private JButton botonGuardar;
    private boolean modoEdicion;
    private PeriodoAcademico periodoSeleccionado;

    public VistaPeriodoSwing() {
        this(new PeriodoAcademicoService(new PeriodoAcademicoDao()));
    }

    public VistaPeriodoSwing(PeriodoAcademicoService periodoAcademicoService) {
        this.periodoAcademicoService = periodoAcademicoService;
        initComponents();
        cargarPeriodos();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_SECUNDARIO);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
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
        scrollPane.setPreferredSize(new Dimension(650, 400));
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
                JOptionPane.showMessageDialog(this, "Seleccione un periodo de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        botonActivar = crearBoton("Activar", new Color(22, 160, 133));
        botonActivar.addActionListener(e -> activarPeriodo());

        botonEliminar = crearBoton("Eliminar", COLOR_ACENTO);
        botonEliminar.addActionListener(e -> eliminarPeriodo());

        botonActualizar = crearBoton("Actualizar", new Color(149, 165, 166));
        botonActualizar.addActionListener(e -> cargarPeriodos());

        panel.add(botonRegistrar);
        panel.add(botonEditar);
        panel.add(botonActivar);
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
        boton.setPreferredSize(new Dimension(120, 35));
        return boton;
    }

    public void cargarPeriodos() {
        modeloTabla.setRowCount(0);
        try {
            List<PeriodoAcademico> periodos = periodoAcademicoService.obtenerTodosLosPeriodos();
            for (PeriodoAcademico periodo : periodos) {
                modeloTabla.addRow(new Object[]{
                        periodo.getId(),
                        periodo.getCodigo(),
                        periodo.getFechaInicio(),
                        periodo.getFechaFin(),
                        periodo.isActivo() ? "Sí" : "No"
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar periodos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoRegistro(boolean esEdicion) {
        modoEdicion = esEdicion;

        if (esEdicion && tabla.getSelectedRow() >= 0) {
            int id = (int) modeloTabla.getValueAt(tabla.getSelectedRow(), 0);
            try {
                periodoSeleccionado = periodoAcademicoService.buscarPeriodoPorId(id).orElse(null);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            periodoSeleccionado = new PeriodoAcademico();
        }

        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        dialogoFormulario = new JDialog(parentFrame, esEdicion ? "Editar Periodo Académico" : "Registrar Periodo Académico", true);
        dialogoFormulario.setSize(420, 260);
        dialogoFormulario.setLocationRelativeTo(this);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel labelTitulo = new JLabel(esEdicion ? "Editar Periodo" : "Registrar Periodo");
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelTitulo.setForeground(COLOR_PRIMARIO);
        panelFormulario.add(labelTitulo, gbc);

        gbc.gridwidth = 1;

        gbc.gridy++;
        gbc.gridx = 0;
        panelFormulario.add(new JLabel("Código:"), gbc);
        campoCodigo = new JTextField(18);
        gbc.gridx = 1;
        panelFormulario.add(campoCodigo, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panelFormulario.add(new JLabel("Fecha Inicio (yyyy-MM-dd):"), gbc);
        campoFechaInicio = new JTextField(18);
        gbc.gridx = 1;
        panelFormulario.add(campoFechaInicio, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panelFormulario.add(new JLabel("Fecha Fin (yyyy-MM-dd):"), gbc);
        campoFechaFin = new JTextField(18);
        gbc.gridx = 1;
        panelFormulario.add(campoFechaFin, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel panelBotonesForm = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotonesForm.setBackground(Color.WHITE);

        botonGuardar = crearBoton("Guardar", COLOR_EXITO);
        botonGuardar.addActionListener(e -> guardarPeriodo());

        JButton botonCancelar = crearBoton("Cancelar", new Color(149, 165, 166));
        botonCancelar.addActionListener(e -> dialogoFormulario.dispose());

        panelBotonesForm.add(botonGuardar);
        panelBotonesForm.add(botonCancelar);
        panelFormulario.add(panelBotonesForm, gbc);

        if (esEdicion && periodoSeleccionado != null) {
            campoCodigo.setText(periodoSeleccionado.getCodigo());
            campoFechaInicio.setText(periodoSeleccionado.getFechaInicio().toString());
            campoFechaFin.setText(periodoSeleccionado.getFechaFin().toString());
        }

        dialogoFormulario.add(panelFormulario);
        dialogoFormulario.setVisible(true);
    }

    private void guardarPeriodo() {
        try {
            String codigo = campoCodigo.getText().trim();
            LocalDate fechaInicio = LocalDate.parse(campoFechaInicio.getText().trim());
            LocalDate fechaFin = LocalDate.parse(campoFechaFin.getText().trim());

            if (codigo.isEmpty()) {
                JOptionPane.showMessageDialog(dialogoFormulario, "El código del periodo es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            periodoSeleccionado.setCodigo(codigo);
            periodoSeleccionado.setFechaInicio(fechaInicio);
            periodoSeleccionado.setFechaFin(fechaFin);
            periodoSeleccionado.setActivo(periodoSeleccionado.isActivo() || false);

            if (modoEdicion) {
                periodoAcademicoService.actualizarPeriodo(periodoSeleccionado);
                JOptionPane.showMessageDialog(dialogoFormulario, "Periodo académico actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                periodoAcademicoService.registrarPeriodo(periodoSeleccionado);
                JOptionPane.showMessageDialog(dialogoFormulario, "Periodo académico registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }

            dialogoFormulario.dispose();
            cargarPeriodos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialogoFormulario, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void activarPeriodo() {
        if (tabla.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un periodo de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = (int) modeloTabla.getValueAt(tabla.getSelectedRow(), 0);
            periodoAcademicoService.activarPeriodo(id);
            JOptionPane.showMessageDialog(this, "Periodo activado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarPeriodos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al activar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPeriodo() {
        if (tabla.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un periodo de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el periodo académico seleccionado?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = (int) modeloTabla.getValueAt(tabla.getSelectedRow(), 0);
                periodoAcademicoService.eliminarPeriodo(id);
                JOptionPane.showMessageDialog(this, "Periodo eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarPeriodos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}
