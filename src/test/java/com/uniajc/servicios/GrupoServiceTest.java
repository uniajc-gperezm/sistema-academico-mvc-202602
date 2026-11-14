package com.uniajc.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.uniajc.dao.GrupoDaoEnMemoria;
import com.uniajc.modelo.Grupo;

@DisplayName("GrupoService")
class GrupoServiceTest {

    private GrupoDaoEnMemoria repositorio;
    private GrupoService service;

    @BeforeEach
    void preparar() {
        repositorio = new GrupoDaoEnMemoria();
        service = new GrupoService(repositorio);
    }

    private Grupo grupoValido() {
        return new Grupo(0, 1, 1, "A101", "Lunes 7:00 - 9:00");
    }

    @Nested
    @DisplayName("Registro")
    class Registro {

        @Test
        @DisplayName("guarda el grupo con datos validos")
        void registrarGuardaGrupoValido() {
            Grupo g = grupoValido();

            service.registrarGrupo(g);

            assertEquals(1, repositorio.contar());
            assertTrue(g.getId_grupo() > 0);
        }

        @Test
        @DisplayName("rechaza grupo sin materia asignada")
        void registrarRechazaSinMateria() {
            Grupo g = grupoValido();
            g.setId_materia(0);
            assertThrows(IllegalArgumentException.class, () -> service.registrarGrupo(g));
            assertEquals(0, repositorio.contar());
        }

        @Test
        @DisplayName("rechaza grupo sin docente asignado")
        void registrarRechazaSinDocente() {
            Grupo g = grupoValido();
            g.setId_docente(-1);
            assertThrows(IllegalArgumentException.class, () -> service.registrarGrupo(g));
        }

        @Test
        @DisplayName("rechaza aula vacia")
        void registrarRechazaAulaVacia() {
            Grupo g = grupoValido();
            g.setAula(" ");
            assertThrows(IllegalArgumentException.class, () -> service.registrarGrupo(g));
        }

        @Test
        @DisplayName("rechaza horario vacio")
        void registrarRechazaHorarioVacio() {
            Grupo g = grupoValido();
            g.setHorario("");
            assertThrows(IllegalArgumentException.class, () -> service.registrarGrupo(g));
        }

        @Test
        @DisplayName("rechaza entidad nula")
        void registrarRechazaNulo() {
            assertThrows(IllegalArgumentException.class, () -> service.registrarGrupo(null));
        }
    }

    @Nested
    @DisplayName("Consulta")
    class Consulta {

        @Test
        @DisplayName("busca por ID existente")
        void buscarPorIdEncuentraExistente() {
            Grupo g = grupoValido();
            service.registrarGrupo(g);

            assertTrue(service.buscarGrupoPorId(g.getId_grupo()).isPresent());
        }

        @Test
        @DisplayName("devuelve vacio si el ID no existe")
        void buscarPorIdDevuelveVacioSiNoExiste() {
            assertFalse(service.buscarGrupoPorId(321).isPresent());
        }

        @Test
        @DisplayName("rechaza IDs no positivos")
        void buscarPorIdRechazaIdInvalido() {
            assertThrows(IllegalArgumentException.class, () -> service.buscarGrupoPorId(0));
        }
    }

    @Nested
    @DisplayName("Actualizacion y eliminacion")
    class ActualizacionYEliminacion {

        @Test
        @DisplayName("actualiza un grupo existente")
        void actualizarModificaDatos() {
            Grupo g = grupoValido();
            service.registrarGrupo(g);

            g.setAula("B202");
            service.actualizarGrupo(g);

            assertEquals("B202", repositorio.buscarPorId(g.getId_grupo()).orElseThrow().getAula());
        }

        @Test
        @DisplayName("rechaza actualizar un ID inexistente")
        void actualizarRechazaIdInexistente() {
            assertThrows(IllegalArgumentException.class,
                    () -> service.actualizarGrupo(new Grupo(555, 1, 1, "A1", "Lunes")));
        }

        @Test
        @DisplayName("elimina un grupo existente")
        void eliminarQuitaElRegistro() {
            Grupo g = grupoValido();
            service.registrarGrupo(g);

            service.eliminarGrupo(g.getId_grupo());

            assertEquals(0, repositorio.contar());
        }

        @Test
        @DisplayName("rechaza eliminar un ID inexistente")
        void eliminarRechazaIdInexistente() {
            assertThrows(IllegalArgumentException.class, () -> service.eliminarGrupo(321));
        }
    }
}
