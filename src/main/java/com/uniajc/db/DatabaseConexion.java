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
    // Reutiliza una conexión durante la ejecución; las credenciales se leen al abrirla.
    private static Connection connection;

    /** Obtiene la conexión PostgreSQL configurada en el archivo local config.properties. */
    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        }

        Properties properties = new Properties();
        try {
            // Carga la configuración local para no guardar secretos dentro del código fuente.
            loadProperties(properties);

            // Admite el formato JDBC y el formato postgresql:// de algunos proveedores.
            String url = required(properties, "db.url", "URL");
            if (url.startsWith("postgresql://")) {
                url = "jdbc:" + url;
            }
            String user = firstProperty(properties, "db.user", "USERNAME", "USER");
            if (user == null || user.isBlank()) {
                throw new IOException("Falta la propiedad USER, USERNAME o db.user en config.properties.");
            }

            // Fuerza la sintaxis CALL de JDBC para que PostgreSQL invoque procedimientos, no funciones.
            Properties connectionProperties = new Properties();
            connectionProperties.setProperty("user", user);
            connectionProperties.setProperty("password", required(properties, "db.password", "PASSWORD"));
            connectionProperties.setProperty("escapeSyntaxCallMode", "callIfNoReturn");
            connection = DriverManager.getConnection(url, connectionProperties);
            System.out.println("Conexión exitosa.");
            return connection;
        } catch (SQLException | IOException error) {
            throw new IllegalStateException(
                    "No se pudo configurar o abrir la conexión a PostgreSQL: " + error.getMessage(), error);
        }
    }

    private static void loadProperties(Properties properties) throws IOException {
        // Busca primero en el directorio de ejecución y luego en el classpath del proyecto.
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
                    + "de proyecto ni en el classpath. Créalo con db.url, db.user y db.password, o con "
                    + "URL, USERNAME (o USER) y PASSWORD.");
        }
        try (resource) {
            properties.load(resource);
        }
    }

    private static Path findConfigFile() throws IOException {
        // También permite ejecutar desde la carpeta que contiene el proyecto.
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

    private static String required(Properties properties, String... keys) throws IOException {
        // Comprueba que la propiedad exista sin imprimir su valor sensible.
        String value = firstProperty(properties, keys);
        if (value == null || value.isBlank()) {
            throw new IOException("Falta la propiedad " + String.join(" o ", keys) + " en config.properties.");
        }
        return value;
    }

    private static String firstProperty(Properties properties, String... keys) {
        // Usa el primer alias configurado, por ejemplo db.user o USERNAME.
        for (String key : keys) {
            String value = properties.getProperty(key);
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
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
