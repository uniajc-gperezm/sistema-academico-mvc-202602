package com.uniajc.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.uniajc.db.DatabaseConexion;
import com.uniajc.modelo.Calificacion;

public class CalificacionDao {

    public void guardar(Calificacion calificacion) {
        String sql = "INSERT INTO \"practica-mvc\".calificaciones (inscripcion_id, corte, porcentaje, nota, observacion) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, calificacion.getInscripcionId());
            pstmt.setString(2, calificacion.getCorte());
            pstmt.setBigDecimal(3, calificacion.getPorcentaje());
            pstmt.setBigDecimal(4, calificacion.getNota());
            pstmt.setString(5, calificacion.getObservacion());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    calificacion.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al guardar calificación: " + e.getMessage(), e);
        }
    }

    public List<Calificacion> obtenerTodos() {
        List<Calificacion> lista = new ArrayList<>();
        String sql = "SELECT id, inscripcion_id, corte, porcentaje, nota, observacion " +
                     "FROM \"practica-mvc\".calificaciones ORDER BY inscripcion_id ASC, id ASC";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearCalificacion(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al obtener calificaciones: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<Calificacion> obtenerPorInscripcion(int inscripcionId) {
        List<Calificacion> lista = new ArrayList<>();
        String sql = "SELECT id, inscripcion_id, corte, porcentaje, nota, observacion " +
                     "FROM \"practica-mvc\".calificaciones WHERE inscripcion_id = ? ORDER BY id ASC";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, inscripcionId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearCalificacion(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al buscar calificaciones por inscripción: " + e.getMessage(), e);
        }
        return lista;
    }

    public Optional<Calificacion> buscarPorId(int id) {
        String sql = "SELECT id, inscripcion_id, corte, porcentaje, nota, observacion " +
                     "FROM \"practica-mvc\".calificaciones WHERE id = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearCalificacion(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al buscar calificación por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public void actualizar(Calificacion calificacion) {
        String sql = "UPDATE \"practica-mvc\".calificaciones " +
                     "SET inscripcion_id = ?, corte = ?, porcentaje = ?, nota = ?, observacion = ? WHERE id = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, calificacion.getInscripcionId());
            pstmt.setString(2, calificacion.getCorte());
            pstmt.setBigDecimal(3, calificacion.getPorcentaje());
            pstmt.setBigDecimal(4, calificacion.getNota());
            pstmt.setString(5, calificacion.getObservacion());
            pstmt.setInt(6, calificacion.getId());

            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new RuntimeException("Calificación no encontrada con ID: " + calificacion.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al actualizar calificación: " + e.getMessage(), e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM \"practica-mvc\".calificaciones WHERE id = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new RuntimeException("Calificación no encontrada con ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al eliminar calificación: " + e.getMessage(), e);
        }
    }

    public BigDecimal obtenerPorcentajeAcumulado(int inscripcionId, int excludeId) {
        String sql = "SELECT COALESCE(SUM(porcentaje), 0) AS total " +
                     "FROM \"practica-mvc\".calificaciones WHERE inscripcion_id = ? AND id != ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, inscripcionId);
            pstmt.setInt(2, excludeId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al calcular porcentaje acumulado: " + e.getMessage(), e);
        }
        return BigDecimal.ZERO;
    }

    private Calificacion mapearCalificacion(ResultSet rs) throws SQLException {
        Calificacion c = new Calificacion();
        c.setId(rs.getInt("id"));
        c.setInscripcionId(rs.getInt("inscripcion_id"));
        c.setCorte(rs.getString("corte"));
        c.setPorcentaje(rs.getBigDecimal("porcentaje"));
        c.setNota(rs.getBigDecimal("nota"));
        c.setObservacion(rs.getString("observacion"));
        return c;
    }
}

