package com.uniajc.seguridad;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** Genera y verifica hashes PBKDF2-HMAC-SHA512; acepta hashes antiguos para migrarlos al autenticar. */
public final class PasswordHasher {
    private static final int ITERACIONES = 220_000;
    private static final int TAMANO_SAL = 16;
    private static final int TAMANO_HASH_BITS = 256;
    private static final String PREFIJO = "pbkdf2-sha512";
    private final SecureRandom random = new SecureRandom();

    public String hash(char[] contrasena) {
        if (contrasena == null || contrasena.length == 0) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }
        byte[] sal = new byte[TAMANO_SAL];
        random.nextBytes(sal);
        byte[] derivado = derivar(contrasena, sal, ITERACIONES, TAMANO_HASH_BITS);
        return PREFIJO + "$" + ITERACIONES + "$" + Base64.getEncoder().encodeToString(sal)
                + "$" + Base64.getEncoder().encodeToString(derivado);
    }

    public boolean verificar(char[] contrasena, String almacenada) {
        if (contrasena == null || almacenada == null) return false;
        if (almacenada.startsWith(PREFIJO + "$")) return verificarPbkdf2(contrasena, almacenada);
        return verificarFormatoAnterior(contrasena, almacenada);
    }

    public boolean necesitaRehash(String almacenada) {
        if (almacenada == null || !almacenada.startsWith(PREFIJO + "$")) return true;
        String[] partes = almacenada.split("\\$", -1);
        if (partes.length != 4) return true;
        try {
            return Integer.parseInt(partes[1]) < ITERACIONES;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private boolean verificarPbkdf2(char[] contrasena, String almacenada) {
        try {
            String[] partes = almacenada.split("\\$", -1);
            if (partes.length != 4) return false;
            int iteraciones = Integer.parseInt(partes[1]);
            if (iteraciones < 10_000 || iteraciones > 2_000_000) return false;
            byte[] sal = Base64.getDecoder().decode(partes[2]);
            byte[] hashEsperado = Base64.getDecoder().decode(partes[3]);
            byte[] hashCalculado = derivar(contrasena, sal, iteraciones, hashEsperado.length * 8);
            return MessageDigest.isEqual(hashEsperado, hashCalculado);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean verificarFormatoAnterior(char[] contrasena, String almacenada) {
        byte[] hashEsperado;
        byte[] hashCalculado;
        try {
            if (almacenada.matches("(?i)^[0-9a-f]{32}$")) {
                hashEsperado = HexFormat.of().parseHex(almacenada);
                hashCalculado = digest("MD5", contrasena);
            } else if (almacenada.matches("(?i)^[0-9a-f]{40}$")) {
                hashEsperado = HexFormat.of().parseHex(almacenada);
                hashCalculado = digest("SHA-1", contrasena);
            } else if (almacenada.matches("(?i)^[0-9a-f]{64}$")) {
                hashEsperado = HexFormat.of().parseHex(almacenada);
                hashCalculado = digest("SHA-256", contrasena);
            } else if (almacenada.matches("(?i)^[0-9a-f]{128}$")) {
                hashEsperado = HexFormat.of().parseHex(almacenada);
                hashCalculado = digest("SHA-512", contrasena);
            } else {
                hashEsperado = almacenada.getBytes(StandardCharsets.UTF_8);
                hashCalculado = new String(contrasena).getBytes(StandardCharsets.UTF_8);
            }
            return MessageDigest.isEqual(hashEsperado, hashCalculado);
        } catch (GeneralSecurityException | IllegalArgumentException e) {
            throw new IllegalStateException("No se pudo verificar la contraseña.", e);
        }
    }

    private byte[] digest(String algoritmo, char[] contrasena) throws GeneralSecurityException {
        MessageDigest digest = MessageDigest.getInstance(algoritmo);
        return digest.digest(new String(contrasena).getBytes(StandardCharsets.UTF_8));
    }

    private byte[] derivar(char[] contrasena, byte[] sal, int iteraciones, int bits) {
        PBEKeySpec spec = new PBEKeySpec(contrasena, sal, iteraciones, bits);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512").generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("PBKDF2-HMAC-SHA512 no está disponible en este Java.", e);
        } finally {
            spec.clearPassword();
        }
    }
}
