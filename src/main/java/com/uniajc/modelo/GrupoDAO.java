package com.uniajc.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.uniajc.db.DatabaseConexion;

public class GrupoDAO {

    public boolean insertar(Grupo grupo) {
        String sql = "INSERT INTO public.grupos (id_materia, id_docente, aula, horario) VALUES (?, ?, ?, ?)";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, grupo.getIdMateria());
            ps.setLong(2, grupo.getIdDocente());
            ps.setString(3, grupo.getAula());
            ps.setString(4, grupo.getHorario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar grupo: " + e.getMessage(), e);
        }
    }

    public List<Grupo> listarTodos() {
        List<Grupo> lista = new ArrayList<>();
        String sql = "SELECT id_grupo, id_materia, id_docente, aula, horario FROM public.grupos ORDER BY id_grupo";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Grupo(
                    rs.getLong("id_grupo"),
                    rs.getLong("id_materia"),
                    rs.getLong("id_docente"),
                    rs.getString("aula"),
                    rs.getString("horario")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar grupos: " + e.getMessage(), e);
        }
        return lista;
    }

    // Actualiza el curso asociado, el docente y los datos de programación del grupo.
    public boolean actualizar(Grupo grupo) {
        String sql = "UPDATE public.grupos SET id_materia = ?, id_docente = ?, aula = ?, horario = ? "
                + "WHERE id_grupo = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, grupo.getIdMateria());
            ps.setLong(2, grupo.getIdDocente());
            ps.setString(3, grupo.getAula());
            ps.setString(4, grupo.getHorario());
            ps.setLong(5, grupo.getIdGrupo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar grupo: " + e.getMessage(), e);
        }
    }

    public boolean eliminar(long idGrupo) {
        String sql = "DELETE FROM public.grupos WHERE id_grupo = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idGrupo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar grupo: " + e.getMessage(), e);
        }
    }
}
