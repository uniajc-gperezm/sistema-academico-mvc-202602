package com.uniajc.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionPostgresDatabase {

    private static final Properties PROPS = new Properties();

    static {
        try (FileInputStream in = new FileInputStream("config.properties")) {
            PROPS.load(in);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo leer config.properties", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            PROPS.getProperty("db.url").trim(),
            PROPS.getProperty("db.user").trim(),
            PROPS.getProperty("db.password").trim());
    }
}