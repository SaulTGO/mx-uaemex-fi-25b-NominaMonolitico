package mx.uaemex.fi.isii.nomina.controller;

import mx.uaemex.fi.isii.nomina.domain.POJO.Nomina;
import mx.uaemex.fi.isii.nomina.domain.entity.Empleado;
import mx.uaemex.fi.isii.nomina.services.CalcularNominaService;
import mx.uaemex.fi.isii.nomina.services.EmpleadoRepositoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CalcularNominaControllerTest {

    private CalcularNominaController calcularNominaController;

    @Mock
    private CalcularNominaService mockCalcularNominaService;

    @Mock
    private EmpleadoRepositoryService mockEmpleadoRepositoryService;

    @Mock
    private Model mockModel;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        calcularNominaController = new CalcularNominaController(
                mockCalcularNominaService,
                mockEmpleadoRepositoryService
        );
    }

    @Test
    public void UT_CNC_001_getCalcularNomina_ValidRequest_ReturnsCorrectView() {
        // Arrange
        List<Empleado> empleados = new ArrayList<>();
        when(mockEmpleadoRepositoryService.findAllEmpleados()).thenReturn(empleados);

        // Act
        String viewName = calcularNominaController.calcularNomina(
                null, null, null, null, null, null, null, null, null, mockModel
        );

        // Assert
        assertEquals("calcular-nomina", viewName);
        verify(mockEmpleadoRepositoryService, times(1)).findAllEmpleados();
    }

    @Test
    public void UT_CNC_002_getCalcularNomina_ValidRequest_AddsEmpleadosToModel() {
        // Arrange
        List<Empleado> empleados = new ArrayList<>();
        Empleado empleado1 = new Empleado();
        empleado1.setIdempleado(1);
        empleado1.setNombre("JUAN");
        empleado1.setApellidos("PEREZ GARCIA");
        empleado1.setRfc("PEGJ850101ABC");
        empleado1.setCorreo("juan.perez@example.com");

        Empleado empleado2 = new Empleado();
        empleado2.setIdempleado(2);
        empleado2.setNombre("MARIA");
        empleado2.setApellidos("LOPEZ MARTINEZ");
        empleado2.setRfc("LOMM900505XYZ");
        empleado2.setCorreo("maria.lopez@example.com");

        empleados.add(empleado1);
        empleados.add(empleado2);

        when(mockEmpleadoRepositoryService.findAllEmpleados()).thenReturn(empleados);

        // Act
        String viewName = calcularNominaController.calcularNomina(
                null, null, null, null, null, null, null, null, null, mockModel
        );

        // Assert
        assertEquals("calcular-nomina", viewName);
        verify(mockModel, times(1)).addAttribute("empleados", empleados);
        verify(mockEmpleadoRepositoryService, times(1)).findAllEmpleados();
    }

    @Test
    public void UT_CNC_003_getCalcularNomina_EmptyEmployeeList_HandlesCorrectly() {
        // Arrange
        List<Empleado> empleadosVacios = new ArrayList<>();
        when(mockEmpleadoRepositoryService.findAllEmpleados()).thenReturn(empleadosVacios);

        // Act
        String viewName = calcularNominaController.calcularNomina(
                null, null, null, null, null, null, null, null, null, mockModel
        );

        // Assert
        assertEquals("calcular-nomina", viewName);
        verify(mockModel, times(1)).addAttribute("empleados", empleadosVacios);
        verify(mockEmpleadoRepositoryService, times(1)).findAllEmpleados();

        // Verificar que la lista está vacía
        assertTrue(empleadosVacios.isEmpty());
    }

    @Test
    public void UT_CNC_004_postCalcularNomina_ValidData_AddsAllAttributesToModel() {
        // Arrange
        String nombre = "CARLOS";
        String apellidos = "RODRIGUEZ SANCHEZ";
        String rfc = "ROSC880312DEF";
        String correo = "carlos.rodriguez@example.com";
        Double montoNomina = 15000.0;

        Nomina nominaCalculada = new Nomina(15000.0, 960.0, 6.40, 14040.0);

        List<Empleado> empleados = new ArrayList<>();
        Empleado empleado = new Empleado();
        empleado.setNombre(nombre);
        empleado.setApellidos(apellidos);
        empleado.setRfc(rfc);
        empleado.setCorreo(correo);
        empleados.add(empleado);

        when(mockCalcularNominaService.calcularNomina(montoNomina)).thenReturn(nominaCalculada);
        when(mockEmpleadoRepositoryService.findAllEmpleados()).thenReturn(empleados);

        // Act
        String viewName = calcularNominaController.calcularNomina(
                null, nombre, apellidos, rfc, correo, montoNomina, mockModel
        );

        // Assert
        assertEquals("calcular-nomina", viewName);

        // Verificar que todos los atributos se agregaron al modelo
        verify(mockModel, times(1)).addAttribute("empleados", empleados);
        verify(mockModel, times(1)).addAttribute("empleadoNombre", nombre);
        verify(mockModel, times(1)).addAttribute("empleadoApellidos", apellidos);
        verify(mockModel, times(1)).addAttribute("empleadoRFC", rfc);
        verify(mockModel, times(1)).addAttribute("empleadoCorreo", correo);
        verify(mockModel, times(1)).addAttribute("montoNomina", nominaCalculada.getMontoNomina());
        verify(mockModel, times(1)).addAttribute("isr", nominaCalculada.getIsr());
        verify(mockModel, times(1)).addAttribute("deducciones", nominaCalculada.getDeducciones());
        verify(mockModel, times(1)).addAttribute("nominaNeto", nominaCalculada.getNominaNeto());
        verify(mockModel, times(1)).addAttribute("mostrarResultados", true);

        // Verificar llamadas a servicios
        verify(mockCalcularNominaService, times(1)).calcularNomina(montoNomina);
        verify(mockEmpleadoRepositoryService, times(1)).findAllEmpleados();
    }

    @Test
    public void UT_CNC_005_postCalcularNomina_ValidData_ReturnsCorrectView() {
        // Arrange
        String nombre = "ANA";
        String apellidos = "MARTINEZ FLORES";
        String rfc = "MAFA920815GHI";
        String correo = "ana.martinez@example.com";
        Double montoNomina = 25000.0;

        Nomina nominaCalculada = new Nomina(25000.0, 1600.0, 6.40, 23400.0);
        List<Empleado> empleados = new ArrayList<>();

        when(mockCalcularNominaService.calcularNomina(montoNomina)).thenReturn(nominaCalculada);
        when(mockEmpleadoRepositoryService.findAllEmpleados()).thenReturn(empleados);

        // Act
        String viewName = calcularNominaController.calcularNomina(
                null, nombre, apellidos, rfc, correo, montoNomina, mockModel
        );

        // Assert
        assertEquals("calcular-nomina", viewName);
        assertNotEquals("redirect:/calcular-nomina", viewName);

        verify(mockCalcularNominaService, times(1)).calcularNomina(montoNomina);
        verify(mockEmpleadoRepositoryService, times(1)).findAllEmpleados();
    }

    @Test
    public void UT_CNC_006_postCalcularNomina_ServiceThrowsException_RedirectsToGetMapping() {
        // Arrange
        String nombre = "ERROR";
        String apellidos = "TEST ERROR";
        String rfc = "TEER900101XYZ";
        String correo = "error@example.com";
        Double montoNomina = -5000.0; // Monto inválido que causará excepción

        when(mockCalcularNominaService.calcularNomina(montoNomina))
                .thenThrow(new RuntimeException("Error al calcular nómina"));

        // Act
        String viewName = calcularNominaController.calcularNomina(
                null, nombre, apellidos, rfc, correo, montoNomina, mockModel
        );

        // Assert
        assertEquals("redirect:/calcular-nomina", viewName);

        // Verificar que se intentó calcular la nómina
        verify(mockCalcularNominaService, times(1)).calcularNomina(montoNomina);

        // Verificar que NO se agregaron atributos al modelo debido a la excepción
        verify(mockModel, never()).addAttribute(eq("empleadoNombre"), anyString());
        verify(mockModel, never()).addAttribute(eq("mostrarResultados"), anyBoolean());
    }

}