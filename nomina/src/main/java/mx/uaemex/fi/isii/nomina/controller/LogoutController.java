package mx.uaemex.fi.isii.nomina.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.uaemex.fi.isii.nomina.util.CookieManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller. To handle /logout actions. Redirects to /
 */
@Controller
public class LogoutController {

    private final CookieManager cookieManager;

    public LogoutController(CookieManager cookieManager) {
        this.cookieManager = cookieManager;
    }

    /**
     * When logout is requested, deletes the cookie.
     * @return redirects to the context path /
     * @see CookieManager
     */
    @GetMapping("/logout")
    public String logout(@CookieValue(value = "session", required = false) String token, HttpServletResponse response,
                         HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("session".equals(cookie.getName())) {
                    cookieManager.deleteCookie(cookie, response);
                    break;
                }
            }
        }
        return "redirect:/" ;
    }
}
