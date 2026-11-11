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

import com.uniajc.dao.DocenteDao;
import com.uniajc.dao.GrupoDao;
import com.uniajc.dao.MateriaDao;
import com.uniajc.modelo.Docente;
import com.uniajc.modelo.Grupo;
import com.uniajc.modelo.Materia;
import com.uniajc.servicios.GrupoService;

/** Panel CRUD de grupos. Combos de materia y docente, aula y horario. */
public class PanelGrupo extends PanelCrudBase<Grupo> {

    private static final String[] COLUMNAS = {"ID", "Materia", "Docente", "Aula", "Horario"};

    private final GrupoService service = new GrupoService(new GrupoDao());
    private final Map<Integer, String> nombreMaterias = new HashMap<>();
    private final Map<Integer, String> nombreDocentes = new HashMap<>();

    private JComboBox<ItemCombo> comboMaterias;
    private JComboBox<ItemCombo> comboDocentes;
    private JTextField campoAula;
    private JTextField campoHorario;

    public PanelGrupo() {
        cargarMapas();
        recargar();
    }

    private void cargarMapas() {
        try {
            for (Materia m : new MateriaDao().obtenerTodos()) {
                nombreMaterias.put(m.getId_materia(), m.getNombre_materia());
            }
        } catch (Exception ignored) {
        }
        try {
            for (Docente d : new DocenteDao().obtenerTodos()) {
                nombreDocentes.put(d.getId_docente(), d.getNombre());
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
        return "Grupo";
    }

    @Override
    protected List<Grupo> obtenerTodos() {
        return service.obtenerTodosLosGrupos();
    }

    @Override
    protected Object[] aFila(Grupo g) {
        String materia = nombreMaterias.getOrDefault(g.getId_materia(),
                "ID " + g.getId_materia());
        String docente = nombreDocentes.getOrDefault(g.getId_docente(),
                "ID " + g.getId_docente());
        return new Object[]{g.getId_grupo(), materia, docente, g.getAula(), g.getHorario()};
    }

    @Override
    protected void guardarEntidad(Grupo g) {
        service.registrarGrupo(g);
    }

    @Override
    protected void actualizarEntidad(Grupo g) {
        service.actualizarGrupo(g);
    }

    @Override
    protected void eliminarPorEntidad(Grupo g) {
        service.eliminarGrupo(g.getId_grupo());
    }

    @Override
    protected JPanel crearPanelFormulario() {
        comboMaterias = new JComboBox<>();
        comboDocentes = new JComboBox<>();
        try {
            for (Materia m : new MateriaDao().obtenerTodos()) {
                comboMaterias.addItem(new ItemCombo(m.getId_materia(), m.getNombre_materia()));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se pudieron cargar las materias: "
                    + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        try {
            for (Docente d : new DocenteDao().obtenerTodos()) {
                comboDocentes.addItem(new ItemCombo(d.getId_docente(), d.getNombre()));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No se pudieron cargar los docentes: "
                    + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        campoAula = UiUtil.campo(20);
        campoHorario = UiUtil.campo(20);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(java.awt.Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        panel.add(UiUtil.etiqueta("Materia:", UiUtil.PRIMARIO), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(comboMaterias, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        panel.add(UiUtil.etiqueta("Docente:", UiUtil.PRIMARIO), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(comboDocentes, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        panel.add(UiUtil.etiqueta("Aula:", UiUtil.PRIMARIO), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(campoAula, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        panel.add(UiUtil.etiqueta("Horario:", UiUtil.PRIMARIO), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(campoHorario, gbc);

        return panel;
    }

    @Override
    protected Grupo nuevoDesdeFormulario() {
        Grupo g = new Grupo();
        ItemCombo m = (ItemCombo) comboMaterias.getSelectedItem();
        ItemCombo d = (ItemCombo) comboDocentes.getSelectedItem();
        g.setId_materia(m != null ? m.id : 0);
        g.setId_docente(d != null ? d.id : 0);
        g.setAula(campoAula.getText().trim());
        g.setHorario(campoHorario.getText().trim());
        return g;
    }

    @Override
    protected void aplicarEnFormulario(Grupo g) {
        for (int i = 0; i < comboMaterias.getItemCount(); i++) {
            if (comboMaterias.getItemAt(i).id == g.getId_materia()) {
                comboMaterias.setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < comboDocentes.getItemCount(); i++) {
            if (comboDocentes.getItemAt(i).id == g.getId_docente()) {
                comboDocentes.setSelectedIndex(i);
                break;
            }
        }
        campoAula.setText(g.getAula());
        campoHorario.setText(g.getHorario());
    }

    @Override
    protected void limpiarFormulario() {
        if (comboMaterias.getItemCount() > 0) {
            comboMaterias.setSelectedIndex(0);
        }
        if (comboDocentes.getItemCount() > 0) {
            comboDocentes.setSelectedIndex(0);
        }
        campoAula.setText("");
        campoHorario.setText("");
    }

    @Override
    protected boolean validarFormulario() {
        if (comboMaterias.getSelectedItem() == null || comboDocentes.getSelectedItem() == null
                || campoAula.getText().trim().isEmpty()
                || campoHorario.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
