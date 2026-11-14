package com.uniajc.db;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Utilidad para inicializar las tablas y rutinas almacenadas en la base de datos de Neon. */
public class InicializadorBaseDatos {

    public static void main(String[] args) {
        // Con "solo-rutinas" se aplican únicamente funciones y procedimientos.
        // Es la opción segura para re-ejecutar: son CREATE OR REPLACE (idempotentes),
        // mientras que esquema.sql volvería a insertar docentes, materias, grupos
        // e inscripciones (esos INSERT no tienen ON CONFLICT).
        boolean soloRutinas = args.length > 0 && args[0].equalsIgnoreCase("solo-rutinas");

        System.out.println(soloRutinas
                ? "Conectando a Neon para crear las funciones y procedimientos..."
                : "Conectando a Neon para crear las tablas y rutinas...");

        try (Connection conn = DatabaseConexion.getConnection();
             Statement stmt = conn.createStatement()) {

            if (!soloRutinas) {
                ejecutarPorComandos(stmt, "esquema.sql", "database/esquema.sql");
            }
            ejecutarScriptCompleto(stmt, "procedimientos.sql", "database/procedimientos.sql");

            System.out.println("Listo.");

        } catch (Exception ex) {
            System.err.println("Error inicializando la base de datos: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DatabaseConexion.closeConnection();
        }
    }

    /** Ejecuta un script comando por comando (para DDL simple, separado por ';'). */
    private static void ejecutarPorComandos(Statement stmt, String recurso, String ruta) throws Exception {
        String sql = leerSql(recurso, ruta);
        List<String> comandos = limpiarComandos(sql);
        int ejecutados = 0;

        for (int i = 0; i < comandos.size(); i++) {
            String cmd = comandos.get(i);
            try {
                stmt.execute(cmd);
                ejecutados++;
                System.out.println("[OK] " + recurso + " #" + (i + 1) + ": " + resumir(cmd));
            } catch (Exception ex) {
                System.err.println("[ERROR] " + recurso + " #" + (i + 1) + " falló: " + ex.getMessage());
                System.err.println("  SQL: " + resumir(cmd, 150));
            }
        }
        System.out.println(recurso + ": " + ejecutados + " de " + comandos.size() + " comandos ejecutados.");
    }

    /**
     * Ejecuta un script completo en un solo envío.
     * Necesario para las funciones y procedimientos: sus cuerpos van entre $$ ... $$,
     * así que no se pueden trocear por ';' como el DDL simple.
     */
    private static void ejecutarScriptCompleto(Statement stmt, String recurso, String ruta) {
        try {
            stmt.execute(leerSql(recurso, ruta));
            System.out.println("[OK] " + recurso + ": funciones y procedimientos creados.");
        } catch (Exception ex) {
            System.err.println("[ERROR] " + recurso + " falló: " + ex.getMessage());
        }
    }

    private static String leerSql(String recurso, String ruta) throws Exception {
        InputStream is = InicializadorBaseDatos.class.getClassLoader()
                .getResourceAsStream(recurso);
        if (is != null) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
        java.nio.file.Path p = java.nio.file.Paths.get(ruta);
        if (java.nio.file.Files.exists(p)) {
            return java.nio.file.Files.readString(p, StandardCharsets.UTF_8);
        }
        throw new RuntimeException("No se encontró " + recurso);
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