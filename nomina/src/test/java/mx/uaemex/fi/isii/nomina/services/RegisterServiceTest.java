package mx.uaemex.fi.isii.nomina.services;

import mx.uaemex.fi.isii.nomina.domain.entity.Empleado;
import mx.uaemex.fi.isii.nomina.domain.entity.Usuario;
import mx.uaemex.fi.isii.nomina.repository.EmpleadoRepository;
import mx.uaemex.fi.isii.nomina.repository.UsuarioRepository;
import mx.uaemex.fi.isii.nomina.util.Valid;
import mx.uaemex.fi.isii.nomina.util.ValidateRegisterData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RegisterServiceTest {

    private RegisterService registerService;

    @Mock
    private EmpleadoRepository mockEmpleadoRepository;

    @Mock
    private UsuarioRepository mockUsuarioRepository;

    @Mock
    private ValidateRegisterData mockValidateRegisterData;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        registerService = new RegisterService(mockEmpleadoRepository, mockUsuarioRepository, mockValidateRegisterData);
    }

    @Test
    public void UT_RS_001_saveUsuario_ValidEmpleadoAndPassword_SavesSuccessfully() {
        // Arrange
        Empleado empleado = new Empleado();
        empleado.setNombre("JUAN");
        empleado.setApellidos("PEREZ GARCIA");
        empleado.setRfc("PEGJ850101ABC");
        empleado.setCorreo("juan.perez@example.com");
        String password = "Password123!";

        when(mockUsuarioRepository.save(any(Usuario.class))).thenReturn(new Usuario());

        // Act
        registerService.saveUsuario(empleado, password);

        // Assert
        verify(mockUsuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    public void UT_RS_002_saveUsuario_CreatesUsuarioWithCorrectEmpleado_AssociatesCorrectly() {
        // Arrange
        Empleado empleado = new Empleado();
        empleado.setIdempleado(1);
        empleado.setNombre("MARIA");
        empleado.setApellidos("LOPEZ MARTINEZ");
        empleado.setRfc("LOMM900505XYZ");
        empleado.setCorreo("maria.lopez@example.com");
        String password = "SecurePass1@";

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);

        // Act
        registerService.saveUsuario(empleado, password);

        // Assert
        verify(mockUsuarioRepository, times(1)).save(usuarioCaptor.capture());
        Usuario capturedUsuario = usuarioCaptor.getValue();

        assertNotNull(capturedUsuario);
        assertEquals(empleado, capturedUsuario.getEmpleado());
        assertEquals(password, capturedUsuario.getPassword());
        assertEquals("MARIA", capturedUsuario.getEmpleado().getNombre());
        assertEquals("LOPEZ MARTINEZ", capturedUsuario.getEmpleado().getApellidos());
    }

    @Test
    public void UT_RS_003_saveEmpleado_ValidEmpleado_SavesSuccessfully() {
        // Arrange
        Empleado empleado = new Empleado();
        empleado.setNombre("CARLOS");
        empleado.setApellidos("RODRIGUEZ SANCHEZ");
        empleado.setRfc("ROSC880312DEF");
        empleado.setCorreo("carlos.rodriguez@example.com");

        when(mockEmpleadoRepository.save(any(Empleado.class))).thenReturn(empleado);

        // Act
        registerService.saveEmpleado(empleado);

        // Assert
        verify(mockEmpleadoRepository, times(1)).save(empleado);
    }

    @Test
    public void UT_RS_004_createEmpleado_ValidData_ReturnsEmpleadoWithCorrectFields() {
        // Arrange
        String nombre = "ANA";
        String apellidos = "MARTINEZ FLORES";
        String rfc = "MAFA920815GHI";
        String correo = "ana.martinez@example.com";

        // Act
        Empleado empleado = registerService.createEmpleado(nombre, apellidos, rfc, correo);

        // Assert
        assertNotNull(empleado);
        assertEquals(nombre, empleado.getNombre());
        assertEquals(apellidos, empleado.getApellidos());
        assertEquals(rfc, empleado.getRfc());
        assertEquals(correo, empleado.getCorreo());
    }

    @Test
    public void UT_RS_005_validateRegisterParams_AllValidData_ReturnsValidTrue() {
        // Arrange
        String nombre = "PEDRO";
        String apellidos = "GOMEZ RAMIREZ";
        String rfc = "GORP950220JKL";
        String correo = "pedro.gomez@example.com";
        Boolean isAdmin = true;
        String password = "ValidPass1@";

        ValidateRegisterData validData = new ValidateRegisterData(true, "", "");
        when(mockValidateRegisterData.checkRegisterData(nombre, apellidos, rfc, correo, isAdmin, password))
                .thenReturn(validData);

        // Act
        Valid result = registerService.ValidateRegisterParams(nombre, apellidos, rfc, correo, isAdmin, password);

        // Assert
        assertTrue(result.getIsValid());
        assertEquals("", result.getError());
        verify(mockValidateRegisterData, times(1)).checkRegisterData(nombre, apellidos, rfc, correo, isAdmin, password);
    }

    @Test
    public void UT_RS_006_validateRegisterParams_InvalidNombre_ReturnsValidFalse() {
        // Arrange
        String nombre = "pe"; // Muy corto
        String apellidos = "GOMEZ RAMIREZ";
        String rfc = "GORP950220JKL";
        String correo = "pedro.gomez@example.com";
        Boolean isAdmin = false;
        String password = null;

        ValidateRegisterData validData = new ValidateRegisterData(false, nombre, "Longitud de Nombre demasiado corta");
        when(mockValidateRegisterData.checkRegisterData(nombre, apellidos, rfc, correo, isAdmin, password))
                .thenReturn(validData);

        // Act
        Valid result = registerService.ValidateRegisterParams(nombre, apellidos, rfc, correo, isAdmin, password);

        // Assert
        assertFalse(result.getIsValid());
        assertEquals("Longitud de Nombre demasiado corta", result.getError());
    }

    @Test
    public void UT_RS_007_validateRegisterParams_InvalidApellidos_ReturnsValidFalse() {
        // Arrange
        String nombre = "PEDRO";
        String apellidos = "go"; // Muy corto
        String rfc = "GORP950220JKL";
        String correo = "pedro.gomez@example.com";
        Boolean isAdmin = null;
        String password = null;

        ValidateRegisterData validData = new ValidateRegisterData(false, apellidos, "Longitud de Apellidos demasiado corta");
        when(mockValidateRegisterData.checkRegisterData(nombre, apellidos, rfc, correo, isAdmin, password))
                .thenReturn(validData);

        // Act
        Valid result = registerService.ValidateRegisterParams(nombre, apellidos, rfc, correo, isAdmin, password);

        // Assert
        assertFalse(result.getIsValid());
        assertEquals("Longitud de Apellidos demasiado corta", result.getError());
    }

    @Test
    public void UT_RS_008_validateRegisterParams_InvalidRfc_ReturnsValidFalse() {
        // Arrange
        String nombre = "PEDRO";
        String apellidos = "GOMEZ RAMIREZ";
        String rfc = "INVALID"; // RFC inválido
        String correo = "pedro.gomez@example.com";
        Boolean isAdmin = null;
        String password = null;

        ValidateRegisterData validData = new ValidateRegisterData(false, rfc, "Formato de RFC incorrecto");
        when(mockValidateRegisterData.checkRegisterData(nombre, apellidos, rfc, correo, isAdmin, password))
                .thenReturn(validData);

        // Act
        Valid result = registerService.ValidateRegisterParams(nombre, apellidos, rfc, correo, isAdmin, password);

        // Assert
        assertFalse(result.getIsValid());
        assertEquals("Formato de RFC incorrecto", result.getError());
    }

    @Test
    public void UT_RS_009_validateRegisterParams_InvalidCorreo_ReturnsValidFalse() {
        // Arrange
        String nombre = "PEDRO";
        String apellidos = "GOMEZ RAMIREZ";
        String rfc = "GORP950220JKL";
        String correo = "correo_invalido"; // Correo sin formato válido
        Boolean isAdmin = null;
        String password = null;

        ValidateRegisterData validData = new ValidateRegisterData(false, correo, "Formato de correo incorrecto");
        when(mockValidateRegisterData.checkRegisterData(nombre, apellidos, rfc, correo, isAdmin, password))
                .thenReturn(validData);

        // Act
        Valid result = registerService.ValidateRegisterParams(nombre, apellidos, rfc, correo, isAdmin, password);

        // Assert
        assertFalse(result.getIsValid());
        assertEquals("Formato de correo incorrecto", result.getError());
    }

    @Test
    public void UT_RS_010_validateRegisterParams_InvalidPassword_ReturnsValidFalse() {
        // Arrange
        String nombre = "PEDRO";
        String apellidos = "GOMEZ RAMIREZ";
        String rfc = "GORP950220JKL";
        String correo = "pedro.gomez@example.com";
        Boolean isAdmin = true;
        String password = "weak"; // Contraseña débil

        ValidateRegisterData validData = new ValidateRegisterData(false, password, "Formato de password incorrecto");
        when(mockValidateRegisterData.checkRegisterData(nombre, apellidos, rfc, correo, isAdmin, password))
                .thenReturn(validData);

        // Act
        Valid result = registerService.ValidateRegisterParams(nombre, apellidos, rfc, correo, isAdmin, password);

        // Assert
        assertFalse(result.getIsValid());
        assertEquals("Formato de password incorrecto", result.getError());
    }

    @Test
    public void UT_RS_011_createAndSaveEmpleado_ValidData_WorksCorrectly() {
        // Arrange
        String nombre = "LAURA";
        String apellidos = "HERNANDEZ CRUZ";
        String rfc = "HECL930615MNO";
        String correo = "laura.hernandez@example.com";

        when(mockEmpleadoRepository.save(any(Empleado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Empleado empleadoCreado = registerService.createEmpleado(nombre, apellidos, rfc, correo);
        registerService.saveEmpleado(empleadoCreado);

        // Assert
        assertNotNull(empleadoCreado);
        assertEquals(nombre, empleadoCreado.getNombre());
        assertEquals(apellidos, empleadoCreado.getApellidos());
        assertEquals(rfc, empleadoCreado.getRfc());
        assertEquals(correo, empleadoCreado.getCorreo());
        verify(mockEmpleadoRepository, times(1)).save(empleadoCreado);
    }

    @Test
    public void UT_RS_012_createEmpleadoAndSaveUsuario_ValidData_WorksCorrectly() {
        // Arrange
        String nombre = "ROBERTO";
        String apellidos = "DIAZ MORALES";
        String rfc = "DIMR880420PQR";
        String correo = "roberto.diaz@example.com";
        String password = "StrongPass1@";

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);

        // Act
        Empleado empleadoCreado = registerService.createEmpleado(nombre, apellidos, rfc, correo);
        registerService.saveUsuario(empleadoCreado, password);

        // Assert
        assertNotNull(empleadoCreado);
        verify(mockUsuarioRepository, times(1)).save(usuarioCaptor.capture());

        Usuario capturedUsuario = usuarioCaptor.getValue();
        assertEquals(empleadoCreado, capturedUsuario.getEmpleado());
        assertEquals(password, capturedUsuario.getPassword());
        assertEquals(nombre, capturedUsuario.getEmpleado().getNombre());
    }

    @Test
    public void UT_RS_013_fullRegistrationFlow_ValidDataWithAdmin_CompletesSuccessfully() {
        // Arrange
        String nombre = "SOFIA";
        String apellidos = "VARGAS ORTIZ";
        String rfc = "VAOS910725STU";
        String correo = "sofia.vargas@example.com";
        Boolean isAdmin = true;
        String password = "AdminPass1@";

        ValidateRegisterData validData = new ValidateRegisterData(true, "", "");
        when(mockValidateRegisterData.checkRegisterData(nombre, apellidos, rfc, correo, isAdmin, password))
                .thenReturn(validData);
        when(mockEmpleadoRepository.save(any(Empleado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Valid validationResult = registerService.ValidateRegisterParams(nombre, apellidos, rfc, correo, isAdmin, password);
        assertTrue(validationResult.getIsValid());

        Empleado empleado = registerService.createEmpleado(nombre, apellidos, rfc, correo);
        registerService.saveEmpleado(empleado);
        registerService.saveUsuario(empleado, password);

        // Assert
        verify(mockValidateRegisterData, times(1)).checkRegisterData(nombre, apellidos, rfc, correo, isAdmin, password);
        verify(mockEmpleadoRepository, times(1)).save(any(Empleado.class));
        verify(mockUsuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    public void UT_RS_014_saveUsuario_RepositoryThrowsException_PropagatesException() {
        // Arrange
        Empleado empleado = new Empleado();
        empleado.setNombre("TEST");
        empleado.setApellidos("TEST APELLIDOS");
        empleado.setRfc("TEST850101ABC");
        empleado.setCorreo("test@example.com");
        String password = "TestPass1@";

        when(mockUsuarioRepository.save(any(Usuario.class)))
                .thenThrow(new RuntimeException("Database connection error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            registerService.saveUsuario(empleado, password);
        });

        verify(mockUsuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    public void UT_RS_015_saveEmpleado_RepositoryThrowsException_PropagatesException() {
        // Arrange
        Empleado empleado = new Empleado();
        empleado.setNombre("ERROR");
        empleado.setApellidos("TEST ERROR");
        empleado.setRfc("TEER900101XYZ");
        empleado.setCorreo("error@example.com");

        when(mockEmpleadoRepository.save(any(Empleado.class)))
                .thenThrow(new RuntimeException("Database constraint violation"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            registerService.saveEmpleado(empleado);
        });

        verify(mockEmpleadoRepository, times(1)).save(empleado);
    }

    @Test
    public void UT_RS_016_validateRegisterParams_MultipleInvalidFields_ReturnsFirstError() {
        // Arrange - Múltiples campos inválidos
        String nombre = "ab"; // Nombre muy corto
        String apellidos = "xy"; // Apellidos muy cortos
        String rfc = "INVALID"; // RFC inválido
        String correo = "invalid"; // Correo inválido
        Boolean isAdmin = true;
        String password = "weak"; // Password débil

        // El validador debe retornar el PRIMER error que encuentra (nombre)
        ValidateRegisterData validData = new ValidateRegisterData(false, nombre, "Longitud de Nombre demasiado corta");
        when(mockValidateRegisterData.checkRegisterData(nombre, apellidos, rfc, correo, isAdmin, password))
                .thenReturn(validData);

        // Act
        Valid result = registerService.ValidateRegisterParams(nombre, apellidos, rfc, correo, isAdmin, password);

        // Assert
        assertFalse(result.getIsValid());
        assertEquals("Longitud de Nombre demasiado corta", result.getError());

        // Verificar que solo se reporta el primer error, no todos
        verify(mockValidateRegisterData, times(1)).checkRegisterData(nombre, apellidos, rfc, correo, isAdmin, password);
    }
}