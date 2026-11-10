package com.uniajc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.uniajc.db.DatabaseConexion;
import com.uniajc.modelo.Inscripcion;

public class InscripcionDao {

    public void guardar(Inscripcion inscripcion) {
        String sql = "INSERT INTO \"practica-mvc\".inscripciones (id_estudiante, id_grupo, final_grade, status) VALUES (?, ?, ?, ?)";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, inscripcion.getIdEstudiante());
                pstmt.setInt(2, inscripcion.getIdGrupo());

                if (inscripcion.getNotaFinal() != null) {
                    pstmt.setDouble(3, inscripcion.getNotaFinal());
                } else {
                    pstmt.setNull(3, Types.DECIMAL);
                }

                String estado = (inscripcion.getEstado() != null) ? inscripcion.getEstado() : "ACTIVO";
                pstmt.setString(4, estado);

                pstmt.executeUpdate();

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        inscripcion.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar inscripcion: " + e.getMessage(), e);
        }
    }

    public List<Inscripcion> obtenerTodos() {
        List<Inscripcion> lista = new ArrayList<>();
        String sql = "SELECT i.id, i.id_estudiante, i.id_grupo, i.final_grade, i.status, " +
                "CONCAT(e.name, ' ', e.lastname) AS estudiante_nombre, " +
                "CONCAT(m.name, ' - Aula: ', g.classroom, ' (', g.schedule, ')') AS grupo_detalle " +
                "FROM \"practica-mvc\".inscripciones i " +
                "LEFT JOIN \"practica-mvc\".estudiantes e ON i.id_estudiante = e.id " +
                "LEFT JOIN \"practica-mvc\".grupos g ON i.id_grupo = g.id " +
                "LEFT JOIN \"practica-mvc\".materias m ON g.id_materia = m.id " +
                "ORDER BY i.id ASC";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    lista.add(mapearEntidad(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener inscripciones: " + e.getMessage(), e);
        }
        return lista;
    }

    public Optional<Inscripcion> buscarPorId(int id) {
        String sql = "SELECT i.id, i.id_estudiante, i.id_grupo, i.final_grade, i.status, " +
                "CONCAT(e.name, ' ', e.lastname) AS estudiante_nombre, " +
                "CONCAT(m.name, ' - Aula: ', g.classroom, ' (', g.schedule, ')') AS grupo_detalle " +
                "FROM \"practica-mvc\".inscripciones i " +
                "LEFT JOIN \"practica-mvc\".estudiantes e ON i.id_estudiante = e.id " +
                "LEFT JOIN \"practica-mvc\".grupos g ON i.id_grupo = g.id " +
                "LEFT JOIN \"practica-mvc\".materias m ON g.id_materia = m.id " +
                "WHERE i.id = ?";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapearEntidad(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar inscripcion por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<Inscripcion> buscarPorEstudianteYGrupo(int idEstudiante, int idGrupo) {
        String sql = "SELECT i.id, i.id_estudiante, i.id_grupo, i.final_grade, i.status, " +
                "CONCAT(e.name, ' ', e.lastname) AS estudiante_nombre, " +
                "CONCAT(m.name, ' - Aula: ', g.classroom, ' (', g.schedule, ')') AS grupo_detalle " +
                "FROM \"practica-mvc\".inscripciones i " +
                "LEFT JOIN \"practica-mvc\".estudiantes e ON i.id_estudiante = e.id " +
                "LEFT JOIN \"practica-mvc\".grupos g ON i.id_grupo = g.id " +
                "LEFT JOIN \"practica-mvc\".materias m ON g.id_materia = m.id " +
                "WHERE i.id_estudiante = ? AND i.id_grupo = ?";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, idEstudiante);
                pstmt.setInt(2, idGrupo);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapearEntidad(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar inscripcion por estudiante y grupo: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public List<Inscripcion> buscarPorEstudiante(int idEstudiante) {
        List<Inscripcion> lista = new ArrayList<>();
        String sql = "SELECT i.id, i.id_estudiante, i.id_grupo, i.final_grade, i.status, " +
                "CONCAT(e.name, ' ', e.lastname) AS estudiante_nombre, " +
                "CONCAT(m.name, ' - Aula: ', g.classroom, ' (', g.schedule, ')') AS grupo_detalle " +
                "FROM \"practica-mvc\".inscripciones i " +
                "LEFT JOIN \"practica-mvc\".estudiantes e ON i.id_estudiante = e.id " +
                "LEFT JOIN \"practica-mvc\".grupos g ON i.id_grupo = g.id " +
                "LEFT JOIN \"practica-mvc\".materias m ON g.id_materia = m.id " +
                "WHERE i.id_estudiante = ? ORDER BY i.id ASC";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, idEstudiante);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        lista.add(mapearEntidad(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar inscripciones por estudiante: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<Inscripcion> buscarPorGrupo(int idGrupo) {
        List<Inscripcion> lista = new ArrayList<>();
        String sql = "SELECT i.id, i.id_estudiante, i.id_grupo, i.final_grade, i.status, " +
                "CONCAT(e.name, ' ', e.lastname) AS estudiante_nombre, " +
                "CONCAT(m.name, ' - Aula: ', g.classroom, ' (', g.schedule, ')') AS grupo_detalle " +
                "FROM \"practica-mvc\".inscripciones i " +
                "LEFT JOIN \"practica-mvc\".estudiantes e ON i.id_estudiante = e.id " +
                "LEFT JOIN \"practica-mvc\".grupos g ON i.id_grupo = g.id " +
                "LEFT JOIN \"practica-mvc\".materias m ON g.id_materia = m.id " +
                "WHERE i.id_grupo = ? ORDER BY i.id ASC";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, idGrupo);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        lista.add(mapearEntidad(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar inscripciones por grupo: " + e.getMessage(), e);
        }
        return lista;
    }

    public void actualizar(Inscripcion inscripcion) {
        String sql = "UPDATE \"practica-mvc\".inscripciones SET id_estudiante = ?, id_grupo = ?, final_grade = ?, status = ? WHERE id = ?";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, inscripcion.getIdEstudiante());
                pstmt.setInt(2, inscripcion.getIdGrupo());

                if (inscripcion.getNotaFinal() != null) {
                    pstmt.setDouble(3, inscripcion.getNotaFinal());
                } else {
                    pstmt.setNull(3, Types.DECIMAL);
                }

                pstmt.setString(4, inscripcion.getEstado());
                pstmt.setInt(5, inscripcion.getId());

                int filas = pstmt.executeUpdate();
                if (filas == 0) {
                    throw new RuntimeException("Inscripcion no encontrada con ID: " + inscripcion.getId());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar inscripcion: " + e.getMessage(), e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM \"practica-mvc\".inscripciones WHERE id = ?";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);

                int filas = pstmt.executeUpdate();
                if (filas == 0) {
                    throw new RuntimeException("Inscripcion no encontrada con ID: " + id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar inscripcion: " + e.getMessage(), e);
        }
    }

    private Inscripcion mapearEntidad(ResultSet rs) throws SQLException {
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setId(rs.getInt("id"));
        inscripcion.setIdEstudiante(rs.getInt("id_estudiante"));
        inscripcion.setIdGrupo(rs.getInt("id_grupo"));

        double nota = rs.getDouble("final_grade");
        if (rs.wasNull()) {
            inscripcion.setNotaFinal(null);
        } else {
            inscripcion.setNotaFinal(nota);
        }

        inscripcion.setEstado(rs.getString("status"));

        try {
            inscripcion.setEstudianteNombre(rs.getString("estudiante_nombre"));
            inscripcion.setGrupoDetalle(rs.getString("grupo_detalle"));
        } catch (SQLException ignored) {}

        return inscripcion;
    }
}
