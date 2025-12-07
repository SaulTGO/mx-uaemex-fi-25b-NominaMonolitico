package mx.uaemex.fi.isii.nomina.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import mx.uaemex.fi.isii.nomina.services.UsuarioRepositoryService;
import mx.uaemex.fi.isii.nomina.util.CookieManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class LoginControllerTest {

    private LoginController loginController;

    @Mock
    private CookieManager mockCookieManager;

    @Mock
    private UsuarioRepositoryService mockUsuarioRepositoryService;

    @Mock
    private Model mockModel;

    @Mock
    private HttpServletResponse mockResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        loginController = new LoginController(mockCookieManager, mockUsuarioRepositoryService);
    }

    @Test
    public void UT_LC_001_getLogin_ValidRequest_ReturnsLoginView() {

        // Act
        String viewName = loginController.login();

        // Assert
        assertEquals("login", viewName);
    }

    @Test
    public void UT_LC_002_postLogin_ValidCredentials_RedirectsToHome() {
        // Arrange
        String correo = "admin@example.com";
        String password = "AdminPass1@";
        Cookie mockCookie = new Cookie("session", correo);

        when(mockUsuarioRepositoryService.validateLogin(correo, password)).thenReturn(true);
        when(mockCookieManager.createCookie("session", correo)).thenReturn(mockCookie);

        // Act
        String viewName = loginController.processLogin(correo, password, mockModel, null, mockResponse);

        // Assert
        assertEquals("redirect:/home", viewName);
        verify(mockUsuarioRepositoryService, times(1)).validateLogin(correo, password);
        verify(mockCookieManager, times(1)).createCookie("session", correo);
        verify(mockResponse, times(1)).addCookie(mockCookie);
    }

    @Test
    public void UT_LC_003_postLogin_ValidCredentials_CreatesCookie() {
        // Arrange
        String correo = "usuario@example.com";
        String password = "Password123!";
        Cookie mockCookie = new Cookie("session", correo);

        when(mockUsuarioRepositoryService.validateLogin(correo, password)).thenReturn(true);
        when(mockCookieManager.createCookie("session", correo)).thenReturn(mockCookie);

        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);

        // Act
        String viewName = loginController.processLogin(correo, password, mockModel, null, mockResponse);

        // Assert
        assertEquals("redirect:/home", viewName);
        verify(mockCookieManager, times(1)).createCookie("session", correo);
        verify(mockResponse, times(1)).addCookie(cookieCaptor.capture());

        Cookie capturedCookie = cookieCaptor.getValue();
        assertNotNull(capturedCookie);
        assertEquals("session", capturedCookie.getName());
        assertEquals(correo, capturedCookie.getValue());
    }

    @Test
    public void UT_LC_004_postLogin_InvalidCredentials_ReturnsLoginView() {
        // Arrange
        String correo = "invalid@example.com";
        String password = "wrongpassword";

        when(mockUsuarioRepositoryService.validateLogin(correo, password)).thenReturn(false);

        // Act
        String viewName = loginController.processLogin(correo, password, mockModel, null, mockResponse);

        // Assert
        assertEquals("login", viewName);
        assertNotEquals("redirect:/home", viewName);

        verify(mockUsuarioRepositoryService, times(1)).validateLogin(correo, password);
        verify(mockCookieManager, never()).createCookie(anyString(), anyString());
        verify(mockResponse, never()).addCookie(any(Cookie.class));
    }

    @Test
    public void UT_LC_005_postLogin_CallsValidateLogin_Once() {
        // Arrange
        String correo = "test@example.com";
        String password = "TestPass1@";

        when(mockUsuarioRepositoryService.validateLogin(correo, password)).thenReturn(true);
        when(mockCookieManager.createCookie(anyString(), anyString())).thenReturn(new Cookie("session", correo));

        // Act
        loginController.processLogin(correo, password, mockModel, null, mockResponse);

        // Assert
        verify(mockUsuarioRepositoryService, times(1)).validateLogin(correo, password);
        verify(mockUsuarioRepositoryService, times(1)).validateLogin(anyString(), anyString());
    }

    @Test
    public void UT_LC_006_loginFlow_SuccessfulLogin_CompletesCorrectly() {
        // Arrange - Flujo completo de login exitoso
        String correo = "empleado@example.com";
        String password = "SecurePass1@";
        String sessionToken = null;
        Cookie sessionCookie = new Cookie("session", correo);

        when(mockUsuarioRepositoryService.validateLogin(correo, password)).thenReturn(true);
        when(mockCookieManager.createCookie("session", correo)).thenReturn(sessionCookie);

        // Act - Paso 1: Validar credenciales
        boolean isValid = mockUsuarioRepositoryService.validateLogin(correo, password);
        assertTrue(isValid, "Las credenciales deben ser válidas");

        // Paso 2: Crear cookie
        Cookie cookie = mockCookieManager.createCookie("session", correo);
        assertNotNull(cookie, "La cookie debe ser creada");

        // Paso 3: Procesar login completo
        String viewName = loginController.processLogin(correo, password, mockModel, sessionToken, mockResponse);

        // Assert - Verificar flujo completo
        assertEquals("redirect:/home", viewName);

        // Verificar orden de operaciones
        verify(mockUsuarioRepositoryService, times(2)).validateLogin(correo, password); // Una directa, una en el controlador
        verify(mockCookieManager, times(2)).createCookie("session", correo); // Una directa, una en el controlador
        verify(mockResponse, times(1)).addCookie(sessionCookie);

        // Verificar que NO se agregó error al modelo
        verify(mockModel, never()).addAttribute(eq("loginError"), anyString());

        // Verificar propiedades de la cookie
        assertEquals("session", sessionCookie.getName());
        assertEquals(correo, sessionCookie.getValue());
    }

    @Test
    public void UT_LC_007_loginFlow_FailedLogin_HandlesCorrectly() {
        // Arrange - Flujo completo de login fallido
        String correo = "noexiste@example.com";
        String password = "wrongpassword";
        String sessionToken = null;

        when(mockUsuarioRepositoryService.validateLogin(correo, password)).thenReturn(false);

        // Act - Paso 1: Validar credenciales (fallan)
        boolean isValid = mockUsuarioRepositoryService.validateLogin(correo, password);
        assertFalse(isValid, "Las credenciales deben ser inválidas");

        // Paso 2: Procesar login completo
        String viewName = loginController.processLogin(correo, password, mockModel, sessionToken, mockResponse);

        // Assert - Verificar flujo completo de fallo
        assertEquals("login", viewName);
        assertNotEquals("redirect:/home", viewName);

        // Verificar que se validó el login
        verify(mockUsuarioRepositoryService, times(2)).validateLogin(correo, password); // Una directa, una en el controlador

        // Verificar que NO se creó cookie
        verify(mockCookieManager, never()).createCookie(anyString(), anyString());
        verify(mockResponse, never()).addCookie(any(Cookie.class));

        // Verificar que se agregó mensaje de error al modelo
        verify(mockModel, times(1)).addAttribute(eq("loginError"), anyString());

        // Capturar y verificar el mensaje de error
        ArgumentCaptor<String> errorCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockModel).addAttribute(eq("loginError"), errorCaptor.capture());
        String errorMessage = errorCaptor.getValue();
        assertNotNull(errorMessage);
        assertTrue(errorMessage.contains("Correo") || errorMessage.contains("contraseña") || errorMessage.contains("erroneo"));
    }

}