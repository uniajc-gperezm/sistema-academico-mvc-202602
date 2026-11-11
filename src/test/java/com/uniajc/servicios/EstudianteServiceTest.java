package com.uniajc.servicios;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.uniajc.dao.EstudianteDaoEnMemoria;
import com.uniajc.modelo.Estudiante;

@DisplayName("EstudianteService")
class EstudianteServiceTest {

    private EstudianteDaoEnMemoria repositorio;
    private EstudianteService service;

    @BeforeEach
    void preparar() {
        repositorio = new EstudianteDaoEnMemoria();
        service = new EstudianteService(repositorio);
    }

    private Estudiante estudianteValido() {
        return new Estudiante(0, "Juan", "Perez", "juan.perez@uniajc.edu.co");
    }

    @Nested
    @DisplayName("Registro")
    class Registro {

        @Test
        @DisplayName("guarda el estudiante cuando los datos son validos")
        void registrarGuardaEstudianteValido() {
            Estudiante e = estudianteValido();

            service.registrarEstudiante(e);

            assertEquals(1, repositorio.contar());
            assertTrue(e.getId_estudiante() > 0, "el repositorio debe asignar un ID");
            assertEquals(e, repositorio.buscarPorId(e.getId_estudiante()).orElseThrow());
        }

        @Test
        @DisplayName("no guarda nada si la validacion falla")
        void registrarNoGuardaSiEsInvalido() {
            Estudiante e = new Estudiante(0, " ", "Perez", "juan@correo.com");

            assertThrows(IllegalArgumentException.class, () -> service.registrarEstudiante(e));
            assertEquals(0, repositorio.contar(), "no debe persistirse un registro invalido");
        }

        @Test
        @DisplayName("rechaza nombre vacio")
        void registrarRechazaNombreVacio() {
            Estudiante e = estudianteValido();
            e.setNombre(" ");
            assertThrows(IllegalArgumentException.class, () -> service.registrarEstudiante(e));
        }

        @Test
        @DisplayName("rechaza email con formato invalido")
        void registrarRechazaEmailInvalido() {
            Estudiante e = estudianteValido();
            e.setEmail("correo-sin-arroba");
            assertThrows(IllegalArgumentException.class, () -> service.registrarEstudiante(e));
        }

        @Test
        @DisplayName("rechaza entidad nula")
        void registrarRechazaNulo() {
            assertThrows(IllegalArgumentException.class, () -> service.registrarEstudiante(null));
        }
    }

    @Nested
    @DisplayName("Consulta")
    class Consulta {

        @Test
        @DisplayName("lista todos los estudiantes registrados")
        void obtenerTodosDevuelveLosRegistrados() {
            service.registrarEstudiante(estudianteValido());
            service.registrarEstudiante(new Estudiante(0, "Maria", "Gomez", "maria@correo.com"));

            List<Estudiante> lista = service.obtenerTodosLosEstudiantes();

            assertEquals(2, lista.size());
        }

        @Test
        @DisplayName("busca por ID existente")
        void buscarPorIdEncuentraExistente() {
            Estudiante e = estudianteValido();
            service.registrarEstudiante(e);

            Optional<Estudiante> hallado = service.buscarEstudiantePorId(e.getId_estudiante());

            assertTrue(hallado.isPresent());
            assertEquals("Juan", hallado.get().getNombre());
        }

        @Test
        @DisplayName("devuelve vacio si el ID no existe")
        void buscarPorIdDevuelveVacioSiNoExiste() {
            assertFalse(service.buscarEstudiantePorId(99).isPresent());
        }

        @Test
        @DisplayName("rechaza IDs no positivos")
        void buscarPorIdRechazaIdInvalido() {
            assertThrows(IllegalArgumentException.class, () -> service.buscarEstudiantePorId(0));
            assertThrows(IllegalArgumentException.class, () -> service.buscarEstudiantePorId(-5));
        }
    }

    @Nested
    @DisplayName("Actualizacion")
    class Actualizacion {

        @Test
        @DisplayName("modifica los datos de un estudiante existente")
        void actualizarModificaDatos() {
            Estudiante e = estudianteValido();
            service.registrarEstudiante(e);

            e.setApellido("Ramirez");
            service.actualizarEstudiante(e);

            assertEquals("Ramirez",
                    repositorio.buscarPorId(e.getId_estudiante()).orElseThrow().getApellido());
        }

        @Test
        @DisplayName("rechaza actualizar un ID que no existe")
        void actualizarRechazaIdInexistente() {
            Estudiante e = new Estudiante(77, "Juan", "Perez", "juan@correo.com");
            assertThrows(IllegalArgumentException.class, () -> service.actualizarEstudiante(e));
        }

        @Test
        @DisplayName("rechaza actualizar con ID invalido")
        void actualizarRechazaIdInvalido() {
            Estudiante e = new Estudiante(-1, "Juan", "Perez", "juan@correo.com");
            assertThrows(IllegalArgumentException.class, () -> service.actualizarEstudiante(e));
        }

        @Test
        @DisplayName("rechaza actualizar con datos invalidos")
        void actualizarRechazaDatosInvalidos() {
            Estudiante e = estudianteValido();
            service.registrarEstudiante(e);
            e.setEmail("correo-malo");

            assertThrows(IllegalArgumentException.class, () -> service.actualizarEstudiante(e));
        }
    }

    @Nested
    @DisplayName("Eliminacion")
    class Eliminacion {

        @Test
        @DisplayName("elimina un estudiante existente")
        void eliminarQuitaElRegistro() {
            Estudiante e = estudianteValido();
            service.registrarEstudiante(e);

            service.eliminarEstudiante(e.getId_estudiante());

            assertEquals(0, repositorio.contar());
            assertFalse(service.buscarEstudiantePorId(e.getId_estudiante()).isPresent());
        }

        @Test
        @DisplayName("rechaza eliminar un ID inexistente")
        void eliminarRechazaIdInexistente() {
            assertThrows(IllegalArgumentException.class, () -> service.eliminarEstudiante(99));
        }

        @Test
        @DisplayName("rechaza eliminar con ID invalido")
        void eliminarRechazaIdInvalido() {
            assertThrows(IllegalArgumentException.class, () -> service.eliminarEstudiante(0));
        }
    }

    @Test
    @DisplayName("la validacion publica acepta un email correcto")
    void validacionConEmailCorrectoPasa() {
        assertDoesNotThrow(() -> service.validarPublico(estudianteValido()));
    }
}
