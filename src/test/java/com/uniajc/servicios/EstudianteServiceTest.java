package com.uniajc.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uniajc.dao.EstudianteDao;
import com.uniajc.modelo.Estudiante;

class EstudianteServiceTest {

    /** DAO falso en memoria: permite probar el servicio sin base de datos. */
    static class DaoFalso extends EstudianteDao {
        private final List<Estudiante> datos = new ArrayList<>();
        private int siguiente = 1;

        @Override
        public void guardar(Estudiante e) {
            e.setId(siguiente++);
            datos.add(e);
        }

        @Override
        public List<Estudiante> obtenerTodos() {
            return new ArrayList<>(datos);
        }

        @Override
        public Optional<Estudiante> buscarPorId(int id) {
            return datos.stream().filter(x -> x.getId() == id).findFirst();
        }

        @Override
        public Optional<Estudiante> buscarPorCampo(String email) {
            return datos.stream().filter(x -> x.getEmail().equals(email)).findFirst();
        }

        @Override
        public void actualizar(Estudiante e) {
            datos.removeIf(x -> x.getId() == e.getId());
            datos.add(e);
        }

        @Override
        public void eliminar(int id) {
            datos.removeIf(x -> x.getId() == id);
        }
    }

    private EstudianteService servicio;

    @BeforeEach
    void preparar() {
        servicio = new EstudianteService(new DaoFalso());
    }

    private Estudiante valido() {
        return new Estudiante(0, "Ana", "Perez", "ana@correo.com");
    }

    @Test
    void registraEstudianteValido() {
        servicio.registrarEstudiante(valido());
        assertEquals(1, servicio.obtenerTodosLosEstudiantes().size());
    }

    @Test
    void rechazaNombreVacio() {
        Estudiante e = valido();
        e.setNombre("   ");
        assertThrows(IllegalArgumentException.class, () -> servicio.registrarEstudiante(e));
    }

    @Test
    void rechazaNombreConNumeros() {
        Estudiante e = valido();
        e.setNombre("An4");
        assertThrows(IllegalArgumentException.class, () -> servicio.registrarEstudiante(e));
    }

    @Test
    void rechazaApellidoNulo() {
        Estudiante e = valido();
        e.setApellido(null);
        assertThrows(IllegalArgumentException.class, () -> servicio.registrarEstudiante(e));
    }

    @Test
    void rechazaEmailInvalido() {
        Estudiante e = valido();
        e.setEmail("ana.com");
        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class, () -> servicio.registrarEstudiante(e));
        assertTrue(ex.getMessage().contains("email"));
    }

    @Test
    void rechazaEmailDuplicado() {
        servicio.registrarEstudiante(valido());
        Estudiante otro = new Estudiante(0, "Luis", "Gomez", "ana@correo.com");
        assertThrows(IllegalArgumentException.class, () -> servicio.registrarEstudiante(otro));
    }

    @Test
    void actualizaEstudianteExistente() {
        servicio.registrarEstudiante(valido());
        Estudiante cambio = new Estudiante(1, "Ana", "Lopez", "ana@correo.com");
        servicio.actualizarEstudiante(cambio);
        assertEquals("Lopez", servicio.buscarEstudiantePorId(1).get().getApellido());
    }

    @Test
    void rechazaActualizarConIdInvalido() {
        Estudiante e = new Estudiante(0, "Ana", "Perez", "ana@correo.com");
        assertThrows(IllegalArgumentException.class, () -> servicio.actualizarEstudiante(e));
    }

    @Test
    void rechazaActualizarEstudianteInexistente() {
        Estudiante e = new Estudiante(99, "Ana", "Perez", "ana@correo.com");
        assertThrows(IllegalArgumentException.class, () -> servicio.actualizarEstudiante(e));
    }

    @Test
    void eliminaEstudianteExistente() {
        servicio.registrarEstudiante(valido());
        servicio.eliminarEstudiante(1);
        assertTrue(servicio.obtenerTodosLosEstudiantes().isEmpty());
    }

    @Test
    void rechazaEliminarConIdInvalido() {
        assertThrows(IllegalArgumentException.class, () -> servicio.eliminarEstudiante(0));
    }

    @Test
    void rechazaEliminarEstudianteInexistente() {
        assertThrows(IllegalArgumentException.class, () -> servicio.eliminarEstudiante(99));
    }
}