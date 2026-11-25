package com.uniajc.controlador;

import java.util.List;
import java.util.Optional;

import com.uniajc.modelo.Materia;
import com.uniajc.servicios.MateriaService;

public class ControladorMateria {

    private final MateriaService materiaService;

    public ControladorMateria() {
        this.materiaService = new MateriaService();
    }

    public ControladorMateria(MateriaService materiaService) {
        this.materiaService = materiaService;
    }

    public void registrarMateria(String nombre, int creditos) {
        Materia materia = new Materia(nombre, creditos);
        materia.setCodigo("GEN");
        materia.setHorasSemanales(2);
        materiaService.registrarMateria(materia);
    }

    public void registrarMateria(String nombre, int creditos, String codigo, int horasSemanales) {
        Materia materia = new Materia(nombre, creditos, codigo, horasSemanales);
        materiaService.registrarMateria(materia);
    }

    public List<Materia> listarMaterias() {
        return materiaService.obtenerTodasLasMaterias();
    }

    public Optional<Materia> buscarMateriaPorId(int id) {
        return materiaService.buscarMateriaPorId(id);
    }

    public List<Materia> buscarMateriasPorNombre(String nombre) {
        return materiaService.buscarMateriasPorNombre(nombre);
    }

    public void actualizarMateria(int id, String nombre, int creditos) {
        Materia materia = new Materia(id, nombre, creditos);
        materia.setCodigo("GEN");
        materia.setHorasSemanales(2);
        materiaService.actualizarMateria(materia);
    }

    public void actualizarMateria(int id, String nombre, int creditos, String codigo, int horasSemanales) {
        Materia materia = new Materia(id, nombre, creditos, codigo, horasSemanales);
        materiaService.actualizarMateria(materia);
    }

    public void eliminarMateria(int id) {
        materiaService.eliminarMateria(id);
    }
}

