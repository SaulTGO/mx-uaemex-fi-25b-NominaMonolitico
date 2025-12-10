package mx.uaemex.fi.isii.nomina.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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

public class LogoutControllerTest {

    private LogoutController logoutController;

    @Mock
    private CookieManager mockCookieManager;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        logoutController = new LogoutController(mockCookieManager);
    }

    @Test
    public void UT_LOC_001_logout_WithValidSessionCookie_DeletesCookie() {
        // Arrange
        String token = "usuario@example.com";
        Cookie sessionCookie = new Cookie("session", token);
        Cookie[] cookies = {sessionCookie};

        when(mockRequest.getCookies()).thenReturn(cookies);

        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);

        // Act
        String viewName = logoutController.logout(token, mockResponse, mockRequest);

        // Assert
        assertEquals("redirect:/", viewName);

        // Verificar que se llamó a deleteCookie con la cookie correcta
        verify(mockCookieManager, times(1)).deleteCookie(cookieCaptor.capture(), eq(mockResponse));

        Cookie capturedCookie = cookieCaptor.getValue();
        assertNotNull(capturedCookie);
        assertEquals("session", capturedCookie.getName());
        assertEquals(token, capturedCookie.getValue());
    }

    @Test
    public void UT_LOC_002_logout_WithValidSessionCookie_RedirectsToRoot() {
        // Arrange
        String token = "admin@example.com";
        Cookie sessionCookie = new Cookie("session", token);
        Cookie[] cookies = {sessionCookie};

        when(mockRequest.getCookies()).thenReturn(cookies);

        // Act
        String viewName = logoutController.logout(token, mockResponse, mockRequest);

        // Assert
        assertEquals("redirect:/", viewName);
        assertNotEquals("redirect:/login", viewName);
        assertNotEquals("redirect:/home", viewName);
        assertNotEquals("logout", viewName);
    }

    @Test
    public void UT_LOC_003_logout_WithSessionCookie_CallsCookieManagerDelete() {
        // Arrange
        String token = "test@example.com";
        Cookie sessionCookie = new Cookie("session", token);
        Cookie[] cookies = {sessionCookie};

        when(mockRequest.getCookies()).thenReturn(cookies);

        // Act
        logoutController.logout(token, mockResponse, mockRequest);

        // Assert
        // Verificar que se llamó a deleteCookie exactamente una vez
        verify(mockCookieManager, times(1)).deleteCookie(any(Cookie.class), eq(mockResponse));

        // Verificar que se pasó una cookie con nombre "session"
        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(mockCookieManager).deleteCookie(cookieCaptor.capture(), any(HttpServletResponse.class));

        assertEquals("session", cookieCaptor.getValue().getName());
    }

    @Test
    public void UT_LOC_004_logout_WithoutCookies_RedirectsToRoot() {
        // Arrange
        String token = null;
        Cookie[] cookies = null; // Sin cookies

        when(mockRequest.getCookies()).thenReturn(cookies);

        // Act
        String viewName = logoutController.logout(token, mockResponse, mockRequest);

        // Assert
        assertEquals("redirect:/", viewName);

        // Verificar que NO se llamó a deleteCookie porque no hay cookies
        verify(mockCookieManager, never()).deleteCookie(any(Cookie.class), any(HttpServletResponse.class));
    }

    @Test
    public void UT_LOC_005_loginAndLogout_CompleteFlow_WorksCorrectly() {
        // Este test simula el flujo completo: Login → Home → Logout

        // ========== FASE 1: LOGIN ==========
        // Arrange - Componentes para login
        UsuarioRepositoryService mockUsuarioService = mock(UsuarioRepositoryService.class);
        CookieManager realCookieManager = new CookieManager();
        LoginController loginController = new LoginController(realCookieManager, mockUsuarioService);
        HomeController homeController = new HomeController();

        Model mockLoginModel = mock(Model.class);
        Model mockHomeModel = mock(Model.class);
        HttpServletResponse mockLoginResponse = mock(HttpServletResponse.class);

        String correo = "empleado@example.com";
        String password = "SecurePass1@";

        // Configurar login exitoso
        when(mockUsuarioService.validateLogin(correo, password)).thenReturn(true);

        // Act - Paso 1: Usuario hace login
        String loginView = loginController.processLogin(correo, password, mockLoginModel, null, mockLoginResponse);

        // Assert - Verificar login exitoso
        assertEquals("redirect:/home", loginView);
        verify(mockLoginResponse, times(1)).addCookie(any(Cookie.class));

        // ========== FASE 2: ACCESO A HOME ==========
        // Act - Paso 2: Usuario accede a home con sesión
        String sessionValue = correo; // La cookie contiene el correo
        String homeView = homeController.home(mockHomeModel, sessionValue);

        // Assert - Verificar acceso exitoso a home
        assertEquals("home", homeView);
        verify(mockHomeModel, times(1)).addAttribute("value", sessionValue);

        // ========== FASE 3: LOGOUT ==========
        // Arrange - Preparar logout
        Cookie sessionCookie = new Cookie("session", correo);
        Cookie[] cookiesForLogout = {sessionCookie};

        when(mockRequest.getCookies()).thenReturn(cookiesForLogout);

        // Act - Paso 3: Usuario hace logout
        String logoutView = logoutController.logout(correo, mockResponse, mockRequest);

        // Assert - Verificar logout exitoso
        assertEquals("redirect:/", logoutView);
        verify(mockCookieManager, times(1)).deleteCookie(any(Cookie.class), eq(mockResponse));

        // ========== VERIFICACIÓN DEL FLUJO COMPLETO ==========
        // Verificar que después del logout, el usuario no puede acceder a home
        Model mockHomeModelAfterLogout = mock(Model.class);
        String homeViewAfterLogout = homeController.home(mockHomeModelAfterLogout, null);

        // Assert - Sin sesión, debe redirigir a login
        assertEquals("redirect:/login", homeViewAfterLogout);
        verify(mockHomeModelAfterLogout, never()).addAttribute(anyString(), any());

        // Resumen del flujo:
        // 1. Login exitoso → redirect:/home ✓
        // 2. Acceso a home con sesión → home ✓
        // 3. Logout → redirect:/ ✓
        // 4. Intento de acceso a home sin sesión → redirect:/login ✓
    }
}