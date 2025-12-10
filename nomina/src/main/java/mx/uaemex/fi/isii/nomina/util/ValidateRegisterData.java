package mx.uaemex.fi.isii.nomina.util;

import org.springframework.stereotype.Component;

/**
 * Class for validate the register data.
 * Manages error scenarios, and gives the wrong param and a error description.
 */
@Component
public class ValidateRegisterData {
    boolean isValidData;
    private String param;
    private String error;

    private static final String REGEX_MAYUSCULAS_Y_ESPACIOS = "^[A-Z\\s]+$";
    private static final String REGEX_RFC = "^[A-Z]{4}\\d{6}[A-Z0-9]{3}$";
    private static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,10}$";
    private static final String REGEX_PASSWORD =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&])[A-Za-z\\d!@#$%^&]{8,}$";

    public ValidateRegisterData() {}

    public ValidateRegisterData(boolean isValidData, String param, String error) {
        this.isValidData = isValidData;
        this.param = param;
        this.error = error;
    }

    /**
     * Checks all the params used to create either an Usuario or an Empleado.
     * validates parameter by parameter.
     * @param nombre name
     * @param apellido apellido
     * @param rfc rfc
     * @param correo correo
     * @param isAdmin Boolean, sets if the Empleado is also an admin
     * @param password password
     * @return an object of the type ValidateRegisterData ehich gives information about the validation.
     * If {@code object.getIsValidData()} returns {@code true} then the process continues. param and error are set as {@code ""}
     * If {@code object.getIsValidData()} returns {@code false} then in the object, the parameter that failed teh validation is returned and the error.
     */
    public ValidateRegisterData checkRegisterData(String nombre, String apellido, String rfc, String correo, Boolean isAdmin, String password) {
        if (!isValidNombre(nombre).getIsValid()){
            return new ValidateRegisterData(false, nombre, isValidNombre(nombre).getError());
        } else if (!isValidApellido(apellido).getIsValid()){
            return new ValidateRegisterData(false, apellido, isValidApellido(apellido).getError());
        } else if (!isValidRfc(rfc).getIsValid()){
            return new ValidateRegisterData(false, rfc, isValidRfc(rfc).getError());
        }  else if (!isValidCorreo(correo).getIsValid()){
            return new ValidateRegisterData(false, correo, isValidCorreo(correo).getError());
        } else if (isAdmin != null && !isValidPassword(password).getIsValid()){
            return new ValidateRegisterData(false, password, isValidPassword(password).getError());
        }
        return new ValidateRegisterData(true, "", "");
    }

    /**
     * Validates the name
     * @param name name
     * @return an object of the type {@code Valid} which determinate if the data is valid, if not, gives the reason why it's not
     * @see Valid
     */
    private static Valid isValidNombre(String name) {
        if (name == null) {
            return new Valid(false, "'Nombre' no puede estar vacio");
        } else if (name.length() < 3){
            return new Valid(false, "Longitud de Nombre demasiado corta");
        } else if (!name.matches(REGEX_MAYUSCULAS_Y_ESPACIOS)){
            return new Valid(false, "Solo letras mayusculas");
        } else {
            return new Valid(true, "");
        }
    }

    /**
     * Validates the Apellido
     * @param apellido name
     * @return an object of the type {@code Valid} which determinate if the data is valid, if not, gives the reason why it's not
     * @see Valid
     */
    private static Valid isValidApellido(String apellido) {
        if (apellido == null) {
            return new Valid(false, "'Apellido' no puede estar vacio");
        } else if (apellido.length() < 3){
            return new Valid(false, "Longitud de Apellidos demasiado corta");
        } else if (!apellido.matches(REGEX_MAYUSCULAS_Y_ESPACIOS)){
            return new Valid(false, "Solo letras mayusculas o espacios");
        } else {
            return new Valid(true, "");
        }
    }

    /**
     * Validates the rfc
     * @param rfc rfc
     * @return an object of the type {@code Valid} which determinate if the data is valid, if not, gives the reason why it's not
     * @see Valid
     */
    private static Valid isValidRfc(String rfc) {
        if (rfc == null) {
            return new Valid(false, "'RFC' no puede estar vacio");
        } else if (rfc.length() != 13){
            return new Valid(false, "Longitud de RFC demasiado corta");
        } else if (!rfc.matches(REGEX_RFC)){
            return new Valid(false, "Formato de RFC incorrecto");
        } else {
            return new Valid(true, "");
        }
    }

    /**
     * Validates the correo
     * @param correo correo
     * @return an object of the type {@code Valid} which determinate if the data is valid, if not, gives the reason why it's not
     * @see Valid
     */
    private static Valid isValidCorreo(String correo) {
        if (correo == null) {
            return new Valid(false, "'Correo' no puede estar vacio");
        } else if (correo.length() == 13){
            return new Valid(false, "Longitud de correo demasiado corta");
        } else if (!correo.matches(REGEX_EMAIL)){
            return new Valid(false, "Formato de correo incorrecto");
        } else {
            return new Valid(true, "");
        }
    }

    /**
     * Validates the password
     * @param password password
     * @return an object of the type {@code Valid} which determinate if the data is valid, if not, gives the reason why it's not
     * @see Valid
     */
    private static Valid isValidPassword(String password) {
        if (password == null) {
            return new Valid(false, "'Password' no puede estar vacio");
        } else if (password.length() < 8){
            return new Valid(false, "Longitud de password demasiado corta");
        } else if (!password.matches(REGEX_PASSWORD)){
            return new Valid(false, "Formato de password incorrecto");
        } else {
            return new Valid(true, "");
        }
    }

    public String getError() {
        return error;
    }

    public String getParam() {
        return param;
    }

    public boolean getIsValidData() {
        return isValidData;
    }
}
