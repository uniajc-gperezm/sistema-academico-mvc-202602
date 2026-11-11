package com.uniajc.vista;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.uniajc.dao.EstudianteDao;
import com.uniajc.dao.GrupoDao;
import com.uniajc.dao.InscripcionDao;
import com.uniajc.modelo.Estudiante;
import com.uniajc.modelo.Grupo;
import com.uniajc.modelo.InscripcionCurso;
import com.uniajc.servicios.InscripcionService;

/** Panel CRUD de inscripciones. Combos de estudiante y grupo, nota y estado. */
public class PanelInscripcion extends PanelCrudBase<InscripcionCurso> {

    private static final String[] COLUMNAS = {"ID", "Estudiante", "Grupo", "Nota", "Estado"};
    private static final String[] ESTADOS = {"ACTIVO", "APROBADO", "REPROBADO", "CANCELADO"};

    private final InscripcionService service = new InscripcionService(new InscripcionDao());
    private final Map<Integer, String> nombreEstudiantes = new HashMap<>();
    private final Map<Integer, String> nombreGrupos = new HashMap<>();

    private JComboBox<ItemCombo> comboEstudiantes;
    private JComboBox<ItemCombo> comboGrupos;
    private JTextField campoNota;
    private JComboBox<String> comboEstado;

    public PanelInscripcion() {
        cargarMapas();
        recargar();
    }

    private void cargarMapas() {
        try {
            for (Estudiante e : new EstudianteDao().obtenerTodos()) {
                nombreEstudiantes.put(e.getId_estudiante(), e.getNombre() + " " + e.getApellido());
            }
        } catch (Exception ignored) {
        }
        try {
            for (Grupo g : new GrupoDao().obtenerTodos()) {
                nombreGrupos.put(g.getId_grupo(), "Grupo " + g.getId_grupo());
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    protected String[] getColumnas() {
        return COLUMNAS;
    }

    @Override
    protected String getTitulo() {
        return "Inscripción";
    }

    @Override
    protected List<InscripcionCurso> obtenerTodos() {
        return service.obtenerTodasLasInscripciones();
    }

    @Override
    protected Object[] aFila(InscripcionCurso i) {
        String estudiante = nombreEstudiantes.getOrDefault(i.getId_estudiante(),
                "ID " + i.getId_estudiante());
        String grupo = nombreGrupos.getOrDefault(i.getId_grupo(), "ID " + i.getId_grupo());
        return new Object[]{i.getId_inscripcion(), estudiante, grupo,
                i.getNota_final(), i.getEstado()};
    }

    @Override
    protected void guardarEntidad(InscripcionCurso i) {
        service.registrarInscripcion(i);
    }

    @Override
    protected void actualizarEntidad(InscripcionCurso i) {
        service.actualizarInscripcion(i);
    }

    @Override
    protected void eliminarPorEntidad(InscripcionCurso i) {
        service.eliminarInscripcion(i.getId_inscripcion());
    }

    @Override
    protected JPanel crearPanelFormulario() {
        comboEstudiantes = new JComboBox<>();
        comboGrupos = new JComboBox<>();
        try {
            for (Estudiante e : new EstudianteDao().obtenerTodos()) {
                comboEstudiantes.addItem(new ItemCombo(e.getId_estudiante(), e.getNombre() + " " + e.getApellido()));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se pudieron cargar los estudiantes: "
                    + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        try {
            for (Grupo g : new GrupoDao().obtenerTodos()) {
                comboGrupos.addItem(new ItemCombo(g.getId_grupo(), "Grupo " + g.getId_grupo()));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se pudieron cargar los grupos: "
                    + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        campoNota = UiUtil.campo(20);
        comboEstado = new JComboBox<>(ESTADOS);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(java.awt.Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        panel.add(UiUtil.etiqueta("Estudiante:", UiUtil.PRIMARIO), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(comboEstudiantes, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        panel.add(UiUtil.etiqueta("Grupo:", UiUtil.PRIMARIO), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(comboGrupos, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        panel.add(UiUtil.etiqueta("Nota final:", UiUtil.PRIMARIO), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(campoNota, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        panel.add(UiUtil.etiqueta("Estado:", UiUtil.PRIMARIO), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(comboEstado, gbc);

        return panel;
    }

    @Override
    protected InscripcionCurso nuevoDesdeFormulario() {
        InscripcionCurso i = new InscripcionCurso();
        aplicarDatosFormulario(i);
        return i;
    }

    private void aplicarDatosFormulario(InscripcionCurso i) {
        ItemCombo e = (ItemCombo) comboEstudiantes.getSelectedItem();
        ItemCombo g = (ItemCombo) comboGrupos.getSelectedItem();
        i.setId_estudiante(e != null ? e.id : 0);
        i.setId_grupo(g != null ? g.id : 0);
        if (comboEstado.getSelectedItem() != null) {
            i.setEstado((String) comboEstado.getSelectedItem());
        }
        i.setNota_final(Float.parseFloat(campoNota.getText().trim()));
    }

    @Override
    protected void aplicarEnFormulario(InscripcionCurso i) {
        for (int k = 0; k < comboEstudiantes.getItemCount(); k++) {
            if (comboEstudiantes.getItemAt(k).id == i.getId_estudiante()) {
                comboEstudiantes.setSelectedIndex(k);
                break;
            }
        }
        for (int k = 0; k < comboGrupos.getItemCount(); k++) {
            if (comboGrupos.getItemAt(k).id == i.getId_grupo()) {
                comboGrupos.setSelectedIndex(k);
                break;
            }
        }
        campoNota.setText(String.valueOf(i.getNota_final()));
        for (int k = 0; k < comboEstado.getItemCount(); k++) {
            if (comboEstado.getItemAt(k).equals(i.getEstado())) {
                comboEstado.setSelectedIndex(k);
                break;
            }
        }
    }

    @Override
    protected void limpiarFormulario() {
        if (comboEstudiantes.getItemCount() > 0) {
            comboEstudiantes.setSelectedIndex(0);
        }
        if (comboGrupos.getItemCount() > 0) {
            comboGrupos.setSelectedIndex(0);
        }
        campoNota.setText("");
        if (comboEstado.getItemCount() > 0) {
            comboEstado.setSelectedIndex(0);
        }
    }

    @Override
    protected boolean validarFormulario() {
        if (comboEstudiantes.getSelectedItem() == null || comboGrupos.getSelectedItem() == null
                || campoNota.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Los campos Estudiante, Grupo y Nota son obligatorios.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            float nota = Float.parseFloat(campoNota.getText().trim());
            if (nota < 0 || nota > 5) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "La nota debe ser un número entre 0 y 5.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
