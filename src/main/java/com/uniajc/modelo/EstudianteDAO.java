package com.uniajc.modelo;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.uniajc.db.DatabaseConexion;

/** Acceso a datos de estudiante: cada método delega su operación a PostgreSQL. */
public class EstudianteDAO {

    // Ejecuta el procedimiento de alta, incluyendo NIT y celular, y recupera el ID nuevo.
    public boolean insertar(Estudiante est) {
        String sql = "{call public.sp_insertar_estudiante(?, ?, ?, ?, ?, ?)}";
        Connection conn = DatabaseConexion.getConnection();
        try (CallableStatement llamada = conn.prepareCall(sql)) {
            llamada.setString(1, est.getName());
            llamada.setString(2, est.getLastname());
            llamada.setString(3, est.getEmail());
            setNumeroOpcional(llamada, 4, est.getNit());
            setNumeroOpcional(llamada, 5, est.getNumcelular());
            llamada.registerOutParameter(6, Types.BIGINT);
            llamada.execute();
            return llamada.getLong(6) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear estudiante con el procedimiento almacenado: "
                    + e.getMessage(), e);
        }
    }

    // Lee todos los estudiantes mediante una función SQL almacenada en PostgreSQL.
    // Convierte las filas JDBC en objetos que la vista puede mostrar.
    public List<Estudiante> listarTodos() {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT id, name, lastname, email, nit, numcelular "
                + "FROM public.fn_listar_estudiantes()";
        Connection conn = DatabaseConexion.getConnection();
        try (PreparedStatement consulta = conn.prepareStatement(sql);
             ResultSet rs = consulta.executeQuery()) {
            while (rs.next()) {
                lista.add(new Estudiante(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("lastname"),
                    rs.getString("email"),
                    numeroOpcional(rs, "nit"),
                    numeroOpcional(rs, "numcelular")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar estudiantes: " + e.getMessage(), e);
        }
        return lista;
    }

    // Ejecuta el procedimiento almacenado que actualiza los datos y retorna si encontró el ID.
    public boolean actualizar(Estudiante est) {
        String sql = "{call public.sp_actualizar_estudiante(?, ?, ?, ?, ?, ?, ?)}";
        Connection conn = DatabaseConexion.getConnection();
        try (CallableStatement llamada = conn.prepareCall(sql)) {
            llamada.setLong(1, est.getId());
            llamada.setString(2, est.getName());
            llamada.setString(3, est.getLastname());
            llamada.setString(4, est.getEmail());
            setNumeroOpcional(llamada, 5, est.getNit());
            setNumeroOpcional(llamada, 6, est.getNumcelular());
            llamada.registerOutParameter(7, Types.BOOLEAN);
            llamada.execute();
            return llamada.getBoolean(7);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar estudiante con el procedimiento almacenado: "
                    + e.getMessage(), e);
        }
    }

    // Ejecuta el procedimiento almacenado que elimina por ID e informa si borró una fila.
    public boolean eliminar(long id) {
        String sql = "{call public.sp_eliminar_estudiante(?, ?)}";
        Connection conn = DatabaseConexion.getConnection();
        try (CallableStatement llamada = conn.prepareCall(sql)) {
            llamada.setLong(1, id);
            llamada.registerOutParameter(2, Types.BOOLEAN);
            llamada.execute();
            return llamada.getBoolean(2);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar estudiante con el procedimiento almacenado: "
                    + e.getMessage(), e);
        }
    }

    // Envía el número como BIGINT o como SQL NULL cuando el campo quedó vacío.
    private void setNumeroOpcional(CallableStatement llamada, int posicion, Long valor) throws SQLException {
        if (valor == null) {
            llamada.setNull(posicion, Types.BIGINT);
        } else {
            llamada.setLong(posicion, valor);
        }
    }

    // Convierte columnas numéricas opcionales sin confundir NULL con el número cero.
    private Long numeroOpcional(ResultSet rs, String columna) throws SQLException {
        long valor = rs.getLong(columna);
        return rs.wasNull() ? null : valor;
    }
}

