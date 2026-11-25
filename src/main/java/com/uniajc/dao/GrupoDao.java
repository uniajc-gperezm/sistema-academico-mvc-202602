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
        String sql = "INSERT INTO \"practica-mvc\".grupos (id_materia, id_docente, aula, horario, cupo_maximo, periodo) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, grupo.getIdMateria());
            pstmt.setInt(2, grupo.getIdDocente());
            pstmt.setString(3, grupo.getClassroom());
            pstmt.setString(4, grupo.getSchedule());
            pstmt.setInt(5, grupo.getCupoMaximo());
            pstmt.setString(6, grupo.getPeriodo());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    grupo.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al guardar grupo: " + e.getMessage(), e);
        }
    }

    public List<Grupo> obtenerTodos() {
        List<Grupo> lista = new ArrayList<>();
        String sql = "SELECT id_grupo AS id, id_materia, id_docente, aula, horario, cupo_maximo, periodo FROM \"practica-mvc\".grupos ORDER BY id_grupo ASC";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearGrupo(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al obtener grupos: " + e.getMessage(), e);
        }
        return lista;
    }

    public Optional<Grupo> buscarPorId(int id) {
        String sql = "SELECT id_grupo AS id, id_materia, id_docente, aula, horario, cupo_maximo, periodo FROM \"practica-mvc\".grupos WHERE id_grupo = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearGrupo(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al buscar grupo por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public List<Grupo> buscarPorMateria(int idMateria) {
        List<Grupo> lista = new ArrayList<>();
        String sql = "SELECT id_grupo AS id, id_materia, id_docente, aula, horario, cupo_maximo, periodo FROM \"practica-mvc\".grupos WHERE id_materia = ? ORDER BY id_grupo ASC";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idMateria);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearGrupo(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al buscar grupos por materia: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<Grupo> buscarPorAula(String classroom) {
        List<Grupo> lista = new ArrayList<>();
        String sql = "SELECT id_grupo AS id, id_materia, id_docente, aula, horario, cupo_maximo, periodo FROM \"practica-mvc\".grupos " +
                     "WHERE LOWER(aula) LIKE ? ORDER BY id_grupo ASC";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String termino = "%" + (classroom != null ? classroom.trim().toLowerCase() : "") + "%";
            pstmt.setString(1, termino);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearGrupo(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al buscar grupos por aula: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<Grupo> buscarPorPeriodo(String periodo) {
        List<Grupo> lista = new ArrayList<>();
        String sql = "SELECT id_grupo AS id, id_materia, id_docente, aula, horario, cupo_maximo, periodo FROM \"practica-mvc\".grupos " +
                     "WHERE LOWER(periodo) = ? ORDER BY id_grupo ASC";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, periodo != null ? periodo.trim().toLowerCase() : "");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearGrupo(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al buscar grupos por periodo: " + e.getMessage(), e);
        }
        return lista;
    }

    public void actualizar(Grupo grupo) {
        String sql = "UPDATE \"practica-mvc\".grupos SET id_materia = ?, id_docente = ?, aula = ?, horario = ?, cupo_maximo = ?, periodo = ? WHERE id_grupo = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, grupo.getIdMateria());
            pstmt.setInt(2, grupo.getIdDocente());
            pstmt.setString(3, grupo.getClassroom());
            pstmt.setString(4, grupo.getSchedule());
            pstmt.setInt(5, grupo.getCupoMaximo());
            pstmt.setString(6, grupo.getPeriodo());
            pstmt.setInt(7, grupo.getId());

            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new RuntimeException("Grupo no encontrado con ID: " + grupo.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al actualizar grupo: " + e.getMessage(), e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM \"practica-mvc\".grupos WHERE id_grupo = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new RuntimeException("Grupo no encontrado con ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al eliminar grupo: " + e.getMessage(), e);
        }
    }

    private Grupo mapearGrupo(ResultSet rs) throws SQLException {
        Grupo grupo = new Grupo();
        grupo.setId(rs.getInt("id"));
        grupo.setIdMateria(rs.getInt("id_materia"));
        grupo.setIdDocente(rs.getInt("id_docente"));
        grupo.setClassroom(rs.getString("aula"));
        grupo.setSchedule(rs.getString("horario"));
        int cupo = rs.getObject("cupo_maximo") == null ? 0 : rs.getInt("cupo_maximo");
        grupo.setCupoMaximo(cupo);
        grupo.setPeriodo(rs.getString("periodo"));
        return grupo;
    }
}
