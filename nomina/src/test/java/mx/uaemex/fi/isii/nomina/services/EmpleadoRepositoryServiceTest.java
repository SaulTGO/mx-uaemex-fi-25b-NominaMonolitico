package mx.uaemex.fi.isii.nomina.services;

import mx.uaemex.fi.isii.nomina.domain.entity.Empleado;
import mx.uaemex.fi.isii.nomina.repository.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmpleadoRepositoryServiceTest {

    private EmpleadoRepositoryService empleadoRepositoryService;

    @Mock
    private EmpleadoRepository mockEmpleadoRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        empleadoRepositoryService = new EmpleadoRepositoryService(mockEmpleadoRepository);
    }

    @Test
    public void UT_ERS_001_findAllEmpleados_EmptyDatabase_ReturnsEmptyList() {
        // Arrange
        List<Empleado> emptyList = new ArrayList<>();
        when(mockEmpleadoRepository.findAll()).thenReturn(emptyList);

        // Act
        List<Empleado> result = empleadoRepositoryService.findAllEmpleados();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
        verify(mockEmpleadoRepository, times(1)).findAll();
    }

    @Test
    public void UT_ERS_002_findAllEmpleados_OneEmployee_ReturnsListWithOneElement() {
        // Arrange
        Empleado empleado = new Empleado();
        empleado.setIdempleado(1);
        empleado.setNombre("Juan");
        empleado.setApellidos("Pérez García");
        empleado.setRfc("PEGJ850101ABC");
        empleado.setCorreo("juan.perez@example.com");

        List<Empleado> empleadosList = new ArrayList<>();
        empleadosList.add(empleado);

        when(mockEmpleadoRepository.findAll()).thenReturn(empleadosList);

        // Act
        List<Empleado> result = empleadoRepositoryService.findAllEmpleados();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getNombre());
        assertEquals("Pérez García", result.get(0).getApellidos());
        assertEquals("PEGJ850101ABC", result.get(0).getRfc());
        assertEquals("juan.perez@example.com", result.get(0).getCorreo());
        verify(mockEmpleadoRepository, times(1)).findAll();
    }

    @Test
    public void UT_ERS_003_findAllEmpleados_ArbitraryNumberOfEmployees_ReturnsCorrectList() {
        // Arrange
        int numberOfEmployees = 7; // Número arbitrario de empleados
        List<Empleado> empleadosList = new ArrayList<>();

        // Crear empleados con datos únicos
        for (int i = 1; i <= numberOfEmployees; i++) {
            Empleado empleado = new Empleado();
            empleado.setIdempleado(i);
            empleado.setNombre("Empleado" + i);
            empleado.setApellidos("Apellidos" + i);
            empleado.setRfc("RFC" + String.format("%06d", i) + "ABC");
            empleado.setCorreo("empleado" + i + "@example.com");
            empleadosList.add(empleado);
        }

        when(mockEmpleadoRepository.findAll()).thenReturn(empleadosList);

        // Act
        List<Empleado> result = empleadoRepositoryService.findAllEmpleados();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(numberOfEmployees, result.size());

        // Verificar que todos los empleados están presentes con datos correctos
        for (int i = 0; i < numberOfEmployees; i++) {
            Empleado empleado = result.get(i);
            assertEquals(i + 1, empleado.getIdempleado());
            assertEquals("Empleado" + (i + 1), empleado.getNombre());
            assertEquals("Apellidos" + (i + 1), empleado.getApellidos());
            assertEquals("RFC" + String.format("%06d", i + 1) + "ABC", empleado.getRfc());
            assertEquals("empleado" + (i + 1) + "@example.com", empleado.getCorreo());
        }

        verify(mockEmpleadoRepository, times(1)).findAll();
    }

    /**
     * Método auxiliar para crear listas de empleados de prueba
     * @param count número de empleados a crear
     * @return lista de empleados
     */
    private List<Empleado> createEmpleadosList(int count) {
        List<Empleado> empleadosList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            Empleado empleado = new Empleado();
            empleado.setIdempleado(i);
            empleado.setNombre("Empleado" + i);
            empleado.setApellidos("Apellidos" + i);
            empleado.setRfc("RFC" + String.format("%06d", i) + "ABC");
            empleado.setCorreo("empleado" + i + "@example.com");
            empleadosList.add(empleado);
        }
        return empleadosList;
    }
}