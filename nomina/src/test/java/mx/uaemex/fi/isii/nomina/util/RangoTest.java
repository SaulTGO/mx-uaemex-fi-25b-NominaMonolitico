package mx.uaemex.fi.isii.nomina.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import mx.uaemex.fi.isii.nomina.util.Rango;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RangoTest {


    @Test
    public void UT_R_001_estaEnRango_HighestRangeVeryLargeValue_ReturnsTrue() {
        TablaISR tabla = TablaISR.getTabla();
        Rango rango = tabla.buscarRango(10000000.0);
        assertNotNull(rango);
        assertTrue(rango.estaEnRango(10000000.0));
        assertEquals(35.0, rango.getIsr(), 0.001);
    }

    @Test
    public void UT_R_002_estaEnRango_BoundaryBetweenFirstAndSecond_ReturnsFalse() {
        TablaISR tabla = TablaISR.getTabla();
        Rango primerRango = tabla.buscarRango(8364.0);
        assertNotNull(primerRango);
        assertFalse(primerRango.estaEnRango(8364.01));
    }

    @Test
    public void UT_R_003_estaEnRango_BoundaryBetweenSecondAndThird_ReturnsFalse() {
        TablaISR tabla = TablaISR.getTabla();
        Rango segundoRango = tabla.buscarRango(8952.49);
        assertNotNull(segundoRango);
        assertFalse(segundoRango.estaEnRango(8952.50));
    }

    @Test
    public void UT_R_004_estaEnRango_MidRangeValue_ReturnsTrue() {
        TablaISR tabla = TablaISR.getTabla();
        Rango rango = tabla.buscarRango(100000.0);
        assertNotNull(rango);
        assertTrue(rango.estaEnRango(100000.0));
        assertEquals(10.88, rango.getIsr(), 0.001);
    }

    @Test
    public void UT_R_005_estaEnRango_NegativeValue_ReturnsFalse() {
        TablaISR tabla = TablaISR.getTabla();
        Rango primerRango = tabla.buscarRango(0.0);
        assertNotNull(primerRango);
        assertFalse(primerRango.estaEnRango(-1.0));
    }

    @Test
    public void UT_R_006_estaEnRango_TypicalSalaryRange_ReturnsTrue() {
        TablaISR tabla = TablaISR.getTabla();
        // Salario típico de 15,000 mensuales
        Rango rango = tabla.buscarRango(15000.0);
        assertNotNull(rango);
        assertTrue(rango.estaEnRango(15000.0));
        assertEquals(6.40, rango.getIsr(), 0.001);
    }

}