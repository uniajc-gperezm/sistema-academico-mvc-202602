package com.uniajc.vista;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.uniajc.dao.EstudianteDao;
import com.uniajc.modelo.Estudiante;
import com.uniajc.servicios.EstudianteService;

/** Panel CRUD de estudiantes. */
public class PanelEstudiante extends PanelCrudBase<Estudiante> {

    private static final String[] COLUMNAS = {"ID", "Nombre", "Apellido", "Email"};

    private final EstudianteService service = new EstudianteService(new EstudianteDao());

    private JTextField campoNombre;
    private JTextField campoApellido;
    private JTextField campoEmail;

    public PanelEstudiante() {
        recargar();
    }

    @Override
    protected String[] getColumnas() {
        return COLUMNAS;
    }

    @Override
    protected String getTitulo() {
        return "Estudiante";
    }

    @Override
    protected List<Estudiante> obtenerTodos() {
        return service.obtenerTodosLosEstudiantes();
    }

    @Override
    protected Object[] aFila(Estudiante e) {
        return new Object[]{e.getId_estudiante(), e.getNombre(), e.getApellido(), e.getEmail()};
    }

    @Override
    protected void guardarEntidad(Estudiante e) {
        service.registrarEstudiante(e);
    }

    @Override
    protected void actualizarEntidad(Estudiante e) {
        service.actualizarEstudiante(e);
    }

    @Override
    protected void eliminarPorEntidad(Estudiante e) {
        service.eliminarEstudiante(e.getId_estudiante());
    }

    @Override
    protected JPanel crearPanelFormulario() {
        campoNombre = UiUtil.campo(20);
        campoApellido = UiUtil.campo(20);
        campoEmail = UiUtil.campo(20);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(java.awt.Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        panel.add(UiUtil.etiqueta("Nombre:", UiUtil.PRIMARIO), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(campoNombre, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        panel.add(UiUtil.etiqueta("Apellido:", UiUtil.PRIMARIO), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(campoApellido, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        panel.add(UiUtil.etiqueta("Email:", UiUtil.PRIMARIO), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(campoEmail, gbc);

        return panel;
    }

    @Override
    protected Estudiante nuevoDesdeFormulario() {
        Estudiante e = new Estudiante();
        e.setNombre(campoNombre.getText().trim());
        e.setApellido(campoApellido.getText().trim());
        e.setEmail(campoEmail.getText().trim());
        return e;
    }

    @Override
    protected void aplicarEnFormulario(Estudiante e) {
        campoNombre.setText(e.getNombre());
        campoApellido.setText(e.getApellido());
        campoEmail.setText(e.getEmail());
    }

    @Override
    protected void limpiarFormulario() {
        campoNombre.setText("");
        campoApellido.setText("");
        campoEmail.setText("");
    }

    @Override
    protected boolean validarFormulario() {
        if (campoNombre.getText().trim().isEmpty()
                || campoApellido.getText().trim().isEmpty()
                || campoEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
