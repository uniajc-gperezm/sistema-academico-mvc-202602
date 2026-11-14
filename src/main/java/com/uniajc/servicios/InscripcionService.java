package com.uniajc.servicios;

import com.uniajc.modelo.Inscripcion;
import com.uniajc.modelo.InscripcionDAO;
import java.util.List;

/** Valida las referencias y el rango de nota de una inscripción. */
public class InscripcionService {
    private final InscripcionDAO dao;

    public InscripcionService() { this(new InscripcionDAO()); }
    public InscripcionService(InscripcionDAO dao) { this.dao = dao; }

    public List<Inscripcion> listar() { return dao.listarTodos(); }
    public boolean crear(Inscripcion inscripcion) { validar(inscripcion); return dao.insertar(inscripcion); }
    public boolean actualizar(Inscripcion inscripcion) {
        validar(inscripcion);
        if (inscripcion.getIdInscripcion() <= 0) throw new IllegalArgumentException("El ID de la inscripción no es válido.");
        return dao.actualizar(inscripcion);
    }
    public boolean eliminar(int id) {
        if (id <= 0) throw new IllegalArgumentException("El ID de la inscripción no es válido.");
        return dao.eliminar(id);
    }

    private void validar(Inscripcion inscripcion) {
        if (inscripcion == null) throw new IllegalArgumentException("La inscripción es obligatoria.");
        if (inscripcion.getIdEstudiante() <= 0) throw new IllegalArgumentException("El ID del estudiante no es válido.");
        if (inscripcion.getIdGrupo() <= 0) throw new IllegalArgumentException("El ID del grupo no es válido.");
        if (!Double.isFinite(inscripcion.getNotaFinal()) || inscripcion.getNotaFinal() < 0 || inscripcion.getNotaFinal() > 5) {
            throw new IllegalArgumentException("La nota debe estar entre 0 y 5.");
        }
        if (inscripcion.getEstado() == null || inscripcion.getEstado().isBlank()) {
            throw new IllegalArgumentException("El estado de la inscripción es obligatorio.");
        }
    }
}
