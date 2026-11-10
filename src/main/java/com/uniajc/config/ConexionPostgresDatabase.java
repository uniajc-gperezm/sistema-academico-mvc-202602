package com.uniajc.config;

import java.sql.Connection;
import com.uniajc.db.DatabaseConexion;

/**
 * Clase alias de conexión requerida por la Guía de Implementación.
 */
public class ConexionPostgresDatabase {
    public static Connection getConnection() {
        return DatabaseConexion.getConnection();
    }

    public static void closeConnection() {
        DatabaseConexion.closeConnection();
    }
}
