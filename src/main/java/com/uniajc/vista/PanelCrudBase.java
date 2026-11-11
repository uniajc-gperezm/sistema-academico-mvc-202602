package com.uniajc.vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
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

/**
 * Base genérica para los paneles CRUD de cada entidad.
 * Maneja búsqueda, tabla, paginación en memoria y diálogo de registro/edición.
 *
 * @param <T> tipo de la entidad
 */
public abstract class PanelCrudBase<T> extends JPanel {

    protected static final int FILAS_POR_PAGINA = 5;

    protected JTable tabla;
    protected DefaultTableModel modeloTabla;
    protected JTextField textoBusqueda;
    protected JLabel labelPagina;
    protected JButton botonAnterior;
    protected JButton botonSiguiente;

    protected List<T> todos = new ArrayList<>();
    protected List<T> visibles = new ArrayList<>();
    protected int paginaActual = 0;

    protected JDialog dialogoFormulario;
    protected boolean modoEdicion;
    protected T entidadSeleccionada;

    public PanelCrudBase() {
        inicializarComponentes();
    }

    // ---- Métodos abstractos que implementa cada entidad ----
    protected abstract String[] getColumnas();
    protected abstract String getTitulo();
    protected abstract List<T> obtenerTodos();
    protected abstract Object[] aFila(T entidad);
    protected abstract T nuevoDesdeFormulario();
    protected abstract void aplicarEnFormulario(T entidad);
    protected abstract void limpiarFormulario();
    protected abstract boolean validarFormulario();
    protected abstract JPanel crearPanelFormulario();

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UiUtil.SECUNDARIO);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        add(crearPanelBusqueda(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        panel.setBackground(UiUtil.SECUNDARIO);

        JLabel label = UiUtil.etiqueta("Buscar:", UiUtil.PRIMARIO);
        textoBusqueda = UiUtil.campo(18);

        JButton botonBuscar = UiUtil.boton("Buscar", UiUtil.PRIMARIO);
        botonBuscar.addActionListener(e -> buscar());

        JButton botonLimpiar = UiUtil.boton("Limpiar", UiUtil.GRIS);
        botonLimpiar.addActionListener(e -> {
            textoBusqueda.setText("");
            recargar();
        });

        panel.add(label);
        panel.add(textoBusqueda);
        panel.add(botonBuscar);
        panel.add(botonLimpiar);
        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UiUtil.SECUNDARIO);

        modeloTabla = new DefaultTableModel(getColumnas(), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setFont(UiUtil.FUENTE);
        tabla.setRowHeight(28);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setGridColor(new java.awt.Color(210, 210, 225));
        tabla.setBackground(java.awt.Color.WHITE);
        tabla.setSelectionBackground(UiUtil.PRIMARIO_CLARO);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(UiUtil.PRIMARIO);
        tabla.getTableHeader().setForeground(java.awt.Color.WHITE);

        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centro);
        }

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(UiUtil.PRIMARIO, 2));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout(10, 6));
        panel.setBackground(UiUtil.SECUNDARIO);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        botones.setBackground(UiUtil.SECUNDARIO);

        JButton botonRegistrar = UiUtil.boton("Registrar", UiUtil.EXITO);
        botonRegistrar.addActionListener(e -> mostrarDialogoRegistro(false));

        JButton botonEditar = UiUtil.boton("Editar", UiUtil.PRIMARIO);
        botonEditar.addActionListener(e -> {
            if (filaSeleccionada() < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione una fila.", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            mostrarDialogoRegistro(true);
        });

        JButton botonEliminar = UiUtil.boton("Eliminar", UiUtil.PELIGRO);
        botonEliminar.addActionListener(e -> eliminar());

        JButton botonActualizar = UiUtil.boton("Actualizar", UiUtil.GRIS);
        botonActualizar.addActionListener(e -> recargar());

        botones.add(botonRegistrar);
        botones.add(botonEditar);
        botones.add(botonEliminar);
        botones.add(botonActualizar);

        JPanel paginacion = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        paginacion.setBackground(UiUtil.SECUNDARIO);

        botonAnterior = UiUtil.boton("Anterior", UiUtil.GRIS);
        botonAnterior.addActionListener(e -> {
            if (paginaActual > 0) {
                paginaActual--;
                renderizarPagina();
            }
        });

        botonSiguiente = UiUtil.boton("Siguiente", UiUtil.GRIS);
        botonSiguiente.addActionListener(e -> {
            int totalPaginas = totalPaginas();
            if (paginaActual + 1 < totalPaginas) {
                paginaActual++;
                renderizarPagina();
            }
        });

        labelPagina = new JLabel();
        labelPagina.setFont(UiUtil.FUENTE_NEGRITA);
        labelPagina.setForeground(UiUtil.PRIMARIO);

        paginacion.add(botonAnterior);
        paginacion.add(labelPagina);
        paginacion.add(botonSiguiente);

        panel.add(botones, BorderLayout.WEST);
        panel.add(paginacion, BorderLayout.EAST);
        return panel;
    }

    private int filaSeleccionada() {
        return tabla.getSelectedRow();
    }

    private int totalPaginas() {
        if (visibles.isEmpty()) return 1;
        return (int) Math.ceil((double) visibles.size() / FILAS_POR_PAGINA);
    }

    private void renderizarPagina() {
        modeloTabla.setRowCount(0);
        int inicio = paginaActual * FILAS_POR_PAGINA;
        int fin = Math.min(inicio + FILAS_POR_PAGINA, visibles.size());
        for (int i = inicio; i < fin; i++) {
            modeloTabla.addRow(aFila(visibles.get(i)));
        }
        labelPagina.setText("Página " + (paginaActual + 1) + " de " + totalPaginas());
        botonAnterior.setEnabled(paginaActual > 0);
        botonSiguiente.setEnabled(paginaActual + 1 < totalPaginas());
    }

    /** Devuelve cuántas filas hay visibles (para pruebas). */
    public int contarFilasVisibles() {
        return visibles.size();
    }

    public void recargar() {
        try {
            todos = obtenerTodos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando datos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            todos = new ArrayList<>();
        }
        visibles = new ArrayList<>(todos);
        paginaActual = 0;
        renderizarPagina();
    }

    private void buscar() {
        String criterio = textoBusqueda.getText().trim().toLowerCase();
        visibles = new ArrayList<>();
        if (criterio.isEmpty()) {
            visibles.addAll(todos);
        } else {
            for (T entidad : todos) {
                StringBuilder sb = new StringBuilder();
                for (Object campo : aFila(entidad)) {
                    sb.append(campo).append(' ');
                }
                if (sb.toString().toLowerCase().contains(criterio)) {
                    visibles.add(entidad);
                }
            }
        }
        paginaActual = 0;
        renderizarPagina();
    }

    private void eliminar() {
        int fila = filaSeleccionada();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila.", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el registro seleccionado?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            T entidad = visibles.get(fila);
            eliminarPorEntidad(entidad);
            JOptionPane.showMessageDialog(this, "Registro eliminado.", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            recargar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Subclase: elimina la entidad según su id. */
    protected abstract void eliminarPorEntidad(T entidad);

    private void mostrarDialogoRegistro(boolean esEdicion) {
        modoEdicion = esEdicion;

        if (esEdicion) {
            int fila = filaSeleccionada();
            entidadSeleccionada = visibles.get(fila);
        } else {
            entidadSeleccionada = null;
        }

        JPanel formulario = crearPanelFormulario();
        dialogoFormulario = new JDialog(
                (java.awt.Frame) SwingUtilities.getWindowAncestor(this),
                (esEdicion ? "Editar " : "Registrar ") + getTitulo(), true);
        dialogoFormulario.setSize(480, 360);
        dialogoFormulario.setLocationRelativeTo(this);
        dialogoFormulario.setResizable(false);

        JPanel contenido = new JPanel(new BorderLayout(12, 12));
        contenido.setBackground(java.awt.Color.WHITE);
        contenido.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        JLabel titulo = new JLabel((esEdicion ? "✎ Editar " : "＋ Registrar ") + getTitulo());
        titulo.setFont(UiUtil.FUENTE_TITULO);
        titulo.setForeground(UiUtil.PRIMARIO);
        contenido.add(titulo, BorderLayout.NORTH);
        contenido.add(formulario, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 6));
        botones.setBackground(java.awt.Color.WHITE);

        JButton botonGuardar = UiUtil.boton("Guardar", UiUtil.EXITO);
        botonGuardar.addActionListener(e -> guardarDesdeFormulario());

        JButton botonCancelar = UiUtil.boton("Cancelar", UiUtil.GRIS);
        botonCancelar.addActionListener(e -> dialogoFormulario.dispose());

        botones.add(botonGuardar);
        botones.add(botonCancelar);
        contenido.add(botones, BorderLayout.SOUTH);

        if (esEdicion) {
            aplicarEnFormulario(entidadSeleccionada);
        } else {
            limpiarFormulario();
        }

        dialogoFormulario.add(contenido);
        dialogoFormulario.setVisible(true);
    }

    private void guardarDesdeFormulario() {
        if (!validarFormulario()) {
            return;
        }
        try {
            if (modoEdicion) {
                actualizarEntidad(entidadSeleccionada);
                JOptionPane.showMessageDialog(dialogoFormulario, getTitulo() + " actualizado.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                T nueva = nuevoDesdeFormulario();
                guardarEntidad(nueva);
                JOptionPane.showMessageDialog(dialogoFormulario, getTitulo() + " registrado.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
            dialogoFormulario.dispose();
            recargar();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(dialogoFormulario, ex.getMessage(), "Validación",
                    JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialogoFormulario, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected abstract void guardarEntidad(T entidad);
    protected abstract void actualizarEntidad(T entidad);
}
