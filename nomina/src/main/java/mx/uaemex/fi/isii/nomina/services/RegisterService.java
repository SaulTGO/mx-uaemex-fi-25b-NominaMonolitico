package mx.uaemex.fi.isii.nomina.services;

import mx.uaemex.fi.isii.nomina.domain.entity.Empleado;
import mx.uaemex.fi.isii.nomina.domain.entity.Usuario;
import mx.uaemex.fi.isii.nomina.repository.EmpleadoRepository;
import mx.uaemex.fi.isii.nomina.repository.UsuarioRepository;
import mx.uaemex.fi.isii.nomina.util.Valid;
import mx.uaemex.fi.isii.nomina.util.ValidateRegisterData;
import org.springframework.stereotype.Service;

/**
 * Service. For managing attempts to insert a new register to the BD.
 * Manage data validation
 * Saves usuarios
 * Saves empleados
 * Gives utilities for the controller to create an Empleado
 * @see mx.uaemex.fi.isii.nomina.repository.EmpleadoRepository
 * @see mx.uaemex.fi.isii.nomina.repository.UsuarioRepository
 * @see mx.uaemex.fi.isii.nomina.util.ValidateRegisterData
 */
@Service
public class RegisterService {

    private final EmpleadoRepository empleadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ValidateRegisterData validateRegisterData;

    public RegisterService(EmpleadoRepository empleadoRepository,  UsuarioRepository usuarioRepository,  ValidateRegisterData validateRegisterData) {
        this.empleadoRepository = empleadoRepository;
        this.usuarioRepository = usuarioRepository;
        this.validateRegisterData = validateRegisterData;
    }

    /**
     * Saves an Usuario register into the BD
     * @param empleado Object Empleado
     * @param password password
     */
    public void saveUsuario(Empleado empleado, String password) {
        Usuario usuario = new Usuario();
        usuario.setEmpleado(empleado);
        usuario.setPassword(password);

        usuarioRepository.save(usuario);
    }

    /**
     * Saves an Empleado register into the BD
     * @param empleado Object Empleado with all the fields
     */
    public void saveEmpleado(Empleado empleado) {
        empleadoRepository.save(empleado);
    }

    /**
     * @return an object Empleado
     */
    public Empleado createEmpleado(String nombre, String apellidos, String rfc, String correo) {
        Empleado empleado = new Empleado();
        empleado.setNombre(nombre);
        empleado.setApellidos(apellidos);
        empleado.setRfc(rfc);
        empleado.setCorreo(correo);
        return empleado;
    }

    /**
     * Used in the {@code Controller.RegisterController} for data validation.
     * Validates each parameter.
     * @return object {@code Valid}, if {@code valid.getIsValidData() = true} then the data is correct. If not, returns {@code false} and the error
     * @see ValidateRegisterData
     */
    public Valid ValidateRegisterParams(String nombre, String apellidos, String rfc, String correo,  Boolean isAdmin, String password) {
        ValidateRegisterData validations;
        Valid valid = new Valid();
        validations = validateRegisterData.checkRegisterData(nombre, apellidos, rfc, correo, isAdmin, password);
        valid.setIsValid(validations.getIsValidData());
        valid.setError(validations.getError());
        return valid;
    }

}
