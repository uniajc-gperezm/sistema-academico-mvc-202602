package com.uniajc.servicios;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.uniajc.dao.DocenteDao;
import com.uniajc.dao.GrupoDao;
import com.uniajc.dao.MateriaDao;
import com.uniajc.db.DatabaseConexion;
import com.uniajc.modelo.Docente;
import com.uniajc.modelo.Grupo;
import com.uniajc.modelo.Materia;

public class GrupoServiceTest {

    private GrupoService grupoService;
    private GrupoDao grupoDao;
    private MateriaDao materiaDao;
    private DocenteDao docenteDao;

    private int materiaIdValida;
    private int docenteIdValido;

    @BeforeAll
    public static void setupDatabase() {
        DatabaseConexion.setConfig(
                "jdbc:h2:mem:test_grupo_db;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
                "sa", ""
        );
        DatabaseConexion.initDatabase();
    }

    @BeforeEach
    public void setup() {
        grupoDao = new GrupoDao();
        materiaDao = new MateriaDao();
        docenteDao = new DocenteDao();
        grupoService = new GrupoService(grupoDao, materiaDao, docenteDao);

        Materia m = new Materia("Sistemas Operativos " + UUID.randomUUID().toString().substring(0, 4), 3);
        materiaDao.guardar(m);
        materiaIdValida = m.getId();

        Docente d = new Docente("Docente Grupo", "Sistemas", "doc.grupo." + UUID.randomUUID().toString().substring(0, 5) + "@uniajc.edu.co");
        docenteDao.guardar(d);
        docenteIdValido = d.getId();
    }

    @Test
    @DisplayName("Debe registrar un grupo con materia y docente válidos")
    public void testRegistrarGrupoExitoso() {
        Grupo grupo = new Grupo(materiaIdValida, docenteIdValido, "Aula 301", "Lun-Mie 08:00-10:00");
        grupoService.registrarGrupo(grupo);

        assertTrue(grupo.getId() > 0);
        Optional<Grupo> opt = grupoService.buscarGrupoPorId(grupo.getId());
        assertTrue(opt.isPresent());
        assertEquals("Aula 301", opt.get().getAula());
        assertEquals("Lun-Mie 08:00-10:00", opt.get().getHorario());
    }

    @Test
    @DisplayName("Debe fallar al registrar un grupo con ID de materia inexistente")
    public void testRegistrarGrupoMateriaInexistente() {
        Grupo grupo = new Grupo(99999, docenteIdValido, "Aula 101", "Mar-Jue 10:00-12:00");
        assertThrows(IllegalArgumentException.class, () -> grupoService.registrarGrupo(grupo));
    }

    @Test
    @DisplayName("Debe fallar al registrar un grupo con ID de docente inexistente")
    public void testRegistrarGrupoDocenteInexistente() {
        Grupo grupo = new Grupo(materiaIdValida, 99999, "Aula 101", "Mar-Jue 10:00-12:00");
        assertThrows(IllegalArgumentException.class, () -> grupoService.registrarGrupo(grupo));
    }

    @Test
    @DisplayName("Debe fallar si el aula o horario son vacíos")
    public void testRegistrarGrupoCamposVacios() {
        Grupo g1 = new Grupo(materiaIdValida, docenteIdValido, "", "08:00-10:00");
        assertThrows(IllegalArgumentException.class, () -> grupoService.registrarGrupo(g1));

        Grupo g2 = new Grupo(materiaIdValida, docenteIdValido, "Aula 102", "");
        assertThrows(IllegalArgumentException.class, () -> grupoService.registrarGrupo(g2));
    }

    @Test
    @DisplayName("Debe actualizar un grupo")
    public void testActualizarGrupo() {
        Grupo g = new Grupo(materiaIdValida, docenteIdValido, "Lab 1", "Vie 14:00-18:00");
        grupoService.registrarGrupo(g);

        g.setAula("Lab 2 - Redes");
        g.setHorario("Sab 08:00-12:00");
        grupoService.actualizarGrupo(g);

        Optional<Grupo> opt = grupoService.buscarGrupoPorId(g.getId());
        assertTrue(opt.isPresent());
        assertEquals("Lab 2 - Redes", opt.get().getAula());
        assertEquals("Sab 08:00-12:00", opt.get().getHorario());
    }

    @Test
    @DisplayName("Debe eliminar un grupo")
    public void testEliminarGrupo() {
        Grupo g = new Grupo(materiaIdValida, docenteIdValido, "Aula 404", "Lun 10:00");
        grupoService.registrarGrupo(g);
        int id = g.getId();

        grupoService.eliminarGrupo(id);

        Optional<Grupo> opt = grupoService.buscarGrupoPorId(id);
        assertTrue(opt.isEmpty());
    }
}
