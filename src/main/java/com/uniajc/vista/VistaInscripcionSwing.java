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
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.uniajc.dao.DocenteDao;
import com.uniajc.dao.EstudianteDao;
import com.uniajc.dao.GrupoDao;
import com.uniajc.dao.InscripcionDao;
import com.uniajc.dao.MateriaDao;
import com.uniajc.modelo.Estudiante;
import com.uniajc.modelo.Grupo;
import com.uniajc.modelo.Inscripcion;
import com.uniajc.servicios.EstudianteService;
import com.uniajc.servicios.GrupoService;
import com.uniajc.servicios.InscripcionService;

public class VistaInscripcionSwing extends JPanel implements ActionListener {

    private static final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    private static final Color COLOR_SECUNDARIO = new Color(236, 240, 241);
    private static final Color COLOR_ACENTO = new Color(231, 76, 60);
    private static final Color COLOR_EXITO = new Color(39, 174, 96);
    private static final Color COLOR_AMARILLO = new Color(243, 156, 18);

    private static final String[] COLUMNAS = {"ID", "ID Est", "Estudiante", "ID Grp", "Grupo / Materia", "Nota Final", "Estado"};

    private final InscripcionService inscripcionService;
    private final EstudianteService estudianteService;
    private final GrupoService grupoService;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField textoBusqueda;
    private JButton botonBuscar;
    private JButton botonInscribir;
    private JButton botonCalificar;
    private JButton botonEditar;
    private JButton botonEliminar;
    private JButton botonActualizar;

    private JDialog dialogoFormulario;
    private JComboBox<EstudianteItem> comboEstudiante;
    private JComboBox<GrupoItem> comboGrupo;
    private JTextField campoNota;
    private JComboBox<String> comboEstado;
    private JButton botonGuardar;
    private boolean modoEdicion;
    private Inscripcion inscripcionSeleccionada;

    private static class EstudianteItem {
        final int id;
        final String nombre;
        EstudianteItem(int id, String nombre) { this.id = id; this.nombre = nombre; }
        @Override public String toString() { return id + " - " + nombre; }
    }

    private static class GrupoItem {
        final int id;
        final String detalle;
        GrupoItem(int id, String detalle) { this.id = id; this.detalle = detalle; }
        @Override public String toString() { return "Grupo " + id + ": " + detalle; }
    }

    public VistaInscripcionSwing() {
        this.inscripcionService = new InscripcionService(new InscripcionDao(), new EstudianteDao(), new GrupoDao());
        this.estudianteService = new EstudianteService(new EstudianteDao());
        this.grupoService = new GrupoService(new GrupoDao(), new MateriaDao(), new DocenteDao());
        initComponents();
        cargarInscripciones();
    }

    public VistaInscripcionSwing(InscripcionService inscripcionService, EstudianteService estudianteService, GrupoService grupoService) {
        this.inscripcionService = inscripcionService;
        this.estudianteService = estudianteService;
        this.grupoService = grupoService;
        initComponents();
        cargarInscripciones();
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

        JLabel labelBusqueda = new JLabel("Buscar Inscripción:");
        labelBusqueda.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelBusqueda.setForeground(COLOR_PRIMARIO);

        textoBusqueda = new JTextField(25);
        textoBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textoBusqueda.setPreferredSize(new Dimension(220, 30));

        botonBuscar = crearBoton("Buscar", COLOR_PRIMARIO);
        botonBuscar.addActionListener(e -> buscarInscripciones());

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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        panel.setBackground(COLOR_SECUNDARIO);

        botonInscribir = crearBoton("Inscribir", COLOR_EXITO);
        botonInscribir.addActionListener(e -> mostrarDialogoRegistro(false));

        botonCalificar = crearBoton("Calificar", COLOR_AMARILLO);
        botonCalificar.addActionListener(e -> mostrarDialogoCalificar());

        botonEditar = crearBoton("Editar", COLOR_PRIMARIO);
        botonEditar.addActionListener(e -> {
            if (tabla.getSelectedRow() >= 0) {
                mostrarDialogoRegistro(true);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una inscripción para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        botonEliminar = crearBoton("Eliminar", COLOR_ACENTO);
        botonEliminar.addActionListener(e -> eliminarInscripcion());

        botonActualizar = crearBoton("Actualizar", new Color(149, 165, 166));
        botonActualizar.addActionListener(e -> cargarInscripciones());

        panel.add(botonInscribir);
        panel.add(botonCalificar);
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

    public void cargarInscripciones() {
        modeloTabla.setRowCount(0);
        try {
            List<Inscripcion> lista = inscripcionService.obtenerTodasLasInscripciones();
            for (Inscripcion i : lista) {
                String estNom = (i.getEstudianteNombre() != null) ? i.getEstudianteNombre() : "N/A";
                String grpDet = (i.getGrupoDetalle() != null) ? i.getGrupoDetalle() : "N/A";
                String notaStr = (i.getNotaFinal() != null) ? String.format("%.2f", i.getNotaFinal()) : "Sin Calificar";
                modeloTabla.addRow(new Object[]{
                        i.getId(), i.getIdEstudiante(), estNom, i.getIdGrupo(), grpDet, notaStr, i.getEstado()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarInscripciones() {
        modeloTabla.setRowCount(0);
        String busqueda = textoBusqueda.getText().trim().toLowerCase();

        if (busqueda.isEmpty()) {
            cargarInscripciones();
            return;
        }

        try {
            List<Inscripcion> lista = inscripcionService.obtenerTodasLasInscripciones();
            for (Inscripcion i : lista) {
                String estNom = (i.getEstudianteNombre() != null) ? i.getEstudianteNombre() : "";
                String grpDet = (i.getGrupoDetalle() != null) ? i.getGrupoDetalle() : "";
                boolean coincide = String.valueOf(i.getId()).contains(busqueda) ||
                        estNom.toLowerCase().contains(busqueda) ||
                        grpDet.toLowerCase().contains(busqueda) ||
                        i.getEstado().toLowerCase().contains(busqueda);
                if (coincide) {
                    String notaStr = (i.getNotaFinal() != null) ? String.format("%.2f", i.getNotaFinal()) : "Sin Calificar";
                    modeloTabla.addRow(new Object[]{
                            i.getId(), i.getIdEstudiante(), estNom, i.getIdGrupo(), grpDet, notaStr, i.getEstado()
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoCalificar() {
        if (tabla.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una inscripción para calificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idInscripcion = (int) modeloTabla.getValueAt(tabla.getSelectedRow(), 0);
        String alumno = (String) modeloTabla.getValueAt(tabla.getSelectedRow(), 2);

        String entrada = JOptionPane.showInputDialog(this,
                "Ingrese la nota final (0.0 - 5.0) para " + alumno + ":", "Calificar Inscripción #" + idInscripcion, JOptionPane.QUESTION_MESSAGE);

        if (entrada != null && !entrada.trim().isEmpty()) {
            try {
                double nota = Double.parseDouble(entrada.trim().replace(',', '.'));
                inscripcionService.registrarNota(idInscripcion, nota);
                JOptionPane.showMessageDialog(this, "Nota registrada exitosamente. Estado actualizado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarInscripciones();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarDialogoRegistro(boolean esEdicion) {
        modoEdicion = esEdicion;

        List<Estudiante> estudiantes = estudianteService.obtenerTodosLosEstudiantes();
        List<Grupo> grupos = grupoService.obtenerTodosLosGrupos();

        if (estudiantes.isEmpty() || grupos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe haber al menos un Estudiante y un Grupo registrados para realizar una inscripción.",
                    "Datos Faltantes", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (esEdicion && tabla.getSelectedRow() >= 0) {
            int id = (int) modeloTabla.getValueAt(tabla.getSelectedRow(), 0);
            try {
                inscripcionSeleccionada = inscripcionService.buscarInscripcionPorId(id).orElse(null);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            inscripcionSeleccionada = new Inscripcion();
        }

        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        dialogoFormulario = new JDialog(parentFrame, esEdicion ? "Editar Inscripción" : "Registrar Inscripción", true);
        dialogoFormulario.setSize(520, 360);
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
        JLabel labelTitulo = new JLabel(esEdicion ? "Editar Inscripción" : "Registrar Inscripción");
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelTitulo.setForeground(COLOR_PRIMARIO);
        panelFormulario.add(labelTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        JLabel labelEstudiante = new JLabel("Estudiante:");
        labelEstudiante.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0;
        panelFormulario.add(labelEstudiante, gbc);

        comboEstudiante = new JComboBox<>();
        for (Estudiante e : estudiantes) {
            comboEstudiante.addItem(new EstudianteItem(e.getId(), e.getNombre() + " " + e.getApellido()));
        }
        gbc.gridx = 1;
        panelFormulario.add(comboEstudiante, gbc);

        gbc.gridy++;
        JLabel labelGrupo = new JLabel("Grupo:");
        labelGrupo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0;
        panelFormulario.add(labelGrupo, gbc);

        comboGrupo = new JComboBox<>();
        for (Grupo g : grupos) {
            String matNom = (g.getMateriaNombre() != null) ? g.getMateriaNombre() : "Mat " + g.getIdMateria();
            comboGrupo.addItem(new GrupoItem(g.getId(), matNom + " (Aula: " + g.getAula() + ")"));
        }
        gbc.gridx = 1;
        panelFormulario.add(comboGrupo, gbc);

        gbc.gridy++;
        JLabel labelNota = new JLabel("Nota Final (Opcional):");
        labelNota.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0;
        panelFormulario.add(labelNota, gbc);

        campoNota = new JTextField(20);
        campoNota.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1;
        panelFormulario.add(campoNota, gbc);

        gbc.gridy++;
        JLabel labelEstado = new JLabel("Estado:");
        labelEstado.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0;
        panelFormulario.add(labelEstado, gbc);

        comboEstado = new JComboBox<>(new String[]{"ACTIVO", "APROBADO", "REPROBADO", "CANCELADO"});
        gbc.gridx = 1;
        panelFormulario.add(comboEstado, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.gridx = 0;

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setBackground(Color.WHITE);

        botonGuardar = crearBoton("Guardar", COLOR_EXITO);
        botonGuardar.addActionListener(e -> guardarInscripcion());

        JButton botonCancelar = crearBoton("Cancelar", new Color(149, 165, 166));
        botonCancelar.addActionListener(e -> dialogoFormulario.dispose());

        panelBotones.add(botonGuardar);
        panelBotones.add(botonCancelar);
        panelFormulario.add(panelBotones, gbc);

        if (esEdicion && inscripcionSeleccionada != null) {
            for (int i = 0; i < comboEstudiante.getItemCount(); i++) {
                if (comboEstudiante.getItemAt(i).id == inscripcionSeleccionada.getIdEstudiante()) {
                    comboEstudiante.setSelectedIndex(i);
                    break;
                }
            }
            for (int i = 0; i < comboGrupo.getItemCount(); i++) {
                if (comboGrupo.getItemAt(i).id == inscripcionSeleccionada.getIdGrupo()) {
                    comboGrupo.setSelectedIndex(i);
                    break;
                }
            }
            if (inscripcionSeleccionada.getNotaFinal() != null) {
                campoNota.setText(String.valueOf(inscripcionSeleccionada.getNotaFinal()));
            }
            if (inscripcionSeleccionada.getEstado() != null) {
                comboEstado.setSelectedItem(inscripcionSeleccionada.getEstado());
            }
        }

        dialogoFormulario.add(panelFormulario);
        dialogoFormulario.setVisible(true);
    }

    private void guardarInscripcion() {
        try {
            EstudianteItem est = (EstudianteItem) comboEstudiante.getSelectedItem();
            GrupoItem grp = (GrupoItem) comboGrupo.getSelectedItem();
            String notaTexto = campoNota.getText().trim();
            String estado = (String) comboEstado.getSelectedItem();

            if (est == null || grp == null) {
                JOptionPane.showMessageDialog(dialogoFormulario, "Seleccione estudiante y grupo.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Double nota = null;
            if (!notaTexto.isEmpty()) {
                nota = Double.parseDouble(notaTexto.replace(',', '.'));
            }

            inscripcionSeleccionada.setIdEstudiante(est.id);
            inscripcionSeleccionada.setIdGrupo(grp.id);
            inscripcionSeleccionada.setNotaFinal(nota);
            inscripcionSeleccionada.setEstado(estado);

            if (modoEdicion) {
                inscripcionService.actualizarInscripcion(inscripcionSeleccionada);
                JOptionPane.showMessageDialog(dialogoFormulario, "Inscripción actualizada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                inscripcionService.registrarInscripcion(inscripcionSeleccionada);
                JOptionPane.showMessageDialog(dialogoFormulario, "Inscripción registrada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }

            dialogoFormulario.dispose();
            cargarInscripciones();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialogoFormulario, "La nota final debe ser un número decimal válido.", "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(dialogoFormulario, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialogoFormulario, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarInscripcion() {
        if (tabla.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una inscripción de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar la inscripción seleccionada?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = (int) modeloTabla.getValueAt(tabla.getSelectedRow(), 0);
                inscripcionService.eliminarInscripcion(id);
                JOptionPane.showMessageDialog(this, "Inscripción eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarInscripciones();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}
