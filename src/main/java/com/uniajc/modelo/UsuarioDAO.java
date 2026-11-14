package com.uniajc.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.uniajc.db.DatabaseConexion;

/** Consulta cuentas y actualiza su hash sin devolver contraseñas a la vista. */
public class UsuarioDAO {
    public Optional<Usuario> buscarPorUsername(String username) {
        String sql = "SELECT id, username, password, nombre, rol, activo "
                + "FROM public.username WHERE username = ?";
        Connection connection = DatabaseConexion.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) return Optional.empty();
                Boolean activo = (Boolean) result.getObject("activo");
                return Optional.of(new Usuario(
                        result.getLong("id"),
                        result.getString("username"),
                        result.getString("nombre"),
                        result.getString("rol"),
                        activo == null || activo,
                        result.getString("password")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo consultar el usuario: " + e.getMessage(), e);
        }
    }

    public boolean actualizarHashContrasena(long id, String hash) {
        String sql = "UPDATE public.username SET password = ? WHERE id = ?";
        Connection connection = DatabaseConexion.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, hash);
            statement.setLong(2, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo actualizar el hash de la contraseña: " + e.getMessage(), e);
        }
    }
}
