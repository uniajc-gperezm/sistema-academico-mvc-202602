package com.uniajc.vista;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.uniajc.dao.MateriaDao;
import com.uniajc.modelo.Materia;
import com.uniajc.servicios.MateriaService;

/** Panel CRUD de materias. */
public class PanelMateria extends PanelCrudBase<Materia> {

    private static final String[] COLUMNAS = {"ID", "Nombre", "Créditos"};

    private final MateriaService service = new MateriaService(new MateriaDao());

    private JTextField campoNombre;
    private JTextField campoCreditos;

    public PanelMateria() {
        recargar();
    }

    @Override
    protected String[] getColumnas() {
        return COLUMNAS;
    }

    @Override
    protected String getTitulo() {
        return "Materia";
    }

    @Override
    protected List<Materia> obtenerTodos() {
        return service.obtenerTodasLasMaterias();
    }

    @Override
    protected Object[] aFila(Materia m) {
        return new Object[]{m.getId_materia(), m.getNombre_materia(), m.getCreditos()};
    }

    @Override
    protected void guardarEntidad(Materia m) {
        service.registrarMateria(m);
    }

    @Override
    protected void actualizarEntidad(Materia m) {
        service.actualizarMateria(m);
    }

    @Override
    protected void eliminarPorEntidad(Materia m) {
        service.eliminarMateria(m.getId_materia());
    }

    @Override
    protected JPanel crearPanelFormulario() {
        campoNombre = UiUtil.campo(20);
        campoCreditos = UiUtil.campo(20);

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
        panel.add(UiUtil.etiqueta("Créditos:", UiUtil.PRIMARIO), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(campoCreditos, gbc);

        return panel;
    }

    @Override
    protected Materia nuevoDesdeFormulario() {
        Materia m = new Materia();
        m.setNombre_materia(campoNombre.getText().trim());
        m.setCreditos(Integer.parseInt(campoCreditos.getText().trim()));
        return m;
    }

    @Override
    protected void aplicarEnFormulario(Materia m) {
        campoNombre.setText(m.getNombre_materia());
        campoCreditos.setText(String.valueOf(m.getCreditos()));
    }

    @Override
    protected void limpiarFormulario() {
        campoNombre.setText("");
        campoCreditos.setText("");
    }

    @Override
    protected boolean validarFormulario() {
        String nombre = campoNombre.getText().trim();
        String creditos = campoCreditos.getText().trim();
        if (nombre.isEmpty() || creditos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            int c = Integer.parseInt(creditos);
            if (c < 1 || c > 10) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Los créditos deben ser un número entre 1 y 10.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
