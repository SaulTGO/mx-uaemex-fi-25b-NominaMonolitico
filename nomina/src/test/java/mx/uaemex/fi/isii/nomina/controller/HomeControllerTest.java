package mx.uaemex.fi.isii.nomina.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import mx.uaemex.fi.isii.nomina.services.UsuarioRepositoryService;
import mx.uaemex.fi.isii.nomina.util.CookieManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class HomeControllerTest {

    private HomeController homeController;

    @Mock
    private Model mockModel;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        homeController = new HomeController();
    }

    @Test
    public void UT_HC_001_home_WithValidSessionCookie_ReturnsHomeView() {
        // Arrange
        String sessionValue = "usuario@example.com";

        // Act
        String viewName = homeController.home(mockModel, sessionValue);

        // Assert
        assertEquals("home", viewName);
        assertNotEquals("redirect:/login", viewName);
        verify(mockModel, times(1)).addAttribute("value", sessionValue);
    }

    @Test
    public void UT_HC_002_home_WithNullSessionCookie_RedirectsToLogin() {
        // Arrange
        String sessionValue = null;

        // Act
        String viewName = homeController.home(mockModel, sessionValue);

        // Assert
        assertEquals("redirect:/login", viewName);
        assertNotEquals("home", viewName);

        // Verificar que NO se agregó ningún atributo al modelo
        verify(mockModel, never()).addAttribute(anyString(), any());
        verify(mockModel, never()).addAttribute(eq("value"), any());
    }

    @Test
    public void UT_HC_003_loginAndHome_SuccessfulFlow_AccessesHome() {
        // Este test simula el flujo completo: login exitoso → acceso a home

        // Arrange - Simular componentes necesarios para el flujo de login
        CookieManager cookieManager = new CookieManager();

        // Mock de UsuarioRepositoryService para simular login
        UsuarioRepositoryService mockUsuarioService = mock(UsuarioRepositoryService.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        Model mockLoginModel = mock(Model.class);

        LoginController loginController = new LoginController(cookieManager, mockUsuarioService);

        String correo = "empleado@example.com";
        String password = "SecurePass1@";

        // Configurar mock para login exitoso
        when(mockUsuarioService.validateLogin(correo, password)).thenReturn(true);

        // Act - Paso 1: Realizar login
        String loginView = loginController.processLogin(correo, password, mockLoginModel, null, mockResponse);

        // Assert - Verificar que el login fue exitoso
        assertEquals("redirect:/home", loginView);

        // Verificar que se creó la cookie (a través del response)
        verify(mockResponse, times(1)).addCookie(any(Cookie.class));

        // Act - Paso 2: Acceder a /home con la sesión (simulando que la cookie existe)
        // En un escenario real, el navegador enviaría la cookie automáticamente
        String sessionCookieValue = correo; // El valor de la cookie es el correo
        String homeView = homeController.home(mockModel, sessionCookieValue);

        // Assert - Verificar que se accede correctamente a home
        assertEquals("home", homeView);
        verify(mockModel, times(1)).addAttribute("value", sessionCookieValue);

        // Verificar flujo completo
        // 1. Login fue exitoso (redirect a home)
        // 2. Cookie fue creada
        // 3. Home es accesible con la cookie
        assertNotEquals("redirect:/login", homeView);

        // Verificar que el valor en el modelo es el correo del usuario
        verify(mockModel).addAttribute(eq("value"), eq(correo));
    }

    @Test
    public void UT_HC_004_home_NullSession_DoesNotProcessHomePage() {
        // Arrange
        String nullSession = null;

        // Act
        String viewName = homeController.home(mockModel, nullSession);

        // Assert
        assertEquals("redirect:/login", viewName);

        // Verificar que el modelo nunca fue modificado
        verifyNoInteractions(mockModel);
    }

}