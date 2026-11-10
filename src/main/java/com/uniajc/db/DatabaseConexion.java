package com.uniajc.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseConexion {
    private static Connection connection = null;
    private static String customUrl = null;
    private static String customUser = null;
    private static String customPassword = null;
    private static boolean isInitialized = false;

    /**
     * Permite configurar manualmente los parámetros de conexión (útil para pruebas o custom DB).
     */
    public static synchronized void setConfig(String url, String user, String password) {
        customUrl = url;
        customUser = user;
        customPassword = password;
        closeConnection();
    }

    /**
     * Permite inyectar una conexión personalizada (ej. en pruebas).
     */
    public static synchronized void setConnection(Connection conn) {
        connection = conn;
    }

    /**
     * Obtiene la conexión activa a la base de datos.
     * Carga las propiedades desde config.properties si existen; de lo contrario utiliza
     * una base de datos local H2 en modo PostgreSQL para compatibilidad total sin dependencias externas.
     */
    public static synchronized Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
        } catch (SQLException e) {
            connection = null;
        }

        Properties properties = new Properties();
        String url = customUrl;
        String user = customUser;
        String password = customPassword;

        if (url == null) {
            InputStream in = null;
            File[] posiblesArchivos = new File[]{
                    new File("config.properties"),
                    new File("sistema-academico-mvc-202602/config.properties"),
                    new File("../config.properties")
            };

            for (File f : posiblesArchivos) {
                if (f.exists()) {
                    try {
                        in = new FileInputStream(f);
                        System.out.println("Cargando configuracion desde: " + f.getAbsolutePath());
                        break;
                    } catch (IOException ignored) {}
                }
            }

            if (in == null) {
                in = DatabaseConexion.class.getClassLoader().getResourceAsStream("config.properties");
            }

            if (in != null) {
                try {
                    properties.load(in);
                    in.close();
                    url = properties.getProperty("URL", properties.getProperty("url", null));
                    user = properties.getProperty("USER", properties.getProperty("user", ""));
                    password = properties.getProperty("PASSWORD", properties.getProperty("password", ""));
                } catch (IOException e) {
                    System.err.println("Error leyendo config.properties: " + e.getMessage());
                }
            }
        }

        // Si no se encontró config.properties o no especificó URL, usar base de datos embebida H2 compatible con Postgres
        if (url == null || url.trim().isEmpty()) {
            System.out.println("Aviso: config.properties no encontrado o sin URL. Usando H2 embebido local.");
            url = "jdbc:h2:./academico_local_db;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH";
            user = "sa";
            password = "";
        }

        // Intentar registrar drivers conocidos si están en classpath
        try { Class.forName("org.postgresql.Driver"); } catch (ClassNotFoundException ignored) {}
        try { Class.forName("org.h2.Driver"); } catch (ClassNotFoundException ignored) {}

        try {
            if (user != null && !user.isEmpty()) {
                connection = DriverManager.getConnection(url, user, password);
            } else {
                connection = DriverManager.getConnection(url);
            }
            System.out.println("Conexion exitosa a la base de datos.");

            if (!isInitialized) {
                initDatabase();
                isInitialized = true;
            }
        } catch (SQLException e) {
            System.err.println("Error al establecer la conexion: " + e.getMessage());
            // Fallback a base en memoria en caso de fallo de red en Neon/Postgres
            try {
                System.out.println("Intentando conexion de respaldo en memoria H2...");
                connection = DriverManager.getConnection("jdbc:h2:mem:academico_backup;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1", "sa", "");
                initDatabase();
                isInitialized = true;
            } catch (SQLException ex) {
                throw new RuntimeException("No se pudo conectar a la base de datos ni inicializar la de respaldo", ex);
            }
        }

        return connection;
    }

    /**
     * Inicializa el esquema y las tablas requeridas por la guía.
     */
    public static synchronized void initDatabase() {
        if (connection == null) return;
        try (Statement stmt = connection.createStatement()) {
            // Crear esquema si es soportado
            try {
                stmt.execute("CREATE SCHEMA IF NOT EXISTS \"practica-mvc\"");
            } catch (SQLException ignored) {}

            try {
                stmt.execute("SET search_path TO \"practica-mvc\", public");
            } catch (SQLException ignored) {}

            // Crear tabla estudiantes
            stmt.execute("CREATE TABLE IF NOT EXISTS \"practica-mvc\".estudiantes (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "lastname VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(100) UNIQUE NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");

            // Crear tabla docentes
            stmt.execute("CREATE TABLE IF NOT EXISTS \"practica-mvc\".docentes (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "specialty VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(100) UNIQUE NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");

            // Crear tabla materias
            stmt.execute("CREATE TABLE IF NOT EXISTS \"practica-mvc\".materias (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "credits INT NOT NULL CHECK (credits >= 1 AND credits <= 10), " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");

            // Crear tabla grupos
            stmt.execute("CREATE TABLE IF NOT EXISTS \"practica-mvc\".grupos (" +
                    "id SERIAL PRIMARY KEY, " +
                    "id_materia INT NOT NULL REFERENCES \"practica-mvc\".materias(id) ON DELETE CASCADE, " +
                    "id_docente INT NOT NULL REFERENCES \"practica-mvc\".docentes(id) ON DELETE CASCADE, " +
                    "classroom VARCHAR(50) NOT NULL, " +
                    "schedule VARCHAR(100) NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");

            // Crear tabla inscripciones
            stmt.execute("CREATE TABLE IF NOT EXISTS \"practica-mvc\".inscripciones (" +
                    "id SERIAL PRIMARY KEY, " +
                    "id_estudiante INT NOT NULL REFERENCES \"practica-mvc\".estudiantes(id) ON DELETE CASCADE, " +
                    "id_grupo INT NOT NULL REFERENCES \"practica-mvc\".grupos(id) ON DELETE CASCADE, " +
                    "final_grade DECIMAL(5,2) CHECK (final_grade >= 0 AND final_grade <= 5), " +
                    "status VARCHAR(20) DEFAULT 'ACTIVO' CHECK (status IN ('ACTIVO', 'APROBADO', 'REPROBADO', 'CANCELADO')), " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "UNIQUE(id_estudiante, id_grupo)" +
                    ")");

        } catch (SQLException e) {
            System.err.println("Aviso durante la inicializacion de la base de datos: " + e.getMessage());
        }
    }

    /**
     * Cierra la conexión si está abierta.
     */
    public static synchronized void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error cerrando conexion: " + e.getMessage());
            } finally {
                connection = null;
                isInitialized = false;
            }
        }
    }
}
