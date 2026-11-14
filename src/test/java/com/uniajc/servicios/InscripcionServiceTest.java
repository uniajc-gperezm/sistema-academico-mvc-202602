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

    @Nested
    @DisplayName("Rutinas almacenadas")
    class RutinasAlmacenadas {

        @Test
        @DisplayName("asignarNota actualiza la nota de una inscripcion existente")
        void asignarNotaModificaLaNota() {
            InscripcionCurso i = inscripcionValida();
            service.registrarInscripcion(i);

            service.asignarNota(i.getId_inscripcion(), 4.8f);

            assertEquals(4.8f,
                    repositorio.buscarPorId(i.getId_inscripcion()).orElseThrow().getNota_final());
        }

        @Test
        @DisplayName("asignarNota recalcula el estado segun la nota")
        void asignarNotaRecalculaElEstado() {
            InscripcionCurso i = inscripcionValida();
            service.registrarInscripcion(i);

            service.asignarNota(i.getId_inscripcion(), 4.5f);
            assertEquals("APROBADO",
                    repositorio.buscarPorId(i.getId_inscripcion()).orElseThrow().getEstado());

            service.asignarNota(i.getId_inscripcion(), 2.0f);
            assertEquals("REPROBADO",
                    repositorio.buscarPorId(i.getId_inscripcion()).orElseThrow().getEstado());
        }

        @Test
        @DisplayName("asignarNota no revive una inscripcion cancelada")
        void asignarNotaRespetaCancelado() {
            InscripcionCurso i = inscripcionValida();
            i.setEstado("CANCELADO");
            service.registrarInscripcion(i);

            service.asignarNota(i.getId_inscripcion(), 5f);

            InscripcionCurso guardada = repositorio.buscarPorId(i.getId_inscripcion()).orElseThrow();
            assertEquals(5f, guardada.getNota_final());
            assertEquals("CANCELADO", guardada.getEstado());
        }

        @Test
        @DisplayName("asignarNota rechaza nota fuera de rango sin tocar el registro")
        void asignarNotaRechazaNotaInvalida() {
            InscripcionCurso i = inscripcionValida();
            service.registrarInscripcion(i);

            assertThrows(IllegalArgumentException.class,
                    () -> service.asignarNota(i.getId_inscripcion(), 6f));
            assertThrows(IllegalArgumentException.class,
                    () -> service.asignarNota(i.getId_inscripcion(), -1f));
            assertEquals(4.5f,
                    repositorio.buscarPorId(i.getId_inscripcion()).orElseThrow().getNota_final());
        }

        @Test
        @DisplayName("asignarNota rechaza una inscripcion inexistente")
        void asignarNotaRechazaIdInexistente() {
            assertThrows(IllegalArgumentException.class, () -> service.asignarNota(777, 4f));
        }

        @Test
        @DisplayName("promedioGrupo calcula el promedio de las notas del grupo")
        void promedioGrupoCalculaPromedio() {
            service.registrarInscripcion(new InscripcionCurso(0, 1, 1, 4.0f, "ACTIVO"));
            service.registrarInscripcion(new InscripcionCurso(0, 2, 1, 2.0f, "ACTIVO"));
            service.registrarInscripcion(new InscripcionCurso(0, 3, 2, 5.0f, "ACTIVO"));

            assertEquals(3.0f, service.promedioGrupo(1));
        }

        @Test
        @DisplayName("promedioGrupo devuelve 0 si el grupo no tiene inscripciones")
        void promedioGrupoDevuelveCeroSinInscripciones() {
            assertEquals(0f, service.promedioGrupo(99));
        }

        @Test
        @DisplayName("estadoPorNota clasifica aprobado y reprobado")
        void estadoPorNotaClasifica() {
            assertEquals("APROBADO", service.estadoPorNota(3.0f));
            assertEquals("APROBADO", service.estadoPorNota(5f));
            assertEquals("REPROBADO", service.estadoPorNota(2.9f));
            assertEquals("REPROBADO", service.estadoPorNota(0f));
        }
    }
}
