package com.uniajc.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.uniajc.dao.UsuarioDaoEnMemoria;
import com.uniajc.db.EncriptacionUtil;
import com.uniajc.modelo.Rol;
import com.uniajc.modelo.Usuario;

@DisplayName("UsuarioService")
class UsuarioServiceTest {

    private UsuarioDaoEnMemoria repositorio;
    private UsuarioService service;

    @BeforeEach
    void preparar() {
        repositorio = new UsuarioDaoEnMemoria();
        service = new UsuarioService(repositorio);
    }

    private void registrarAdmin(String passwordPlano) {
        service.registrarUsuario(
                new Usuario(0, "admin", null, "Administrador", Rol.ADMIN, true), passwordPlano);
    }

    @Nested
    @DisplayName("Registro")
    class Registro {

        @Test
        @DisplayName("almacena la contrasena encriptada, nunca en texto plano")
        void registrarGuardaHashNoTextoPlano() {
            registrarAdmin("admin123");

            Usuario guardado = repositorio.buscarPorUsername("admin").orElseThrow();
            assertNotEquals("admin123", guardado.getPassword());
            assertEquals(EncriptacionUtil.md5("admin123"), guardado.getPassword());
        }
    }

    @Nested
    @DisplayName("Autenticacion")
    class Autenticacion {

        @Test
        @DisplayName("autentica con credenciales correctas")
        void autenticarAceptaCredencialesCorrectas() {
            registrarAdmin("admin123");

            Optional<Usuario> resultado = service.autenticar("admin", "admin123");

            assertTrue(resultado.isPresent());
            assertEquals(Rol.ADMIN, resultado.get().getRol());
        }

        @Test
        @DisplayName("rechaza contrasena incorrecta")
        void autenticarRechazaPasswordIncorrecta() {
            registrarAdmin("admin123");

            assertFalse(service.autenticar("admin", "otra-clave").isPresent());
        }

        @Test
        @DisplayName("rechaza usuario inexistente")
        void autenticarRechazaUsuarioInexistente() {
            assertFalse(service.autenticar("fantasma", "admin123").isPresent());
        }

        @Test
        @DisplayName("rechaza usuario inactivo")
        void autenticarRechazaUsuarioInactivo() {
            service.registrarUsuario(
                    new Usuario(0, "inactivo", null, "Inactivo", Rol.USUARIO, false), "clave123");

            assertFalse(service.autenticar("inactivo", "clave123").isPresent());
        }

        @Test
        @DisplayName("rechaza credenciales vacias o nulas")
        void autenticarRechazaCredencialesVacias() {
            registrarAdmin("admin123");

            assertFalse(service.autenticar("", "admin123").isPresent());
            assertFalse(service.autenticar("admin", "").isPresent());
            assertFalse(service.autenticar(null, null).isPresent());
            assertFalse(service.autenticar("   ", "   ").isPresent());
        }

        @Test
        @DisplayName("ignora espacios alrededor del nombre de usuario")
        void autenticarIgnoraEspaciosEnUsername() {
            registrarAdmin("admin123");

            assertTrue(service.autenticar("  admin  ", "admin123").isPresent());
        }
    }

    @Nested
    @DisplayName("Cambio de contrasena")
    class CambioDePassword {

        @Test
        @DisplayName("cambia la contrasena y permite autenticar con la nueva")
        void cambiarPasswordActualizaElHash() {
            registrarAdmin("admin123");
            int id = repositorio.buscarPorUsername("admin").orElseThrow().getId();

            service.cambiarPassword(id, "nuevaClave");

            assertTrue(service.autenticar("admin", "nuevaClave").isPresent());
            assertFalse(service.autenticar("admin", "admin123").isPresent());
        }

        @Test
        @DisplayName("rechaza contrasenas demasiado cortas")
        void cambiarPasswordRechazaClaveCorta() {
            registrarAdmin("admin123");
            int id = repositorio.buscarPorUsername("admin").orElseThrow().getId();

            assertThrows(IllegalArgumentException.class, () -> service.cambiarPassword(id, "abc"));
        }

        @Test
        @DisplayName("rechaza contrasena nula")
        void cambiarPasswordRechazaNula() {
            assertThrows(IllegalArgumentException.class, () -> service.cambiarPassword(1, null));
        }
    }
}
