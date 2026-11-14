package com.uniajc.servicios;

import com.uniajc.modelo.Materia;
import com.uniajc.modelo.MateriaDAO;
import java.util.List;

/** Valida la materia y sus créditos antes de persistirla. */
public class MateriaService {
    private final MateriaDAO dao;

    public MateriaService() { this(new MateriaDAO()); }
    public MateriaService(MateriaDAO dao) { this.dao = dao; }

    public List<Materia> listar() { return dao.listarTodos(); }
    public boolean crear(Materia materia) { validar(materia); return dao.insertar(materia); }
    public boolean actualizar(Materia materia) { validar(materia); return dao.actualizar(materia); }
    public boolean eliminar(int id) {
        if (id <= 0) throw new IllegalArgumentException("El ID de la materia debe ser mayor que cero.");
        return dao.eliminar(id);
    }

    private void validar(Materia materia) {
        if (materia == null) throw new IllegalArgumentException("La materia es obligatoria.");
        if (materia.getIdMateria() <= 0) throw new IllegalArgumentException("El ID de la materia debe ser mayor que cero.");
        if (materia.getNombreMateria() == null || materia.getNombreMateria().isBlank()) {
            throw new IllegalArgumentException("El nombre de la materia es obligatorio.");
        }
        if (materia.getCreditos() <= 0) throw new IllegalArgumentException("Los créditos deben ser mayores que cero.");
    }
}
