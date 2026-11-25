package com.uniajc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.uniajc.db.DatabaseConexion;
import com.uniajc.modelo.Usuario;

public class UsuarioDao {

    public void guardar(Usuario usuario) {
        String sql = "INSERT INTO \"practica-mvc\".usuarios (username, password, nombre, rol, activo, email, estudiante_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, usuario.getUsername());
            pstmt.setString(2, usuario.getPassword());
            pstmt.setString(3, usuario.getNombre());
            pstmt.setString(4, usuario.getRol());
            pstmt.setBoolean(5, usuario.isActivo());
            pstmt.setString(6, usuario.getEmail());
            if (usuario.getEstudianteId() != null && usuario.getEstudianteId() > 0) {
                pstmt.setInt(7, usuario.getEstudianteId());
            } else {
                pstmt.setNull(7, Types.INTEGER);
            }

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usuario.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al guardar usuario: " + e.getMessage(), e);
        }
    }

    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id, username, password, nombre, rol, activo, fecha_creacion, email, ultimo_acceso, estudiante_id " +
                     "FROM \"practica-mvc\".usuarios ORDER BY id ASC";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al obtener usuarios: " + e.getMessage(), e);
        }
        return lista;
    }

    public Optional<Usuario> buscarPorId(int id) {
        String sql = "SELECT id, username, password, nombre, rol, activo, fecha_creacion, email, ultimo_acceso, estudiante_id " +
                     "FROM \"practica-mvc\".usuarios WHERE id = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearUsuario(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al buscar usuario por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<Usuario> buscarPorUsername(String username) {
        String sql = "SELECT id, username, password, nombre, rol, activo, fecha_creacion, email, ultimo_acceso, estudiante_id " +
                     "FROM \"practica-mvc\".usuarios WHERE LOWER(username) = LOWER(?)";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username != null ? username.trim() : "");

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearUsuario(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al buscar por username: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public void actualizar(Usuario usuario) {
        String sql = "UPDATE \"practica-mvc\".usuarios " +
                     "SET username = ?, password = ?, nombre = ?, rol = ?, activo = ?, email = ?, estudiante_id = ? " +
                     "WHERE id = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getUsername());
            pstmt.setString(2, usuario.getPassword());
            pstmt.setString(3, usuario.getNombre());
            pstmt.setString(4, usuario.getRol());
            pstmt.setBoolean(5, usuario.isActivo());
            pstmt.setString(6, usuario.getEmail());
            if (usuario.getEstudianteId() != null && usuario.getEstudianteId() > 0) {
                pstmt.setInt(7, usuario.getEstudianteId());
            } else {
                pstmt.setNull(7, Types.INTEGER);
            }
            pstmt.setInt(8, usuario.getId());

            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new RuntimeException("Usuario no encontrado con ID: " + usuario.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al actualizar usuario: " + e.getMessage(), e);
        }
    }

    public void actualizarUltimoAcceso(int idUsuario) {
        String sql = "UPDATE \"practica-mvc\".usuarios SET ultimo_acceso = NOW() WHERE id = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar último acceso del usuario: " + e.getMessage(), e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM \"practica-mvc\".usuarios WHERE id = ?";

        try (Connection conn = DatabaseConexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                throw new RuntimeException("Usuario no encontrado con ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error en base de datos al eliminar usuario: " + e.getMessage(), e);
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setUsername(rs.getString("username"));
        usuario.setPassword(rs.getString("password"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setRol(rs.getString("rol"));
        usuario.setActivo(rs.getBoolean("activo"));
        usuario.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        usuario.setEmail(rs.getString("email"));
        usuario.setUltimoAcceso(rs.getTimestamp("ultimo_acceso"));

        int estId = rs.getInt("estudiante_id");
        if (!rs.wasNull()) {
            usuario.setEstudianteId(estId);
        }
        return usuario;
    }
}
