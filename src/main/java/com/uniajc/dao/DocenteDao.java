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

public class DocenteDao {

    public void guardar(Docente docente) {
        String sql = "INSERT INTO \"practica-mvc\".docentes (nombre, especialidad, documento, email, telefono, titulo_academico, tipo_contrato) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, docente.getNombre());
            pstmt.setString(2, docente.getEspecialidad());
            pstmt.setString(3, docente.getDocumento());
            pstmt.setString(4, docente.getEmail());
            pstmt.setString(5, docente.getTelefono());
            pstmt.setString(6, docente.getTituloAcademico());
            pstmt.setString(7, docente.getTipoContrato());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    docente.setIdDocente(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al guardar docente: " + e.getMessage(), e);
        }
    }

    public List<Docente> obtenerTodos() {
        List<Docente> lista = new ArrayList<>();
        String sql = "SELECT id_docente, nombre, especialidad, documento, email, telefono, titulo_academico, tipo_contrato FROM \"practica-mvc\".docentes ORDER BY id_docente ASC";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearDocente(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al obtener docentes: " + e.getMessage(), e);
        }
        return lista;
    }

    public Optional<Docente> buscarPorId(int id) {
        String sql = "SELECT id_docente, nombre, especialidad, documento, email, telefono, titulo_academico, tipo_contrato FROM \"practica-mvc\".docentes WHERE id_docente = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearDocente(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al buscar docente por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public List<Docente> buscarPorNombre(String nombre) {
        List<Docente> lista = new ArrayList<>();
        String sql = "SELECT id_docente, nombre, especialidad, documento, email, telefono, titulo_academico, tipo_contrato FROM \"practica-mvc\".docentes " +
                     "WHERE LOWER(nombre) LIKE ? OR LOWER(especialidad) LIKE ? OR LOWER(documento) LIKE ? ORDER BY id_docente ASC";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String termino = "%" + (nombre != null ? nombre.trim().toLowerCase() : "") + "%";
            pstmt.setString(1, termino);
            pstmt.setString(2, termino);
            pstmt.setString(3, termino);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearDocente(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al buscar docente: " + e.getMessage(), e);
        }
        return lista;
    }

    public void actualizar(Docente docente) {
        String sql = "UPDATE \"practica-mvc\".docentes SET nombre = ?, especialidad = ?, documento = ?, email = ?, telefono = ?, titulo_academico = ?, tipo_contrato = ? WHERE id_docente = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, docente.getNombre());
            pstmt.setString(2, docente.getEspecialidad());
            pstmt.setString(3, docente.getDocumento());
            pstmt.setString(4, docente.getEmail());
            pstmt.setString(5, docente.getTelefono());
            pstmt.setString(6, docente.getTituloAcademico());
            pstmt.setString(7, docente.getTipoContrato());
            pstmt.setInt(8, docente.getIdDocente());

            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new RuntimeException("Docente no encontrado con ID: " + docente.getIdDocente());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al actualizar docente: " + e.getMessage(), e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM \"practica-mvc\".docentes WHERE id_docente = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new RuntimeException("Docente no encontrado con ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al eliminar docente: " + e.getMessage(), e);
        }
    }

    private Docente mapearDocente(ResultSet rs) throws SQLException {
        Docente docente = new Docente();
        docente.setIdDocente(rs.getInt("id_docente"));
        docente.setNombre(rs.getString("nombre"));
        docente.setEspecialidad(rs.getString("especialidad"));
        docente.setDocumento(rs.getString("documento"));
        docente.setEmail(rs.getString("email"));
        docente.setTelefono(rs.getString("telefono"));
        docente.setTituloAcademico(rs.getString("titulo_academico"));
        docente.setTipoContrato(rs.getString("tipo_contrato"));
        return docente;
    }
}