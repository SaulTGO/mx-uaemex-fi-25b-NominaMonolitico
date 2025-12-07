package mx.uaemex.fi.isii.nomina.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas Unitarias - TablaISR.buscarRango()")
class TablaISRTest {

    private TablaISR tablaISR;

    @BeforeEach
    void setUp() {
        tablaISR = TablaISR.getTabla();
    }

    // ========== PRUEBAS POSITIVAS - VALORES EN LÍMITE INFERIOR ==========

    @Test
    @DisplayName("UT-TISR-001: Buscar rango con nómina en límite inferior del Rango 1 (0.0)")
    void UTTISR001_buscarRango_lowerLimitRange1_0PercentISR() {
        // Arrange
        double nomina = 0.0;
        double isrEsperado = 0.0;

        // Act
        Rango resultado = tablaISR.buscarRango(nomina);

        // Assert
        assertNotNull(resultado, "El rango no debe ser null");
        assertEquals(isrEsperado, resultado.getIsr(), 0.01, "El ISR debe ser 0%");
    }

    @Test
    @DisplayName("UT-TISR-002: Buscar rango con nómina en límite inferior del Rango 2 (8364.1)")
    void UTTISR002_buscarRango_lowerLimitRange2_1Point92PercentISR() {
        // Arrange
        double nomina = 8364.1;
        double isrEsperado = 1.92;

        // Act
        Rango resultado = tablaISR.buscarRango(nomina);

        // Assert
        assertNotNull(resultado);
        assertEquals(isrEsperado, resultado.getIsr(), 0.01);
    }

    @Test
    @DisplayName("UT-TISR-003: Buscar rango con nómina en límite inferior del Rango 3 (8952.50)")
    void UTTISR003_buscarRango_lowerLimitRange3_6Point40PercentISR() {
        // Arrange
        double nomina = 8952.50;
        double isrEsperado = 6.40;

        // Act
        Rango resultado = tablaISR.buscarRango(nomina);

        // Assert
        assertNotNull(resultado);
        assertEquals(isrEsperado, resultado.getIsr(), 0.01);
    }

    // ========== PRUEBAS POSITIVAS - VALORES EN LÍMITE SUPERIOR ==========

    @Test
    @DisplayName("UT-TISR-004: Buscar rango con nómina en límite superior del Rango 1 (8364.0)")
    void UTTISR004_buscarRango_upperLimitRange1_0PercentISR() {
        // Arrange
        double nomina = 8364.0;
        double isrEsperado = 0.0;

        // Act
        Rango resultado = tablaISR.buscarRango(nomina);

        // Assert
        assertNotNull(resultado);
        assertEquals(isrEsperado, resultado.getIsr(), 0.01);
    }

    @Test
    @DisplayName("UT-TISR-005: Buscar rango con nómina en límite superior del Rango 2 (8952.49)")
    void UTTISR005_buscarRango_upperLimitRange2_1Point92PercentISR() {
        // Arrange
        double nomina = 8952.49;
        double isrEsperado = 1.92;

        // Act
        Rango resultado = tablaISR.buscarRango(nomina);

        // Assert
        assertNotNull(resultado);
        assertEquals(isrEsperado, resultado.getIsr(), 0.01);
    }

    // ========== PRUEBAS POSITIVAS - VALORES MEDIOS ==========

    @Test
    @DisplayName("UT-TISR-006: Buscar rango con nómina en valor medio del Rango 1 (5000.0)")
    void UTTISR006_buscarRango_middleValueRange1_0PercentISR() {
        // Arrange
        double nomina = 5000.0;
        double isrEsperado = 0.0;

        // Act
        Rango resultado = tablaISR.buscarRango(nomina);

        // Assert
        assertNotNull(resultado);
        assertEquals(isrEsperado, resultado.getIsr(), 0.01);
    }

    @Test
    @DisplayName("UT-TISR-007: Buscar rango con nómina en valor medio del Rango 3 (50000.0)")
    void UTTISR007_buscarRango_middleValueRange3_6Point40PercentISR() {
        // Arrange
        double nomina = 50000.0;
        double isrEsperado = 6.40;

        // Act
        Rango resultado = tablaISR.buscarRango(nomina);

        // Assert
        assertNotNull(resultado);
        assertEquals(isrEsperado, resultado.getIsr(), 0.01);
    }

    @Test
    @DisplayName("UT-TISR-008: Buscar rango con nómina en valor medio del Rango 4 (100000.0)")
    void UTTISR008_buscarRango_middleValueRange4_10Point88PercentISR() {
        // Arrange
        double nomina = 100000.0;
        double isrEsperado = 10.88;

        // Act
        Rango resultado = tablaISR.buscarRango(nomina);

        // Assert
        assertNotNull(resultado);
        assertEquals(isrEsperado, resultado.getIsr(), 0.01);
    }

    @Test
    @DisplayName("UT-TISR-009: Buscar rango con nómina en Rango 9 (800000.0)")
    void UTTISR009_buscarRango_range9Value_30PercentISR() {
        // Arrange
        double nomina = 800000.0;
        double isrEsperado = 30.00;

        // Act
        Rango resultado = tablaISR.buscarRango(nomina);

        // Assert
        assertNotNull(resultado);
        assertEquals(isrEsperado, resultado.getIsr(), 0.01);
    }

    @Test
    @DisplayName("UT-TISR-010: Buscar rango con nómina en último rango (5000000.0)")
    void UTTISR010_buscarRango_lastRangeValue_35PercentISR() {
        // Arrange
        double nomina = 5000000.0;
        double isrEsperado = 35.00;

        // Act
        Rango resultado = tablaISR.buscarRango(nomina);

        // Assert
        assertNotNull(resultado);
        assertEquals(isrEsperado, resultado.getIsr(), 0.01);
    }

    // ========== PRUEBA DE SINGLETON ==========

    @Test
    @DisplayName("UT-TISR-011: Verificar que getTabla() retorna la misma instancia (Singleton)")
    void UTTISR015_getTabla_singletonPattern_True() {
        // Arrange & Act
        TablaISR tabla1 = TablaISR.getTabla();
        TablaISR tabla2 = TablaISR.getTabla();

        // Assert
        assertSame(tabla1, tabla2, "getTabla() debe retornar la misma instancia (Singleton)");
        assertNotNull(tabla1);
        assertNotNull(tabla2);
    }


}