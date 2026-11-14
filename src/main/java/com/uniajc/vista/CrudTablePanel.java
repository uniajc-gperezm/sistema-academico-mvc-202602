package com.uniajc.vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/** Panel reutilizable de tabla y acciones CRUD para una entidad. */
public final class CrudTablePanel<T> extends JPanel {
    private final Supplier<List<T>> listar;
    private final Function<T, Object[]> convertirFila;
    private final Function<T, T> mostrarFormulario;
    private final Predicate<T> crear;
    private final Predicate<T> actualizar;
    private final Predicate<T> eliminar;
    private final List<T> registros = new ArrayList<>();
    private final DefaultTableModel modelo;
    private final JTable tabla;

    public CrudTablePanel(String[] columnas,
                          Supplier<List<T>> listar,
                          Function<T, Object[]> convertirFila,
                          Function<T, T> mostrarFormulario,
                          Predicate<T> crear,
                          Predicate<T> actualizar,
                          Predicate<T> eliminar) {
        super(new BorderLayout(0, 10));
        this.listar = listar;
        this.convertirFila = convertirFila;
        this.mostrarFormulario = mostrarFormulario;
        this.crear = crear;
        this.actualizar = actualizar;
        this.eliminar = eliminar;

        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setFillsViewportHeight(true);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton nuevo = new JButton("Crear");
        nuevo.addActionListener(event -> crearRegistro());
        JButton editar = new JButton("Editar");
        editar.addActionListener(event -> editarRegistro());
        JButton borrar = new JButton("Eliminar");
        borrar.addActionListener(event -> eliminarRegistro());
        JButton recargar = new JButton("Actualizar lista");
        recargar.addActionListener(event -> recargar());
        acciones.add(nuevo);
        acciones.add(editar);
        acciones.add(borrar);
        acciones.add(recargar);
        add(acciones, BorderLayout.SOUTH);
        recargar();
    }

    // Pide la lista al servicio y reemplaza las filas visibles de la tabla.
    private void recargar() {
        try {
            registros.clear();
            registros.addAll(listar.get());
            modelo.setRowCount(0);
            for (T registro : registros) modelo.addRow(convertirFila.apply(registro));
        } catch (RuntimeException error) {
            mostrarError(error);
        }
    }

    private void crearRegistro() {
        try {
            T registro = mostrarFormulario.apply(null);
            if (registro != null && crear.test(registro)) {
                recargar();
                JOptionPane.showMessageDialog(this, "Registro creado correctamente.");
            }
        } catch (RuntimeException error) {
            mostrarError(error);
        }
    }

    private void editarRegistro() {
        T seleccionado = seleccionado();
        if (seleccionado == null) return;
        try {
            T editado = mostrarFormulario.apply(seleccionado);
            if (editado != null && actualizar.test(editado)) {
                recargar();
                JOptionPane.showMessageDialog(this, "Registro actualizado correctamente.");
            } else if (editado != null) {
                JOptionPane.showMessageDialog(this, "No se encontró el registro seleccionado.");
            }
        } catch (RuntimeException error) {
            mostrarError(error);
        }
    }

    private void eliminarRegistro() {
        T seleccionado = seleccionado();
        if (seleccionado == null) return;
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Eliminar el registro seleccionado?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (respuesta != JOptionPane.YES_OPTION) return;
        try {
            if (eliminar.test(seleccionado)) {
                recargar();
                JOptionPane.showMessageDialog(this, "Registro eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el registro seleccionado.");
            }
        } catch (RuntimeException error) {
            mostrarError(error);
        }
    }

    private T seleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila primero.");
            return null;
        }
        return registros.get(fila);
    }

    private void mostrarError(RuntimeException error) {
        JOptionPane.showMessageDialog(this, error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
