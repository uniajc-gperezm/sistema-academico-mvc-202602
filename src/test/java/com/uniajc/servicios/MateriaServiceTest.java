package com.uniajc.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.uniajc.dao.MateriaDaoEnMemoria;
import com.uniajc.modelo.Materia;

@DisplayName("MateriaService")
class MateriaServiceTest {

    private MateriaDaoEnMemoria repositorio;
    private MateriaService service;

    @BeforeEach
    void preparar() {
        repositorio = new MateriaDaoEnMemoria();
        service = new MateriaService(repositorio);
    }

    private Materia materiaValida() {
        return new Materia(0, "Programacion 2", 4);
    }

    @Nested
    @DisplayName("Registro")
    class Registro {

        @Test
        @DisplayName("guarda la materia con datos validos")
        void registrarGuardaMateriaValida() {
            Materia m = materiaValida();

            service.registrarMateria(m);

            assertEquals(1, repositorio.contar());
            assertTrue(m.getId_materia() > 0);
        }

        @Test
        @DisplayName("rechaza nombre vacio")
        void registrarRechazaNombreVacio() {
            Materia m = materiaValida();
            m.setNombre_materia("  ");
            assertThrows(IllegalArgumentException.class, () -> service.registrarMateria(m));
            assertEquals(0, repositorio.contar());
        }

        @Test
        @DisplayName("rechaza creditos menores que 1")
        void registrarRechazaCreditosBajos() {
            Materia m = materiaValida();
            m.setCreditos(0);
            assertThrows(IllegalArgumentException.class, () -> service.registrarMateria(m));
        }

        @Test
        @DisplayName("rechaza creditos mayores que 10")
        void registrarRechazaCreditosAltos() {
            Materia m = materiaValida();
            m.setCreditos(11);
            assertThrows(IllegalArgumentException.class, () -> service.registrarMateria(m));
        }

        @Test
        @DisplayName("acepta los limites validos de creditos (1 y 10)")
        void registrarAceptaLimitesDeCreditos() {
            service.registrarMateria(new Materia(0, "Minima", 1));
            service.registrarMateria(new Materia(0, "Maxima", 10));

            assertEquals(2, repositorio.contar());
        }

        @Test
        @DisplayName("rechaza entidad nula")
        void registrarRechazaNulo() {
            assertThrows(IllegalArgumentException.class, () -> service.registrarMateria(null));
        }
    }

    @Nested
    @DisplayName("Consulta")
    class Consulta {

        @Test
        @DisplayName("busca por ID existente")
        void buscarPorIdEncuentraExistente() {
            Materia m = materiaValida();
            service.registrarMateria(m);

            assertTrue(service.buscarMateriaPorId(m.getId_materia()).isPresent());
        }

        @Test
        @DisplayName("devuelve vacio si el ID no existe")
        void buscarPorIdDevuelveVacioSiNoExiste() {
            assertFalse(service.buscarMateriaPorId(404).isPresent());
        }

        @Test
        @DisplayName("rechaza IDs no positivos")
        void buscarPorIdRechazaIdInvalido() {
            assertThrows(IllegalArgumentException.class, () -> service.buscarMateriaPorId(-1));
        }
    }

    @Nested
    @DisplayName("Actualizacion y eliminacion")
    class ActualizacionYEliminacion {

        @Test
        @DisplayName("actualiza una materia existente")
        void actualizarModificaDatos() {
            Materia m = materiaValida();
            service.registrarMateria(m);

            m.setCreditos(3);
            service.actualizarMateria(m);

            assertEquals(3, repositorio.buscarPorId(m.getId_materia()).orElseThrow().getCreditos());
        }

        @Test
        @DisplayName("rechaza actualizar un ID inexistente")
        void actualizarRechazaIdInexistente() {
            assertThrows(IllegalArgumentException.class,
                    () -> service.actualizarMateria(new Materia(999, "X", 3)));
        }

        @Test
        @DisplayName("rechaza actualizar con creditos fuera de rango")
        void actualizarRechazaCreditosInvalidos() {
            Materia m = materiaValida();
            service.registrarMateria(m);
            m.setCreditos(99);

            assertThrows(IllegalArgumentException.class, () -> service.actualizarMateria(m));
        }

        @Test
        @DisplayName("elimina una materia existente")
        void eliminarQuitaElRegistro() {
            Materia m = materiaValida();
            service.registrarMateria(m);

            service.eliminarMateria(m.getId_materia());

            assertEquals(0, repositorio.contar());
        }

        @Test
        @DisplayName("rechaza eliminar un ID inexistente")
        void eliminarRechazaIdInexistente() {
            assertThrows(IllegalArgumentException.class, () -> service.eliminarMateria(999));
        }
    }
}
