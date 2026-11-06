package com.uniajc;

import java.sql.Connection;

import com.uniajc.config.ConexionPostgresDatabase;

public class Main {
    public static void main(String[] args) {
        try (Connection c = ConexionPostgresDatabase.getConnection()) {
            System.out.println("Conexión OK: " + !c.isClosed());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}