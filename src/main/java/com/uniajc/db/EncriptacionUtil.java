package com.uniajc.db;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilidad de encriptación de contraseñas.
 * Implementa MD5 (mínimo) y SHA-256. Las contraseñas nunca se guardan en texto plano.
 */
public final class EncriptacionUtil {

    private EncriptacionUtil() {}

    /** Genera el hash MD5 de un texto. */
    public static String md5(String texto) {
        return hash(texto, "MD5");
    }

    /** Genera el hash SHA-256 de un texto. */
    public static String sha256(String texto) {
        return hash(texto, "SHA-256");
    }

    private static String hash(String texto, String algoritmo) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algoritmo);
            byte[] hash = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                String h = Integer.toHexString(0xFF & b);
                if (h.length() == 1) hex.append('0');
                hex.append(h);
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Algoritmo no disponible: " + algoritmo, ex);
        }
    }
}