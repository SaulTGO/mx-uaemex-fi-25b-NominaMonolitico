package mx.uaemex.fi.isii.nomina.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class ValidateRegisterDataTest {

    private ValidateRegisterData validator;

    @BeforeEach
    void setUp() {
        validator = new ValidateRegisterData();
    }

    // ==================== PRUEBAS DE NOMBRE ====================

    @Test
    @DisplayName("UT-VRD-001: Validar nombre nulo - Debe retornar falso")
    void UT_VRD_001_isValidNombre_nullNombre_False(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                null, "PEREZ", "ABCD123456XYZ", "test@example.com", null, null
        );
        assertFalse(result.getIsValidData());
    }

    @Test
    @DisplayName("UT-VRD-002: Validar nombre muy corto - Debe retornar falso")
    void UT_VRD_002_isValidNombre_tooShortNombre_False(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JU", "PEREZ", "ABCD123456XYZ", "test@example.com", null, null
        );
        assertFalse(result.getIsValidData());
    }

    @Test
    @DisplayName("UT-VRD-003: Validar nombre con minúsculas - Debe retornar falso")
    void UT_VRD_003_isValidNombre_lowerCaseNombre_False(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "juan", "PEREZ", "ABCD123456XYZ", "test@example.com", null, null
        );
        assertFalse(result.getIsValidData());
    }

    @Test
    @DisplayName("UT-VRD-004: Validar nombre válido - Debe retornar verdadero")
    void UT_VRD_004_isValidNombre_validNombre_True(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", "ABCD123456XYZ", "test@example.com", null, null
        );
        assertTrue(result.getIsValidData());
    }

    // ==================== PRUEBAS DE APELLIDO ====================

    @Test
    @DisplayName("UT-VRD-005: Validar apellido nulo - Debe retornar falso")
    void UT_VRD_005_isValidApellido_nullApellido_False(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", null, "ABCD123456XYZ", "test@example.com", null, null
        );
        assertFalse(result.getIsValidData());
        assertEquals("'Apellido' no puede estar vacio", result.getError());
    }

    @Test
    @DisplayName("UT-VRD-006: Validar apellido muy corto - Debe retornar falso")
    void UT_VRD_006_isValidApellido_tooShortApellido_False(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PE", "ABCD123456XYZ", "test@example.com", null, null
        );
        assertFalse(result.getIsValidData());
    }

    @Test
    @DisplayName("UT-VRD-007: Validar apellido con minúsculas - Debe retornar falso")
    void UT_VRD_007_isValidApellido_lowerCaseApellido_False(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "perez", "ABCD123456XYZ", "test@example.com", null, null
        );
        assertFalse(result.getIsValidData());
    }

    @Test
    @DisplayName("UT-VRD-008: Validar apellido válido - Debe retornar verdadero")
    void UT_VRD_008_isValidApellido_validApellido_True(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", "ABCD123456XYZ", "test@example.com", null, null
        );
        assertTrue(result.getIsValidData());
    }

    // ==================== PRUEBAS DE RFC ====================

    @Test
    @DisplayName("UT-VRD-009: Validar RFC nulo - Debe retornar falso")
    void UT_VRD_009_isValidRFC_nullRFC_False(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", null, "test@example.com", null, null
        );
        assertFalse(result.getIsValidData());
    }

    @Test
    @DisplayName("UT-VRD-010: Validar RFC con longitud incorrecta - Debe retornar falso")
    void UT_VRD_010_isValidRFC_tooShortRFC_False(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", "ABCD12345", "test@example.com", null, null
        );
        assertFalse(result.getIsValidData());
    }

    @Test
    @DisplayName("UT-VRD-011: Validar RFC con formato incorrecto - Debe retornar falso")
    void UT_VRD_011_isValidRFC_incorrectFormatRFC_False(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", "ABC1234567XYZ", "test@example.com", null, null
        );
        assertFalse(result.getIsValidData());
    }

    @Test
    @DisplayName("UT-VRD-012: Validar RFC válido - Debe retornar verdadero")
    void UT_VRD_012_isValidRFC_validRFC_True(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", "ABCD123456XYZ", "test@example.com", null, null
        );
        assertTrue(result.getIsValidData());
    }

    // ==================== PRUEBAS DE CORREO ====================

    @Test
    @DisplayName("UT-VRD-013: Validar correo nulo - Debe retornar falso")
    void UT_VRD_013_isValidCorreo_nullCorreo_False(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", "ABCD123456XYZ", null, null, null
        );
        assertFalse(result.getIsValidData());
    }

    @Test
    @DisplayName("UT-VRD-014: Validar correo muy corto - Debe retornar falso")
    void UT_VRD_014_isValidCorreo_tooShortCorreo_False(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", "ABCD123456XYZ", "a@a.a", null, null
        );
        assertFalse(result.getIsValidData());
    }

    @Test
    @DisplayName("UT-VRD-015: Validar correo con formato incorrecto - Debe retornar falso")
    void UT_VRD_015_isValidCorreo_incorrectFormatCorreo_False(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", "ABCD123456XYZ", "testexample.com", null, null
        );
        assertFalse(result.getIsValidData());
    }

    @Test
    @DisplayName("UT-VRD-016: Validar correo válido - Debe retornar verdadero")
    void UT_VRD_016_isValidCorreo_validCorreo_True(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", "ABCD123456XYZ", "test@example.com", null, null
        );
        assertTrue(result.getIsValidData());
    }

    // ==================== PRUEBAS DE PASSWORD ====================

    @Test
    @DisplayName("UT-VRD-017: Validar password nulo - Debe retornar falso")
    void UT_VRD_017_isValidPassword_nullPassword_False(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", "ABCD123456XYZ", "test@example.com", true, null
        );
        assertFalse(result.getIsValidData());
    }

    @Test
    @DisplayName("UT-VRD-018: Validar password muy corto - Debe retornar falso")
    void UT_VRD_018_isValidPassword_tooShortPassword_False(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", "ABCD123456XYZ", "test@example.com", true, "Pass1!"
        );
        assertFalse(result.getIsValidData());
    }

    @Test
    @DisplayName("UT-VRD-019: Validar password con formato incorrecto - Debe retornar falso")
    void UT_VRD_019_isValidPassword_incorrectFormatPassword_False(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", "ABCD123456XYZ", "test@example.com", true, "password123"
        );
        assertFalse(result.getIsValidData());
    }

    @Test
    @DisplayName("UT-VRD-020: Validar password válido - Debe retornar verdadero")
    void UT_VRD_020_isValidPassword_validPassword_True(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", "ABCD123456XYZ", "test@example.com", true, "Password123!"
        );
        assertTrue(result.getIsValidData());
    }

    // ==================== PRUEBAS ADICIONALES ====================

    @Test
    @DisplayName("UT-VRD-021: Validar password sin mayúscula - Debe retornar falso")
    void UT_VRD_021_isValidPassword_noUpperCasePassword_False(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", "ABCD123456XYZ", "test@example.com", true, "password123!"
        );
        assertFalse(result.getIsValidData());
    }

    @Test
    @DisplayName("UT-VRD-022: Validar password sin minúscula - Debe retornar falso")
    void UT_VRD_022_isValidPassword_noLowerCasePassword_False(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", "ABCD123456XYZ", "test@example.com", true, "PASSWORD123!"
        );
        assertFalse(result.getIsValidData());
    }

    @Test
    @DisplayName("UT-VRD-023: Validar password sin número - Debe retornar falso")
    void UT_VRD_023_isValidPassword_noNumberPassword_False(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", "ABCD123456XYZ", "test@example.com", true, "Password!"
        );
        assertFalse(result.getIsValidData());
    }

    @Test
    @DisplayName("UT-VRD-024: Validar password sin caracter especial - Debe retornar falso")
    void UT_VRD_024_isValidPassword_noSpecialCharPassword_False(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", "ABCD123456XYZ", "test@example.com", true, "Password123"
        );
        assertFalse(result.getIsValidData());
    }

    @Test
    @DisplayName("UT-VRD-025: Validar nombre con números - Debe retornar falso")
    void UT_VRD_025_isValidNombre_numbersInNombre_False(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN123", "PEREZ", "ABCD123456XYZ", "test@example.com", null, null
        );
        assertFalse(result.getIsValidData());
    }

    @Test
    @DisplayName("UT-VRD-026: Validar apellido con caracteres especiales - Debe retornar falso")
    void UT_VRD_026_isValidApellido_specialCharsApellido_False(){
        ValidateRegisterData validator = new ValidateRegisterData();
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ@GARCIA", "ABCD123456XYZ", "test@example.com", null, null
        );
        assertFalse(result.getIsValidData());
    }

    // ==================== PRUEBAS DE checkRegisterData ====================

    @Test
    @DisplayName("UT-CRD-001: Validar registro completo válido para empleado sin privilegios - Debe retornar verdadero")
    void UT_CRD_001_checkRegisterData_completeValidDataNoAdmin_True(){
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN CARLOS",
                "PEREZ GARCIA",
                "ABCD123456XYZ",
                "juan.perez@example.com",
                null,
                null
        );

        assertTrue(result.getIsValidData());
        assertEquals("", result.getError());
    }

    @Test
    @DisplayName("UT-CRD-002: Validar registro completo válido para administrador - Debe retornar verdadero")
    void UT_CRD_002_checkRegisterData_completeValidDataAdmin_True(){
        ValidateRegisterData result = validator.checkRegisterData(
                "MARIA FERNANDA",
                "LOPEZ MARTINEZ",
                "WXYZ987654ABC",
                "maria.lopez@example.com",
                true,
                "SecurePass123!"
        );

        assertTrue(result.getIsValidData());
        assertEquals("", result.getError());
    }

    @Test
    @DisplayName("UT-CRD-003: Validar registro con todos los campos nulos - Debe retornar falso")
    void UT_CRD_003_checkRegisterData_allNullFields_False(){
        ValidateRegisterData result = validator.checkRegisterData(
                null, null, null, null, null, null
        );

        assertFalse(result.getIsValidData());
        assertEquals("'Nombre' no puede estar vacio", result.getError());
    }

    @Test
    @DisplayName("UT-CRD-004: Validar registro con múltiples errores - Debe retornar el primer error")
    void UT_CRD_004_checkRegisterData_multipleErrors_False(){
        ValidateRegisterData result = validator.checkRegisterData(
                null, null, "ABC", "invalid", true, "weak"
        );

        assertFalse(result.getIsValidData());
        assertEquals("'Nombre' no puede estar vacio", result.getError());
    }

    @Test
    @DisplayName("UT-CRD-005: Validar que isAdmin null no requiere password - Debe retornar verdadero")
    void UT_CRD_005_checkRegisterData_nullAdminNoPassword_True(){
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", "ABCD123456XYZ", "test@example.com", null, null
        );

        assertTrue(result.getIsValidData());
    }

    @Test
    @DisplayName("UT-CRD-006: Validar que isAdmin false no requiere password - Debe retornar verdadero")
    void UT_CRD_006_checkRegisterData_adminFalseNoPassword_True(){
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", "ABCD123456XYZ", "test@example.com", null, null
        );

        assertTrue(result.getIsValidData());
    }

    @Test
    @DisplayName("UT-CRD-007: Validar registro de administrador con password nulo - Debe retornar falso")
    void UT_CRD_007_checkRegisterData_adminNullPassword_False(){
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", "ABCD123456XYZ", "test@example.com", true, null
        );

        assertFalse(result.getIsValidData());
        assertEquals("'Password' no puede estar vacio", result.getError());
    }

    @Test
    @DisplayName("UT-CRD-008: Validar que retorna el parámetro inválido correcto")
    void UT_CRD_008_checkRegisterData_returnsCorrectParam_False(){
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", "INVALID", "test@example.com", false, null
        );

        assertFalse(result.getIsValidData());
        assertEquals("INVALID", result.getParam());
        assertEquals("Longitud de RFC demasiado corta", result.getError());
    }

    @Test
    @DisplayName("UT-CRD-009: Validar registro con correo institucional válido - Debe retornar verdadero")
    void UT_CRD_009_checkRegisterData_institutionalEmail_True(){
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", "ABCD123456XYZ", "juan.perez@uaemex.mx", null, null
        );

        assertTrue(result.getIsValidData());
    }

    @Test
    @DisplayName("UT-CRD-010: Validar que el objeto retornado es válido y completo")
    void UT_CRD_010_checkRegisterData_returnsValidObject_True(){
        ValidateRegisterData result = validator.checkRegisterData(
                "JUAN", "PEREZ", "ABCD123456XYZ", "test@example.com", null, null
        );

        assertNotNull(result);
        assertTrue(result.getIsValidData());
        assertEquals("", result.getError());
        assertEquals("", result.getParam());
    }
}