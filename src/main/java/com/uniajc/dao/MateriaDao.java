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
        String sql = "INSERT INTO \"practica-mvc\".materias (nombre_materia, creditos, codigo, horas_semanales) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, materia.getName());
            pstmt.setInt(2, materia.getCredits());
            pstmt.setString(3, materia.getCodigo());
            pstmt.setInt(4, materia.getHorasSemanales());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    materia.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al guardar materia: " + e.getMessage(), e);
        }
    }

    public List<Materia> obtenerTodos() {
        List<Materia> lista = new ArrayList<>();
        String sql = "SELECT id_materia AS id, nombre_materia, creditos, codigo, horas_semanales FROM \"practica-mvc\".materias ORDER BY id_materia ASC";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearMateria(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al obtener materias: " + e.getMessage(), e);
        }
        return lista;
    }

    public Optional<Materia> buscarPorId(int id) {
        String sql = "SELECT id_materia AS id, nombre_materia, creditos, codigo, horas_semanales FROM \"practica-mvc\".materias WHERE id_materia = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearMateria(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al buscar materia por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public List<Materia> buscarPorNombre(String nombre) {
        List<Materia> lista = new ArrayList<>();
        String sql = "SELECT id_materia AS id, nombre_materia, creditos, codigo, horas_semanales FROM \"practica-mvc\".materias " +
                     "WHERE LOWER(nombre_materia) LIKE ? ORDER BY id_materia ASC";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String termino = "%" + (nombre != null ? nombre.trim().toLowerCase() : "") + "%";
            pstmt.setString(1, termino);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearMateria(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al buscar materia por nombre: " + e.getMessage(), e);
        }
        return lista;
    }

    public Optional<Materia> buscarPorCodigo(String codigo) {
        String sql = "SELECT id_materia AS id, nombre_materia, creditos, codigo, horas_semanales FROM \"practica-mvc\".materias WHERE LOWER(codigo) = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigo != null ? codigo.trim().toLowerCase() : "");

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearMateria(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al buscar materia por código: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public void actualizar(Materia materia) {
        String sql = "UPDATE \"practica-mvc\".materias SET nombre_materia = ?, creditos = ?, codigo = ?, horas_semanales = ? WHERE id_materia = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, materia.getName());
            pstmt.setInt(2, materia.getCredits());
            pstmt.setString(3, materia.getCodigo());
            pstmt.setInt(4, materia.getHorasSemanales());
            pstmt.setInt(5, materia.getId());

            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new RuntimeException("Materia no encontrada con ID: " + materia.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al actualizar materia: " + e.getMessage(), e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM \"practica-mvc\".materias WHERE id_materia = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new RuntimeException("Materia no encontrada con ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al eliminar materia: " + e.getMessage(), e);
        }
    }

    private Materia mapearMateria(ResultSet rs) throws SQLException {
        Materia materia = new Materia();
        materia.setId(rs.getInt("id"));
        materia.setName(rs.getString("nombre_materia"));
        materia.setCredits(rs.getInt("creditos"));
        materia.setCodigo(rs.getString("codigo"));
        materia.setHorasSemanales(rs.getInt("horas_semanales"));
        return materia;
    }
}
