package com.uniajc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.uniajc.config.ConexionPostgresDatabase;
import com.uniajc.modelo.Estudiante;

public class EstudianteDao {

    private static final String TABLA = "\"practica-mvc\".estudiante";
    private static final String COLUMNAS = "id_estudiante, nombre, apellido, email";

    public void guardar(Estudiante e) {
        String sql = "INSERT INTO " + TABLA + " (nombre, apellido, email) VALUES (?, ?, ?)";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getApellido());
            ps.setString(3, e.getEmail());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error en base de datos", ex);
        }
    }

    public List<Estudiante> obtenerTodos() {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT " + COLUMNAS + " FROM " + TABLA + " ORDER BY id_estudiante";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error en base de datos", ex);
        }
        return lista;
    }

    public Optional<Estudiante> buscarPorId(int id) {
        String sql = "SELECT " + COLUMNAS + " FROM " + TABLA + " WHERE id_estudiante = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error en base de datos", ex);
        }
        return Optional.empty();
    }

    public Optional<Estudiante> buscarPorCampo(String email) {
        String sql = "SELECT " + COLUMNAS + " FROM " + TABLA + " WHERE email = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error en base de datos", ex);
        }
        return Optional.empty();
    }

    public void actualizar(Estudiante e) {
        String sql = "UPDATE " + TABLA + " SET nombre = ?, apellido = ?, email = ? WHERE id_estudiante = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getApellido());
            ps.setString(3, e.getEmail());
            ps.setInt(4, e.getId());
            if (ps.executeUpdate() == 0) {
                throw new RuntimeException("Estudiante no encontrado con ID: " + e.getId());
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error en base de datos", ex);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM " + TABLA + " WHERE id_estudiante = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new RuntimeException("Estudiante no encontrado con ID: " + id);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error en base de datos", ex);
        }
    }

    private Estudiante mapear(ResultSet rs) throws SQLException {
        Estudiante e = new Estudiante();
        e.setId(rs.getInt("id_estudiante"));
        e.setNombre(rs.getString("nombre"));
        e.setApellido(rs.getString("apellido"));
        e.setEmail(rs.getString("email"));
        return e;
    }
}