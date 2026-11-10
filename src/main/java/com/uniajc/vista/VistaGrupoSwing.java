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
import com.uniajc.dao.GrupoDao;
import com.uniajc.dao.MateriaDao;
import com.uniajc.modelo.Docente;
import com.uniajc.modelo.Grupo;
import com.uniajc.modelo.Materia;
import com.uniajc.servicios.DocenteService;
import com.uniajc.servicios.GrupoService;
import com.uniajc.servicios.MateriaService;

public class VistaGrupoSwing extends JPanel implements ActionListener {

    private static final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    private static final Color COLOR_SECUNDARIO = new Color(236, 240, 241);
    private static final Color COLOR_ACENTO = new Color(231, 76, 60);
    private static final Color COLOR_EXITO = new Color(39, 174, 96);

    private static final String[] COLUMNAS = {"ID", "ID Mat", "Materia", "ID Doc", "Docente", "Aula", "Horario"};

    private final GrupoService grupoService;
    private final MateriaService materiaService;
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
    private JComboBox<MateriaItem> comboMateria;
    private JComboBox<DocenteItem> comboDocente;
    private JTextField campoAula;
    private JTextField campoHorario;
    private JButton botonGuardar;
    private boolean modoEdicion;
    private Grupo grupoSeleccionado;

    // Helper item classes for combo box selection
    private static class MateriaItem {
        final int id;
        final String nombre;
        MateriaItem(int id, String nombre) { this.id = id; this.nombre = nombre; }
        @Override public String toString() { return id + " - " + nombre; }
    }

    private static class DocenteItem {
        final int id;
        final String nombre;
        DocenteItem(int id, String nombre) { this.id = id; this.nombre = nombre; }
        @Override public String toString() { return id + " - " + nombre; }
    }

    public VistaGrupoSwing() {
        this.grupoService = new GrupoService(new GrupoDao(), new MateriaDao(), new DocenteDao());
        this.materiaService = new MateriaService(new MateriaDao());
        this.docenteService = new DocenteService(new DocenteDao());
        initComponents();
        cargarGrupos();
    }

    public VistaGrupoSwing(GrupoService grupoService, MateriaService materiaService, DocenteService docenteService) {
        this.grupoService = grupoService;
        this.materiaService = materiaService;
        this.docenteService = docenteService;
        initComponents();
        cargarGrupos();
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

        JLabel labelBusqueda = new JLabel("Buscar Grupo:");
        labelBusqueda.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelBusqueda.setForeground(COLOR_PRIMARIO);

        textoBusqueda = new JTextField(25);
        textoBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textoBusqueda.setPreferredSize(new Dimension(220, 30));

        botonBuscar = crearBoton("Buscar", COLOR_PRIMARIO);
        botonBuscar.addActionListener(e -> buscarGrupos());

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
                JOptionPane.showMessageDialog(this, "Seleccione un grupo para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        botonEliminar = crearBoton("Eliminar", COLOR_ACENTO);
        botonEliminar.addActionListener(e -> eliminarGrupo());

        botonActualizar = crearBoton("Actualizar", new Color(149, 165, 166));
        botonActualizar.addActionListener(e -> cargarGrupos());

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

    public void cargarGrupos() {
        modeloTabla.setRowCount(0);
        try {
            List<Grupo> grupos = grupoService.obtenerTodosLosGrupos();
            for (Grupo g : grupos) {
                String matNom = (g.getMateriaNombre() != null) ? g.getMateriaNombre() : "N/A";
                String docNom = (g.getDocenteNombre() != null) ? g.getDocenteNombre() : "N/A";
                modeloTabla.addRow(new Object[]{
                        g.getId(), g.getIdMateria(), matNom, g.getIdDocente(), docNom, g.getAula(), g.getHorario()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarGrupos() {
        modeloTabla.setRowCount(0);
        String busqueda = textoBusqueda.getText().trim().toLowerCase();

        if (busqueda.isEmpty()) {
            cargarGrupos();
            return;
        }

        try {
            List<Grupo> grupos = grupoService.obtenerTodosLosGrupos();
            for (Grupo g : grupos) {
                String matNom = (g.getMateriaNombre() != null) ? g.getMateriaNombre() : "";
                String docNom = (g.getDocenteNombre() != null) ? g.getDocenteNombre() : "";
                boolean coincide = String.valueOf(g.getId()).contains(busqueda) ||
                        matNom.toLowerCase().contains(busqueda) ||
                        docNom.toLowerCase().contains(busqueda) ||
                        g.getAula().toLowerCase().contains(busqueda) ||
                        g.getHorario().toLowerCase().contains(busqueda);
                if (coincide) {
                    modeloTabla.addRow(new Object[]{
                            g.getId(), g.getIdMateria(), matNom, g.getIdDocente(), docNom, g.getAula(), g.getHorario()
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoRegistro(boolean esEdicion) {
        modoEdicion = esEdicion;

        List<Materia> materias = materiaService.obtenerTodasLasMaterias();
        List<Docente> docentes = docenteService.obtenerTodosLosDocentes();

        if (materias.isEmpty() || docentes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe haber al menos una Materia y un Docente registrados para crear un grupo.",
                    "Datos Faltantes", JOptionPane.WARNING_MESSAGE);
            return;
        }

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
        dialogoFormulario.setSize(500, 340);
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
        JLabel labelTitulo = new JLabel(esEdicion ? "Editar Grupo" : "Registrar Grupo");
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelTitulo.setForeground(COLOR_PRIMARIO);
        panelFormulario.add(labelTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        JLabel labelMateria = new JLabel("Materia:");
        labelMateria.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0;
        panelFormulario.add(labelMateria, gbc);

        comboMateria = new JComboBox<>();
        for (Materia m : materias) {
            comboMateria.addItem(new MateriaItem(m.getId(), m.getNombre()));
        }
        gbc.gridx = 1;
        panelFormulario.add(comboMateria, gbc);

        gbc.gridy++;
        JLabel labelDocente = new JLabel("Docente:");
        labelDocente.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0;
        panelFormulario.add(labelDocente, gbc);

        comboDocente = new JComboBox<>();
        for (Docente d : docentes) {
            comboDocente.addItem(new DocenteItem(d.getId(), d.getNombre() + " (" + d.getEspecialidad() + ")"));
        }
        gbc.gridx = 1;
        panelFormulario.add(comboDocente, gbc);

        gbc.gridy++;
        JLabel labelAula = new JLabel("Aula:");
        labelAula.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0;
        panelFormulario.add(labelAula, gbc);

        campoAula = new JTextField(20);
        campoAula.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1;
        panelFormulario.add(campoAula, gbc);

        gbc.gridy++;
        JLabel labelHorario = new JLabel("Horario:");
        labelHorario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0;
        panelFormulario.add(labelHorario, gbc);

        campoHorario = new JTextField(20);
        campoHorario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1;
        panelFormulario.add(campoHorario, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.gridx = 0;

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setBackground(Color.WHITE);

        botonGuardar = crearBoton("Guardar", COLOR_EXITO);
        botonGuardar.addActionListener(e -> guardarGrupo());

        JButton botonCancelar = crearBoton("Cancelar", new Color(149, 165, 166));
        botonCancelar.addActionListener(e -> dialogoFormulario.dispose());

        panelBotones.add(botonGuardar);
        panelBotones.add(botonCancelar);
        panelFormulario.add(panelBotones, gbc);

        if (esEdicion && grupoSeleccionado != null) {
            for (int i = 0; i < comboMateria.getItemCount(); i++) {
                if (comboMateria.getItemAt(i).id == grupoSeleccionado.getIdMateria()) {
                    comboMateria.setSelectedIndex(i);
                    break;
                }
            }
            for (int i = 0; i < comboDocente.getItemCount(); i++) {
                if (comboDocente.getItemAt(i).id == grupoSeleccionado.getIdDocente()) {
                    comboDocente.setSelectedIndex(i);
                    break;
                }
            }
            campoAula.setText(grupoSeleccionado.getAula());
            campoHorario.setText(grupoSeleccionado.getHorario());
        }

        dialogoFormulario.add(panelFormulario);
        dialogoFormulario.setVisible(true);
    }

    private void guardarGrupo() {
        try {
            MateriaItem mat = (MateriaItem) comboMateria.getSelectedItem();
            DocenteItem doc = (DocenteItem) comboDocente.getSelectedItem();
            String aula = campoAula.getText().trim();
            String horario = campoHorario.getText().trim();

            if (mat == null || doc == null) {
                JOptionPane.showMessageDialog(dialogoFormulario, "Seleccione materia y docente.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            grupoSeleccionado.setIdMateria(mat.id);
            grupoSeleccionado.setIdDocente(doc.id);
            grupoSeleccionado.setAula(aula);
            grupoSeleccionado.setHorario(horario);

            if (modoEdicion) {
                grupoService.actualizarGrupo(grupoSeleccionado);
                JOptionPane.showMessageDialog(dialogoFormulario, "Grupo actualizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                grupoService.registrarGrupo(grupoSeleccionado);
                JOptionPane.showMessageDialog(dialogoFormulario, "Grupo registrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }

            dialogoFormulario.dispose();
            cargarGrupos();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(dialogoFormulario, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialogoFormulario, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarGrupo() {
        if (tabla.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un grupo de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el grupo seleccionado?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = (int) modeloTabla.getValueAt(tabla.getSelectedRow(), 0);
                grupoService.eliminarGrupo(id);
                JOptionPane.showMessageDialog(this, "Grupo eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarGrupos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}
