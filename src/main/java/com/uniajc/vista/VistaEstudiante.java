package com.uniajc.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import com.uniajc.controlador.ControladorEstudiante;
import com.uniajc.modelo.Estudiante;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/** Interfaz Swing para consultar, crear, editar y eliminar estudiantes. */
public class VistaEstudiante {
    private final JFrame ventana;
    private final DefaultTableModel modeloTabla;
    private final ControladorEstudiante controlador;
    private final JLabel totalEstudiantes;

    public VistaEstudiante() {
        ventana = new JFrame("Dashboard académico");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(1050, 560);
        ventana.setLocationRelativeTo(null);
        controlador = new ControladorEstudiante();

        JLabel titulo = new JLabel("Dashboard académico");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));

        modeloTabla = new DefaultTableModel(
                new String[] { "ID", "Nombre", "Apellido", "Correo electrónico", "NIT", "Celular" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tablaEstudiantes = new JTable(modeloTabla);
        tablaEstudiantes.setFillsViewportHeight(true);

        // Cada botón conecta una acción de usuario con el método correspondiente.
        JButton botonCrear = new JButton("Nuevo estudiante");
        botonCrear.addActionListener(event -> crearEstudiante());
        JButton botonEditar = new JButton("Editar");
        botonEditar.addActionListener(event -> editarEstudiante(tablaEstudiantes));
        JButton botonEliminar = new JButton("Eliminar");
        botonEliminar.addActionListener(event -> eliminarEstudiante(tablaEstudiantes));
        JButton botonActualizar = new JButton("Actualizar lista");
        botonActualizar.addActionListener(event -> cargarEstudiantes());

        JLabel etiquetaTotal = new JLabel("ESTUDIANTES REGISTRADOS");
        etiquetaTotal.setFont(new Font("SansSerif", Font.BOLD, 12));
        totalEstudiantes = new JLabel("0");
        totalEstudiantes.setFont(new Font("SansSerif", Font.BOLD, 28));

        JPanel tarjetaTotal = new JPanel(new BorderLayout(8, 8));
        tarjetaTotal.setBackground(new Color(239, 246, 255));
        tarjetaTotal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(191, 219, 254)),
                BorderFactory.createEmptyBorder(14, 18, 14, 18)));
        tarjetaTotal.add(etiquetaTotal, BorderLayout.NORTH);
        tarjetaTotal.add(totalEstudiantes, BorderLayout.CENTER);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acciones.add(botonCrear);
        acciones.add(botonEditar);
        acciones.add(botonEliminar);
        acciones.add(botonActualizar);

        JPanel resumen = new JPanel(new BorderLayout(12, 12));
        resumen.add(tarjetaTotal, BorderLayout.WEST);
        resumen.add(acciones, BorderLayout.CENTER);

        JPanel encabezado = new JPanel(new BorderLayout(0, 14));
        encabezado.add(titulo, BorderLayout.NORTH);
        encabezado.add(resumen, BorderLayout.CENTER);

        JLabel tituloTabla = new JLabel("Estudiantes");
        tituloTabla.setFont(new Font("SansSerif", Font.BOLD, 16));
        JPanel panelTabla = new JPanel(new BorderLayout(0, 8));
        panelTabla.add(tituloTabla, BorderLayout.NORTH);
        panelTabla.add(new JScrollPane(tablaEstudiantes), BorderLayout.CENTER);

        JPanel contenido = new JPanel(new BorderLayout(0, 18));
        contenido.add(encabezado, BorderLayout.NORTH);
        contenido.add(panelTabla, BorderLayout.CENTER);
        contenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        ventana.add(contenido);
        cargarEstudiantes();
    }

    public void mostrar() {
        ventana.setVisible(true);
    }

    // CREATE: captura los datos, los envía al controlador y recarga la tabla.
    private void crearEstudiante() {
        Estudiante estudiante = mostrarFormulario(null);
        if (estudiante == null) {
            return;
        }
        try {
            if (controlador.crearEstudiante(estudiante)) {
                cargarEstudiantes();
                JOptionPane.showMessageDialog(ventana, "Estudiante creado correctamente.");
            }
        } catch (RuntimeException error) {
            mostrarError(error);
        }
    }

    // UPDATE: precarga la fila seleccionada y envía los cambios al controlador.
    private void editarEstudiante(JTable tabla) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(ventana, "Selecciona un estudiante para editar.");
            return;
        }

        Estudiante original = new Estudiante(
                ((Number) modeloTabla.getValueAt(fila, 0)).longValue(),
                (String) modeloTabla.getValueAt(fila, 1),
                (String) modeloTabla.getValueAt(fila, 2),
                (String) modeloTabla.getValueAt(fila, 3),
                numeroDeFila(fila, 4),
                numeroDeFila(fila, 5));
        Estudiante editado = mostrarFormulario(original);
        if (editado == null) {
            return;
        }
        try {
            if (controlador.actualizarEstudiante(editado)) {
                cargarEstudiantes();
                JOptionPane.showMessageDialog(ventana, "Estudiante actualizado correctamente.");
            } else {
                JOptionPane.showMessageDialog(ventana, "No se encontró el estudiante seleccionado.");
            }
        } catch (RuntimeException error) {
            mostrarError(error);
        }
    }

    // DELETE: confirma la acción antes de pedir al controlador que elimine por ID.
    private void eliminarEstudiante(JTable tabla) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(ventana, "Selecciona un estudiante para eliminar.");
            return;
        }
        long id = ((Number) modeloTabla.getValueAt(fila, 0)).longValue();
        int confirmacion = JOptionPane.showConfirmDialog(
                ventana,
                "¿Deseas eliminar el estudiante seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (controlador.eliminarEstudiante(id)) {
                cargarEstudiantes();
                JOptionPane.showMessageDialog(ventana, "Estudiante eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(ventana, "No se encontró el estudiante seleccionado.");
            }
        } catch (RuntimeException error) {
            mostrarError(error);
        }
    }

    // Construye el formulario compartido por alta y edición y valida campos obligatorios.
    private Estudiante mostrarFormulario(Estudiante existente) {
        JTextField nombre = new JTextField(existente == null ? "" : existente.getName());
        JTextField apellido = new JTextField(existente == null ? "" : existente.getLastname());
        JTextField correo = new JTextField(existente == null ? "" : existente.getEmail());
        JTextField nit = new JTextField(textoNumero(existente == null ? null : existente.getNit()));
        JTextField celular = new JTextField(textoNumero(existente == null ? null : existente.getNumcelular()));

        JPanel formulario = new JPanel(new GridLayout(0, 2, 8, 8));
        formulario.add(new JLabel("Nombre:"));
        formulario.add(nombre);
        formulario.add(new JLabel("Apellido:"));
        formulario.add(apellido);
        formulario.add(new JLabel("Correo electrónico:"));
        formulario.add(correo);
        formulario.add(new JLabel("NIT (opcional):"));
        formulario.add(nit);
        formulario.add(new JLabel("Celular (opcional):"));
        formulario.add(celular);

        String titulo = existente == null ? "Nuevo estudiante" : "Editar estudiante";
        int resultado = JOptionPane.showConfirmDialog(
                ventana, formulario, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (resultado != JOptionPane.OK_OPTION) {
            return null;
        }

        String nombreValor = nombre.getText().trim();
        String apellidoValor = apellido.getText().trim();
        String correoValor = correo.getText().trim();
        if (nombreValor.isEmpty() || apellidoValor.isEmpty() || correoValor.isEmpty()) {
            JOptionPane.showMessageDialog(ventana, "Todos los campos son obligatorios.");
            return null;
        }

        Long nitValor;
        Long celularValor;
        try {
            nitValor = convertirNumeroOpcional(nit.getText(), "NIT");
            celularValor = convertirNumeroOpcional(celular.getText(), "celular");
        } catch (IllegalArgumentException error) {
            JOptionPane.showMessageDialog(ventana, error.getMessage());
            return null;
        }

        Estudiante estudiante = new Estudiante(nombreValor, apellidoValor, correoValor, nitValor, celularValor);
        if (existente != null) {
            estudiante.setId(existente.getId());
        }
        return estudiante;
    }

    private void mostrarError(RuntimeException error) {
        JOptionPane.showMessageDialog(
                ventana,
                "No se pudo completar la operación: " + error.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    // Permite dejar vacío un número o valida que se hayan escrito solo dígitos.
    private Long convertirNumeroOpcional(String texto, String campo) {
        String valor = texto.trim();
        if (valor.isEmpty()) {
            return null;
        }
        if (!valor.matches("\\d+")) {
            throw new IllegalArgumentException("El campo " + campo + " debe contener solo números.");
        }
        try {
            return Long.valueOf(valor);
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("El valor de " + campo + " es demasiado largo.");
        }
    }

    // Obtiene un valor numérico opcional de la fila actualmente seleccionada.
    private Long numeroDeFila(int fila, int columna) {
        Object valor = modeloTabla.getValueAt(fila, columna);
        return valor instanceof Number numero ? numero.longValue() : null;
    }

    private String textoNumero(Long valor) {
        return valor == null ? "" : valor.toString();
    }

    // READ: convierte los registros del controlador en filas visibles de la tabla.
    private void cargarEstudiantes() {
        try {
            List<Estudiante> estudiantes = controlador.listarEstudiantes();
            modeloTabla.setRowCount(0);
            for (Estudiante estudiante : estudiantes) {
                modeloTabla.addRow(new Object[] {
                        estudiante.getId(),
                        estudiante.getName(),
                        estudiante.getLastname(),
                        estudiante.getEmail(),
                        estudiante.getNit(),
                        estudiante.getNumcelular()
                });
            }
            totalEstudiantes.setText(String.valueOf(estudiantes.size()));
        } catch (RuntimeException error) {
            mostrarError(error);
        }
    }
}
