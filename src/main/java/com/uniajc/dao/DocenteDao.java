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

/** DAO de docentes. Tabla real: docentes (id_docente, nombre, especialidad). */
public class DocenteDao implements DocenteRepositorio {

    private static final String TABLA = "docentes";

    @Override
    public void guardar(Docente d) {
        String sql = "INSERT INTO " + TABLA + " (nombre, especialidad) VALUES (?, ?)";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getNombre());
            ps.setString(2, d.getEspecialidad());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) d.setId_docente(rs.getInt(1));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error guardando docente: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Docente> obtenerTodos() {
        List<Docente> lista = new ArrayList<>();
        String sql = "SELECT id_docente, nombre, especialidad FROM " + TABLA + " ORDER BY id_docente";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException ex) {
            throw new RuntimeException("Error listando docentes: " + ex.getMessage(), ex);
        }
        return lista;
    }

    @Override
    public Optional<Docente> buscarPorId(int id) {
        String sql = "SELECT id_docente, nombre, especialidad FROM " + TABLA + " WHERE id_docente = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error buscando docente: " + ex.getMessage(), ex);
        }
        return Optional.empty();
    }

    @Override
    public void actualizar(Docente d) {
        String sql = "UPDATE " + TABLA + " SET nombre = ?, especialidad = ? WHERE id_docente = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getNombre());
            ps.setString(2, d.getEspecialidad());
            ps.setInt(3, d.getId_docente());
            int filas = ps.executeUpdate();
            if (filas == 0) throw new RuntimeException("Docente no encontrado con ID: " + d.getId_docente());
        } catch (SQLException ex) {
            throw new RuntimeException("Error actualizando docente: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM " + TABLA + " WHERE id_docente = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas == 0) throw new RuntimeException("Docente no encontrado con ID: " + id);
        } catch (SQLException ex) {
            throw new RuntimeException("Error eliminando docente: " + ex.getMessage(), ex);
        }
    }

    private Docente mapear(ResultSet rs) throws SQLException {
        Docente d = new Docente();
        d.setId_docente(rs.getInt("id_docente"));
        d.setNombre(rs.getString("nombre"));
        d.setEspecialidad(rs.getString("especialidad"));
        return d;
    }
}
