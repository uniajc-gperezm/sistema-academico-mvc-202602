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

/** DAO de estudiantes. Tabla: public.estudiantes (id, name, lastname, email). */
public class EstudianteDao implements EstudianteRepositorio {

    private static final String TABLA = "estudiantes";

    @Override
    public void guardar(Estudiante e) {
        String sql = "INSERT INTO " + TABLA + " (name, lastname, email) VALUES (?, ?, ?)";
        Connection conn = DatabaseConexion.getConnection();
        try (
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getApellido());
            ps.setString(3, e.getEmail());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) e.setId_estudiante(rs.getInt(1));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error guardando estudiante: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Estudiante> obtenerTodos() {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT id, name, lastname, email FROM " + TABLA + " ORDER BY id";
        Connection conn = DatabaseConexion.getConnection();
        try (
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException ex) {
            throw new RuntimeException("Error listando estudiantes: " + ex.getMessage(), ex);
        }
        return lista;
    }

    @Override
    public Optional<Estudiante> buscarPorId(int id) {
        String sql = "SELECT id, name, lastname, email FROM " + TABLA + " WHERE id = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error buscando estudiante: " + ex.getMessage(), ex);
        }
        return Optional.empty();
    }

    @Override
    public void actualizar(Estudiante e) {
        String sql = "UPDATE " + TABLA + " SET name = ?, lastname = ?, email = ? WHERE id = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getApellido());
            ps.setString(3, e.getEmail());
            ps.setInt(4, e.getId_estudiante());
            int filas = ps.executeUpdate();
            if (filas == 0) throw new RuntimeException("Estudiante no encontrado con ID: " + e.getId_estudiante());
        } catch (SQLException ex) {
            throw new RuntimeException("Error actualizando estudiante: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM " + TABLA + " WHERE id = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas == 0) throw new RuntimeException("Estudiante no encontrado con ID: " + id);
        } catch (SQLException ex) {
            throw new RuntimeException("Error eliminando estudiante: " + ex.getMessage(), ex);
        }
    }

    private Estudiante mapear(ResultSet rs) throws SQLException {
        Estudiante e = new Estudiante();
        e.setId_estudiante(rs.getInt("id"));
        e.setNombre(rs.getString("name"));
        e.setApellido(rs.getString("lastname"));
        e.setEmail(rs.getString("email"));
        return e;
    }
}
