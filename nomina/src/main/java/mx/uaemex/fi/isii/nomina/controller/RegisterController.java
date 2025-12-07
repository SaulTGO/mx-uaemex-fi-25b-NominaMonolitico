package mx.uaemex.fi.isii.nomina.controller;

import mx.uaemex.fi.isii.nomina.domain.entity.Empleado;
import mx.uaemex.fi.isii.nomina.domain.entity.Usuario;
import mx.uaemex.fi.isii.nomina.services.EmpleadoRepositoryService;
import mx.uaemex.fi.isii.nomina.services.RegisterService;
import mx.uaemex.fi.isii.nomina.services.UsuarioRepositoryService;
import mx.uaemex.fi.isii.nomina.util.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller. Handles /register and register.html
 */
@Controller
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    /**
     * GET method. For loading the page
     * @param value cookie value
     * @return /register Shows the view
     */
    @GetMapping("/register")
    public String register(
            @CookieValue(value = "session", required = false) String value
    ){

        return "register";
    }

    /**
     * POST method. Handle the request for a register
     * Allows to fill and send the form
     * Shows an error message if exists
     * @return /register Refresh the page
     */
    @PostMapping("/register")
    public String register(
            @RequestParam String nombre,
            @RequestParam String apellidos,
            @RequestParam String rfc,
            @RequestParam String correo,
            @RequestParam(required = false) Boolean esAdministrador,
            @RequestParam(required = false) String password,
            Model model,
            @CookieValue(value = "session", required = false) String value
    ){
        Valid valid = registerService.ValidateRegisterParams(nombre, apellidos, rfc, correo, esAdministrador, password);
        if(!valid.getIsValid()){
            model.addAttribute("messageerror", valid.getError());
            return "register";
        }
        try{
            Empleado empleado = registerService.createEmpleado(nombre, apellidos, rfc, correo);
            if (esAdministrador == null) {
                registerService.saveEmpleado(empleado);
            } else if (esAdministrador) {
                registerService.saveEmpleado(empleado);
                registerService.saveUsuario(empleado, password);
            }
            model.addAttribute("message", "Registrado correctamente");
            return "register";
        } catch (Exception e){
            model.addAttribute("messageerror", valid.getError());
            return "redirect:/register";
        }

    }

}
