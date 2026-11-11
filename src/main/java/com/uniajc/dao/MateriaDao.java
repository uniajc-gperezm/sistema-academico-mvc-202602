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

/** DAO de materias. Tabla real: materias (id_materia, nombre_materia, creditos). */
public class MateriaDao {

    private static final String TABLA = "materias";

    public void guardar(Materia m) {
        String sql = "INSERT INTO " + TABLA + " (nombre_materia, creditos) VALUES (?, ?)";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getNombre_materia());
            ps.setInt(2, m.getCreditos());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) m.setId_materia(rs.getInt(1));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error guardando materia: " + ex.getMessage(), ex);
        }
    }

    public List<Materia> obtenerTodos() {
        List<Materia> lista = new ArrayList<>();
        String sql = "SELECT id_materia, nombre_materia, creditos FROM " + TABLA + " ORDER BY id_materia";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException ex) {
            throw new RuntimeException("Error listando materias: " + ex.getMessage(), ex);
        }
        return lista;
    }

    public Optional<Materia> buscarPorId(int id) {
        String sql = "SELECT id_materia, nombre_materia, creditos FROM " + TABLA + " WHERE id_materia = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error buscando materia: " + ex.getMessage(), ex);
        }
        return Optional.empty();
    }

    public void actualizar(Materia m) {
        String sql = "UPDATE " + TABLA + " SET nombre_materia = ?, creditos = ? WHERE id_materia = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getNombre_materia());
            ps.setInt(2, m.getCreditos());
            ps.setInt(3, m.getId_materia());
            int filas = ps.executeUpdate();
            if (filas == 0) throw new RuntimeException("Materia no encontrada con ID: " + m.getId_materia());
        } catch (SQLException ex) {
            throw new RuntimeException("Error actualizando materia: " + ex.getMessage(), ex);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM " + TABLA + " WHERE id_materia = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas == 0) throw new RuntimeException("Materia no encontrada con ID: " + id);
        } catch (SQLException ex) {
            throw new RuntimeException("Error eliminando materia: " + ex.getMessage(), ex);
        }
    }

    private Materia mapear(ResultSet rs) throws SQLException {
        Materia m = new Materia();
        m.setId_materia(rs.getInt("id_materia"));
        m.setNombre_materia(rs.getString("nombre_materia"));
        m.setCreditos(rs.getInt("creditos"));
        return m;
    }
}
