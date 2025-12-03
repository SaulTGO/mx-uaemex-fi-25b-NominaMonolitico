package mx.uaemex.fi.isii.nomina.controller;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import mx.uaemex.fi.isii.nomina.services.EmpleadoRepositoryService;
import mx.uaemex.fi.isii.nomina.services.UsuarioRepositoryService;
import mx.uaemex.fi.isii.nomina.util.CookieManager;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller. Handles /login and login.html
 */
@Controller
public class LoginController {

    private final CookieManager cookieManager;
    private final UsuarioRepositoryService usuarioRepositoryService;

    public LoginController(CookieManager cookieManager, UsuarioRepositoryService usuarioRepositoryService) {
        this.cookieManager = cookieManager;
        this.usuarioRepositoryService = usuarioRepositoryService;
    }

    /**
     * GET method. Loads the view
     * @return /login Shows the view
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    /**
     * POST method. Handles the form
     * @return if login is validated, create a cookie then go /home, if not, refresh and shows a message
     */
    @PostMapping(value = "/login")
    public String processLogin(
            @RequestParam String correo,
            @RequestParam String password,
            Model model,
            @CookieValue(value = "session", required = false) String token, HttpServletResponse response
            ) {

        if (usuarioRepositoryService.validateLogin(correo, password)){
            Cookie cookie = cookieManager.createCookie("session", correo);

            response.addCookie(cookie);

            return "redirect:/home";
        } else {
            String error = "Correo o contraseña erroneos";
            model.addAttribute("loginError", error);
        }

        return "login";
    }

}
