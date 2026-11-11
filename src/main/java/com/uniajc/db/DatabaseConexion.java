package com.uniajc.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestor JDBC singleton para PostgreSQL (Neon).
 * Lee config.properties (HOST/URL, USER, PASSWORD, DATABASE).
 * Si la URL viene enmascarada con ***, la reconstruye con los datos reales.
 */
public class DatabaseConexion {

    private static Connection connection = null;
    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream is = DatabaseConexion.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (is != null) {
                properties.load(is);
                return;
            }
        } catch (IOException ignored) {}

        try (InputStream is = new FileInputStream("config.properties")) {
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(
                "No se encontro config.properties en el classpath ni en la raiz del proyecto.", e);
        }
    }

    private static String construirUrl() {
        String url = properties.getProperty("URL");
        String user = properties.getProperty("USER");
        String password = properties.getProperty("PASSWORD");
        String host = properties.getProperty("HOST");
        String database = properties.getProperty("DATABASE", "neondb");

        // La URL suele venir enmascarada (***) o sin prefijo jdbc: en el archivo.
        // Usar la URL solo si ya trae el prefijo jdbc: y no esta enmascarada.
        if (url != null && url.startsWith("jdbc:") && !url.contains("*")) {
            return url;
        }

        if (host == null || user == null || password == null) {
            throw new RuntimeException(
                "Faltan propiedades HOST/USER/PASSWORD en config.properties para reconstruir la URL.");
        }
        String base = host.startsWith("jdbc:") ? host : "jdbc:postgresql://" + host;
        return base + "/" + database + "?sslmode=require";
    }

    public static synchronized Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        String user = properties.getProperty("USER");
        String password = properties.getProperty("PASSWORD");
        String url = construirUrl();

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("Error conectando a la base de datos: " + e.getMessage(), e);
        }
        return connection;
    }

    public static synchronized void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                throw new RuntimeException("Error cerrando conexion: " + e.getMessage(), e);
            }
        }
    }
}
