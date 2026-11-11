package com.uniajc.vista;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.uniajc.dao.DocenteDao;
import com.uniajc.modelo.Docente;
import com.uniajc.servicios.DocenteService;

/** Panel CRUD de docentes. */
public class PanelDocente extends PanelCrudBase<Docente> {

    private static final String[] COLUMNAS = {"ID", "Nombre", "Especialidad"};

    private final DocenteService service = new DocenteService(new DocenteDao());

    private JTextField campoNombre;
    private JTextField campoEspecialidad;

    public PanelDocente() {
        recargar();
    }

    @Override
    protected String[] getColumnas() {
        return COLUMNAS;
    }

    @Override
    protected String getTitulo() {
        return "Docente";
    }

    @Override
    protected List<Docente> obtenerTodos() {
        return service.obtenerTodosLosDocentes();
    }

    @Override
    protected Object[] aFila(Docente d) {
        return new Object[]{d.getId_docente(), d.getNombre(), d.getEspecialidad()};
    }

    @Override
    protected void guardarEntidad(Docente d) {
        service.registrarDocente(d);
    }

    @Override
    protected void actualizarEntidad(Docente d) {
        service.actualizarDocente(d);
    }

    @Override
    protected void eliminarPorEntidad(Docente d) {
        service.eliminarDocente(d.getId_docente());
    }

    @Override
    protected JPanel crearPanelFormulario() {
        campoNombre = UiUtil.campo(20);
        campoEspecialidad = UiUtil.campo(20);

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
        panel.add(UiUtil.etiqueta("Especialidad:", UiUtil.PRIMARIO), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(campoEspecialidad, gbc);

        return panel;
    }

    @Override
    protected Docente nuevoDesdeFormulario() {
        Docente d = new Docente();
        d.setNombre(campoNombre.getText().trim());
        d.setEspecialidad(campoEspecialidad.getText().trim());
        return d;
    }

    @Override
    protected void aplicarEnFormulario(Docente d) {
        campoNombre.setText(d.getNombre());
        campoEspecialidad.setText(d.getEspecialidad());
    }

    @Override
    protected void limpiarFormulario() {
        campoNombre.setText("");
        campoEspecialidad.setText("");
    }

    @Override
    protected boolean validarFormulario() {
        if (campoNombre.getText().trim().isEmpty()
                || campoEspecialidad.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
