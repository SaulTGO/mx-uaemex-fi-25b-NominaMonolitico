package mx.uaemex.fi.isii.nomina.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CookieManagerTest {

    private CookieManager cookieManager;

    @Mock
    private HttpServletResponse mockResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cookieManager = new CookieManager();
    }

    @Test
    public void UT_CM_001_createCookie_ValidParameters_DomainIsLocalhost() {
        // Arrange
        String name = "sessionId";
        String value = "abc123";

        // Act
        Cookie cookie = cookieManager.createCookie(name, value);

        // Assert
        assertNotNull(cookie);
        assertEquals("localhost", cookie.getDomain());
    }

    @Test
    public void UT_CM_002_createCookie_ValidParameters_PathIsRoot() {
        // Arrange
        String name = "userId";
        String value = "12345";

        // Act
        Cookie cookie = cookieManager.createCookie(name, value);

        // Assert
        assertNotNull(cookie);
        assertEquals("/", cookie.getPath());
    }

    @Test
    public void UT_CM_003_deleteCookie_ValidCookie_SetsEmptyValue() {
        // Arrange
        Cookie cookie = new Cookie("testCookie", "testValue");

        // Act
        cookieManager.deleteCookie(cookie, mockResponse);

        // Assert
        assertEquals("", cookie.getValue());
        assertEquals("/", cookie.getPath());
        assertEquals(0, cookie.getMaxAge());
        assertTrue(cookie.isHttpOnly());
        verify(mockResponse, times(1)).addCookie(cookie);
    }

    @Test
    public void UT_CM_004_createAndDelete_ValidCookie_WorksCorrectly() {
        // Arrange
        String cookieName = "userToken";
        String cookieValue = "token123456";

        // Act - Crear cookie
        Cookie createdCookie = cookieManager.createCookie(cookieName, cookieValue);

        // Assert - Verificar cookie creada
        assertNotNull(createdCookie);
        assertEquals(cookieName, createdCookie.getName());
        assertEquals(cookieValue, createdCookie.getValue());
        assertEquals("localhost", createdCookie.getDomain());
        assertEquals("/", createdCookie.getPath());
        assertEquals(1000, createdCookie.getMaxAge());
        assertTrue(createdCookie.isHttpOnly());

        // Act - Eliminar cookie
        cookieManager.deleteCookie(createdCookie, mockResponse);

        // Assert - Verificar cookie eliminada
        assertEquals("", createdCookie.getValue());
        assertEquals("/", createdCookie.getPath());
        assertEquals(0, createdCookie.getMaxAge());
        assertTrue(createdCookie.isHttpOnly());
        verify(mockResponse, times(1)).addCookie(createdCookie);
    }
}