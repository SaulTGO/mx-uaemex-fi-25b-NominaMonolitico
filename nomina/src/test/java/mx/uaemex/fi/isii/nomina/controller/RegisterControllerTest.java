package mx.uaemex.fi.isii.nomina.controller;

import mx.uaemex.fi.isii.nomina.domain.entity.Empleado;
import mx.uaemex.fi.isii.nomina.services.RegisterService;
import mx.uaemex.fi.isii.nomina.util.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class RegisterControllerTest {

    private RegisterController registerController;

    @Mock
    private RegisterService mockRegisterService;

    @Mock
    private Model mockModel;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        registerController = new RegisterController(mockRegisterService);
    }

    @Test
    public void UT_RC_001_getRegister_WithSessionCookie_ReturnsView() {
        // Arrange
        String sessionValue = "admin@example.com";

        // Act
        String viewName = registerController.register(sessionValue);

        // Assert
        assertEquals("register", viewName);

        // Verificar que no se interactúa con ningún servicio en el GET
        verifyNoInteractions(mockRegisterService);
    }

    @Test
    public void UT_RC_002_getRegister_WithoutSessionCookie_ReturnsView() {
        // Arrange
        String sessionValue = null;

        // Act
        String viewName = registerController.register(sessionValue);

        // Assert
        assertEquals("register", viewName);

        // No debería haber interacción con servicios
        verifyNoInteractions(mockRegisterService);
    }

    @Test
    public void UT_RC_003_postRegister_ValidDataNoAdmin_SavesEmpleadoOnly() {
        // Arrange
        String nombre = "JUAN";
        String apellidos = "PEREZ GARCIA";
        String rfc = "PEGJ850101ABC";
        String correo = "juan.perez@example.com";
        Boolean esAdministrador = null;
        String password = null;

        Valid validResult = new Valid(true, "");
        Empleado empleado = new Empleado();
        empleado.setNombre(nombre);
        empleado.setApellidos(apellidos);
        empleado.setRfc(rfc);
        empleado.setCorreo(correo);

        when(mockRegisterService.ValidateRegisterParams(nombre, apellidos, rfc, correo, esAdministrador, password))
                .thenReturn(validResult);
        when(mockRegisterService.createEmpleado(nombre, apellidos, rfc, correo))
                .thenReturn(empleado);

        // Act
        String viewName = registerController.register(nombre, apellidos, rfc, correo, esAdministrador, password, mockModel, null);

        // Assert
        assertEquals("register", viewName);

        // Verificar que se validaron los datos
        verify(mockRegisterService, times(1)).ValidateRegisterParams(nombre, apellidos, rfc, correo, esAdministrador, password);

        // Verificar que se creó el empleado
        verify(mockRegisterService, times(1)).createEmpleado(nombre, apellidos, rfc, correo);

        // Verificar que SOLO se guardó el empleado (no el usuario)
        verify(mockRegisterService, times(1)).saveEmpleado(empleado);
        verify(mockRegisterService, never()).saveUsuario(any(Empleado.class), anyString());

        // Verificar mensaje de éxito
        verify(mockModel, times(1)).addAttribute("message", "Registrado correctamente");
        verify(mockModel, never()).addAttribute(eq("messageerror"), anyString());
    }

    @Test
    public void UT_RC_004_postRegister_ValidDataWithAdmin_SavesEmpleadoAndUsuario() {
        // Arrange
        String nombre = "MARIA";
        String apellidos = "LOPEZ MARTINEZ";
        String rfc = "LOMM900505XYZ";
        String correo = "maria.lopez@example.com";
        Boolean esAdministrador = true;
        String password = "AdminPass1@";

        Valid validResult = new Valid(true, "");
        Empleado empleado = new Empleado();
        empleado.setNombre(nombre);
        empleado.setApellidos(apellidos);
        empleado.setRfc(rfc);
        empleado.setCorreo(correo);

        when(mockRegisterService.ValidateRegisterParams(nombre, apellidos, rfc, correo, esAdministrador, password))
                .thenReturn(validResult);
        when(mockRegisterService.createEmpleado(nombre, apellidos, rfc, correo))
                .thenReturn(empleado);

        // Act
        String viewName = registerController.register(nombre, apellidos, rfc, correo, esAdministrador, password, mockModel, null);

        // Assert
        assertEquals("register", viewName);

        // Verificar que se validaron los datos
        verify(mockRegisterService, times(1)).ValidateRegisterParams(nombre, apellidos, rfc, correo, esAdministrador, password);

        // Verificar que se creó el empleado
        verify(mockRegisterService, times(1)).createEmpleado(nombre, apellidos, rfc, correo);

        // Verificar que se guardaron AMBOS: empleado Y usuario
        verify(mockRegisterService, times(1)).saveEmpleado(empleado);
        verify(mockRegisterService, times(1)).saveUsuario(empleado, password);

        // Verificar mensaje de éxito
        verify(mockModel, times(1)).addAttribute("message", "Registrado correctamente");
        verify(mockModel, never()).addAttribute(eq("messageerror"), anyString());
    }

    @Test
    public void UT_RC_005_postRegister_ValidationFails_AddsErrorToModel() {
        // Arrange
        String nombre = "ab"; // Nombre muy corto - inválido
        String apellidos = "GARCIA LOPEZ";
        String rfc = "GALO950101ABC";
        String correo = "test@example.com";
        Boolean esAdministrador = null;
        String password = null;

        Valid validResult = new Valid(false, "Longitud de Nombre demasiado corta");

        when(mockRegisterService.ValidateRegisterParams(nombre, apellidos, rfc, correo, esAdministrador, password))
                .thenReturn(validResult);

        // Act
        String viewName = registerController.register(nombre, apellidos, rfc, correo, esAdministrador, password, mockModel, null);

        // Assert
        assertEquals("register", viewName);

        // Verificar que se agregó el mensaje de error al modelo
        verify(mockModel, times(1)).addAttribute("messageerror", "Longitud de Nombre demasiado corta");

        // Verificar que NO se agregó mensaje de éxito
        verify(mockModel, never()).addAttribute(eq("message"), anyString());

        // Verificar que se llamó a la validación
        verify(mockRegisterService, times(1)).ValidateRegisterParams(nombre, apellidos, rfc, correo, esAdministrador, password);
    }

    @Test
    public void UT_RC_006_postRegister_ValidationFails_DoesNotSaveAnything() {
        // Arrange
        String nombre = "CARLOS";
        String apellidos = "xy"; // Apellidos muy cortos - inválidos
        String rfc = "CAXY880312DEF";
        String correo = "carlos@example.com";
        Boolean esAdministrador = true;
        String password = "ValidPass1@";

        Valid validResult = new Valid(false, "Longitud de Apellidos demasiado corta");

        when(mockRegisterService.ValidateRegisterParams(nombre, apellidos, rfc, correo, esAdministrador, password))
                .thenReturn(validResult);

        // Act
        String viewName = registerController.register(nombre, apellidos, rfc, correo, esAdministrador, password, mockModel, null);

        // Assert
        assertEquals("register", viewName);

        // Verificar que se validaron los datos
        verify(mockRegisterService, times(1)).ValidateRegisterParams(nombre, apellidos, rfc, correo, esAdministrador, password);

        // Verificar que NO se creó el empleado
        verify(mockRegisterService, never()).createEmpleado(anyString(), anyString(), anyString(), anyString());

        // Verificar que NO se guardó nada
        verify(mockRegisterService, never()).saveEmpleado(any(Empleado.class));
        verify(mockRegisterService, never()).saveUsuario(any(Empleado.class), anyString());

        // Verificar que se agregó error y NO mensaje de éxito
        verify(mockModel, times(1)).addAttribute(eq("messageerror"), anyString());
        verify(mockModel, never()).addAttribute(eq("message"), anyString());
    }

    @Test
    public void UT_RC_007_postRegister_SaveEmpleadoThrowsException_RedirectsToRegister() {
        // Arrange
        String nombre = "ANA";
        String apellidos = "MARTINEZ FLORES";
        String rfc = "MAFA920815GHI";
        String correo = "ana.martinez@example.com";
        Boolean esAdministrador = null;
        String password = null;

        Valid validResult = new Valid(true, "");
        Empleado empleado = new Empleado();
        empleado.setNombre(nombre);
        empleado.setApellidos(apellidos);
        empleado.setRfc(rfc);
        empleado.setCorreo(correo);

        when(mockRegisterService.ValidateRegisterParams(nombre, apellidos, rfc, correo, esAdministrador, password))
                .thenReturn(validResult);
        when(mockRegisterService.createEmpleado(nombre, apellidos, rfc, correo))
                .thenReturn(empleado);

        // Simular excepción al guardar empleado
        doThrow(new RuntimeException("Error de base de datos"))
                .when(mockRegisterService).saveEmpleado(empleado);

        // Act
        String viewName = registerController.register(nombre, apellidos, rfc, correo, esAdministrador, password, mockModel, null);

        // Assert
        assertEquals("redirect:/register", viewName);

        // Verificar que se intentó validar y crear el empleado
        verify(mockRegisterService, times(1)).ValidateRegisterParams(nombre, apellidos, rfc, correo, esAdministrador, password);
        verify(mockRegisterService, times(1)).createEmpleado(nombre, apellidos, rfc, correo);
        verify(mockRegisterService, times(1)).saveEmpleado(empleado);

        // Verificar que se agregó mensaje de error
        verify(mockModel, times(1)).addAttribute(eq("messageerror"), anyString());

        // Verificar que NO se agregó mensaje de éxito
        verify(mockModel, never()).addAttribute(eq("message"), anyString());
    }

}