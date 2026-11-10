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
import com.uniajc.modelo.Estudiante;

public class EstudianteDao {

    public void guardar(Estudiante estudiante) {
        String sql = "INSERT INTO \"practica-mvc\".estudiantes (name, lastname, email) VALUES (?, ?, ?)";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, estudiante.getNombre());
                pstmt.setString(2, estudiante.getApellido());
                pstmt.setString(3, estudiante.getEmail());

                pstmt.executeUpdate();

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        estudiante.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar estudiante: " + e.getMessage(), e);
        }
    }

    public List<Estudiante> obtenerTodos() {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT id, name, lastname, email FROM \"practica-mvc\".estudiantes ORDER BY id ASC";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    lista.add(mapearEntidad(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener estudiantes: " + e.getMessage(), e);
        }
        return lista;
    }

    public Optional<Estudiante> buscarPorId(int id) {
        String sql = "SELECT id, name, lastname, email FROM \"practica-mvc\".estudiantes WHERE id = ?";

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
            throw new RuntimeException("Error al buscar estudiante por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<Estudiante> buscarPorEmail(String email) {
        String sql = "SELECT id, name, lastname, email FROM \"practica-mvc\".estudiantes WHERE LOWER(email) = LOWER(?)";

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
            throw new RuntimeException("Error al buscar estudiante por email: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public void actualizar(Estudiante estudiante) {
        String sql = "UPDATE \"practica-mvc\".estudiantes SET name = ?, lastname = ?, email = ? WHERE id = ?";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, estudiante.getNombre());
                pstmt.setString(2, estudiante.getApellido());
                pstmt.setString(3, estudiante.getEmail());
                pstmt.setInt(4, estudiante.getId());

                int filas = pstmt.executeUpdate();
                if (filas == 0) {
                    throw new RuntimeException("Estudiante no encontrado con ID: " + estudiante.getId());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar estudiante: " + e.getMessage(), e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM \"practica-mvc\".estudiantes WHERE id = ?";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);

                int filas = pstmt.executeUpdate();
                if (filas == 0) {
                    throw new RuntimeException("Estudiante no encontrado con ID: " + id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar estudiante: " + e.getMessage(), e);
        }
    }

    private Estudiante mapearEntidad(ResultSet rs) throws SQLException {
        Estudiante estudiante = new Estudiante();
        estudiante.setId(rs.getInt("id"));
        estudiante.setNombre(rs.getString("name"));
        estudiante.setApellido(rs.getString("lastname"));
        estudiante.setEmail(rs.getString("email"));
        return estudiante;
    }
}
