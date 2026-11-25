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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

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

import com.uniajc.dao.DocenteDao;
import com.uniajc.modelo.Docente;
import com.uniajc.servicios.DocenteService;

public class VistaDocenteSwing extends JPanel implements ActionListener {

    // Degradado suave del panel (coherente con VistaPrincipalSwing y
    // VistaEstudianteSwing)
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
    private static final Color COLOR_INPUT_FONDO = new Color(255, 252, 255);
    private static final Color COLOR_TABLA_FONDO = new Color(253, 251, 255);
    private static final Color COLOR_BORDE = new Color(216, 198, 232);

    private static final String[] COLUMNAS = { "ID", "Nombre", "Especialidad", "Documento", "Email", "Teléfono",
            "Título", "Contrato" };

    private final DocenteService docenteService;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField textoBusqueda;
    private JButton botonBuscar, botonLimpiarBusqueda;
    private JButton botonRegistrar, botonEditar, botonEliminar, botonActualizar;

    private JDialog dialogoFormulario;
    private JTextField campoNombre;
    private JTextField campoEspecialidad;
    private JTextField campoDocumento;
    private JTextField campoEmail;
    private JTextField campoTelefono;
    private JComboBox<String> comboTituloAcademico;
    private JComboBox<String> comboTipoContrato;
    private JButton botonGuardar;
    private boolean modoEdicion;
    private Docente docenteSeleccionado;

    public VistaDocenteSwing() {
        this(new DocenteService(new DocenteDao()));
    }

    public VistaDocenteSwing(DocenteService docenteService) {
        this.docenteService = docenteService;
        initComponents();
        cargarDocentes();
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

    private void initComponents() {
        setLayout(new BorderLayout(12, 12));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        add(crearPanelBusqueda(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panel.setOpaque(false);

        JLabel label = new JLabel("Buscar (ID / Nombre / Especialidad / Doc.):");
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(COLOR_TEXTO_OSCURO);

        textoBusqueda = new JTextField(20);
        textoBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textoBusqueda.setForeground(COLOR_TEXTO_OSCURO);
        textoBusqueda.setBackground(COLOR_INPUT_FONDO);
        textoBusqueda.setPreferredSize(new Dimension(210, 34));
        textoBusqueda.setToolTipText("Ingrese ID, nombre, especialidad o documento");
        textoBusqueda.putClientProperty("JTextField.placeholderText", "ID, nombre, doc...");
        textoBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarDocentes();
                }
            }
        });

        botonBuscar = crearBotonSolido("Buscar", COLOR_SECUNDARIO_MORADO, COLOR_SECUNDARIO_MORADO_HOVER);
        botonBuscar.addActionListener(e -> buscarDocentes());

        botonLimpiarBusqueda = crearBotonSolido("Limpiar", COLOR_NEUTRO, COLOR_NEUTRO_HOVER);
        botonLimpiarBusqueda.addActionListener(e -> {
            textoBusqueda.setText("");
            cargarDocentes();
        });

        panel.add(label);
        panel.add(textoBusqueda);
        panel.add(botonBuscar);
        panel.add(botonLimpiarBusqueda);
        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
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

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.getViewport().setBackground(COLOR_TABLA_FONDO);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 10));
        panel.setOpaque(false);

        botonRegistrar = crearBotonDegradado("Registrar");
        botonRegistrar.addActionListener(e -> mostrarDialogoRegistro(false));

        botonEditar = crearBotonSolido("Editar", COLOR_SECUNDARIO_MORADO, COLOR_SECUNDARIO_MORADO_HOVER);
        botonEditar.addActionListener(e -> {
            if (tabla.getSelectedRow() >= 0) {
                mostrarDialogoRegistro(true);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un docente.", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        botonEliminar = crearBotonSolido("Eliminar", COLOR_ELIMINAR, COLOR_ELIMINAR_HOVER);
        botonEliminar.addActionListener(e -> eliminarDocente());

        botonActualizar = crearBotonSolido("Actualizar", COLOR_NEUTRO, COLOR_NEUTRO_HOVER);
        botonActualizar.addActionListener(e -> cargarDocentes());

        panel.add(botonRegistrar);
        panel.add(botonEditar);
        panel.add(botonEliminar);
        panel.add(botonActualizar);
        return panel;
    }

    private JButton crearBotonDegradado(String texto) {
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
        boton.setPreferredSize(new Dimension(118, 36));
        return boton;
    }

    private JButton crearBotonSolido(String texto, Color colorBase, Color colorHover) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setBackground(colorBase);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(112, 36));

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

    public void cargarDocentes() {
        modeloTabla.setRowCount(0);
        try {
            List<Docente> docentes = docenteService.obtenerTodosLosDocentes();
            for (Docente d : docentes) {
                modeloTabla.addRow(new Object[] {
                        d.getIdDocente(),
                        d.getNombre(),
                        d.getEspecialidad(),
                        d.getDocumento(),
                        d.getEmail(),
                        d.getTelefono(),
                        d.getTituloAcademico(),
                        d.getTipoContrato()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar docentes: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarDocentes() {
        String criterio = textoBusqueda.getText().trim();
        if (criterio.isEmpty()) {
            cargarDocentes();
            return;
        }

        modeloTabla.setRowCount(0);
        try {
            List<Docente> resultados = new ArrayList<>();
            if (criterio.matches("^\\d+$")) {
                docenteService.buscarDocentePorId(Integer.parseInt(criterio)).ifPresent(resultados::add);
            }
            for (Docente d : docenteService.buscarDocentesPorNombre(criterio)) {
                if (resultados.stream().noneMatch(e -> e.getIdDocente() == d.getIdDocente())) {
                    resultados.add(d);
                }
            }
            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron docentes para: \"" + criterio + "\"",
                        "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
                cargarDocentes();
            } else {
                for (Docente d : resultados) {
                    modeloTabla.addRow(new Object[] {
                            d.getIdDocente(),
                            d.getNombre(),
                            d.getEspecialidad(),
                            d.getDocumento(),
                            d.getEmail(),
                            d.getTelefono(),
                            d.getTituloAcademico(),
                            d.getTipoContrato()
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
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

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        dialogoFormulario = new JDialog(parent, esEdicion ? "Editar Docente" : "Registrar Docente", true);
        dialogoFormulario.setSize(440, 490);
        dialogoFormulario.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout()) {
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
        form.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titulo = new JLabel(esEdicion ? "Editar Docente" : "Registrar Docente");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 17));
        titulo.setForeground(COLOR_TEXTO_OSCURO);
        form.add(titulo, gbc);

        gbc.gridwidth = 1;

        gbc.gridy++;
        gbc.gridx = 0;
        form.add(crearEtiquetaForm("Nombre:"), gbc);
        campoNombre = crearCampoForm();
        gbc.gridx = 1;
        form.add(campoNombre, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        form.add(crearEtiquetaForm("Especialidad:"), gbc);
        campoEspecialidad = crearCampoForm();
        gbc.gridx = 1;
        form.add(campoEspecialidad, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        form.add(crearEtiquetaForm("Documento:"), gbc);
        campoDocumento = crearCampoForm();
        gbc.gridx = 1;
        form.add(campoDocumento, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        form.add(crearEtiquetaForm("Email:"), gbc);
        campoEmail = crearCampoForm();
        gbc.gridx = 1;
        form.add(campoEmail, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        form.add(crearEtiquetaForm("Teléfono:"), gbc);
        campoTelefono = crearCampoForm();
        gbc.gridx = 1;
        form.add(campoTelefono, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        form.add(crearEtiquetaForm("Título Académico:"), gbc);
        comboTituloAcademico = new JComboBox<>(new String[] { "Pregrado", "Especialización", "Maestría", "Doctorado" });
        estilizarCombo(comboTituloAcademico);
        gbc.gridx = 1;
        form.add(comboTituloAcademico, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        form.add(crearEtiquetaForm("Tipo Contrato:"), gbc);
        comboTipoContrato = new JComboBox<>(new String[] { "Tiempo Completo", "Medio Tiempo", "Cátedra", "Ocasional" });
        estilizarCombo(comboTipoContrato);
        gbc.gridx = 1;
        form.add(comboTipoContrato, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        botones.setOpaque(false);

        botonGuardar = crearBotonDegradado("Guardar");
        botonGuardar.addActionListener(e -> guardarDocente());
        JButton botonCancelar = crearBotonSolido("Cancelar", COLOR_NEUTRO, COLOR_NEUTRO_HOVER);
        botonCancelar.addActionListener(e -> dialogoFormulario.dispose());

        botones.add(botonGuardar);
        botones.add(botonCancelar);
        form.add(botones, gbc);

        if (esEdicion && docenteSeleccionado != null) {
            campoNombre.setText(docenteSeleccionado.getNombre());
            campoEspecialidad.setText(docenteSeleccionado.getEspecialidad());
            campoDocumento.setText(docenteSeleccionado.getDocumento());
            campoEmail.setText(docenteSeleccionado.getEmail());
            campoTelefono.setText(docenteSeleccionado.getTelefono());
            if (docenteSeleccionado.getTituloAcademico() != null) {
                comboTituloAcademico.setSelectedItem(docenteSeleccionado.getTituloAcademico());
            }
            if (docenteSeleccionado.getTipoContrato() != null) {
                comboTipoContrato.setSelectedItem(docenteSeleccionado.getTipoContrato());
            }
        }

        dialogoFormulario.add(form);
        dialogoFormulario.setVisible(true);
    }

    private JLabel crearEtiquetaForm(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(COLOR_TEXTO_OSCURO);
        return label;
    }

    private JTextField crearCampoForm() {
        JTextField campo = new JTextField(18);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setForeground(COLOR_TEXTO_OSCURO);
        campo.setBackground(COLOR_INPUT_FONDO);
        campo.setPreferredSize(new Dimension(210, 34));
        return campo;
    }

    private void estilizarCombo(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setForeground(COLOR_TEXTO_OSCURO);
        combo.setBackground(COLOR_INPUT_FONDO);
        combo.setPreferredSize(new Dimension(210, 34));
    }

    private void guardarDocente() {
        try {
            String nombre = campoNombre.getText().trim();
            String especialidad = campoEspecialidad.getText().trim();
            String documento = campoDocumento.getText().trim();
            String email = campoEmail.getText().trim();
            String telefono = campoTelefono.getText().trim();
            String tituloAcademico = (String) comboTituloAcademico.getSelectedItem();
            String tipoContrato = (String) comboTipoContrato.getSelectedItem();

            if (nombre.isEmpty() || especialidad.isEmpty() || documento.isEmpty()) {
                JOptionPane.showMessageDialog(dialogoFormulario, "Nombre, Especialidad y Documento son obligatorios.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            docenteSeleccionado.setNombre(nombre);
            docenteSeleccionado.setEspecialidad(especialidad);
            docenteSeleccionado.setDocumento(documento);
            docenteSeleccionado.setEmail(email);
            docenteSeleccionado.setTelefono(telefono);
            docenteSeleccionado.setTituloAcademico(tituloAcademico);
            docenteSeleccionado.setTipoContrato(tipoContrato);

            if (modoEdicion) {
                docenteService.actualizarDocente(docenteSeleccionado);
                JOptionPane.showMessageDialog(dialogoFormulario, "Docente actualizado con éxito.", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                docenteService.registrarDocente(docenteSeleccionado);
                JOptionPane.showMessageDialog(dialogoFormulario, "Docente registrado con éxito.", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            dialogoFormulario.dispose();
            cargarDocentes();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(dialogoFormulario, ex.getMessage(), "Validación",
                    JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialogoFormulario, "Error: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarDocente() {
        if (tabla.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un docente.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar el docente seleccionado?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try {
                int id = (int) modeloTabla.getValueAt(tabla.getSelectedRow(), 0);
                docenteService.eliminarDocente(id);
                JOptionPane.showMessageDialog(this, "Docente eliminado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDocentes();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}