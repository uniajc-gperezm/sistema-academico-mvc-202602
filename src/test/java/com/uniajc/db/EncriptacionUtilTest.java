package com.uniajc.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("EncriptacionUtil")
class EncriptacionUtilTest {

    @Test
    @DisplayName("MD5 coincide con el hash conocido de admin123")
    void md5GeneraHashConocido() {
        // Valor de referencia usado en database/esquema.sql para el usuario admin.
        assertEquals("0192023a7bbd73250516f069df18b500", EncriptacionUtil.md5("admin123"));
    }

    @Test
    @DisplayName("MD5 coincide con el hash conocido de usuario123")
    void md5GeneraHashConocidoDeUsuario() {
        assertEquals("401cec94d3ed586d8cb895c10c0f7db6", EncriptacionUtil.md5("usuario123"));
    }

    @Test
    @DisplayName("es determinista: el mismo texto produce el mismo hash")
    void md5EsDeterminista() {
        assertEquals(EncriptacionUtil.md5("clave-secreta"), EncriptacionUtil.md5("clave-secreta"));
    }

    @Test
    @DisplayName("textos distintos producen hashes distintos")
    void md5DistingueTextosDiferentes() {
        assertNotEquals(EncriptacionUtil.md5("clave1"), EncriptacionUtil.md5("clave2"));
    }

    @Test
    @DisplayName("el hash MD5 tiene 32 caracteres hexadecimales")
    void md5DevuelveLongitudCorrecta() {
        String hash = EncriptacionUtil.md5("cualquier cosa");

        assertEquals(32, hash.length());
        assertTrue(hash.matches("[0-9a-f]{32}"), "el hash debe ser hexadecimal en minusculas");
    }

    @Test
    @DisplayName("SHA-256 produce un hash de 64 caracteres")
    void sha256DevuelveLongitudCorrecta() {
        String hash = EncriptacionUtil.sha256("admin123");

        assertEquals(64, hash.length());
        assertTrue(hash.matches("[0-9a-f]{64}"));
    }

    @Test
    @DisplayName("SHA-256 y MD5 del mismo texto son diferentes")
    void sha256EsDistintoDeMd5() {
        assertNotEquals(EncriptacionUtil.md5("admin123"), EncriptacionUtil.sha256("admin123"));
    }

    @Test
    @DisplayName("no acepta texto nulo")
    void rechazaTextoNulo() {
        assertThrows(NullPointerException.class, () -> EncriptacionUtil.md5(null));
    }
}
