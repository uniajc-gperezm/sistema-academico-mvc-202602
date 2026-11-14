package com.uniajc.vista;

import com.uniajc.controlador.ControladorCurso;
import com.uniajc.controlador.ControladorDocente;
import com.uniajc.controlador.ControladorEstudiante;
import com.uniajc.controlador.ControladorGrupo;
import com.uniajc.controlador.ControladorInscripcion;
import com.uniajc.controlador.ControladorMateria;
import com.uniajc.modelo.Curso;
import com.uniajc.modelo.Docente;
import com.uniajc.modelo.Estudiante;
import com.uniajc.modelo.Grupo;
import com.uniajc.modelo.Inscripcion;
import com.uniajc.modelo.Materia;
import com.uniajc.modelo.Usuario;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/** Ventana principal con un módulo CRUD para cada entidad académica. */
public class VistaAcademica {
    private final JFrame ventana = new JFrame("UNIAJC | Sistema académico");
    private final ControladorEstudiante estudiantes = new ControladorEstudiante();
    private final ControladorDocente docentes = new ControladorDocente();
    private final ControladorMateria materias = new ControladorMateria();
    private final ControladorGrupo grupos = new ControladorGrupo();
    private final ControladorInscripcion inscripciones = new ControladorInscripcion();
    private final ControladorCurso cursos = new ControladorCurso();

    public VistaAcademica(Usuario usuario) {
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(1100, 650);
        ventana.setLocationRelativeTo(null);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.add(new JLabel("Sistema académico  |  " + usuario.nombre()
                + (usuario.rol() == null ? "" : "  (" + usuario.rol() + ")")), BorderLayout.WEST);
        encabezado.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JTabbedPane modulos = new JTabbedPane();
        modulos.addTab("Estudiantes", panelEstudiantes());
        modulos.addTab("Docentes", panelDocentes());
        modulos.addTab("Materias", panelMaterias());
        modulos.addTab("Cursos", panelCursos());
        modulos.addTab("Grupos", panelGrupos());
        modulos.addTab("Inscripciones", panelInscripciones());

        ventana.add(encabezado, BorderLayout.NORTH);
        ventana.add(modulos, BorderLayout.CENTER);
    }

    public void mostrar() { ventana.setVisible(true); }

    private CrudTablePanel<Estudiante> panelEstudiantes() {
        return new CrudTablePanel<>(new String[] { "ID", "Nombre", "Apellido", "Correo", "NIT", "Celular" },
                estudiantes::listarEstudiantes,
                e -> new Object[] { e.getId(), e.getName(), e.getLastname(), e.getEmail(), e.getNit(), e.getNumcelular() },
                this::formularioEstudiante,
                estudiantes::crearEstudiante,
                estudiantes::actualizarEstudiante,
                e -> estudiantes.eliminarEstudiante(e.getId()));
    }

    private CrudTablePanel<Docente> panelDocentes() {
        return new CrudTablePanel<>(new String[] { "ID", "Nombre", "Especialidad", "Área" },
                docentes::listar,
                d -> new Object[] { d.getIdDocente(), d.getNombre(), d.getEspecialidad(), d.getArea() },
                this::formularioDocente, docentes::crear, docentes::actualizar,
                d -> docentes.eliminar(d.getIdDocente()));
    }

    private CrudTablePanel<Materia> panelMaterias() {
        return new CrudTablePanel<>(new String[] { "ID", "Materia", "Créditos" },
                materias::listar,
                m -> new Object[] { m.getIdMateria(), m.getNombreMateria(), m.getCreditos() },
                this::formularioMateria, materias::crear, materias::actualizar,
                m -> materias.eliminar(m.getIdMateria()));
    }

    private CrudTablePanel<Curso> panelCursos() {
        return new CrudTablePanel<>(new String[] { "ID", "Facultad" }, cursos::listar,
                c -> new Object[] { c.getId(), c.getFacultad() }, this::formularioCurso,
                cursos::crear, cursos::actualizar, c -> cursos.eliminar(c.getId()));
    }

    private CrudTablePanel<Grupo> panelGrupos() {
        return new CrudTablePanel<>(new String[] { "ID", "ID curso", "ID docente", "Aula", "Horario" },
                grupos::listar,
                g -> new Object[] { g.getIdGrupo(), g.getIdMateria(), g.getIdDocente(), g.getAula(), g.getHorario() },
                this::formularioGrupo, grupos::crear, grupos::actualizar,
                g -> grupos.eliminar(g.getIdGrupo()));
    }

    private CrudTablePanel<Inscripcion> panelInscripciones() {
        return new CrudTablePanel<>(new String[] { "ID", "ID estudiante", "ID grupo", "Nota final", "Estado" },
                inscripciones::listar,
                i -> new Object[] { i.getIdInscripcion(), i.getIdEstudiante(), i.getIdGrupo(), i.getNotaFinal(), i.getEstado() },
                this::formularioInscripcion, inscripciones::crear, inscripciones::actualizar,
                i -> inscripciones.eliminar(i.getIdInscripcion()));
    }

    private Estudiante formularioEstudiante(Estudiante actual) {
        String[] valores = formulario("Estudiante", new String[] { "Nombre", "Apellido", "Correo", "NIT opcional", "Celular opcional" },
                actual == null ? new String[] { "", "", "", "", "" }
                        : new String[] { actual.getName(), actual.getLastname(), actual.getEmail(), texto(actual.getNit()), texto(actual.getNumcelular()) });
        if (valores == null) return null;
        return new Estudiante(valores[0], valores[1], valores[2], numeroOpcional(valores[3], "NIT"), numeroOpcional(valores[4], "Celular"));
    }

    private Docente formularioDocente(Docente actual) {
        String[] valores = formulario("Docente", new String[] { "Nombre", "Especialidad", "Área" },
                actual == null ? new String[] { "", "", "" }
                        : new String[] { actual.getNombre(), actual.getEspecialidad(), actual.getArea() });
        if (valores == null) return null;
        return new Docente(actual == null ? 0L : actual.getIdDocente(), valores[0], valores[1], valores[2]);
    }

    private Materia formularioMateria(Materia actual) {
        String[] valores = formulario("Materia", new String[] { "ID (entero)", "Nombre", "Créditos" },
                actual == null ? new String[] { "", "", "" }
                        : new String[] { String.valueOf(actual.getIdMateria()), actual.getNombreMateria(), String.valueOf(actual.getCreditos()) },
                actual != null ? new boolean[] { false, true, true } : null);
        if (valores == null) return null;
        int id = actual == null ? entero(valores[0], "ID") : actual.getIdMateria();
        return new Materia(id, valores[1], entero(valores[2], "créditos"));
    }

    private Curso formularioCurso(Curso actual) {
        String[] valores = formulario("Curso", new String[] { "Facultad" },
                actual == null ? new String[] { "" } : new String[] { actual.getFacultad() });
        if (valores == null) return null;
        return new Curso(actual == null ? 0L : actual.getId(), valores[0]);
    }

    private Grupo formularioGrupo(Grupo actual) {
        String[] valores = formulario("Grupo", new String[] { "ID del curso", "ID del docente", "Aula", "Horario" },
                actual == null ? new String[] { "", "", "", "" }
                        : new String[] { String.valueOf(actual.getIdMateria()), String.valueOf(actual.getIdDocente()), actual.getAula(), actual.getHorario() });
        if (valores == null) return null;
        return new Grupo(actual == null ? 0L : actual.getIdGrupo(), largo(valores[0], "ID curso"),
                largo(valores[1], "ID docente"), valores[2], valores[3]);
    }

    private Inscripcion formularioInscripcion(Inscripcion actual) {
        String[] valores = formulario("Inscripción", new String[] { "ID del estudiante", "ID del grupo", "Nota (0 a 5)", "Estado" },
                actual == null ? new String[] { "", "", "", "" }
                        : new String[] { String.valueOf(actual.getIdEstudiante()), String.valueOf(actual.getIdGrupo()), String.valueOf(actual.getNotaFinal()), actual.getEstado() });
        if (valores == null) return null;
        double nota;
        try {
            nota = Double.parseDouble(valores[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La nota debe ser un número entre 0 y 5.");
        }
        return new Inscripcion(actual == null ? 0 : actual.getIdInscripcion(), entero(valores[0], "ID estudiante"),
                entero(valores[1], "ID grupo"), nota, valores[3]);
    }

    private String[] formulario(String titulo, String[] etiquetas, String[] valores) {
        return formulario(titulo, etiquetas, valores, null);
    }

    // Muestra campos de edición y devuelve sus valores; un arreglo nulo significa cancelar.
    private String[] formulario(String titulo, String[] etiquetas, String[] valores, boolean[] editables) {
        JPanel panel = new JPanel(new GridLayout(etiquetas.length, 2, 8, 8));
        JTextField[] campos = new JTextField[etiquetas.length];
        for (int i = 0; i < etiquetas.length; i++) {
            campos[i] = new JTextField(valores[i] == null ? "" : valores[i]);
            if (editables != null) campos[i].setEditable(editables[i]);
            panel.add(new JLabel(etiquetas[i] + ":"));
            panel.add(campos[i]);
        }
        int respuesta = JOptionPane.showConfirmDialog((Component) ventana, panel, titulo,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (respuesta != JOptionPane.OK_OPTION) return null;
        String[] resultado = new String[campos.length];
        for (int i = 0; i < campos.length; i++) resultado[i] = campos[i].getText().trim();
        return resultado;
    }

    private int entero(String texto, String campo) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo " + campo + " debe ser un número entero.");
        }
    }

    private long largo(String texto, String campo) {
        try {
            return Long.parseLong(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo " + campo + " debe ser un número entero.");
        }
    }

    private Long numeroOpcional(String texto, String campo) {
        if (texto.isBlank()) return null;
        try {
            long valor = Long.parseLong(texto.trim());
            if (valor < 0) throw new NumberFormatException();
            return valor;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El campo " + campo + " debe tener solo dígitos.");
        }
    }

    private String texto(Long valor) { return valor == null ? "" : valor.toString(); }
}
