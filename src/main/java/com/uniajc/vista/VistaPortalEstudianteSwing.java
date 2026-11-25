package com.uniajc.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.uniajc.controlador.ControladorCalificacion;
import com.uniajc.modelo.Calificacion;
import com.uniajc.modelo.Estudiante;
import com.uniajc.modelo.Grupo;
import com.uniajc.modelo.InscripcionCurso;
import com.uniajc.modelo.Materia;
import com.uniajc.modelo.Usuario;
import com.uniajc.servicios.EstudianteService;
import com.uniajc.servicios.GrupoService;
import com.uniajc.servicios.InscripcionCursoService;
import com.uniajc.servicios.MateriaService;

public class VistaPortalEstudianteSwing extends JFrame {

    private static final Color COLOR_DEGRADADO_INICIO = new Color(28, 15, 62);
    private static final Color COLOR_DEGRADADO_FIN = new Color(110, 24, 92);
    private static final Color COLOR_FONDO = new Color(245, 247, 250);
    private static final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    private static final Color COLOR_EXITO = new Color(39, 174, 96);
    private static final Color COLOR_ACENTO = new Color(231, 76, 60);

    private final Usuario usuario;
    private Estudiante estudiante;

    private final EstudianteService estudianteService;
    private final InscripcionCursoService inscripcionService;
    private final GrupoService grupoService;
    private final MateriaService materiaService;
    private final ControladorCalificacion controladorCalificacion;

    private JTable tablaInscripciones;
    private DefaultTableModel modeloInscripciones;

    private JTable tablaCortes;
    private DefaultTableModel modeloCortes;
    private JLabel lblResumenCortes;
    private JLabel lblEstadoMateria;

    public VistaPortalEstudianteSwing(Usuario usuario) {
        this.usuario = usuario;
        this.estudianteService = new EstudianteService();
        this.inscripcionService = new InscripcionCursoService();
        this.grupoService = new GrupoService();
        this.materiaService = new MateriaService();
        this.controladorCalificacion = new ControladorCalificacion();

        resolverEstudiante();
        inicializarUI();
        cargarInscripciones();
    }

    private void resolverEstudiante() {
        if (usuario.getEstudianteId() != null && usuario.getEstudianteId() > 0) {
            estudiante = estudianteService.buscarEstudiantePorId(usuario.getEstudianteId()).orElse(null);
        }
        if (estudiante == null && usuario.getEmail() != null && !usuario.getEmail().trim().isEmpty()) {
            estudiante = estudianteService.obtenerTodosLosEstudiantes().stream()
                    .filter(e -> usuario.getEmail().equalsIgnoreCase(e.getEmail()))
                    .findFirst().orElse(null);
        }
        if (estudiante == null) {
            // Estudiante por defecto a partir del usuario si aún no está vinculado en la BD
            estudiante = new Estudiante();
            estudiante.setId(usuario.getEstudianteId() != null ? usuario.getEstudianteId() : 0);
            estudiante.setName(usuario.getNombre());
            estudiante.setEmail(usuario.getEmail());
        }
    }

    private void inicializarUI() {
        setTitle("UNIAJC - Portal del Estudiante (" + usuario.getNombre() + ")");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelContenedor = new JPanel(new BorderLayout());
        panelContenedor.setBackground(COLOR_FONDO);

        // Barra Superior
        panelContenedor.add(crearHeader(), BorderLayout.NORTH);

        // Pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.addTab("  📚 Mis Asignaturas y Calificaciones  ", crearPanelAsignaturas());
        tabbedPane.addTab("  👤 Mi Perfil Académico  ", crearPanelPerfil());

        panelContenedor.add(tabbedPane, BorderLayout.CENTER);
        add(panelContenedor);
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, COLOR_DEGRADADO_INICIO, getWidth(), 0, COLOR_DEGRADADO_FIN);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JPanel panelIzquierdo = new JPanel(new GridLayout(2, 1, 0, 2));
        panelIzquierdo.setOpaque(false);

        JLabel lblBienvenida = new JLabel("Bienvenido(a), " + estudiante.getName() + (estudiante.getLastname() != null ? " " + estudiante.getLastname() : ""));
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblBienvenida.setForeground(Color.WHITE);

        String idTexto = (estudiante.getId() > 0) ? " | Estudiante ID: #" + estudiante.getId() : "";
        JLabel lblSubtitulo = new JLabel("Portal Académico UNIAJC" + idTexto + " | Rol: Estudiante");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(230, 215, 245));

        panelIzquierdo.add(lblBienvenida);
        panelIzquierdo.add(lblSubtitulo);

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrarSesion.setBackground(new Color(231, 76, 60));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        header.add(panelIzquierdo, BorderLayout.WEST);
        header.add(btnCerrarSesion, BorderLayout.EAST);
        return header;
    }

    private JPanel crearPanelAsignaturas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 1. Tabla de Inscripciones (Superior)
        String[] columnasIns = {"ID", "Asignatura / Materia", "Grupo", "Aula", "Horario", "Nota Final", "Estado"};
        modeloInscripciones = new DefaultTableModel(columnasIns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaInscripciones = new JTable(modeloInscripciones);
        tablaInscripciones.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaInscripciones.setRowHeight(26);
        tablaInscripciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaInscripciones.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaInscripciones.getTableHeader().setBackground(COLOR_PRIMARIO);
        tablaInscripciones.getTableHeader().setForeground(Color.WHITE);

        tablaInscripciones.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarCortesDeInscripcionSeleccionada();
            }
        });

        JScrollPane scrollInscripciones = new JScrollPane(tablaInscripciones);
        scrollInscripciones.setBorder(BorderFactory.createTitledBorder("Mis Materias Inscritas (Seleccione una para ver cortes)"));

        // 2. Detalle de Cortes de la materia seleccionada (Inferior)
        JPanel panelDetalleCortes = new JPanel(new BorderLayout(8, 8));
        panelDetalleCortes.setBackground(COLOR_FONDO);
        panelDetalleCortes.setBorder(BorderFactory.createTitledBorder("Desglose de Calificaciones por Corte"));

        String[] columnasCortes = {"Corte / Evaluación", "Porcentaje", "Nota Obtenida", "Observación"};
        modeloCortes = new DefaultTableModel(columnasCortes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCortes = new JTable(modeloCortes);
        tablaCortes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaCortes.setRowHeight(24);
        tablaCortes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaCortes.getTableHeader().setBackground(new Color(52, 73, 94));
        tablaCortes.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollCortes = new JScrollPane(tablaCortes);

        JPanel panelInfoCortes = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 6));
        panelInfoCortes.setBackground(new Color(230, 240, 250));
        panelInfoCortes.setBorder(BorderFactory.createLineBorder(new Color(190, 210, 235), 1));

        lblResumenCortes = new JLabel("Seleccione una materia inscrita arriba para visualizar sus notas.");
        lblResumenCortes.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblResumenCortes.setForeground(new Color(41, 128, 185));

        lblEstadoMateria = new JLabel("");
        lblEstadoMateria.setFont(new Font("Segoe UI", Font.BOLD, 12));

        panelInfoCortes.add(lblResumenCortes);
        panelInfoCortes.add(lblEstadoMateria);

        panelDetalleCortes.add(scrollCortes, BorderLayout.CENTER);
        panelDetalleCortes.add(panelInfoCortes, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollInscripciones, panelDetalleCortes);
        splitPane.setResizeWeight(0.55);
        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelPerfil() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JPanel tarjeta = new JPanel(new GridLayout(8, 2, 12, 12));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        agregarFilaPerfil(tarjeta, "Nombre Completo:", estudiante.getName() + (estudiante.getLastname() != null ? " " + estudiante.getLastname() : ""));
        agregarFilaPerfil(tarjeta, "ID de Estudiante:", estudiante.getId() > 0 ? String.valueOf(estudiante.getId()) : "No asignado");
        agregarFilaPerfil(tarjeta, "Usuario del Sistema:", usuario.getUsername());
        agregarFilaPerfil(tarjeta, "Correo Institucional:", estudiante.getEmail() != null ? estudiante.getEmail() : usuario.getEmail());
        agregarFilaPerfil(tarjeta, "Tipo de Documento:", "C.C.");
        agregarFilaPerfil(tarjeta, "Estado Académico:", "ACTIVO");
        agregarFilaPerfil(tarjeta, "Último Acceso al Sistema:", usuario.getUltimoAcceso() != null ? usuario.getUltimoAcceso().toString() : "Sesión actual");

        panel.add(tarjeta, BorderLayout.NORTH);
        return panel;
    }

    private void agregarFilaPerfil(JPanel panel, String titulo, String valor) {
        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTit.setForeground(new Color(50, 60, 70));

        JLabel lblVal = new JLabel(valor != null && !valor.isEmpty() ? valor : "-");
        lblVal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblVal.setForeground(new Color(20, 30, 40));

        panel.add(lblTit);
        panel.add(lblVal);
    }

    private void cargarInscripciones() {
        modeloInscripciones.setRowCount(0);
        if (estudiante == null || estudiante.getId() <= 0) {
            return;
        }

        try {
            List<InscripcionCurso> lista = inscripcionService.buscarInscripcionesPorEstudiante(estudiante.getId());
            for (InscripcionCurso ins : lista) {
                String nombreMateria = "Materia #" + ins.getIdGrupo();
                String aula = "-";
                String horario = "-";

                Optional<Grupo> optGrupo = grupoService.buscarGrupoPorId(ins.getIdGrupo());
                if (optGrupo.isPresent()) {
                    Grupo g = optGrupo.get();
                    aula = g.getClassroom() != null ? g.getClassroom() : "-";
                    horario = g.getSchedule() != null ? g.getSchedule() : "-";

                    Optional<Materia> optMateria = materiaService.buscarMateriaPorId(g.getIdMateria());
                    if (optMateria.isPresent()) {
                        nombreMateria = optMateria.get().getName();
                    }
                }

                String notaStr = ins.getFinalGrade() != null ? ins.getFinalGrade().toString() : "0.00";
                modeloInscripciones.addRow(new Object[]{
                        ins.getId(),
                        nombreMateria,
                        "Grupo #" + ins.getIdGrupo(),
                        aula,
                        horario,
                        notaStr,
                        ins.getStatus() != null ? ins.getStatus() : "ACTIVO"
                });
            }

            if (modeloInscripciones.getRowCount() > 0) {
                tablaInscripciones.setRowSelectionInterval(0, 0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar inscripciones: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarCortesDeInscripcionSeleccionada() {
        modeloCortes.setRowCount(0);
        int fila = tablaInscripciones.getSelectedRow();
        if (fila == -1) {
            lblResumenCortes.setText("Seleccione una materia inscrita.");
            lblEstadoMateria.setText("");
            return;
        }

        int inscripcionId = (int) modeloInscripciones.getValueAt(fila, 0);
        String nombreMateria = (String) modeloInscripciones.getValueAt(fila, 1);

        try {
            List<Calificacion> cortes = controladorCalificacion.listarPorInscripcion(inscripcionId);
            BigDecimal sumaPorcentaje = BigDecimal.ZERO;
            BigDecimal sumaPonderada = BigDecimal.ZERO;

            for (Calificacion c : cortes) {
                modeloCortes.addRow(new Object[]{
                        c.getCorte(),
                        c.getPorcentaje() != null ? c.getPorcentaje() + "%" : "0%",
                        c.getNota() != null ? c.getNota().toString() : "0.0",
                        c.getObservacion() != null ? c.getObservacion() : ""
                });

                if (c.getPorcentaje() != null && c.getNota() != null) {
                    sumaPorcentaje = sumaPorcentaje.add(c.getPorcentaje());
                    BigDecimal aporte = c.getNota().multiply(c.getPorcentaje()).divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
                    sumaPonderada = sumaPonderada.add(aporte);
                }
            }

            BigDecimal notaFinal = sumaPonderada.setScale(2, RoundingMode.HALF_UP);
            lblResumenCortes.setText(String.format("Asignatura: %s | Porcentaje calificado: %.1f%% / 100%% | Acumulado actual: %.2f / 5.0",
                    nombreMateria, sumaPorcentaje, notaFinal));

            if (sumaPorcentaje.compareTo(new BigDecimal("100.00")) >= 0) {
                if (notaFinal.compareTo(new BigDecimal("3.00")) >= 0) {
                    lblEstadoMateria.setText("✓ APROBADA");
                    lblEstadoMateria.setForeground(COLOR_EXITO);
                } else {
                    lblEstadoMateria.setText("✗ REPROBADA");
                    lblEstadoMateria.setForeground(COLOR_ACENTO);
                }
            } else {
                lblEstadoMateria.setText("⏳ EN CURSO");
                lblEstadoMateria.setForeground(COLOR_PRIMARIO);
            }

        } catch (Exception e) {
            lblResumenCortes.setText("Error al cargar cortes: " + e.getMessage());
        }
    }

    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea cerrar sesión?",
                "Confirmar Cierre", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                VistaLogin login = new VistaLogin();
                login.mostrarVentana();
            });
        }
    }
}
