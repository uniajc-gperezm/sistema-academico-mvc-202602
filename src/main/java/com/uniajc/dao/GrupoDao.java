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
import com.uniajc.modelo.Grupo;

/** DAO de grupos. Tabla real: grupos (id_grupo, id_materia, id_docente, aula, horario). */
public class GrupoDao implements GrupoRepositorio {

    private static final String TABLA = "grupos";

    @Override
    public void guardar(Grupo g) {
        String sql = "INSERT INTO " + TABLA + " (id_materia, id_docente, aula, horario) VALUES (?, ?, ?, ?)";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, g.getId_materia());
            ps.setInt(2, g.getId_docente());
            ps.setString(3, g.getAula());
            ps.setString(4, g.getHorario());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) g.setId_grupo(rs.getInt(1));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error guardando grupo: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Grupo> obtenerTodos() {
        List<Grupo> lista = new ArrayList<>();
        String sql = "SELECT id_grupo, id_materia, id_docente, aula, horario FROM " + TABLA + " ORDER BY id_grupo";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException ex) {
            throw new RuntimeException("Error listando grupos: " + ex.getMessage(), ex);
        }
        return lista;
    }

    @Override
    public Optional<Grupo> buscarPorId(int id) {
        String sql = "SELECT id_grupo, id_materia, id_docente, aula, horario FROM " + TABLA + " WHERE id_grupo = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error buscando grupo: " + ex.getMessage(), ex);
        }
        return Optional.empty();
    }

    @Override
    public void actualizar(Grupo g) {
        String sql = "UPDATE " + TABLA + " SET id_materia = ?, id_docente = ?, aula = ?, horario = ? WHERE id_grupo = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, g.getId_materia());
            ps.setInt(2, g.getId_docente());
            ps.setString(3, g.getAula());
            ps.setString(4, g.getHorario());
            ps.setInt(5, g.getId_grupo());
            int filas = ps.executeUpdate();
            if (filas == 0) throw new RuntimeException("Grupo no encontrado con ID: " + g.getId_grupo());
        } catch (SQLException ex) {
            throw new RuntimeException("Error actualizando grupo: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM " + TABLA + " WHERE id_grupo = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas == 0) throw new RuntimeException("Grupo no encontrado con ID: " + id);
        } catch (SQLException ex) {
            throw new RuntimeException("Error eliminando grupo: " + ex.getMessage(), ex);
        }
    }

    private Grupo mapear(ResultSet rs) throws SQLException {
        Grupo g = new Grupo();
        g.setId_grupo(rs.getInt("id_grupo"));
        g.setId_materia(rs.getInt("id_materia"));
        g.setId_docente(rs.getInt("id_docente"));
        g.setAula(rs.getString("aula"));
        g.setHorario(rs.getString("horario"));
        return g;
    }
}
