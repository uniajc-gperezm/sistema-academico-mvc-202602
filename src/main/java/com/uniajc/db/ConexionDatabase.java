package com.uniajc.db;

import java.sql.Connection;

/**
 * Clase alias para soportar la nomenclatura indicada en el README (ConexionDatabase).
 */
public class ConexionDatabase {
    public static Connection getConnection() {
        return DatabaseConexion.getConnection();
    }

    public static void closeConnection() {
        DatabaseConexion.closeConnection();
    }
}
