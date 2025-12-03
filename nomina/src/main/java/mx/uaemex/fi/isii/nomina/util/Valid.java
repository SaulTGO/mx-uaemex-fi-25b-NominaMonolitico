package mx.uaemex.fi.isii.nomina.util;

/**
 * Class for error management, allows to check if a values is valid, if not, then reads the error
 */
public class Valid {

    private Boolean isValid;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean valid) {
        isValid = valid;
    }

    public  Valid() {}

    public  Valid(Boolean isValid, String error) {
        this.isValid = isValid;
        this.error = error;
    }
}
