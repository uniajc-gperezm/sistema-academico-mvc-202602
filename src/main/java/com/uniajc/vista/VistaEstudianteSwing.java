package com.uniajc.vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.uniajc.controlador.ControladorEstudiante;
import com.uniajc.dao.EstudianteDao;
import com.uniajc.modelo.Estudiante;
import com.uniajc.servicios.EstudianteService;

public class VistaEstudianteSwing extends JFrame {

    private final ControladorEstudiante controlador =
        new ControladorEstudiante(new EstudianteService(new EstudianteDao()));

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
        new String[] {"ID", "Nombre", "Apellido", "Email"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modeloTabla);
    private final JTextField txtBuscar = new JTextField(20);

    public VistaEstudianteSwing() {
        setTitle("Gestión de Estudiantes");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(24);

        add(crearPanelBusqueda(), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);

        cargar();
    }

    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> cargar());
        panel.add(new JLabel("Buscar:"));
        panel.add(txtBuscar);
        panel.add(btnBuscar);
        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(e -> registrar());

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editar());

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminar());

        JButton btnActualizar = new JButton("Actualizar lista");
        btnActualizar.addActionListener(e -> {
            txtBuscar.setText("");
            cargar();
        });

        panel.add(btnRegistrar);
        panel.add(btnEditar);
        panel.add(btnEliminar);
        panel.add(btnActualizar);
        return panel;
    }

    private void cargar() {
        modeloTabla.setRowCount(0);
        String filtro = txtBuscar.getText().trim().toLowerCase();
        try {
            List<Estudiante> lista = controlador.listar();
            for (Estudiante e : lista) {
                boolean coincide = filtro.isEmpty()
                    || String.valueOf(e.getId()).contains(filtro)
                    || e.getNombre().toLowerCase().contains(filtro)
                    || e.getApellido().toLowerCase().contains(filtro)
                    || e.getEmail().toLowerCase().contains(filtro);
                if (coincide) {
                    modeloTabla.addRow(new Object[] {
                        e.getId(), e.getNombre(), e.getApellido(), e.getEmail()});
                }
            }
        } catch (RuntimeException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void registrar() {
        String[] datos = pedirDatos("Registrar estudiante", "", "", "");
        if (datos == null) {
            return;
        }
        String error = controlador.registrar(datos[0], datos[1], datos[2]);
        resultado(error, "Estudiante registrado.");
    }

    private void editar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String[] datos = pedirDatos("Editar estudiante",
            (String) modeloTabla.getValueAt(fila, 1),
            (String) modeloTabla.getValueAt(fila, 2),
            (String) modeloTabla.getValueAt(fila, 3));
        if (datos == null) {
            return;
        }
        String error = controlador.actualizar(id, datos[0], datos[1], datos[2]);
        resultado(error, "Estudiante actualizado.");
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila.",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        int opcion = JOptionPane.showConfirmDialog(this,
            "¿Eliminar al estudiante con ID " + id + "?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            resultado(controlador.eliminar(id), "Estudiante eliminado.");
        }
    }

    /** Muestra el formulario; devuelve {nombre, apellido, email} o null si se cancela. */
    private String[] pedirDatos(String titulo, String nombre, String apellido, String email) {
        JTextField txtNombre = new JTextField(nombre, 20);
        JTextField txtApellido = new JTextField(apellido, 20);
        JTextField txtEmail = new JTextField(email, 20);

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(new JLabel("Apellido:"));
        form.add(txtApellido);
        form.add(new JLabel("Email:"));
        form.add(txtEmail);

        int opcion = JOptionPane.showConfirmDialog(this, form, titulo,
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) {
            return null;
        }
        return new String[] {txtNombre.getText(), txtApellido.getText(), txtEmail.getText()};
    }

    private void resultado(String error, String exito) {
        if (error == null) {
            JOptionPane.showMessageDialog(this, exito, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargar();
        } else {
            mostrarError(error);
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}