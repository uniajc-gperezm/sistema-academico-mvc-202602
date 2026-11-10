package com.uniajc.servicios;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.uniajc.dao.DocenteDao;
import com.uniajc.db.DatabaseConexion;
import com.uniajc.modelo.Docente;

public class DocenteServiceTest {

    private DocenteService docenteService;
    private DocenteDao docenteDao;

    @BeforeAll
    public static void setupDatabase() {
        DatabaseConexion.setConfig(
                "jdbc:h2:mem:test_docente_db;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
                "sa", ""
        );
        DatabaseConexion.initDatabase();
    }

    @BeforeEach
    public void setup() {
        docenteDao = new DocenteDao();
        docenteService = new DocenteService(docenteDao);
    }

    @Test
    @DisplayName("Debe registrar un docente correctamente")
    public void testRegistrarDocenteExitoso() {
        String random = UUID.randomUUID().toString().substring(0, 6);
        Docente docente = new Docente("Roberto", "Inteligencia Artificial", "roberto." + random + "@uniajc.edu.co");

        docenteService.registrarDocente(docente);

        assertTrue(docente.getId() > 0);
        Optional<Docente> opt = docenteService.buscarDocentePorId(docente.getId());
        assertTrue(opt.isPresent());
        assertEquals("Roberto", opt.get().getNombre());
        assertEquals("Inteligencia Artificial", opt.get().getEspecialidad());
    }

    @Test
    @DisplayName("Debe fallar si los datos del docente son nulos")
    public void testRegistrarDocenteNulo() {
        assertThrows(IllegalArgumentException.class, () -> docenteService.registrarDocente(null));
    }

    @Test
    @DisplayName("Debe fallar si la especialidad está vacía")
    public void testRegistrarDocenteEspecialidadVacia() {
        Docente d = new Docente("Fernando", "", "fernando@uniajc.edu.co");
        assertThrows(IllegalArgumentException.class, () -> docenteService.registrarDocente(d));
    }

    @Test
    @DisplayName("Debe fallar si el correo del docente ya existe")
    public void testRegistrarDocenteEmailDuplicado() {
        String random = UUID.randomUUID().toString().substring(0, 6);
        String email = "docente." + random + "@uniajc.edu.co";

        Docente d1 = new Docente("Gloria", "Redes", email);
        docenteService.registrarDocente(d1);

        Docente d2 = new Docente("Gustavo", "Seguridad", email);
        assertThrows(IllegalArgumentException.class, () -> docenteService.registrarDocente(d2));
    }

    @Test
    @DisplayName("Debe actualizar los datos de un docente")
    public void testActualizarDocente() {
        String random = UUID.randomUUID().toString().substring(0, 6);
        Docente d = new Docente("Alonso", "Cálculo", "alonso." + random + "@uniajc.edu.co");
        docenteService.registrarDocente(d);

        d.setEspecialidad("Física Cuántica");
        docenteService.actualizarDocente(d);

        Optional<Docente> actualizado = docenteService.buscarDocentePorId(d.getId());
        assertTrue(actualizado.isPresent());
        assertEquals("Física Cuántica", actualizado.get().getEspecialidad());
    }

    @Test
    @DisplayName("Debe eliminar un docente existente")
    public void testEliminarDocente() {
        String random = UUID.randomUUID().toString().substring(0, 6);
        Docente d = new Docente("Marta", "Química", "marta." + random + "@uniajc.edu.co");
        docenteService.registrarDocente(d);
        int id = d.getId();

        docenteService.eliminarDocente(id);

        Optional<Docente> opt = docenteService.buscarDocentePorId(id);
        assertTrue(opt.isEmpty());
    }
}
