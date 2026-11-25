package com.uniajc.servicios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uniajc.modelo.Calificacion;

public class CalificacionServiceTest {

    private CalificacionService calificacionService;

    @BeforeEach
    public void setUp() {
        calificacionService = new CalificacionService();
    }

    @Test
    public void testValidacionCalificacionNula() {
        assertThrows(IllegalArgumentException.class, () -> calificacionService.registrarCalificacion(null));
    }

    @Test
    public void testValidacionNotaFueraDeRango() {
        Calificacion cMayor = new Calificacion(1, "Corte 1", new BigDecimal("30"), new BigDecimal("5.5"), "");
        assertThrows(IllegalArgumentException.class, () -> calificacionService.registrarCalificacion(cMayor));

        Calificacion cMenor = new Calificacion(1, "Corte 1", new BigDecimal("30"), new BigDecimal("-1.0"), "");
        assertThrows(IllegalArgumentException.class, () -> calificacionService.registrarCalificacion(cMenor));
    }

    @Test
    public void testValidacionPorcentajeInvalido() {
        Calificacion cCero = new Calificacion(1, "Corte 1", BigDecimal.ZERO, new BigDecimal("4.0"), "");
        assertThrows(IllegalArgumentException.class, () -> calificacionService.registrarCalificacion(cCero));

        Calificacion cMayor100 = new Calificacion(1, "Corte 1", new BigDecimal("105"), new BigDecimal("4.0"), "");
        assertThrows(IllegalArgumentException.class, () -> calificacionService.registrarCalificacion(cMayor100));
    }

    @Test
    public void testValidacionCorteVacio() {
        Calificacion cVacio = new Calificacion(1, "   ", new BigDecimal("30"), new BigDecimal("4.0"), "");
        assertThrows(IllegalArgumentException.class, () -> calificacionService.registrarCalificacion(cVacio));
    }

    @Test
    public void testBuscarPorIdInvalido() {
        assertThrows(IllegalArgumentException.class, () -> calificacionService.buscarPorId(-5));
        assertThrows(IllegalArgumentException.class, () -> calificacionService.obtenerCalificacionesPorInscripcion(0));
    }
}

