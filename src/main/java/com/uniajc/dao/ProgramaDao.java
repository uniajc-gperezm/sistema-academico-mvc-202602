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
import com.uniajc.modelo.Programa;

public class ProgramaDao {

    public void guardar(Programa programa) {
        String sql = "INSERT INTO programas (codigo, nombre, facultad, duracion_semestres) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, programa.getCodigo());
            pstmt.setString(2, programa.getNombre());
            pstmt.setString(3, programa.getFacultad());
            pstmt.setInt(4, programa.getDuracionSemestres());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    programa.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al guardar programa: " + e.getMessage(), e);
        }
    }

    public List<Programa> obtenerTodos() {
        List<Programa> lista = new ArrayList<>();
        String sql = "SELECT id, codigo, nombre, facultad, duracion_semestres FROM programas ORDER BY id ASC";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearPrograma(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al obtener programas: " + e.getMessage(), e);
        }
        return lista;
    }

    public Optional<Programa> buscarPorId(int id) {
        String sql = "SELECT id, codigo, nombre, facultad, duracion_semestres FROM programas WHERE id = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearPrograma(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al buscar programa por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<Programa> buscarPorCodigo(String codigo) {
        String sql = "SELECT id, codigo, nombre, facultad, duracion_semestres FROM programas WHERE LOWER(codigo) = LOWER(?)";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigo != null ? codigo.trim() : "");

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearPrograma(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al buscar programa por código: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public void actualizar(Programa programa) {
        String sql = "UPDATE programas SET codigo = ?, nombre = ?, facultad = ?, duracion_semestres = ? WHERE id = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, programa.getCodigo());
            pstmt.setString(2, programa.getNombre());
            pstmt.setString(3, programa.getFacultad());
            pstmt.setInt(4, programa.getDuracionSemestres());
            pstmt.setInt(5, programa.getId());

            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new RuntimeException("Programa no encontrado con ID: " + programa.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al actualizar programa: " + e.getMessage(), e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM programas WHERE id = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new RuntimeException("Programa no encontrado con ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al eliminar programa: " + e.getMessage(), e);
        }
    }

    private Programa mapearPrograma(ResultSet rs) throws SQLException {
        Programa programa = new Programa();
        programa.setId(rs.getInt("id"));
        programa.setCodigo(rs.getString("codigo"));
        programa.setNombre(rs.getString("nombre"));
        programa.setFacultad(rs.getString("facultad"));
        programa.setDuracionSemestres(rs.getInt("duracion_semestres"));
        return programa;
    }
}