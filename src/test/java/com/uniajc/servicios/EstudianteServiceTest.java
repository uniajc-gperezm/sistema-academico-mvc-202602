package com.uniajc.servicios;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.uniajc.dao.EstudianteDao;
import com.uniajc.modelo.Estudiante;

class EstudianteServiceTest {

    private final EstudianteService service = new EstudianteService(new EstudianteDao());

    @Test
    void registrarRechazaNombreVacio() {
        Estudiante e = new Estudiante();
        e.setNombre(" ");
        e.setApellido("Perez");
        e.setEmail("juan@correo.com");
        assertThrows(IllegalArgumentException.class, () -> service.registrarEstudiante(e));
    }

    @Test
    void registrarRechazaEmailInvalido() {
        Estudiante e = new Estudiante();
        e.setNombre("Juan");
        e.setApellido("Perez");
        e.setEmail("correo-sin-arroba");
        assertThrows(IllegalArgumentException.class, () -> service.registrarEstudiante(e));
    }

    @Test
    void validacionConEmailCorrectoPasa() {
        Estudiante e = new Estudiante();
        e.setNombre("Juan");
        e.setApellido("Perez");
        e.setEmail("juan.perez@uniajc.edu.co");
        assertDoesNotThrow(() -> service.validarPublico(e));
    }

    @Test
    void actualizarRechazaIdInvalido() {
        Estudiante e = new Estudiante();
        e.setId_estudiante(-1);
        e.setNombre("Juan");
        e.setApellido("Perez");
        e.setEmail("juan@correo.com");
        assertThrows(IllegalArgumentException.class, () -> service.actualizarEstudiante(e));
    }
}
