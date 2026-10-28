package com.uniajc.db;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.stream.Stream;

public class DatabaseConexion {
    private static Connection connection;

    /** Obtiene la conexión a PostgreSQL usando config.properties. */
    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        }

        Properties properties = new Properties();
        try {
            loadProperties(properties);

            String url = required(properties, "URL");
            if (url.startsWith("postgresql://")) {
                url = "jdbc:" + url;
            }
            String user = properties.getProperty("USERNAME", properties.getProperty("USER"));
            if (user == null || user.isBlank()) {
                throw new IOException("Falta la propiedad USER (o USERNAME) en config.properties.");
            }

            connection = DriverManager.getConnection(url, user, required(properties, "PASSWORD"));
            System.out.println("Conexión exitosa.");
            return connection;
        } catch (SQLException | IOException error) {
            throw new IllegalStateException(
                    "No se pudo configurar o abrir la conexión a PostgreSQL: " + error.getMessage(), error);
        }
    }

    private static void loadProperties(Properties properties) throws IOException {
        Path configPath = findConfigFile();
        if (configPath != null) {
            try (InputStream input = Files.newInputStream(configPath)) {
                properties.load(input);
            }
            return;
        }

        InputStream resource = DatabaseConexion.class.getClassLoader()
                .getResourceAsStream("config.properties");
        if (resource == null) {
            throw new IOException("No se encontró config.properties en la carpeta actual, sus subcarpetas "
                    + "de proyecto ni en el classpath. Créalo en la raíz del proyecto con URL, USERNAME "
                    + "(o USER) y PASSWORD.");
        }
        try (resource) {
            properties.load(resource);
        }
    }

    private static Path findConfigFile() throws IOException {
        Path current = Path.of("").toAbsolutePath().normalize();
        Path direct = current.resolve("config.properties");
        if (Files.isRegularFile(direct)) {
            return direct;
        }

        try (Stream<Path> children = Files.list(current)) {
            return children.filter(Files::isDirectory)
                    .map(child -> child.resolve("config.properties"))
                    .filter(Files::isRegularFile)
                    .findFirst().orElse(null);
        }
    }

    private static String required(Properties properties, String key) throws IOException {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IOException("Falta la propiedad " + key + " en config.properties.");
        }
        return value;
    }

    /** Cierra la conexión si está abierta. */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Conexión cerrada correctamente.");
            } catch (SQLException error) {
                throw new IllegalStateException("No se pudo cerrar la conexión: " + error.getMessage(), error);
            }
        }
    }
}
