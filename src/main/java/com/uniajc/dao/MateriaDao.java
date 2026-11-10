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
import com.uniajc.modelo.Materia;

public class MateriaDao {

    public void guardar(Materia materia) {
        String sql = "INSERT INTO \"practica-mvc\".materias (name, credits) VALUES (?, ?)";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, materia.getNombre());
                pstmt.setInt(2, materia.getCreditos());

                pstmt.executeUpdate();

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        materia.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar materia: " + e.getMessage(), e);
        }
    }

    public List<Materia> obtenerTodos() {
        List<Materia> lista = new ArrayList<>();
        String sql = "SELECT id, name, credits FROM \"practica-mvc\".materias ORDER BY id ASC";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    lista.add(mapearEntidad(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener materias: " + e.getMessage(), e);
        }
        return lista;
    }

    public Optional<Materia> buscarPorId(int id) {
        String sql = "SELECT id, name, credits FROM \"practica-mvc\".materias WHERE id = ?";

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
            throw new RuntimeException("Error al buscar materia por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<Materia> buscarPorNombre(String nombre) {
        String sql = "SELECT id, name, credits FROM \"practica-mvc\".materias WHERE LOWER(name) = LOWER(?)";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nombre);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapearEntidad(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar materia por nombre: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public void actualizar(Materia materia) {
        String sql = "UPDATE \"practica-mvc\".materias SET name = ?, credits = ? WHERE id = ?";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, materia.getNombre());
                pstmt.setInt(2, materia.getCreditos());
                pstmt.setInt(3, materia.getId());

                int filas = pstmt.executeUpdate();
                if (filas == 0) {
                    throw new RuntimeException("Materia no encontrada con ID: " + materia.getId());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar materia: " + e.getMessage(), e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM \"practica-mvc\".materias WHERE id = ?";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);

                int filas = pstmt.executeUpdate();
                if (filas == 0) {
                    throw new RuntimeException("Materia no encontrada con ID: " + id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar materia: " + e.getMessage(), e);
        }
    }

    private Materia mapearEntidad(ResultSet rs) throws SQLException {
        Materia materia = new Materia();
        materia.setId(rs.getInt("id"));
        materia.setNombre(rs.getString("name"));
        materia.setCreditos(rs.getInt("credits"));
        return materia;
    }
}
