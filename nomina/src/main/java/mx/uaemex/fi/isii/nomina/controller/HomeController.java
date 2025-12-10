package mx.uaemex.fi.isii.nomina.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller. Handles /home and home.html
 */
@Controller
public class HomeController {

    /**
     * GET method. Loads the page
     * @return /home Shows the page
     */
    @GetMapping("/home")
    public String home(
            Model model,
            @CookieValue(value = "session", required = false) String value
    ){
        if (value == null) {
            return "redirect:/login";
        }

        model.addAttribute("value", value);
        return "home";
    }
}
