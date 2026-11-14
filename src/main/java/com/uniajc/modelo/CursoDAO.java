package com.uniajc.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.uniajc.db.DatabaseConexion;

public class CursoDAO {

    public boolean insertar(Curso curso) {
        String sql = "INSERT INTO public.curso (facultad) VALUES (?)";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, curso.getFacultad());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar curso: " + e.getMessage(), e);
        }
    }

    public List<Curso> listarTodos() {
        List<Curso> lista = new ArrayList<>();
        String sql = "SELECT id, facultad FROM public.curso ORDER BY id";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Curso(rs.getLong("id"), rs.getString("facultad")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar cursos: " + e.getMessage(), e);
        }
        return lista;
    }

    // Actualiza la facultad asociada al curso indicado.
    public boolean actualizar(Curso curso) {
        String sql = "UPDATE public.curso SET facultad = ? WHERE id = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, curso.getFacultad());
            ps.setLong(2, curso.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar curso: " + e.getMessage(), e);
        }
    }

    public boolean eliminar(long id) {
        String sql = "DELETE FROM public.curso WHERE id = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar curso: " + e.getMessage(), e);
        }
    }
}
