package com.uniajc.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.uniajc.db.DatabaseConexion;

public class MateriaDAO {

    public boolean insertar(Materia mat) {
        String sql = "INSERT INTO public.materias (id_materia, nombre_materia, creditos) VALUES (?, ?, ?)";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mat.getIdMateria());
            ps.setString(2, mat.getNombreMateria());
            ps.setInt(3, mat.getCreditos());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar materia: " + e.getMessage(), e);
        }
    }

    public List<Materia> listarTodos() {
        List<Materia> lista = new ArrayList<>();
        String sql = "SELECT id_materia, nombre_materia, creditos FROM public.materias ORDER BY id_materia";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Materia(
                    rs.getInt("id_materia"),
                    rs.getString("nombre_materia"),
                    rs.getInt("creditos")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar materias: " + e.getMessage(), e);
        }
        return lista;
    }

    // Actualiza los datos descriptivos sin cambiar el identificador de la materia.
    public boolean actualizar(Materia materia) {
        String sql = "UPDATE public.materias SET nombre_materia = ?, creditos = ? WHERE id_materia = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, materia.getNombreMateria());
            ps.setInt(2, materia.getCreditos());
            ps.setInt(3, materia.getIdMateria());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar materia: " + e.getMessage(), e);
        }
    }

    public boolean eliminar(int idMateria) {
        String sql = "DELETE FROM public.materias WHERE id_materia = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMateria);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar materia: " + e.getMessage(), e);
        }
    }
}

