package com.uniajc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.uniajc.db.DatabaseConexion;
import com.uniajc.modelo.Docente;

public class DocenteDao {

    public void guardar(Docente docente) {
        String sql = "INSERT INTO \"practica-mvc\".docentes (name, specialty, email) VALUES (?, ?, ?)";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, docente.getNombre());
                pstmt.setString(2, docente.getEspecialidad());
                pstmt.setString(3, docente.getEmail());

                pstmt.executeUpdate();

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        docente.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar docente: " + e.getMessage(), e);
        }
    }

    public List<Docente> obtenerTodos() {
        List<Docente> lista = new ArrayList<>();
        String sql = "SELECT id, name, specialty, email FROM \"practica-mvc\".docentes ORDER BY id ASC";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    lista.add(mapearEntidad(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener docentes: " + e.getMessage(), e);
        }
        return lista;
    }

    public Optional<Docente> buscarPorId(int id) {
        String sql = "SELECT id, name, specialty, email FROM \"practica-mvc\".docentes WHERE id = ?";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapearEntidad(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar docente por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<Docente> buscarPorEmail(String email) {
        String sql = "SELECT id, name, specialty, email FROM \"practica-mvc\".docentes WHERE LOWER(email) = LOWER(?)";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, email);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapearEntidad(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar docente por email: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public void actualizar(Docente docente) {
        String sql = "UPDATE \"practica-mvc\".docentes SET name = ?, specialty = ?, email = ? WHERE id = ?";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, docente.getNombre());
                pstmt.setString(2, docente.getEspecialidad());
                pstmt.setString(3, docente.getEmail());
                pstmt.setInt(4, docente.getId());

                int filas = pstmt.executeUpdate();
                if (filas == 0) {
                    throw new RuntimeException("Docente no encontrado con ID: " + docente.getId());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar docente: " + e.getMessage(), e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM \"practica-mvc\".docentes WHERE id = ?";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);

                int filas = pstmt.executeUpdate();
                if (filas == 0) {
                    throw new RuntimeException("Docente no encontrado con ID: " + id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar docente: " + e.getMessage(), e);
        }
    }

    private Docente mapearEntidad(ResultSet rs) throws SQLException {
        Docente docente = new Docente();
        docente.setId(rs.getInt("id"));
        docente.setNombre(rs.getString("name"));
        docente.setEspecialidad(rs.getString("specialty"));
        docente.setEmail(rs.getString("email"));
        return docente;
    }
}
