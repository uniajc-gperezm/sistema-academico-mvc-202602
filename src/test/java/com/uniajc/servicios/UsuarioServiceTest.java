package com.uniajc.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uniajc.modelo.Usuario;

public class UsuarioServiceTest {

    private UsuarioService usuarioService;

    @BeforeEach
    public void setUp() {
        usuarioService = new UsuarioService();
    }

    @Test
    public void testAutenticarCamposVacios() {
        assertThrows(IllegalArgumentException.class, () -> usuarioService.autenticar("", "123"));
        assertThrows(IllegalArgumentException.class, () -> usuarioService.autenticar("admin", ""));
        assertThrows(IllegalArgumentException.class, () -> usuarioService.autenticar(null, "123"));
    }

    @Test
    public void testAutenticarUsuarioInexistente() {
        assertThrows(IllegalArgumentException.class, () -> usuarioService.autenticar("usuario_inexistente_xyz_123", "clave"));
    }

    @Test
    public void testAutenticarExitosoTextoPlano() {
        Usuario usuario = usuarioService.autenticar("alejandro", "12345678");
        assertNotNull(usuario);
        assertEquals("alejandro", usuario.getUsername());
    }

    @Test
    public void testAutenticarExitosoAdmin() {
        Usuario usuario = usuarioService.autenticar("admin", "admin");
        assertNotNull(usuario);
        assertEquals("admin", usuario.getUsername());
    }

    @Test
    public void testAutenticarContrasenaErronea() {
        assertThrows(IllegalArgumentException.class, () -> usuarioService.autenticar("alejandro", "password_incorrecta"));
    }

    @Test
    public void testRegistrarUsuarioConSHA256() {
        String testUser = "user_sha_" + System.currentTimeMillis();
        Usuario nuevo = new Usuario(testUser, "claveSecreta123", "Prueba SHA", "ADMIN", true, "test@uniajc.edu.co");
        
        usuarioService.registrarUsuario(nuevo);
        
        // Verificar que se guardó y la contraseña tiene longitud de hash SHA-256 (64 caracteres hexadecimales)
        Usuario guardado = usuarioService.buscarPorUsername(testUser).orElse(null);
        assertNotNull(guardado);
        assertEquals(64, guardado.getPassword().length(), "La contraseña debe guardarse con hash SHA-256 (64 hex)");
        
        // Verificar que autentica exitosamente con la contraseña plana
        Usuario autenticado = usuarioService.autenticar(testUser, "claveSecreta123");
        assertNotNull(autenticado);
        assertEquals(testUser, autenticado.getUsername());
        
        // Limpiar registro de prueba
        usuarioService.eliminarUsuario(guardado.getId());
    }
}

