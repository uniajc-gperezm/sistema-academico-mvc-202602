package com.uniajc.servicios;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.uniajc.dao.DocenteDao;
import com.uniajc.dao.EstudianteDao;
import com.uniajc.dao.GrupoDao;
import com.uniajc.dao.InscripcionDao;
import com.uniajc.dao.MateriaDao;
import com.uniajc.db.DatabaseConexion;
import com.uniajc.modelo.Docente;
import com.uniajc.modelo.Estudiante;
import com.uniajc.modelo.Grupo;
import com.uniajc.modelo.Inscripcion;
import com.uniajc.modelo.Materia;

public class InscripcionServiceTest {

    private InscripcionService inscripcionService;
    private InscripcionDao inscripcionDao;
    private EstudianteDao estudianteDao;
    private GrupoDao grupoDao;
    private MateriaDao materiaDao;
    private DocenteDao docenteDao;

    private int estudianteId;
    private int grupoId;

    @BeforeAll
    public static void setupDatabase() {
        DatabaseConexion.setConfig(
                "jdbc:h2:mem:test_inscripcion_db;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
                "sa", ""
        );
        DatabaseConexion.initDatabase();
    }

    @BeforeEach
    public void setup() {
        inscripcionDao = new InscripcionDao();
        estudianteDao = new EstudianteDao();
        grupoDao = new GrupoDao();
        materiaDao = new MateriaDao();
        docenteDao = new DocenteDao();
        inscripcionService = new InscripcionService(inscripcionDao, estudianteDao, grupoDao);

        String random = UUID.randomUUID().toString().substring(0, 5);
        Estudiante est = new Estudiante("Alumno", "Test", "alumno." + random + "@uniajc.edu.co");
        estudianteDao.guardar(est);
        estudianteId = est.getId();

        Materia mat = new Materia("Materia " + random, 3);
        materiaDao.guardar(mat);

        Docente doc = new Docente("Docente " + random, "Software", "doc." + random + "@uniajc.edu.co");
        docenteDao.guardar(doc);

        Grupo grp = new Grupo(mat.getId(), doc.getId(), "Aula 10", "Mie 07:00-10:00");
        grupoDao.guardar(grp);
        grupoId = grp.getId();
    }

    @Test
    @DisplayName("Debe registrar una inscripción correctamente en estado ACTIVO")
    public void testRegistrarInscripcionExitoso() {
        Inscripcion inscripcion = new Inscripcion(estudianteId, grupoId);
        inscripcionService.registrarInscripcion(inscripcion);

        assertTrue(inscripcion.getId() > 0);
        assertEquals("ACTIVO", inscripcion.getEstado());

        Optional<Inscripcion> opt = inscripcionService.buscarInscripcionPorId(inscripcion.getId());
        assertTrue(opt.isPresent());
        assertEquals(estudianteId, opt.get().getIdEstudiante());
        assertEquals(grupoId, opt.get().getIdGrupo());
    }

    @Test
    @DisplayName("Debe fallar si el estudiante no existe")
    public void testRegistrarInscripcionEstudianteInexistente() {
        Inscripcion i = new Inscripcion(99999, grupoId);
        assertThrows(IllegalArgumentException.class, () -> inscripcionService.registrarInscripcion(i));
    }

    @Test
    @DisplayName("Debe fallar si el grupo no existe")
    public void testRegistrarInscripcionGrupoInexistente() {
        Inscripcion i = new Inscripcion(estudianteId, 99999);
        assertThrows(IllegalArgumentException.class, () -> inscripcionService.registrarInscripcion(i));
    }

    @Test
    @DisplayName("Debe fallar si el estudiante ya está inscrito en el mismo grupo")
    public void testRegistrarInscripcionDuplicada() {
        Inscripcion i1 = new Inscripcion(estudianteId, grupoId);
        inscripcionService.registrarInscripcion(i1);

        Inscripcion i2 = new Inscripcion(estudianteId, grupoId);
        assertThrows(IllegalArgumentException.class, () -> inscripcionService.registrarInscripcion(i2));
    }

    @Test
    @DisplayName("Debe calificar nota aprobatoria (>= 3.0) y cambiar estado a APROBADO")
    public void testRegistrarNotaAprobado() {
        Inscripcion i = new Inscripcion(estudianteId, grupoId);
        inscripcionService.registrarInscripcion(i);

        inscripcionService.registrarNota(i.getId(), 4.5);

        Optional<Inscripcion> calificada = inscripcionService.buscarInscripcionPorId(i.getId());
        assertTrue(calificada.isPresent());
        assertEquals(4.5, calificada.get().getNotaFinal());
        assertEquals("APROBADO", calificada.get().getEstado());
    }

    @Test
    @DisplayName("Debe calificar nota reprobatoria (< 3.0) y cambiar estado a REPROBADO")
    public void testRegistrarNotaReprobado() {
        Inscripcion i = new Inscripcion(estudianteId, grupoId);
        inscripcionService.registrarInscripcion(i);

        inscripcionService.registrarNota(i.getId(), 2.4);

        Optional<Inscripcion> calificada = inscripcionService.buscarInscripcionPorId(i.getId());
        assertTrue(calificada.isPresent());
        assertEquals(2.4, calificada.get().getNotaFinal());
        assertEquals("REPROBADO", calificada.get().getEstado());
    }

    @Test
    @DisplayName("Debe fallar si la nota está fuera de rango (menor a 0.0 o mayor a 5.0)")
    public void testRegistrarNotaFueraDeRango() {
        Inscripcion i = new Inscripcion(estudianteId, grupoId);
        inscripcionService.registrarInscripcion(i);

        assertThrows(IllegalArgumentException.class, () -> inscripcionService.registrarNota(i.getId(), -0.5));
        assertThrows(IllegalArgumentException.class, () -> inscripcionService.registrarNota(i.getId(), 5.5));
    }

    @Test
    @DisplayName("Debe eliminar una inscripción existente")
    public void testEliminarInscripcion() {
        Inscripcion i = new Inscripcion(estudianteId, grupoId);
        inscripcionService.registrarInscripcion(i);
        int id = i.getId();

        inscripcionService.eliminarInscripcion(id);

        Optional<Inscripcion> opt = inscripcionService.buscarInscripcionPorId(id);
        assertTrue(opt.isEmpty());
    }
}
