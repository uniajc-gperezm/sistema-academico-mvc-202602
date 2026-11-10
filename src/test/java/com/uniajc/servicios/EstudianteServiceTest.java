package com.uniajc.servicios;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.uniajc.dao.EstudianteDao;
import com.uniajc.db.DatabaseConexion;
import com.uniajc.modelo.Estudiante;

public class EstudianteServiceTest {

    private EstudianteService estudianteService;
    private EstudianteDao estudianteDao;

    @BeforeAll
    public static void setupDatabase() {
        DatabaseConexion.setConfig(
                "jdbc:h2:mem:test_estudiante_db;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
                "sa", ""
        );
        DatabaseConexion.initDatabase();
    }

    @BeforeEach
    public void setup() {
        estudianteDao = new EstudianteDao();
        estudianteService = new EstudianteService(estudianteDao);
    }

    @Test
    @DisplayName("Debe registrar un estudiante correctamente")
    public void testRegistrarEstudianteExitoso() {
        String random = UUID.randomUUID().toString().substring(0, 6);
        Estudiante nuevo = new Estudiante("Carlos", "Santana", "carlos." + random + "@uniajc.edu.co");

        estudianteService.registrarEstudiante(nuevo);

        assertTrue(nuevo.getId() > 0, "El ID generado debe ser mayor a 0");
        Optional<Estudiante> opt = estudianteService.buscarEstudiantePorId(nuevo.getId());
        assertTrue(opt.isPresent(), "El estudiante debe existir en la base de datos");
        assertEquals("Carlos", opt.get().getNombre());
        assertEquals("Santana", opt.get().getApellido());
    }

    @Test
    @DisplayName("Debe fallar al registrar un estudiante con datos nulos")
    public void testRegistrarEstudianteNulo() {
        assertThrows(IllegalArgumentException.class, () -> estudianteService.registrarEstudiante(null));
    }

    @Test
    @DisplayName("Debe fallar si el nombre está vacío")
    public void testRegistrarEstudianteNombreVacio() {
        Estudiante estudiante = new Estudiante("", "Gómez", "test@uniajc.edu.co");
        assertThrows(IllegalArgumentException.class, () -> estudianteService.registrarEstudiante(estudiante));
    }

    @Test
    @DisplayName("Debe fallar si el apellido está vacío")
    public void testRegistrarEstudianteApellidoVacio() {
        Estudiante estudiante = new Estudiante("Juan", "   ", "test@uniajc.edu.co");
        assertThrows(IllegalArgumentException.class, () -> estudianteService.registrarEstudiante(estudiante));
    }

    @Test
    @DisplayName("Debe fallar si el correo tiene formato inválido")
    public void testRegistrarEstudianteEmailInvalido() {
        Estudiante estudiante = new Estudiante("Juan", "Pérez", "correo_invalido");
        assertThrows(IllegalArgumentException.class, () -> estudianteService.registrarEstudiante(estudiante));
    }

    @Test
    @DisplayName("Debe fallar si el correo ya está registrado para otro estudiante")
    public void testRegistrarEstudianteEmailDuplicado() {
        String random = UUID.randomUUID().toString().substring(0, 6);
        String email = "unico." + random + "@uniajc.edu.co";

        Estudiante e1 = new Estudiante("Ana", "Ríos", email);
        estudianteService.registrarEstudiante(e1);

        Estudiante e2 = new Estudiante("Beatriz", "López", email);
        assertThrows(IllegalArgumentException.class, () -> estudianteService.registrarEstudiante(e2));
    }

    @Test
    @DisplayName("Debe actualizar los datos de un estudiante")
    public void testActualizarEstudiante() {
        String random = UUID.randomUUID().toString().substring(0, 6);
        Estudiante e = new Estudiante("Luis", "Torres", "luis." + random + "@uniajc.edu.co");
        estudianteService.registrarEstudiante(e);

        e.setNombre("Luis Fernando");
        e.setApellido("Torres Ruiz");
        estudianteService.actualizarEstudiante(e);

        Optional<Estudiante> actualizado = estudianteService.buscarEstudiantePorId(e.getId());
        assertTrue(actualizado.isPresent());
        assertEquals("Luis Fernando", actualizado.get().getNombre());
        assertEquals("Torres Ruiz", actualizado.get().getApellido());
    }

    @Test
    @DisplayName("Debe eliminar un estudiante existente")
    public void testEliminarEstudiante() {
        String random = UUID.randomUUID().toString().substring(0, 6);
        Estudiante e = new Estudiante("Pedro", "Díaz", "pedro." + random + "@uniajc.edu.co");
        estudianteService.registrarEstudiante(e);
        int id = e.getId();

        estudianteService.eliminarEstudiante(id);

        Optional<Estudiante> opt = estudianteService.buscarEstudiantePorId(id);
        assertTrue(opt.isEmpty(), "El estudiante no debería encontrarse tras ser eliminado");
    }
}
