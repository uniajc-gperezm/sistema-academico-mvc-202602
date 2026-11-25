package com.uniajc.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.uniajc.db.DatabaseConexion;
import com.uniajc.modelo.PeriodoAcademico;

public class PeriodoAcademicoDao {

    public void guardar(PeriodoAcademico periodo) {
        String sql = "INSERT INTO \"practica-mvc\".periodos_academicos (codigo, fecha_inicio, fecha_fin, activo) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, periodo.getCodigo());
            pstmt.setDate(2, Date.valueOf(periodo.getFechaInicio()));
            pstmt.setDate(3, Date.valueOf(periodo.getFechaFin()));
            pstmt.setBoolean(4, periodo.isActivo());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    periodo.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al guardar periodo académico: " + e.getMessage(), e);
        }
    }

    public List<PeriodoAcademico> obtenerTodos() {
        List<PeriodoAcademico> periodos = new ArrayList<>();
        String sql = "SELECT id_periodo AS id, codigo, fecha_inicio, fecha_fin, activo FROM \"practica-mvc\".periodos_academicos ORDER BY id_periodo ASC";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                periodos.add(mapearPeriodo(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al obtener periodos académicos: " + e.getMessage(), e);
        }
        return periodos;
    }

    public Optional<PeriodoAcademico> buscarPorId(int id) {
        String sql = "SELECT id_periodo AS id, codigo, fecha_inicio, fecha_fin, activo FROM \"practica-mvc\".periodos_academicos WHERE id_periodo = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearPeriodo(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al buscar periodo académico por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<PeriodoAcademico> obtenerPeriodoActivo() {
        String sql = "SELECT id_periodo AS id, codigo, fecha_inicio, fecha_fin, activo FROM \"practica-mvc\".periodos_academicos WHERE activo = true LIMIT 1";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return Optional.of(mapearPeriodo(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al buscar periodo activo: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<PeriodoAcademico> buscarPorCodigo(String codigo) {
        String sql = "SELECT id_periodo AS id, codigo, fecha_inicio, fecha_fin, activo FROM \"practica-mvc\".periodos_academicos WHERE LOWER(codigo) = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigo != null ? codigo.trim().toLowerCase() : "");
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearPeriodo(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al buscar periodo académico por código: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public void actualizar(PeriodoAcademico periodo) {
        String sql = "UPDATE \"practica-mvc\".periodos_academicos SET codigo = ?, fecha_inicio = ?, fecha_fin = ?, activo = ? WHERE id_periodo = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, periodo.getCodigo());
            pstmt.setDate(2, Date.valueOf(periodo.getFechaInicio()));
            pstmt.setDate(3, Date.valueOf(periodo.getFechaFin()));
            pstmt.setBoolean(4, periodo.isActivo());
            pstmt.setInt(5, periodo.getId());

            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new RuntimeException("Periodo académico no encontrado con ID: " + periodo.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al actualizar periodo académico: " + e.getMessage(), e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM \"practica-mvc\".periodos_academicos WHERE id_periodo = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new RuntimeException("Periodo académico no encontrado con ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al eliminar periodo académico: " + e.getMessage(), e);
        }
    }

    private PeriodoAcademico mapearPeriodo(ResultSet rs) throws SQLException {
        PeriodoAcademico periodo = new PeriodoAcademico();
        periodo.setId(rs.getInt("id"));
        periodo.setCodigo(rs.getString("codigo"));
        Date fechaInicio = rs.getDate("fecha_inicio");
        Date fechaFin = rs.getDate("fecha_fin");
        periodo.setFechaInicio(fechaInicio != null ? fechaInicio.toLocalDate() : LocalDate.now());
        periodo.setFechaFin(fechaFin != null ? fechaFin.toLocalDate() : LocalDate.now());
        periodo.setActivo(rs.getBoolean("activo"));
        return periodo;
    }
}
