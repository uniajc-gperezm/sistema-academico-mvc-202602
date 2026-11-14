package com.uniajc.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.uniajc.modelo.Usuario;
import com.uniajc.modelo.UsuarioDAO;
import com.uniajc.seguridad.PasswordHasher;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class AutenticacionServiceTest {
    @Test
    void autenticaYActualizaHashDeContrasenaAnterior() {
        final String[] hashAlmacenado = { "clave-anterior" };
        UsuarioDAO dao = new UsuarioDAO() {
            @Override public Optional<Usuario> buscarPorUsername(String username) {
                return Optional.of(new Usuario(7, username, "Usuario de prueba", "admin", true, hashAlmacenado[0]));
            }
            @Override public boolean actualizarHashContrasena(long id, String hash) {
                hashAlmacenado[0] = hash;
                return true;
            }
        };
        AutenticacionService service = new AutenticacionService(dao, new PasswordHasher());

        Optional<Usuario> resultado = service.autenticar("prueba", "clave-anterior".toCharArray());

        assertTrue(resultado.isPresent());
        assertEquals("prueba", resultado.get().username());
        assertNotNull(hashAlmacenado[0]);
        assertTrue(hashAlmacenado[0].startsWith("pbkdf2-sha512$"));
        assertFalse(service.autenticar("prueba", "incorrecta".toCharArray()).isPresent());
    }
}
