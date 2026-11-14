package com.uniajc.seguridad;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import org.junit.jupiter.api.Test;

class PasswordHasherTest {
    private final PasswordHasher hasher = new PasswordHasher();

    @Test
    void generaUnHashConSalYCompruebaLaContrasena() {
        char[] contrasena = "Clave-de-prueba-123".toCharArray();
        String primero = hasher.hash(contrasena);
        String segundo = hasher.hash(contrasena);

        assertTrue(primero.startsWith("pbkdf2-sha512$"));
        assertNotEquals(primero, segundo, "Cada hash debe llevar una sal distinta.");
        assertTrue(hasher.verificar(contrasena, primero));
        assertFalse(hasher.verificar("otra-clave".toCharArray(), primero));
    }

    @Test
    void verificaContrasenaAnteriorParaPermitirSuMigracion() {
        assertTrue(hasher.verificar("clave-vieja".toCharArray(), "clave-vieja"));
        assertTrue(hasher.necesitaRehash("clave-vieja"));
    }

    @Test
    void verificaHashesAntiguosSinUsarlosParaNuevasClaves() throws Exception {
        byte[] entrada = "clave-vieja".getBytes(StandardCharsets.UTF_8);
        String md5Anterior = HexFormat.of().formatHex(MessageDigest.getInstance("MD5").digest(entrada));
        String sha1Anterior = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-1").digest(entrada));
        String sha256Anterior = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(entrada));
        String sha512Anterior = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-512").digest(entrada));

        assertTrue(hasher.verificar("clave-vieja".toCharArray(), md5Anterior));
        assertTrue(hasher.verificar("clave-vieja".toCharArray(), sha1Anterior));
        assertTrue(hasher.verificar("clave-vieja".toCharArray(), sha256Anterior));
        assertTrue(hasher.verificar("clave-vieja".toCharArray(), sha512Anterior));
        assertTrue(hasher.necesitaRehash(md5Anterior));
        assertTrue(hasher.necesitaRehash(sha1Anterior));
        assertTrue(hasher.necesitaRehash(sha256Anterior));
        assertTrue(hasher.necesitaRehash(sha512Anterior));
    }
}
