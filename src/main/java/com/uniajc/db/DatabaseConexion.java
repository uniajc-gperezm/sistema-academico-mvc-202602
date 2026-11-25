package com.uniajc.db;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class DatabaseConexion {

    private static String url;
    private static String user;
    private static String password;
    private static boolean configured = false;

    private static void cargarPropiedades() {
        if (configured) return;
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File("config.properties")));
            url = properties.getProperty("URL");
            user = properties.getProperty("USER");
            password = properties.getProperty("PASSWORD");
            configured = true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Archivo config.properties no encontrado.", e);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer config.properties.", e);
        }
    }

    public static Connection getConnection() {
        cargarPropiedades();
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException("Error conectando a la base de datos: " + e.getMessage(), e);
        }
    }
}
