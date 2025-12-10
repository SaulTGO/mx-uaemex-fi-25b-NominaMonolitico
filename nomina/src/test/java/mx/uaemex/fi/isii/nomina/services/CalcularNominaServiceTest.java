package mx.uaemex.fi.isii.nomina.services;

import mx.uaemex.fi.isii.nomina.domain.POJO.Nomina;
import mx.uaemex.fi.isii.nomina.util.Rango;
import mx.uaemex.fi.isii.nomina.util.TablaISR;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CalcularNominaServiceTest {

    private CalcularNominaService calcularNominaService;

    @BeforeEach
    public void setUp() {
        calcularNominaService = new CalcularNominaService();
    }

    @Test
    public void UT_CNS_001_calcularNomina_UsesTablaISR_FindsCorrectRange() {
        // Arrange
        double montoNomina1 = 5000.0; // Debe estar en el primer rango (ISR 0%)
        double montoNomina2 = 10000.0; // Debe estar en el tercer rango (ISR 6.40%)
        double montoNomina3 = 200000.0; // Debe estar en el séptimo rango (ISR 21.36%)

        TablaISR tablaISR = TablaISR.getTabla();

        // Act
        Nomina nomina1 = calcularNominaService.calcularNomina(montoNomina1);
        Nomina nomina2 = calcularNominaService.calcularNomina(montoNomina2);
        Nomina nomina3 = calcularNominaService.calcularNomina(montoNomina3);

        // Verificar que TablaISR encuentra los rangos correctos
        Rango rango1 = tablaISR.buscarRango(montoNomina1);
        Rango rango2 = tablaISR.buscarRango(montoNomina2);
        Rango rango3 = tablaISR.buscarRango(montoNomina3);

        // Assert - Verificar que el servicio usa los rangos correctos de TablaISR
        assertNotNull(rango1);
        assertNotNull(rango2);
        assertNotNull(rango3);

        assertEquals(0.0, rango1.getIsr(), 0.001);
        assertEquals(0.0, nomina1.getIsr(), 0.001);

        assertEquals(6.40, rango2.getIsr(), 0.001);
        assertEquals(6.40, nomina2.getIsr(), 0.001);

        assertEquals(21.36, rango3.getIsr(), 0.001);
        assertEquals(21.36, nomina3.getIsr(), 0.001);

        // Verificar que las deducciones se calculan correctamente según el ISR del rango
        assertEquals(montoNomina1 * (0.0 * 0.01), nomina1.getDeducciones(), 0.001);
        assertEquals(montoNomina2 * (6.40 * 0.01), nomina2.getDeducciones(), 0.001);
        assertEquals(montoNomina3 * (21.36 * 0.01), nomina3.getDeducciones(), 0.001);
    }

    @Test
    public void UT_CNS_002_constructor_TablaISRNotNull_AfterConstruction() {
        // Arrange & Act
        CalcularNominaService service = new CalcularNominaService();

        // Assert - Verificar que el servicio puede calcular nóminas (implica que TablaISR está inicializada)
        assertNotNull(service);

        // Realizar un cálculo para verificar que TablaISR está funcionando
        double montoTest = 15000.0;
        Nomina nomina = service.calcularNomina(montoTest);

        // Si TablaISR fuera null, esto lanzaría NullPointerException
        assertNotNull(nomina);
        assertNotNull(nomina.getMontoNomina());

        // Verificar que TablaISR se inicializó correctamente
        // Si TablaISR no estuviera inicializada, buscarRango fallaría
        TablaISR tabla = TablaISR.getTabla();
        assertNotNull(tabla);

        Rango rango = tabla.buscarRango(montoTest);
        assertNotNull(rango);

        // Verificar que el cálculo usa el rango correcto
        assertEquals(rango.getIsr(), nomina.getIsr(), 0.001);
    }

}