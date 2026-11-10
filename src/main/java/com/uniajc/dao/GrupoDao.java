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

public class GrupoDao {

    public void guardar(Grupo grupo) {
        String sql = "INSERT INTO \"practica-mvc\".grupos (id_materia, id_docente, classroom, schedule) VALUES (?, ?, ?, ?)";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, grupo.getIdMateria());
                pstmt.setInt(2, grupo.getIdDocente());
                pstmt.setString(3, grupo.getAula());
                pstmt.setString(4, grupo.getHorario());

                pstmt.executeUpdate();

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        grupo.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar grupo: " + e.getMessage(), e);
        }
    }

    public List<Grupo> obtenerTodos() {
        List<Grupo> lista = new ArrayList<>();
        String sql = "SELECT g.id, g.id_materia, g.id_docente, g.classroom, g.schedule, " +
                "m.name AS materia_nombre, d.name AS docente_nombre " +
                "FROM \"practica-mvc\".grupos g " +
                "LEFT JOIN \"practica-mvc\".materias m ON g.id_materia = m.id " +
                "LEFT JOIN \"practica-mvc\".docentes d ON g.id_docente = d.id " +
                "ORDER BY g.id ASC";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    lista.add(mapearEntidadConNombres(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener grupos: " + e.getMessage(), e);
        }
        return lista;
    }

    public Optional<Grupo> buscarPorId(int id) {
        String sql = "SELECT g.id, g.id_materia, g.id_docente, g.classroom, g.schedule, " +
                "m.name AS materia_nombre, d.name AS docente_nombre " +
                "FROM \"practica-mvc\".grupos g " +
                "LEFT JOIN \"practica-mvc\".materias m ON g.id_materia = m.id " +
                "LEFT JOIN \"practica-mvc\".docentes d ON g.id_docente = d.id " +
                "WHERE g.id = ?";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapearEntidadConNombres(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar grupo por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public List<Grupo> buscarPorMateria(int idMateria) {
        List<Grupo> lista = new ArrayList<>();
        String sql = "SELECT g.id, g.id_materia, g.id_docente, g.classroom, g.schedule, " +
                "m.name AS materia_nombre, d.name AS docente_nombre " +
                "FROM \"practica-mvc\".grupos g " +
                "LEFT JOIN \"practica-mvc\".materias m ON g.id_materia = m.id " +
                "LEFT JOIN \"practica-mvc\".docentes d ON g.id_docente = d.id " +
                "WHERE g.id_materia = ? ORDER BY g.id ASC";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, idMateria);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        lista.add(mapearEntidadConNombres(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar grupos por materia: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<Grupo> buscarPorDocente(int idDocente) {
        List<Grupo> lista = new ArrayList<>();
        String sql = "SELECT g.id, g.id_materia, g.id_docente, g.classroom, g.schedule, " +
                "m.name AS materia_nombre, d.name AS docente_nombre " +
                "FROM \"practica-mvc\".grupos g " +
                "LEFT JOIN \"practica-mvc\".materias m ON g.id_materia = m.id " +
                "LEFT JOIN \"practica-mvc\".docentes d ON g.id_docente = d.id " +
                "WHERE g.id_docente = ? ORDER BY g.id ASC";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, idDocente);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        lista.add(mapearEntidadConNombres(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar grupos por docente: " + e.getMessage(), e);
        }
        return lista;
    }

    public void actualizar(Grupo grupo) {
        String sql = "UPDATE \"practica-mvc\".grupos SET id_materia = ?, id_docente = ?, classroom = ?, schedule = ? WHERE id = ?";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, grupo.getIdMateria());
                pstmt.setInt(2, grupo.getIdDocente());
                pstmt.setString(3, grupo.getAula());
                pstmt.setString(4, grupo.getHorario());
                pstmt.setInt(5, grupo.getId());

                int filas = pstmt.executeUpdate();
                if (filas == 0) {
                    throw new RuntimeException("Grupo no encontrado con ID: " + grupo.getId());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar grupo: " + e.getMessage(), e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM \"practica-mvc\".grupos WHERE id = ?";

        try {
            Connection conn = DatabaseConexion.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);

                int filas = pstmt.executeUpdate();
                if (filas == 0) {
                    throw new RuntimeException("Grupo no encontrado con ID: " + id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar grupo: " + e.getMessage(), e);
        }
    }

    private Grupo mapearEntidadConNombres(ResultSet rs) throws SQLException {
        Grupo grupo = new Grupo();
        grupo.setId(rs.getInt("id"));
        grupo.setIdMateria(rs.getInt("id_materia"));
        grupo.setIdDocente(rs.getInt("id_docente"));
        grupo.setAula(rs.getString("classroom"));
        grupo.setHorario(rs.getString("schedule"));

        try {
            grupo.setMateriaNombre(rs.getString("materia_nombre"));
            grupo.setDocenteNombre(rs.getString("docente_nombre"));
        } catch (SQLException ignored) {}

        return grupo;
    }
}
