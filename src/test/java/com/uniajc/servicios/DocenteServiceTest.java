package com.uniajc.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.uniajc.dao.DocenteDaoEnMemoria;
import com.uniajc.modelo.Docente;

@DisplayName("DocenteService")
class DocenteServiceTest {

    private DocenteDaoEnMemoria repositorio;
    private DocenteService service;

    @BeforeEach
    void preparar() {
        repositorio = new DocenteDaoEnMemoria();
        service = new DocenteService(repositorio);
    }

    private Docente docenteValido() {
        return new Docente(0, "Gabriel", "Programacion");
    }

    @Nested
    @DisplayName("Registro")
    class Registro {

        @Test
        @DisplayName("guarda el docente con datos validos")
        void registrarGuardaDocenteValido() {
            Docente d = docenteValido();

            service.registrarDocente(d);

            assertEquals(1, repositorio.contar());
            assertTrue(d.getId_docente() > 0);
        }

        @Test
        @DisplayName("rechaza nombre vacio")
        void registrarRechazaNombreVacio() {
            Docente d = docenteValido();
            d.setNombre("   ");
            assertThrows(IllegalArgumentException.class, () -> service.registrarDocente(d));
            assertEquals(0, repositorio.contar());
        }

        @Test
        @DisplayName("rechaza especialidad vacia")
        void registrarRechazaEspecialidadVacia() {
            Docente d = docenteValido();
            d.setEspecialidad("");
            assertThrows(IllegalArgumentException.class, () -> service.registrarDocente(d));
        }

        @Test
        @DisplayName("rechaza entidad nula")
        void registrarRechazaNulo() {
            assertThrows(IllegalArgumentException.class, () -> service.registrarDocente(null));
        }
    }

    @Nested
    @DisplayName("Consulta")
    class Consulta {

        @Test
        @DisplayName("lista todos los docentes")
        void obtenerTodosDevuelveLosRegistrados() {
            service.registrarDocente(docenteValido());
            service.registrarDocente(new Docente(0, "Ana", "Matematicas"));

            List<Docente> lista = service.obtenerTodosLosDocentes();

            assertEquals(2, lista.size());
        }

        @Test
        @DisplayName("busca por ID existente")
        void buscarPorIdEncuentraExistente() {
            Docente d = docenteValido();
            service.registrarDocente(d);

            assertTrue(service.buscarDocentePorId(d.getId_docente()).isPresent());
        }

        @Test
        @DisplayName("devuelve vacio si el ID no existe")
        void buscarPorIdDevuelveVacioSiNoExiste() {
            assertFalse(service.buscarDocentePorId(50).isPresent());
        }

        @Test
        @DisplayName("rechaza IDs no positivos")
        void buscarPorIdRechazaIdInvalido() {
            assertThrows(IllegalArgumentException.class, () -> service.buscarDocentePorId(0));
            assertThrows(IllegalArgumentException.class, () -> service.buscarDocentePorId(-3));
        }
    }

    @Nested
    @DisplayName("Actualizacion y eliminacion")
    class ActualizacionYEliminacion {

        @Test
        @DisplayName("actualiza un docente existente")
        void actualizarModificaDatos() {
            Docente d = docenteValido();
            service.registrarDocente(d);

            d.setEspecialidad("Bases de Datos");
            service.actualizarDocente(d);

            assertEquals("Bases de Datos",
                    repositorio.buscarPorId(d.getId_docente()).orElseThrow().getEspecialidad());
        }

        @Test
        @DisplayName("rechaza actualizar un ID inexistente")
        void actualizarRechazaIdInexistente() {
            assertThrows(IllegalArgumentException.class,
                    () -> service.actualizarDocente(new Docente(88, "X", "Y")));
        }

        @Test
        @DisplayName("elimina un docente existente")
        void eliminarQuitaElRegistro() {
            Docente d = docenteValido();
            service.registrarDocente(d);

            service.eliminarDocente(d.getId_docente());

            assertEquals(0, repositorio.contar());
        }

        @Test
        @DisplayName("rechaza eliminar un ID inexistente")
        void eliminarRechazaIdInexistente() {
            assertThrows(IllegalArgumentException.class, () -> service.eliminarDocente(50));
        }
    }
}
