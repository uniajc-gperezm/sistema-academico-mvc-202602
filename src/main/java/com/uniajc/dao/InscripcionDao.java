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
import com.uniajc.modelo.InscripcionCurso;

/** DAO de inscripciones. Tabla real: inscripciones_curso (id_inscripcion, id_estudiante, id_grupo, nota_final, estado). */
public class InscripcionDao {

    private static final String TABLA = "inscripciones_curso";

    public void guardar(InscripcionCurso i) {
        String sql = "INSERT INTO " + TABLA + " (id_estudiante, id_grupo, nota_final, estado) VALUES (?, ?, ?, ?)";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, i.getId_estudiante());
            ps.setInt(2, i.getId_grupo());
            ps.setFloat(3, i.getNota_final());
            ps.setString(4, i.getEstado());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) i.setId_inscripcion(rs.getInt(1));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error guardando inscripcion: " + ex.getMessage(), ex);
        }
    }

    public List<InscripcionCurso> obtenerTodos() {
        List<InscripcionCurso> lista = new ArrayList<>();
        String sql = "SELECT id_inscripcion, id_estudiante, id_grupo, nota_final, estado FROM "
                + TABLA + " ORDER BY id_inscripcion";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException ex) {
            throw new RuntimeException("Error listando inscripciones: " + ex.getMessage(), ex);
        }
        return lista;
    }

    public Optional<InscripcionCurso> buscarPorId(int id) {
        String sql = "SELECT id_inscripcion, id_estudiante, id_grupo, nota_final, estado FROM "
                + TABLA + " WHERE id_inscripcion = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error buscando inscripcion: " + ex.getMessage(), ex);
        }
        return Optional.empty();
    }

    public boolean existeInscripcion(int idEstudiante, int idGrupo) {
        String sql = "SELECT 1 FROM " + TABLA + " WHERE id_estudiante = ? AND id_grupo = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            ps.setInt(2, idGrupo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error validando inscripcion: " + ex.getMessage(), ex);
        }
    }

    public void actualizar(InscripcionCurso i) {
        String sql = "UPDATE " + TABLA
                + " SET id_estudiante = ?, id_grupo = ?, nota_final = ?, estado = ? WHERE id_inscripcion = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, i.getId_estudiante());
            ps.setInt(2, i.getId_grupo());
            ps.setFloat(3, i.getNota_final());
            ps.setString(4, i.getEstado());
            ps.setInt(5, i.getId_inscripcion());
            int filas = ps.executeUpdate();
            if (filas == 0) throw new RuntimeException("Inscripcion no encontrada con ID: " + i.getId_inscripcion());
        } catch (SQLException ex) {
            throw new RuntimeException("Error actualizando inscripcion: " + ex.getMessage(), ex);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM " + TABLA + " WHERE id_inscripcion = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas == 0) throw new RuntimeException("Inscripcion no encontrada con ID: " + id);
        } catch (SQLException ex) {
            throw new RuntimeException("Error eliminando inscripcion: " + ex.getMessage(), ex);
        }
    }

    private InscripcionCurso mapear(ResultSet rs) throws SQLException {
        InscripcionCurso i = new InscripcionCurso();
        i.setId_inscripcion(rs.getInt("id_inscripcion"));
        i.setId_estudiante(rs.getInt("id_estudiante"));
        i.setId_grupo(rs.getInt("id_grupo"));
        i.setNota_final(rs.getFloat("nota_final"));
        i.setEstado(rs.getString("estado"));
        return i;
    }
}
