package com.uniajc.servicios;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.uniajc.dao.MateriaDao;
import com.uniajc.db.DatabaseConexion;
import com.uniajc.modelo.Materia;

public class MateriaServiceTest {

    private MateriaService materiaService;
    private MateriaDao materiaDao;

    @BeforeAll
    public static void setupDatabase() {
        DatabaseConexion.setConfig(
                "jdbc:h2:mem:test_materia_db;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
                "sa", ""
        );
        DatabaseConexion.initDatabase();
    }

    @BeforeEach
    public void setup() {
        materiaDao = new MateriaDao();
        materiaService = new MateriaService(materiaDao);
    }

    @Test
    @DisplayName("Debe registrar una materia con créditos válidos")
    public void testRegistrarMateriaExitoso() {
        Materia materia = new Materia("Estructuras de Datos", 4);
        materiaService.registrarMateria(materia);

        assertTrue(materia.getId() > 0);
        Optional<Materia> opt = materiaService.buscarMateriaPorId(materia.getId());
        assertTrue(opt.isPresent());
        assertEquals("Estructuras de Datos", opt.get().getNombre());
        assertEquals(4, opt.get().getCreditos());
    }

    @Test
    @DisplayName("Debe fallar si los créditos son menores a 1")
    public void testRegistrarMateriaCreditosMenorA1() {
        Materia m = new Materia("Invalida", 0);
        assertThrows(IllegalArgumentException.class, () -> materiaService.registrarMateria(m));
    }

    @Test
    @DisplayName("Debe fallar si los créditos son mayores a 10")
    public void testRegistrarMateriaCreditosMayorA10() {
        Materia m = new Materia("Invalida", 11);
        assertThrows(IllegalArgumentException.class, () -> materiaService.registrarMateria(m));
    }

    @Test
    @DisplayName("Debe fallar si el nombre de la materia es vacío")
    public void testRegistrarMateriaNombreVacio() {
        Materia m = new Materia("", 3);
        assertThrows(IllegalArgumentException.class, () -> materiaService.registrarMateria(m));
    }

    @Test
    @DisplayName("Debe actualizar una materia")
    public void testActualizarMateria() {
        Materia m = new Materia("Algoritmos I", 3);
        materiaService.registrarMateria(m);

        m.setNombre("Algoritmos Avanzados");
        m.setCreditos(5);
        materiaService.actualizarMateria(m);

        Optional<Materia> opt = materiaService.buscarMateriaPorId(m.getId());
        assertTrue(opt.isPresent());
        assertEquals("Algoritmos Avanzados", opt.get().getNombre());
        assertEquals(5, opt.get().getCreditos());
    }

    @Test
    @DisplayName("Debe eliminar una materia")
    public void testEliminarMateria() {
        Materia m = new Materia("Materia Temporal", 2);
        materiaService.registrarMateria(m);
        int id = m.getId();

        materiaService.eliminarMateria(id);

        Optional<Materia> opt = materiaService.buscarMateriaPorId(id);
        assertTrue(opt.isEmpty());
    }
}
