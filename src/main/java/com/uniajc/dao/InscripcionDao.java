package com.uniajc.dao;

import java.sql.CallableStatement;
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
public class InscripcionDao implements InscripcionRepositorio {

    private static final String TABLA = "inscripciones_curso";

    /**
     * Procedimiento almacenado y funciones definidos en database/procedimientos.sql.
     *
     * El procedimiento se invoca con "CALL" explicito y no con el escape JDBC
     * "{call ...}". El driver de PostgreSQL traduce ese escape a
     * "SELECT * FROM sp_asignar_nota(...)" (modo escapeSyntaxCallMode=select por
     * defecto) y el motor responde: "is a procedure. Hint: To call a procedure,
     * use CALL". Verificado contra la base real.
     */
    private static final String SP_ASIGNAR_NOTA = "CALL public.sp_asignar_nota(?, ?)";
    private static final String FN_PROMEDIO_GRUPO = "SELECT public.fn_promedio_grupo(?)";
    private static final String FN_ESTADO_POR_NOTA = "SELECT public.fn_estado_por_nota(?)";

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    // ---- Rutinas almacenadas (database/procedimientos.sql) ----

    /**
     * Llama al procedimiento almacenado sp_asignar_nota.
     * Se usa CallableStatement con la sintaxis JDBC {call ...}; el
     * procedimiento no devuelve valor, su efecto es el UPDATE en la tabla.
     */
    @Override
    public void asignarNota(int idInscripcion, float nota) {
        Connection conn = DatabaseConexion.getConnection();
        try (CallableStatement cs = conn.prepareCall(SP_ASIGNAR_NOTA)) {
            cs.setInt(1, idInscripcion);
            cs.setFloat(2, nota);
            cs.execute();
        } catch (SQLException ex) {
            throw new RuntimeException("Error asignando nota: " + ex.getMessage(), ex);
        }
    }

    /** Llama a la funcion fn_promedio_grupo con SELECT (las funciones se leen, no se invocan con CALL). */
    @Override
    public float promedioGrupo(int idGrupo) {
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(FN_PROMEDIO_GRUPO)) {
            ps.setInt(1, idGrupo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getFloat(1) : 0f;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error calculando promedio: " + ex.getMessage(), ex);
        }
    }

    /** Llama a la funcion fn_estado_por_nota con SELECT. */
    @Override
    public String estadoPorNota(float nota) {
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(FN_ESTADO_POR_NOTA)) {
            ps.setFloat(1, nota);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error consultando estado: " + ex.getMessage(), ex);
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
