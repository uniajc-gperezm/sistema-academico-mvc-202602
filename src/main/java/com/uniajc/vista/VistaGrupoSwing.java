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

import com.uniajc.dao.DocenteDao;
import com.uniajc.dao.GrupoDao;
import com.uniajc.dao.MateriaDao;
import com.uniajc.modelo.Docente;
import com.uniajc.modelo.Grupo;
import com.uniajc.modelo.Materia;
import com.uniajc.servicios.DocenteService;
import com.uniajc.servicios.GrupoService;
import com.uniajc.servicios.MateriaService;

public class VistaGrupoSwing extends JPanel implements ActionListener {

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
    private static final Color COLOR_INPUT_FONDO = new Color(255, 252, 255);
    private static final Color COLOR_TABLA_FONDO = new Color(253, 251, 255);
    private static final Color COLOR_BORDE = new Color(216, 198, 232);

    private static final String[] COLUMNAS = { "ID Grupo", "ID Materia", "ID Docente", "Aula", "Horario", "Cupo Máximo",
            "Periodo" };

    private final GrupoService grupoService;
    private final MateriaService materiaService;
    private final DocenteService docenteService;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField textoBusqueda;
    private JButton botonBuscar;
    private JButton botonLimpiarBusqueda;
    private JButton botonRegistrar;
    private JButton botonEditar;
    private JButton botonEliminar;
    private JButton botonActualizar;

    private JDialog dialogoFormulario;
    private JComboBox<MateriaItem> comboMaterias;
    private JComboBox<DocenteItem> comboDocentes;
    private JTextField campoAula;
    private JTextField campoHorario;
    private JTextField campoPeriodo;
    private JTextField campoCupoMaximo;
    private JButton botonGuardar;
    private boolean modoEdicion;
    private Grupo grupoSeleccionado;

    // Helper classes para comboboxes
    private static class MateriaItem {
        final int id;
        final String nombre;

        MateriaItem(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return id + " - " + nombre;
        }
    }

    private static class DocenteItem {
        final int id;
        final String nombre;

        DocenteItem(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return id + " - " + nombre;
        }
    }

    public VistaGrupoSwing() {
        this.grupoService = new GrupoService(new GrupoDao(), new MateriaDao(), new DocenteDao());
        this.materiaService = new MateriaService(new MateriaDao());
        this.docenteService = new DocenteService(new DocenteDao());
        initComponents();
        cargarGrupos();
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

        JLabel labelBusqueda = new JLabel("Buscar Grupo (ID / Aula):");
        labelBusqueda.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelBusqueda.setForeground(COLOR_TEXTO_OSCURO);

        textoBusqueda = new JTextField(22);
        textoBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textoBusqueda.setForeground(COLOR_TEXTO_OSCURO);
        textoBusqueda.setBackground(COLOR_INPUT_FONDO);
        textoBusqueda.setPreferredSize(new Dimension(220, 34));
        textoBusqueda.setToolTipText("Ingrese ID del grupo o aula");
        textoBusqueda.putClientProperty("JTextField.placeholderText", "ID o aula...");
        textoBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarGrupos();
                }
            }
        });

        botonBuscar = crearBotonSolido("Buscar", COLOR_SECUNDARIO_MORADO, COLOR_SECUNDARIO_MORADO_HOVER);
        botonBuscar.addActionListener(e -> buscarGrupos());

        botonLimpiarBusqueda = crearBotonSolido("Limpiar", COLOR_NEUTRO, COLOR_NEUTRO_HOVER);
        botonLimpiarBusqueda.addActionListener(e -> {
            textoBusqueda.setText("");
            cargarGrupos();
        });

        panel.add(labelBusqueda);
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

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.getViewport().setBackground(COLOR_TABLA_FONDO);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));
        scrollPane.setPreferredSize(new Dimension(600, 400));

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
                JOptionPane.showMessageDialog(this, "Seleccione un grupo de la tabla.", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        botonEliminar = crearBotonSolido("Eliminar", COLOR_ELIMINAR, COLOR_ELIMINAR_HOVER);
        botonEliminar.addActionListener(e -> eliminarGrupo());

        botonActualizar = crearBotonSolido("Actualizar", COLOR_NEUTRO, COLOR_NEUTRO_HOVER);
        botonActualizar.addActionListener(e -> cargarGrupos());

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

    public void cargarGrupos() {
        modeloTabla.setRowCount(0);
        try {
            List<Grupo> grupos = grupoService.obtenerTodosLosGrupos();
            for (Grupo g : grupos) {
                modeloTabla.addRow(new Object[] { g.getId(), g.getIdMateria(), g.getIdDocente(), g.getClassroom(),
                        g.getSchedule(), g.getCupoMaximo(), g.getPeriodo() });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar grupos: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarGrupos() {
        String criterio = textoBusqueda.getText().trim();
        if (criterio.isEmpty()) {
            cargarGrupos();
            return;
        }

        modeloTabla.setRowCount(0);
        try {
            List<Grupo> resultados = new ArrayList<>();
            if (criterio.matches("^\\d+$")) {
                int id = Integer.parseInt(criterio);
                Optional<Grupo> porId = grupoService.buscarGrupoPorId(id);
                porId.ifPresent(resultados::add);
            }

            List<Grupo> porAula = grupoService.buscarGruposPorAula(criterio);
            for (Grupo g : porAula) {
                if (resultados.stream().noneMatch(existente -> existente.getId() == g.getId())) {
                    resultados.add(g);
                }
            }

            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron grupos para: \"" + criterio + "\"",
                        "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
                cargarGrupos();
            } else {
                for (Grupo g : resultados) {
                    modeloTabla.addRow(new Object[] { g.getId(), g.getIdMateria(), g.getIdDocente(), g.getClassroom(),
                            g.getSchedule(), g.getCupoMaximo(), g.getPeriodo() });
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
                grupoSeleccionado = grupoService.buscarGrupoPorId(id).orElse(null);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            grupoSeleccionado = new Grupo();
        }

        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        dialogoFormulario = new JDialog(parentFrame, esEdicion ? "Editar Grupo" : "Registrar Grupo", true);
        dialogoFormulario.setSize(510, 400);
        dialogoFormulario.setLocationRelativeTo(this);

        JPanel panelFormulario = new JPanel(new GridBagLayout()) {
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
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel labelTitulo = new JLabel(esEdicion ? "Editar Grupo" : "Registrar Grupo");
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelTitulo.setForeground(COLOR_TEXTO_OSCURO);
        panelFormulario.add(labelTitulo, gbc);

        gbc.gridwidth = 1;

        // Materia
        gbc.gridy++;
        gbc.gridx = 0;
        panelFormulario.add(crearEtiquetaForm("Materia:"), gbc);
        comboMaterias = new JComboBox<>();
        List<Materia> materias = materiaService.obtenerTodasLasMaterias();
        for (Materia m : materias) {
            comboMaterias.addItem(new MateriaItem(m.getId(), m.getName()));
        }
        estilizarCombo(comboMaterias);
        gbc.gridx = 1;
        panelFormulario.add(comboMaterias, gbc);

        // Docente
        gbc.gridy++;
        gbc.gridx = 0;
        panelFormulario.add(crearEtiquetaForm("Docente:"), gbc);
        comboDocentes = new JComboBox<>();
        List<Docente> docentes = docenteService.obtenerTodosLosDocentes();
        for (Docente d : docentes) {
            comboDocentes.addItem(new DocenteItem(d.getIdDocente(), d.getNombre() + " (" + d.getEspecialidad() + ")"));
        }
        estilizarCombo(comboDocentes);
        gbc.gridx = 1;
        panelFormulario.add(comboDocentes, gbc);

        // Aula
        gbc.gridy++;
        gbc.gridx = 0;
        panelFormulario.add(crearEtiquetaForm("Aula:"), gbc);
        campoAula = crearCampoForm();
        gbc.gridx = 1;
        panelFormulario.add(campoAula, gbc);

        // Horario
        gbc.gridy++;
        gbc.gridx = 0;
        panelFormulario.add(crearEtiquetaForm("Horario:"), gbc);
        campoHorario = crearCampoForm();
        gbc.gridx = 1;
        panelFormulario.add(campoHorario, gbc);

        // Cupo máximo
        gbc.gridy++;
        gbc.gridx = 0;
        panelFormulario.add(crearEtiquetaForm("Cupo Máximo:"), gbc);
        campoCupoMaximo = crearCampoForm();
        gbc.gridx = 1;
        panelFormulario.add(campoCupoMaximo, gbc);

        // Período
        gbc.gridy++;
        gbc.gridx = 0;
        panelFormulario.add(crearEtiquetaForm("Período:"), gbc);
        campoPeriodo = crearCampoForm();
        gbc.gridx = 1;
        panelFormulario.add(campoPeriodo, gbc);

        // Botones formulario
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        JPanel panelBotonesForm = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotonesForm.setOpaque(false);

        botonGuardar = crearBotonDegradado("Guardar");
        botonGuardar.addActionListener(e -> guardarGrupo());

        JButton botonCancelar = crearBotonSolido("Cancelar", COLOR_NEUTRO, COLOR_NEUTRO_HOVER);
        botonCancelar.addActionListener(e -> dialogoFormulario.dispose());

        panelBotonesForm.add(botonGuardar);
        panelBotonesForm.add(botonCancelar);
        panelFormulario.add(panelBotonesForm, gbc);

        if (esEdicion && grupoSeleccionado != null) {
            for (int i = 0; i < comboMaterias.getItemCount(); i++) {
                if (comboMaterias.getItemAt(i).id == grupoSeleccionado.getIdMateria()) {
                    comboMaterias.setSelectedIndex(i);
                    break;
                }
            }
            for (int i = 0; i < comboDocentes.getItemCount(); i++) {
                if (comboDocentes.getItemAt(i).id == grupoSeleccionado.getIdDocente()) {
                    comboDocentes.setSelectedIndex(i);
                    break;
                }
            }
            campoAula.setText(grupoSeleccionado.getClassroom());
            campoHorario.setText(grupoSeleccionado.getSchedule());
            campoCupoMaximo.setText(String.valueOf(grupoSeleccionado.getCupoMaximo()));
            campoPeriodo.setText(grupoSeleccionado.getPeriodo() != null ? grupoSeleccionado.getPeriodo() : "");
        }

        dialogoFormulario.add(panelFormulario);
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
        campo.setPreferredSize(new Dimension(220, 34));
        return campo;
    }

    private <T> void estilizarCombo(JComboBox<T> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setForeground(COLOR_TEXTO_OSCURO);
        combo.setBackground(COLOR_INPUT_FONDO);
        combo.setPreferredSize(new Dimension(220, 34));
    }

    private void guardarGrupo() {
        try {
            MateriaItem materiaSel = (MateriaItem) comboMaterias.getSelectedItem();
            DocenteItem docenteSel = (DocenteItem) comboDocentes.getSelectedItem();
            String aula = campoAula.getText().trim();
            String horario = campoHorario.getText().trim();
            String periodo = campoPeriodo.getText().trim();
            int cupoMaximo = Integer.parseInt(campoCupoMaximo.getText().trim());

            if (materiaSel == null || docenteSel == null || aula.isEmpty() || horario.isEmpty() || periodo.isEmpty()) {
                JOptionPane.showMessageDialog(dialogoFormulario, "Todos los campos son obligatorios.", "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            grupoSeleccionado.setIdMateria(materiaSel.id);
            grupoSeleccionado.setIdDocente(docenteSel.id);
            grupoSeleccionado.setClassroom(aula);
            grupoSeleccionado.setSchedule(horario);
            grupoSeleccionado.setCupoMaximo(cupoMaximo);
            grupoSeleccionado.setPeriodo(periodo);

            if (modoEdicion) {
                grupoService.actualizarGrupo(grupoSeleccionado);
                JOptionPane.showMessageDialog(dialogoFormulario, "Grupo actualizado exitosamente.", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                grupoService.registrarGrupo(grupoSeleccionado);
                JOptionPane.showMessageDialog(dialogoFormulario, "Grupo registrado exitosamente.", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            dialogoFormulario.dispose();
            cargarGrupos();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(dialogoFormulario, ex.getMessage(), "Validación",
                    JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialogoFormulario, "Error: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarGrupo() {
        if (tabla.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un grupo de la tabla.", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el grupo seleccionado?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = (int) modeloTabla.getValueAt(tabla.getSelectedRow(), 0);
                grupoService.eliminarGrupo(id);
                JOptionPane.showMessageDialog(this, "Grupo eliminado exitosamente.", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                cargarGrupos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}