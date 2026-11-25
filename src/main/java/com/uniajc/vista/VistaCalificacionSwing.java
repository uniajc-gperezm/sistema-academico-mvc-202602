package com.uniajc.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.uniajc.controlador.ControladorCalificacion;
import com.uniajc.modelo.Calificacion;
import com.uniajc.modelo.InscripcionCurso;
import com.uniajc.servicios.InscripcionCursoService;

public class VistaCalificacionSwing extends JPanel implements ActionListener {

    // Degradado suave del panel (coherente con las vistas anteriores y sin blanco
    // cegador)
    private static final Color COLOR_FONDO_INICIO = new Color(245, 239, 255);
    private static final Color COLOR_FONDO_FIN = new Color(255, 241, 247);

    // Degradado oscuro GTA VI (encabezado de tabla y acentos)
    private static final Color COLOR_DEGRADADO_OSCURO_INICIO = new Color(28, 15, 62);
    private static final Color COLOR_DEGRADADO_OSCURO_FIN = new Color(110, 24, 92);

    // Degradado principal (magenta vibrante a coral/naranja)
    private static final Color COLOR_PRIMARIO_INICIO = new Color(236, 72, 153);
    private static final Color COLOR_PRIMARIO_FIN = new Color(249, 115, 22);
    private static final Color COLOR_PRIMARIO_HOVER_INICIO = new Color(219, 39, 119);
    private static final Color COLOR_PRIMARIO_HOVER_FIN = new Color(234, 88, 12);

    // Colores secundarios y de estado
    private static final Color COLOR_SECUNDARIO_MORADO = new Color(102, 88, 138);
    private static final Color COLOR_SECUNDARIO_MORADO_HOVER = new Color(82, 68, 118);
    private static final Color COLOR_NEUTRO = new Color(148, 136, 178);
    private static final Color COLOR_NEUTRO_HOVER = new Color(126, 113, 158);
    private static final Color COLOR_ELIMINAR = new Color(225, 29, 72);
    private static final Color COLOR_ELIMINAR_HOVER = new Color(190, 18, 60);

    private static final Color COLOR_TEXTO_OSCURO = new Color(24, 16, 46);
    private static final Color COLOR_ACENTO_MAGENTA = new Color(219, 39, 119);
    private static final Color COLOR_INPUT_FONDO = new Color(255, 252, 255);
    private static final Color COLOR_TABLA_FONDO = new Color(253, 251, 255);
    private static final Color COLOR_RESUMEN_FONDO = new Color(252, 231, 243);
    private static final Color COLOR_BORDE = new Color(216, 198, 232);

    private static final String[] COLUMNAS = { "ID", "Inscripción", "Corte / Evaluación", "Porcentaje (%)", "Nota",
            "Observación" };
    private static final String[] CORTES_PREDEFINIDOS = { "Corte 1 (30%)", "Corte 2 (35%)", "Corte 3 (35%)",
            "Examen Final", "Laboratorio", "Taller", "Otro" };

    private final ControladorCalificacion controladorCalificacion;
    private final InscripcionCursoService inscripcionService;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> comboFiltroInscripcion;
    private JLabel lblPorcentajeTotal;
    private JLabel lblNotaAcumulada;

    private JButton btnNuevo;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnRefrescar;

    public VistaCalificacionSwing() {
        this.controladorCalificacion = new ControladorCalificacion();
        this.inscripcionService = new InscripcionCursoService();
        inicializarUI();
        cargarComboInscripciones();
        cargarCalificaciones();
    }

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

    private void inicializarUI() {
        setLayout(new BorderLayout(12, 12));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        // 1. Panel Superior (Título y Filtro de Inscripción)
        JPanel panelNorte = new JPanel(new BorderLayout(6, 8));
        panelNorte.setOpaque(false);

        JLabel titulo = new JLabel("Gestión de Calificaciones por Corte");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 21));
        titulo.setForeground(COLOR_TEXTO_OSCURO);

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        panelFiltro.setOpaque(false);

        JLabel lblInscripcion = new JLabel("Seleccionar Inscripción:");
        lblInscripcion.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblInscripcion.setForeground(COLOR_TEXTO_OSCURO);

        comboFiltroInscripcion = new JComboBox<>();
        comboFiltroInscripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboFiltroInscripcion.setForeground(COLOR_TEXTO_OSCURO);
        comboFiltroInscripcion.setBackground(COLOR_INPUT_FONDO);
        comboFiltroInscripcion.setPreferredSize(new Dimension(320, 34));
        comboFiltroInscripcion.addActionListener(e -> cargarCalificaciones());

        btnRefrescar = crearBotonSolido("Refrescar", COLOR_SECUNDARIO_MORADO, COLOR_SECUNDARIO_MORADO_HOVER, 112);
        btnRefrescar.addActionListener(this);

        panelFiltro.add(lblInscripcion);
        panelFiltro.add(comboFiltroInscripcion);
        panelFiltro.add(btnRefrescar);

        panelNorte.add(titulo, BorderLayout.NORTH);
        panelNorte.add(panelFiltro, BorderLayout.SOUTH);
        add(panelNorte, BorderLayout.NORTH);

        // 2. Panel Central (Tabla)
        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setForeground(COLOR_TEXTO_OSCURO);
        tabla.setRowHeight(28);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setGridColor(COLOR_BORDE);
        tabla.setBackground(COLOR_TABLA_FONDO);
        tabla.setSelectionBackground(COLOR_PRIMARIO_INICIO);
        tabla.setSelectionForeground(Color.WHITE);

        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(0, 34));
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
                lbl.setForeground(Color.WHITE);
                lbl.setBackground(COLOR_DEGRADADO_OSCURO_INICIO);
                lbl.setOpaque(true);
                lbl.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 1, COLOR_DEGRADADO_OSCURO_FIN),
                        BorderFactory.createEmptyBorder(0, 8, 0, 8)));
                return lbl;
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.getViewport().setBackground(COLOR_TABLA_FONDO);
        scrollTabla.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));
        add(scrollTabla, BorderLayout.CENTER);

        // 3. Panel Inferior (Resumen de Notas y Botones de Acción)
        JPanel panelSur = new JPanel(new BorderLayout(10, 10));
        panelSur.setOpaque(false);

        JPanel panelResumen = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 8));
        panelResumen.setBackground(COLOR_RESUMEN_FONDO);
        panelResumen.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));

        lblPorcentajeTotal = new JLabel("Porcentaje Acumulado: 0.0%");
        lblPorcentajeTotal.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPorcentajeTotal.setForeground(COLOR_TEXTO_OSCURO);

        JLabel separador = new JLabel("|");
        separador.setForeground(COLOR_SECUNDARIO_MORADO);

        lblNotaAcumulada = new JLabel("Nota Final Ponderada: 0.00 / 5.0");
        lblNotaAcumulada.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNotaAcumulada.setForeground(COLOR_ACENTO_MAGENTA);

        panelResumen.add(lblPorcentajeTotal);
        panelResumen.add(separador);
        panelResumen.add(lblNotaAcumulada);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 6));
        panelBotones.setOpaque(false);

        btnNuevo = crearBotonDegradado("Nueva Calificación", 165);
        btnEditar = crearBotonSolido("Modificar", COLOR_SECUNDARIO_MORADO, COLOR_SECUNDARIO_MORADO_HOVER, 112);
        btnEliminar = crearBotonSolido("Eliminar", COLOR_ELIMINAR, COLOR_ELIMINAR_HOVER, 112);

        btnNuevo.addActionListener(this);
        btnEditar.addActionListener(this);
        btnEliminar.addActionListener(this);

        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        panelSur.add(panelResumen, BorderLayout.NORTH);
        panelSur.add(panelBotones, BorderLayout.SOUTH);
        add(panelSur, BorderLayout.SOUTH);
    }

    public void cargarComboInscripciones() {
        comboFiltroInscripcion.removeAllItems();
        comboFiltroInscripcion.addItem("Todas las inscripciones");

        try {
            List<InscripcionCurso> lista = inscripcionService.obtenerTodasLasInscripciones();
            for (InscripcionCurso ins : lista) {
                comboFiltroInscripcion.addItem(String.format("Inscripción #%d (Estudiante: %d | Grupo: %d)",
                        ins.getId(), ins.getIdEstudiante(), ins.getIdGrupo()));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar inscripciones: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void seleccionarInscripcion(int idInscripcion) {
        for (int i = 0; i < comboFiltroInscripcion.getItemCount(); i++) {
            String item = comboFiltroInscripcion.getItemAt(i);
            if (item.startsWith("Inscripción #" + idInscripcion + " ")) {
                comboFiltroInscripcion.setSelectedIndex(i);
                return;
            }
        }
    }

    private void cargarCalificaciones() {
        modeloTabla.setRowCount(0);
        int inscripcionId = obtenerInscripcionSeleccionada();

        try {
            List<Calificacion> lista;
            if (inscripcionId > 0) {
                lista = controladorCalificacion.listarPorInscripcion(inscripcionId);
            } else {
                lista = controladorCalificacion.listarTodas();
            }

            BigDecimal sumaPorcentaje = BigDecimal.ZERO;
            BigDecimal sumaPonderada = BigDecimal.ZERO;

            for (Calificacion c : lista) {
                modeloTabla.addRow(new Object[] {
                        c.getId(),
                        c.getInscripcionId(),
                        c.getCorte(),
                        c.getPorcentaje() != null ? c.getPorcentaje() + "%" : "0%",
                        c.getNota(),
                        c.getObservacion() != null ? c.getObservacion() : ""
                });

                if (c.getPorcentaje() != null && c.getNota() != null) {
                    sumaPorcentaje = sumaPorcentaje.add(c.getPorcentaje());
                    BigDecimal aporte = c.getNota().multiply(c.getPorcentaje()).divide(new BigDecimal("100"), 4,
                            RoundingMode.HALF_UP);
                    sumaPonderada = sumaPonderada.add(aporte);
                }
            }

            if (inscripcionId > 0) {
                lblPorcentajeTotal.setText(String.format("Porcentaje Acumulado: %.1f%% / 100%%", sumaPorcentaje));
                BigDecimal notaFinal = sumaPonderada.setScale(2, RoundingMode.HALF_UP);
                String estado = (sumaPorcentaje.compareTo(new BigDecimal("100.00")) >= 0)
                        ? (notaFinal.compareTo(new BigDecimal("3.00")) >= 0 ? " [APROBADO]" : " [REPROBADO]")
                        : " [EN CURSO]";
                lblNotaAcumulada.setText(String.format("Nota Final Ponderada: %.2f / 5.0 %s", notaFinal, estado));
            } else {
                lblPorcentajeTotal.setText("Total registros: " + lista.size());
                lblNotaAcumulada.setText("Seleccione una inscripción para ver el consolidado");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar calificaciones: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private int obtenerInscripcionSeleccionada() {
        if (comboFiltroInscripcion == null || comboFiltroInscripcion.getSelectedIndex() <= 0) {
            return -1;
        }
        String texto = (String) comboFiltroInscripcion.getSelectedItem();
        try {
            int inicio = texto.indexOf('#') + 1;
            int fin = texto.indexOf(' ', inicio);
            return Integer.parseInt(texto.substring(inicio, fin));
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRefrescar) {
            cargarComboInscripciones();
            cargarCalificaciones();
        } else if (e.getSource() == btnNuevo) {
            mostrarDialogoFormulario(null);
        } else if (e.getSource() == btnEditar) {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una calificación para modificar.", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) modeloTabla.getValueAt(fila, 0);
            Optional<Calificacion> opt = controladorCalificacion.buscarPorId(id);
            if (opt.isPresent()) {
                mostrarDialogoFormulario(opt.get());
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró la calificación seleccionada.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == btnEliminar) {
            eliminarCalificacionSeleccionada();
        }
    }

    private void eliminarCalificacionSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una calificación para eliminar.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(fila, 0);
        String corte = (String) modeloTabla.getValueAt(fila, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar la calificación del " + corte + " (ID: " + id
                        + ")?\nLa nota final se recalculará automáticamente.",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controladorCalificacion.eliminarCalificacion(id);
                JOptionPane.showMessageDialog(this, "Calificación eliminada con éxito.", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                cargarCalificaciones();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarDialogoFormulario(Calificacion existente) {
        boolean esNuevo = (existente == null);
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                esNuevo ? "Registrar Nueva Calificación" : "Modificar Calificación", true);
        dialog.setSize(450, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panelForm = new JPanel(new GridBagLayout()) {
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
        panelForm.setBorder(BorderFactory.createEmptyBorder(16, 20, 14, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);

        // Campos
        JComboBox<String> comboInscripcionModal = new JComboBox<>();
        try {
            List<InscripcionCurso> inscripciones = inscripcionService.obtenerTodasLasInscripciones();
            for (InscripcionCurso ins : inscripciones) {
                comboInscripcionModal.addItem("Inscripción #" + ins.getId() + " (Estudiante: " + ins.getIdEstudiante()
                        + " | Grupo: " + ins.getIdGrupo() + ")");
            }
        } catch (Exception ignored) {
        }
        estilizarCombo(comboInscripcionModal);

        // Preseleccionar la inscripción activa si hay filtro
        int inscripcionActiva = obtenerInscripcionSeleccionada();
        if (inscripcionActiva > 0) {
            for (int i = 0; i < comboInscripcionModal.getItemCount(); i++) {
                if (comboInscripcionModal.getItemAt(i).startsWith("Inscripción #" + inscripcionActiva + " ")) {
                    comboInscripcionModal.setSelectedIndex(i);
                    break;
                }
            }
        }

        JComboBox<String> comboCorte = new JComboBox<>(CORTES_PREDEFINIDOS);
        comboCorte.setEditable(true);
        estilizarCombo(comboCorte);

        JTextField txtPorcentaje = crearCampoForm();
        JTextField txtNota = crearCampoForm();
        JTextField txtObservacion = crearCampoForm();

        if (!esNuevo) {
            for (int i = 0; i < comboInscripcionModal.getItemCount(); i++) {
                if (comboInscripcionModal.getItemAt(i)
                        .startsWith("Inscripción #" + existente.getInscripcionId() + " ")) {
                    comboInscripcionModal.setSelectedIndex(i);
                    break;
                }
            }
            comboInscripcionModal.setEnabled(false);
            comboCorte.setSelectedItem(existente.getCorte());
            txtPorcentaje.setText(existente.getPorcentaje() != null ? existente.getPorcentaje().toString() : "");
            txtNota.setText(existente.getNota() != null ? existente.getNota().toString() : "");
            txtObservacion.setText(existente.getObservacion() != null ? existente.getObservacion() : "");
        }

        // Fila 0: Inscripción
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        panelForm.add(crearEtiquetaForm("Inscripción:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panelForm.add(comboInscripcionModal, gbc);

        // Fila 1: Corte
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        panelForm.add(crearEtiquetaForm("Corte / Evaluación:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panelForm.add(comboCorte, gbc);

        // Fila 2: Porcentaje
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        panelForm.add(crearEtiquetaForm("Porcentaje (%):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panelForm.add(txtPorcentaje, gbc);

        // Fila 3: Nota
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        panelForm.add(crearEtiquetaForm("Nota (0.0 - 5.0):"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panelForm.add(txtNota, gbc);

        // Fila 4: Observación
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        panelForm.add(crearEtiquetaForm("Observación:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panelForm.add(txtObservacion, gbc);

        // Botones del modal
        JPanel panelBotonesModal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10)) {
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
        JButton btnGuardar = crearBotonDegradado("Guardar", 118);
        JButton btnCancelar = crearBotonSolido("Cancelar", COLOR_NEUTRO, COLOR_NEUTRO_HOVER, 112);

        btnCancelar.addActionListener(e -> dialog.dispose());
        btnGuardar.addActionListener(e -> {
            try {
                String itemIns = (String) comboInscripcionModal.getSelectedItem();
                if (itemIns == null)
                    throw new IllegalArgumentException("Seleccione una inscripción.");
                int idIns = Integer.parseInt(itemIns.substring(itemIns.indexOf('#') + 1, itemIns.indexOf(' ')));

                String corte = comboCorte.getSelectedItem() != null ? comboCorte.getSelectedItem().toString().trim()
                        : "";
                if (corte.isEmpty())
                    throw new IllegalArgumentException("El corte o evaluación no puede estar vacío.");

                String strPorcentaje = txtPorcentaje.getText().trim().replace(',', '.');
                String strNota = txtNota.getText().trim().replace(',', '.');

                BigDecimal porcentaje = new BigDecimal(strPorcentaje);
                BigDecimal nota = new BigDecimal(strNota);
                String observacion = txtObservacion.getText().trim();

                if (esNuevo) {
                    controladorCalificacion.registrarCalificacion(idIns, corte, porcentaje, nota, observacion);
                    JOptionPane.showMessageDialog(dialog, "Calificación registrada correctamente.", "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    controladorCalificacion.actualizarCalificacion(existente.getId(), existente.getInscripcionId(),
                            corte, porcentaje, nota, observacion);
                    JOptionPane.showMessageDialog(dialog, "Calificación actualizada correctamente.", "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                }

                dialog.dispose();
                cargarCalificaciones();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Por favor ingrese valores numéricos válidos en porcentaje y nota (ej. 30 y 4.5).", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error de Validación",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        panelBotonesModal.add(btnGuardar);
        panelBotonesModal.add(btnCancelar);

        dialog.add(panelForm, BorderLayout.CENTER);
        dialog.add(panelBotonesModal, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private JLabel crearEtiquetaForm(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(COLOR_TEXTO_OSCURO);
        return label;
    }

    private JTextField crearCampoForm() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setForeground(COLOR_TEXTO_OSCURO);
        campo.setBackground(COLOR_INPUT_FONDO);
        campo.setPreferredSize(new Dimension(210, 34));
        return campo;
    }

    private <T> void estilizarCombo(JComboBox<T> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setForeground(COLOR_TEXTO_OSCURO);
        combo.setBackground(COLOR_INPUT_FONDO);
        combo.setPreferredSize(new Dimension(210, 34));
    }

    private JButton crearBotonDegradado(String texto, int ancho) {
        JButton boton = new JButton(texto) {
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

                GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), 0, c2);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();

                setContentAreaFilled(false);
                super.paintComponent(g);
            }
        };
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(ancho, 36));
        return boton;
    }

    private JButton crearBotonSolido(String texto, Color colorBase, Color colorHover, int ancho) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setBackground(colorBase);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(ancho, 36));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (boton.isEnabled()) {
                    boton.setBackground(colorHover);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(colorBase);
            }
        });

        return boton;
    }
}