package com.uniajc.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.uniajc.db.DatabaseConexion;
import com.uniajc.modelo.Docente;
import com.uniajc.modelo.Estudiante;
import com.uniajc.modelo.Grupo;
import com.uniajc.modelo.Inscripcion;
import com.uniajc.modelo.Materia;

public class DaoIntegrationTest {

    private final EstudianteDao estudianteDao = new EstudianteDao();
    private final DocenteDao docenteDao = new DocenteDao();
    private final MateriaDao materiaDao = new MateriaDao();
    private final GrupoDao grupoDao = new GrupoDao();
    private final InscripcionDao inscripcionDao = new InscripcionDao();

    @BeforeAll
    public static void setup() {
        DatabaseConexion.setConfig(
                "jdbc:h2:mem:dao_test_db;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
                "sa", ""
        );
        DatabaseConexion.initDatabase();
    }

    @Test
    @DisplayName("DAO CRUD completo para todas las entidades en base de datos")
    public void testFlujoCompletoDao() {
        String random = UUID.randomUUID().toString().substring(0, 6);

        // 1. Estudiante
        Estudiante est = new Estudiante("Estudiante", "DaoTest", "est.dao." + random + "@uniajc.edu.co");
        estudianteDao.guardar(est);
        assertTrue(est.getId() > 0);

        Optional<Estudiante> estRecuperado = estudianteDao.buscarPorId(est.getId());
        assertTrue(estRecuperado.isPresent());
        assertEquals(est.getEmail(), estRecuperado.get().getEmail());

        // 2. Docente
        Docente doc = new Docente("Docente Dao", "Programación", "doc.dao." + random + "@uniajc.edu.co");
        docenteDao.guardar(doc);
        assertTrue(doc.getId() > 0);

        Optional<Docente> docRecuperado = docenteDao.buscarPorId(doc.getId());
        assertTrue(docRecuperado.isPresent());

        // 3. Materia
        Materia mat = new Materia("Materia Dao " + random, 3);
        materiaDao.guardar(mat);
        assertTrue(mat.getId() > 0);

        Optional<Materia> matRecuperada = materiaDao.buscarPorId(mat.getId());
        assertTrue(matRecuperada.isPresent());

        // 4. Grupo
        Grupo grp = new Grupo(mat.getId(), doc.getId(), "Lab 101", "Lun 08:00");
        grupoDao.guardar(grp);
        assertTrue(grp.getId() > 0);

        List<Grupo> gruposMateria = grupoDao.buscarPorMateria(mat.getId());
        assertFalse(gruposMateria.isEmpty());

        // 5. Inscripcion
        Inscripcion insc = new Inscripcion(est.getId(), grp.getId());
        inscripcionDao.guardar(insc);
        assertTrue(insc.getId() > 0);

        Optional<Inscripcion> inscOpt = inscripcionDao.buscarPorEstudianteYGrupo(est.getId(), grp.getId());
        assertTrue(inscOpt.isPresent());

        insc.setNotaFinal(4.8);
        insc.setEstado("APROBADO");
        inscripcionDao.actualizar(insc);

        Optional<Inscripcion> inscActualizada = inscripcionDao.buscarPorId(insc.getId());
        assertTrue(inscActualizada.isPresent());
        assertEquals(4.8, inscActualizada.get().getNotaFinal());
        assertEquals("APROBADO", inscActualizada.get().getEstado());

        // Cleanup
        inscripcionDao.eliminar(insc.getId());
        grupoDao.eliminar(grp.getId());
        materiaDao.eliminar(mat.getId());
        docenteDao.eliminar(doc.getId());
        estudianteDao.eliminar(est.getId());
    }
}
