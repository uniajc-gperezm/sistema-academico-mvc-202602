package com.uniajc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.uniajc.db.DatabaseConexion;
import com.uniajc.modelo.Rol;
import com.uniajc.modelo.Usuario;

/** DAO de usuarios. Tabla: users (id, username, password, nombre, rol, activo, fecha_creacion). */
public class UsuarioDao {

    private static final String TABLA = "users";

    /** Busca un usuario por su username. */
    public Optional<Usuario> buscarPorUsername(String username) {
        String sql = "SELECT id, username, password, nombre, rol, activo, fecha_creacion FROM "
                + TABLA + " WHERE username = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error buscando usuario: " + ex.getMessage(), ex);
        }
        return Optional.empty();
    }

    /** Busca un usuario por su id. */
    public Optional<Usuario> buscarPorId(int id) {
        String sql = "SELECT id, username, password, nombre, rol, activo, fecha_creacion FROM "
                + TABLA + " WHERE id = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error buscando usuario: " + ex.getMessage(), ex);
        }
        return Optional.empty();
    }

    /** Guarda un nuevo usuario. */
    public void guardar(Usuario u) {
        String sql = "INSERT INTO " + TABLA
                + " (username, password, nombre, rol, activo) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getNombre());
            ps.setString(4, u.getRol() != null ? u.getRol().name() : "USUARIO");
            ps.setBoolean(5, u.isActivo());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error guardando usuario: " + ex.getMessage(), ex);
        }
    }

    /** Actualiza la contraseña de un usuario. */
    public void cambiarPassword(int id, String nuevoPasswordHash) {
        String sql = "UPDATE " + TABLA + " SET password = ? WHERE id = ?";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoPasswordHash);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error actualizando contraseña: " + ex.getMessage(), ex);
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setNombre(rs.getString("nombre"));
        String rol = rs.getString("rol");
        u.setRol(rol != null ? Rol.valueOf(rol) : Rol.USUARIO);
        u.setActivo(rs.getBoolean("activo"));
        u.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        return u;
    }
}