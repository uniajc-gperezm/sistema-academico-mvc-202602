package com.uniajc.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.uniajc.db.DatabaseConexion;

public class DocenteDAO {

    public boolean insertar(Docente doc) {
        String sql = "INSERT INTO public.docente (nombre, especialidad, area) VALUES (?, ?, ?)";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, doc.getNombre());
            ps.setString(2, doc.getEspecialidad());
            ps.setString(3, doc.getArea());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar docente: " + e.getMessage(), e);
        }
    }

    public List<Docente> listarTodos() {
        List<Docente> lista = new ArrayList<>();
        String sql = "SELECT id_docente, nombre, especialidad, area FROM public.docente ORDER BY id_docente";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Docente(
                    rs.getLong("id_docente"),
                    rs.getString("nombre"),
                    rs.getString("especialidad"),
                    rs.getString("area")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar docentes: " + e.getMessage(), e);
        }
        return lista;
    }

    public boolean actualizar(Docente doc) {
        String sql = "UPDATE public.docente SET nombre = ?, especialidad = ?, area = ? WHERE id_docente = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, doc.getNombre());
            ps.setString(2, doc.getEspecialidad());
            ps.setString(3, doc.getArea());
            ps.setLong(4, doc.getIdDocente());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar docente: " + e.getMessage(), e);
        }
    }

    public boolean eliminar(long idDocente) {
        String sql = "DELETE FROM public.docente WHERE id_docente = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idDocente);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar docente: " + e.getMessage(), e);
        }
    }
}
