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
import com.uniajc.modelo.InscripcionCurso;

public class InscripcionCursoDao {

    public void guardar(InscripcionCurso inscripcion) {
        String sql = "INSERT INTO \"practica-mvc\".inscripciones (id_estudiante, id_grupo, nota_final, estado) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, inscripcion.getIdEstudiante());
            pstmt.setInt(2, inscripcion.getIdGrupo());
            if (inscripcion.getFinalGrade() != null) {
                pstmt.setBigDecimal(3, inscripcion.getFinalGrade());
            } else {
                pstmt.setNull(3, java.sql.Types.DECIMAL);
            }
            pstmt.setString(4, inscripcion.getStatus() != null ? inscripcion.getStatus() : "ACTIVO");

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    inscripcion.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al guardar inscripción: " + e.getMessage(), e);
        }
    }

    public List<InscripcionCurso> obtenerTodos() {
        List<InscripcionCurso> lista = new ArrayList<>();
        String sql = "SELECT id_inscripcion AS id, id_estudiante, id_grupo, nota_final, estado FROM \"practica-mvc\".inscripciones ORDER BY id_inscripcion ASC";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearInscripcion(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al obtener inscripciones: " + e.getMessage(), e);
        }
        return lista;
    }

    public Optional<InscripcionCurso> buscarPorId(int id) {
        String sql = "SELECT id_inscripcion AS id, id_estudiante, id_grupo, nota_final, estado FROM \"practica-mvc\".inscripciones WHERE id_inscripcion = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearInscripcion(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al buscar inscripción por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<InscripcionCurso> buscarPorEstudianteYGrupo(int idEstudiante, int idGrupo) {
        String sql = "SELECT id_inscripcion AS id, id_estudiante, id_grupo, nota_final, estado FROM \"practica-mvc\".inscripciones " +
                     "WHERE id_estudiante = ? AND id_grupo = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEstudiante);
            pstmt.setInt(2, idGrupo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearInscripcion(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al buscar inscripción: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public List<InscripcionCurso> buscarPorEstudiante(int idEstudiante) {
        List<InscripcionCurso> lista = new ArrayList<>();
        String sql = "SELECT id_inscripcion AS id, id_estudiante, id_grupo, nota_final, estado FROM \"practica-mvc\".inscripciones " +
                     "WHERE id_estudiante = ? ORDER BY id_inscripcion ASC";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEstudiante);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearInscripcion(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al buscar inscripciones por estudiante: " + e.getMessage(), e);
        }
        return lista;
    }

    public void actualizar(InscripcionCurso inscripcion) {
        String sql = "UPDATE \"practica-mvc\".inscripciones SET id_estudiante = ?, id_grupo = ?, nota_final = ?, estado = ? WHERE id_inscripcion = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, inscripcion.getIdEstudiante());
            pstmt.setInt(2, inscripcion.getIdGrupo());
            if (inscripcion.getFinalGrade() != null) {
                pstmt.setBigDecimal(3, inscripcion.getFinalGrade());
            } else {
                pstmt.setNull(3, java.sql.Types.DECIMAL);
            }
            pstmt.setString(4, inscripcion.getStatus());
            pstmt.setInt(5, inscripcion.getId());

            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new RuntimeException("Inscripción no encontrada con ID: " + inscripcion.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al actualizar inscripción: " + e.getMessage(), e);
        }
    }

    public void actualizarNotaFinalYEstado(int idInscripcion, BigDecimal notaFinal, String estado) {
        String sql = "UPDATE \"practica-mvc\".inscripciones SET nota_final = ?, estado = ? WHERE id_inscripcion = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (notaFinal != null) {
                pstmt.setBigDecimal(1, notaFinal);
            } else {
                pstmt.setNull(1, java.sql.Types.DECIMAL);
            }
            pstmt.setString(2, estado);
            pstmt.setInt(3, idInscripcion);

            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new RuntimeException("Inscripción no encontrada con ID: " + idInscripcion);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al actualizar nota final de inscripción: " + e.getMessage(), e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM \"practica-mvc\".inscripciones WHERE id_inscripcion = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new RuntimeException("Inscripción no encontrada con ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al eliminar inscripción: " + e.getMessage(), e);
        }
    }

    private InscripcionCurso mapearInscripcion(ResultSet rs) throws SQLException {
        InscripcionCurso inscripcion = new InscripcionCurso();
        inscripcion.setId(rs.getInt("id"));
        inscripcion.setIdEstudiante(rs.getInt("id_estudiante"));
        inscripcion.setIdGrupo(rs.getInt("id_grupo"));
        inscripcion.setFinalGrade(rs.getBigDecimal("nota_final"));
        inscripcion.setStatus(rs.getString("estado"));
        return inscripcion;
    }
}
