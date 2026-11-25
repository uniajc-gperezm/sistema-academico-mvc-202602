package com.uniajc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import com.uniajc.db.DatabaseConexion;

/**
 * Herramienta diagnóstica: lista las columnas de las tablas en practica-mvc.
 */
public class InspectarColumnas {

    public static void main(String[] args) throws Exception {
        String[] tablas = {"usuarios", "estudiantes", "docentes", "materias", "grupos", "inscripciones", "periodos_academicos", "programas", "calificaciones"};

        try (Connection conn = DatabaseConexion.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();

            for (String tabla : tablas) {
                System.out.println("\n=== Tabla: " + tabla + " ===");
                try (ResultSet cols = meta.getColumns(null, "practica-mvc", tabla, null)) {
                    boolean encontro = false;
                    while (cols.next()) {
                        encontro = true;
                        String colName  = cols.getString("COLUMN_NAME");
                        String colType  = cols.getString("TYPE_NAME");
                        int    colSize  = cols.getInt("COLUMN_SIZE");
                        String nullable = cols.getString("IS_NULLABLE");
                        System.out.printf("  %-30s  %-15s  size=%-6d  nullable=%s%n",
                                colName, colType, colSize, nullable);
                    }
                    if (!encontro) {
                        System.out.println("  (tabla no encontrada con ese schema o nombre)");
                    }
                }
            }
        }

        System.out.println("\nDone.");
    }
}

