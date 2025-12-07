package mx.uaemex.fi.isii.nomina.services;

import mx.uaemex.fi.isii.nomina.domain.entity.Usuario;
import mx.uaemex.fi.isii.nomina.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UsuarioRepositoryServiceTest {

    private UsuarioRepositoryService usuarioRepositoryService;

    @Mock
    private UsuarioRepository mockUsuarioRepository;

    @Mock
    private Usuario mockUsuario;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioRepositoryService = new UsuarioRepositoryService(mockUsuarioRepository);
    }

    @Test
    public void UT_URS_001_validateLogin_ValidCredentials_ReturnsTrue() {
        // Arrange
        String correo = "usuario@example.com";
        String password = "password123";

        when(mockUsuarioRepository.findByPasswordAndEmpleadoCorreo(password, correo))
                .thenReturn(Optional.of(mockUsuario));

        // Act
        boolean result = usuarioRepositoryService.validateLogin(correo, password);

        // Assert
        assertTrue(result);
        verify(mockUsuarioRepository, times(1))
                .findByPasswordAndEmpleadoCorreo(password, correo);
    }

    @Test
    public void UT_URS_002_validateLogin_InvalidPassword_ReturnsFalse() {
        // Arrange
        String correo = "usuario@example.com";
        String correctPassword = "password123";
        String wrongPassword = "wrongPassword";

        when(mockUsuarioRepository.findByPasswordAndEmpleadoCorreo(wrongPassword, correo))
                .thenReturn(Optional.empty());

        // Act
        boolean result = usuarioRepositoryService.validateLogin(correo, wrongPassword);

        // Assert
        assertFalse(result);
        verify(mockUsuarioRepository, times(1))
                .findByPasswordAndEmpleadoCorreo(wrongPassword, correo);
    }

    @Test
    public void UT_URS_003_validateLogin_InvalidEmail_ReturnsFalse() {
        // Arrange
        String invalidCorreo = "noexiste@example.com";
        String password = "password123";

        when(mockUsuarioRepository.findByPasswordAndEmpleadoCorreo(password, invalidCorreo))
                .thenReturn(Optional.empty());

        // Act
        boolean result = usuarioRepositoryService.validateLogin(invalidCorreo, password);

        // Assert
        assertFalse(result);
        verify(mockUsuarioRepository, times(1))
                .findByPasswordAndEmpleadoCorreo(password, invalidCorreo);
    }

    @Test
    public void UT_URS_004_validateLogin_RepositoryReturnsEmpty_ReturnsFalse() {
        // Arrange
        String correo = "test@example.com";
        String password = "testPassword";

        when(mockUsuarioRepository.findByPasswordAndEmpleadoCorreo(anyString(), anyString()))
                .thenReturn(Optional.empty());

        // Act
        boolean result = usuarioRepositoryService.validateLogin(correo, password);

        // Assert
        assertFalse(result);
        verify(mockUsuarioRepository, times(1))
                .findByPasswordAndEmpleadoCorreo(password, correo);
    }

    @Test
    public void UT_URS_005_validateLogin_SQLInjectionAttempt_HandlesSecurely() {
        // Arrange
        String maliciousEmail = "admin@example.com' OR '1'='1";
        String maliciousPassword = "' OR '1'='1' --";

        when(mockUsuarioRepository.findByPasswordAndEmpleadoCorreo(maliciousPassword, maliciousEmail))
                .thenReturn(Optional.empty());

        // Act
        boolean result = usuarioRepositoryService.validateLogin(maliciousEmail, maliciousPassword);

        // Assert
        assertFalse(result);
        verify(mockUsuarioRepository, times(1))
                .findByPasswordAndEmpleadoCorreo(maliciousPassword, maliciousEmail);

        verify(mockUsuarioRepository, never())
                .findByPasswordAndEmpleadoCorreo(eq("password123"), anyString());
    }

}