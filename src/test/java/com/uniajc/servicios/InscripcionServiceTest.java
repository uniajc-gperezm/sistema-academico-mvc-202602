package com.uniajc.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.uniajc.dao.InscripcionDaoEnMemoria;
import com.uniajc.modelo.InscripcionCurso;

@DisplayName("InscripcionService")
class InscripcionServiceTest {

    private InscripcionDaoEnMemoria repositorio;
    private InscripcionService service;

    @BeforeEach
    void preparar() {
        repositorio = new InscripcionDaoEnMemoria();
        service = new InscripcionService(repositorio);
    }

    private InscripcionCurso inscripcionValida() {
        return new InscripcionCurso(0, 1, 1, 4.5f, "ACTIVO");
    }

    @Nested
    @DisplayName("Registro")
    class Registro {

        @Test
        @DisplayName("guarda la inscripcion con datos validos")
        void registrarGuardaInscripcionValida() {
            InscripcionCurso i = inscripcionValida();

            service.registrarInscripcion(i);

            assertEquals(1, repositorio.contar());
            assertTrue(i.getId_inscripcion() > 0);
        }

        @Test
        @DisplayName("impide inscribir dos veces al mismo estudiante en el mismo grupo")
        void registrarRechazaDuplicado() {
            service.registrarInscripcion(inscripcionValida());

            assertThrows(IllegalArgumentException.class,
                    () -> service.registrarInscripcion(inscripcionValida()));
            assertEquals(1, repositorio.contar(), "el duplicado no debe persistirse");
        }

        @Test
        @DisplayName("permite al mismo estudiante inscribirse en otro grupo")
        void registrarPermiteMismoEstudianteEnOtroGrupo() {
            service.registrarInscripcion(new InscripcionCurso(0, 1, 1, 4.0f, "ACTIVO"));
            service.registrarInscripcion(new InscripcionCurso(0, 1, 2, 0f, "ACTIVO"));

            assertEquals(2, repositorio.contar());
        }

        @Test
        @DisplayName("rechaza inscripcion sin estudiante")
        void registrarRechazaSinEstudiante() {
            InscripcionCurso i = inscripcionValida();
            i.setId_estudiante(0);
            assertThrows(IllegalArgumentException.class, () -> service.registrarInscripcion(i));
        }

        @Test
        @DisplayName("rechaza inscripcion sin grupo")
        void registrarRechazaSinGrupo() {
            InscripcionCurso i = inscripcionValida();
            i.setId_grupo(0);
            assertThrows(IllegalArgumentException.class, () -> service.registrarInscripcion(i));
        }

        @Test
        @DisplayName("rechaza nota fuera del rango 0 a 5")
        void registrarRechazaNotaFueraDeRango() {
            InscripcionCurso baja = inscripcionValida();
            baja.setNota_final(-0.5f);
            InscripcionCurso alta = new InscripcionCurso(0, 2, 2, 5.5f, "ACTIVO");

            assertThrows(IllegalArgumentException.class, () -> service.registrarInscripcion(baja));
            assertThrows(IllegalArgumentException.class, () -> service.registrarInscripcion(alta));
        }

        @Test
        @DisplayName("rechaza estado no reconocido")
        void registrarRechazaEstadoInvalido() {
            InscripcionCurso i = inscripcionValida();
            i.setEstado("PENDIENTE");
            assertThrows(IllegalArgumentException.class, () -> service.registrarInscripcion(i));
        }

        @Test
        @DisplayName("acepta todos los estados validos")
        void registrarAceptaEstadosValidos() {
            service.registrarInscripcion(new InscripcionCurso(0, 1, 1, 4f, "ACTIVO"));
            service.registrarInscripcion(new InscripcionCurso(0, 2, 1, 4f, "APROBADO"));
            service.registrarInscripcion(new InscripcionCurso(0, 3, 1, 2f, "REPROBADO"));
            service.registrarInscripcion(new InscripcionCurso(0, 4, 1, 0f, "CANCELADO"));

            assertEquals(4, repositorio.contar());
        }

        @Test
        @DisplayName("rechaza entidad nula")
        void registrarRechazaNulo() {
            assertThrows(IllegalArgumentException.class, () -> service.registrarInscripcion(null));
        }
    }

    @Nested
    @DisplayName("Consulta")
    class Consulta {

        @Test
        @DisplayName("busca por ID existente")
        void buscarPorIdEncuentraExistente() {
            InscripcionCurso i = inscripcionValida();
            service.registrarInscripcion(i);

            assertTrue(service.buscarInscripcionPorId(i.getId_inscripcion()).isPresent());
        }

        @Test
        @DisplayName("devuelve vacio si el ID no existe")
        void buscarPorIdDevuelveVacioSiNoExiste() {
            assertFalse(service.buscarInscripcionPorId(777).isPresent());
        }

        @Test
        @DisplayName("rechaza IDs no positivos")
        void buscarPorIdRechazaIdInvalido() {
            assertThrows(IllegalArgumentException.class, () -> service.buscarInscripcionPorId(0));
        }
    }

    @Nested
    @DisplayName("Actualizacion y eliminacion")
    class ActualizacionYEliminacion {

        @Test
        @DisplayName("actualiza la nota de una inscripcion existente")
        void actualizarModificaNota() {
            InscripcionCurso i = inscripcionValida();
            service.registrarInscripcion(i);

            i.setNota_final(5f);
            i.setEstado("APROBADO");
            service.actualizarInscripcion(i);

            InscripcionCurso guardada = repositorio.buscarPorId(i.getId_inscripcion()).orElseThrow();
            assertEquals(5f, guardada.getNota_final());
            assertEquals("APROBADO", guardada.getEstado());
        }

        @Test
        @DisplayName("rechaza actualizar un ID inexistente")
        void actualizarRechazaIdInexistente() {
            assertThrows(IllegalArgumentException.class,
                    () -> service.actualizarInscripcion(new InscripcionCurso(999, 1, 1, 3f, "ACTIVO")));
        }

        @Test
        @DisplayName("elimina una inscripcion existente")
        void eliminarQuitaElRegistro() {
            InscripcionCurso i = inscripcionValida();
            service.registrarInscripcion(i);

            service.eliminarInscripcion(i.getId_inscripcion());

            assertEquals(0, repositorio.contar());
        }

        @Test
        @DisplayName("rechaza eliminar un ID inexistente")
        void eliminarRechazaIdInexistente() {
            assertThrows(IllegalArgumentException.class, () -> service.eliminarInscripcion(777));
        }
    }
}
