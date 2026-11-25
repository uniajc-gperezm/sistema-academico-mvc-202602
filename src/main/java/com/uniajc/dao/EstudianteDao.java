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
import com.uniajc.modelo.Estudiante;

public class EstudianteDao {

    public void guardar(Estudiante estudiante) {

        String sql = """
            INSERT INTO "practica-mvc".estudiantes
            (name, lastname, email, tipo_documento,
             documento, telefono, semestre_actual, estado)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, estudiante.getName());
            pstmt.setString(2, estudiante.getLastname());
            pstmt.setString(3, estudiante.getEmail());
            pstmt.setString(4, estudiante.getTipoDocumento());
            pstmt.setString(5, estudiante.getDocumento());
            pstmt.setString(6, estudiante.getTelefono());
            pstmt.setInt(7, estudiante.getSemestreActual());
            pstmt.setString(8, estudiante.getEstado());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    estudiante.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error en base de datos al guardar estudiante: "
                            + e.getMessage(),
                    e);
        }
    }

    public List<Estudiante> obtenerTodos() {

        List<Estudiante> lista = new ArrayList<>();

        String sql = """
            SELECT id, name, lastname, email,
                   tipo_documento, documento,
                   telefono, semestre_actual, estado
            FROM "practica-mvc".estudiantes
            ORDER BY id ASC
            """;

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearEstudiante(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error en base de datos al obtener estudiantes: "
                            + e.getMessage(),
                    e);
        }

        return lista;
    }

    public Optional<Estudiante> buscarPorId(int id) {

        String sql = """
            SELECT id, name, lastname, email,
                   tipo_documento, documento,
                   telefono, semestre_actual, estado
            FROM "practica-mvc".estudiantes
            WHERE id = ?
            """;

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(mapearEstudiante(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error en base de datos al buscar por ID: "
                            + e.getMessage(),
                    e);
        }

        return Optional.empty();
    }

    public List<Estudiante> buscarPorNombre(String nombre) {

        List<Estudiante> lista = new ArrayList<>();

        String sql = """
            SELECT id, name, lastname, email,
                   tipo_documento, documento,
                   telefono, semestre_actual, estado
            FROM "practica-mvc".estudiantes
            WHERE LOWER(name) LIKE ?
               OR LOWER(lastname) LIKE ?
            ORDER BY id ASC
            """;

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String termino =
                    "%" +
                    (nombre != null
                            ? nombre.trim().toLowerCase()
                            : "")
                    + "%";

            pstmt.setString(1, termino);
            pstmt.setString(2, termino);

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    lista.add(mapearEstudiante(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error en base de datos al buscar por nombre: "
                            + e.getMessage(),
                    e);
        }

        return lista;
    }

    public Optional<Estudiante> buscarPorEmail(String email) {

        String sql = """
            SELECT id, name, lastname, email,
                   tipo_documento, documento,
                   telefono, semestre_actual, estado
            FROM "practica-mvc".estudiantes
            WHERE LOWER(email) = LOWER(?)
            """;

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(
                    1,
                    email != null ? email.trim() : "");

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(mapearEstudiante(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error en base de datos al buscar por email: "
                            + e.getMessage(),
                    e);
        }

        return Optional.empty();
    }

    public Optional<Estudiante> buscarPorDocumento(String documento) {

        String sql = """
            SELECT id, name, lastname, email,
                   tipo_documento, documento,
                   telefono, semestre_actual, estado
            FROM "practica-mvc".estudiantes
            WHERE documento = ?
            """;

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, documento);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(mapearEstudiante(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al buscar por documento: "
                            + e.getMessage(),
                    e);
        }

        return Optional.empty();
    }

    public void actualizar(Estudiante estudiante) {

        String sql = """
            UPDATE "practica-mvc".estudiantes
            SET name = ?,
                lastname = ?,
                email = ?,
                tipo_documento = ?,
                documento = ?,
                telefono = ?,
                semestre_actual = ?,
                estado = ?
            WHERE id = ?
            """;

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, estudiante.getName());
            pstmt.setString(2, estudiante.getLastname());
            pstmt.setString(3, estudiante.getEmail());
            pstmt.setString(4, estudiante.getTipoDocumento());
            pstmt.setString(5, estudiante.getDocumento());
            pstmt.setString(6, estudiante.getTelefono());
            pstmt.setInt(7, estudiante.getSemestreActual());
            pstmt.setString(8, estudiante.getEstado());
            pstmt.setInt(9, estudiante.getId());

            int filas = pstmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException(
                        "Estudiante no encontrado con ID: "
                                + estudiante.getId());
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error en base de datos al actualizar estudiante: "
                            + e.getMessage(),
                    e);
        }
    }

    public void eliminar(int id) {

        String sql =
                "DELETE FROM \"practica-mvc\".estudiantes WHERE id = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int filas = pstmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException(
                        "Estudiante no encontrado con ID: " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error en base de datos al eliminar estudiante: "
                            + e.getMessage(),
                    e);
        }
    }

    private Estudiante mapearEstudiante(ResultSet rs)
            throws SQLException {

        Estudiante estudiante = new Estudiante();

        estudiante.setId(rs.getInt("id"));
        estudiante.setName(rs.getString("name"));
        estudiante.setLastname(rs.getString("lastname"));
        estudiante.setEmail(rs.getString("email"));

        estudiante.setTipoDocumento(
                rs.getString("tipo_documento"));

        estudiante.setDocumento(
                rs.getString("documento"));

        estudiante.setTelefono(
                rs.getString("telefono"));

        estudiante.setSemestreActual(
                rs.getInt("semestre_actual"));

        estudiante.setEstado(
                rs.getString("estado"));

        return estudiante;
    }
}