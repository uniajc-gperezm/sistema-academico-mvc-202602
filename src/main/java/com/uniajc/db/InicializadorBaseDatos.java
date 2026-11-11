package com.uniajc.db;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Utilidad para inicializar las tablas en la base de datos de Neon. */
public class InicializadorBaseDatos {

    public static void main(String[] args) {
        System.out.println("Conectando a Neon para crear las tablas...");
        try (Connection conn = DatabaseConexion.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = leerSql();
            List<String> comandos = limpiarComandos(sql);
            int ejecutados = 0;

            for (int i = 0; i < comandos.size(); i++) {
                String cmd = comandos.get(i);
                try {
                    stmt.execute(cmd);
                    ejecutados++;
                    System.out.println("[OK] Comando #" + (i + 1)
                            + ": " + resumir(cmd));
                } catch (Exception ex) {
                    System.err.println("[ERROR] Comando #" + (i + 1) + " falló: " + ex.getMessage());
                    System.err.println("  SQL: " + resumir(cmd, 150));
                }
            }
            System.out.println("Comandos ejecutados correctamente: " + ejecutados + " de " + comandos.size());

        } catch (Exception ex) {
            System.err.println("Error inicializando la base de datos: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DatabaseConexion.closeConnection();
        }
    }

    private static String leerSql() throws Exception {
        InputStream is = InicializadorBaseDatos.class.getClassLoader()
                .getResourceAsStream("esquema.sql");
        if (is != null) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
        java.nio.file.Path p = java.nio.file.Paths.get("database/esquema.sql");
        if (java.nio.file.Files.exists(p)) {
            return java.nio.file.Files.readString(p, StandardCharsets.UTF_8);
        }
        throw new RuntimeException("No se encontró esquema.sql");
    }

    /** Divide en comandos y elimina líneas de comentario (--) dentro de cada uno. */
    private static List<String> limpiarComandos(String sqlCompleto) {
        String[] bloques = sqlCompleto.split(";");
        List<String> comandos = new ArrayList<>();
        for (String bloque : bloques) {
            StringBuilder sb = new StringBuilder();
            for (String linea : bloque.split("\n")) {
                String l = linea.trim();
                if (l.isEmpty() || l.startsWith("--")) {
                    continue;
                }
                sb.append(l).append(' ');
            }
            String limpio = sb.toString().trim();
            if (!limpio.isEmpty()) {
                comandos.add(limpio);
            }
        }
        return comandos;
    }

    private static String resumir(String cmd) {
        return resumir(cmd, 60);
    }

    private static String resumir(String cmd, int max) {
        String plano = cmd.replaceAll("\\s+", " ");
        return plano.substring(0, Math.min(max, plano.length()));
    }
}