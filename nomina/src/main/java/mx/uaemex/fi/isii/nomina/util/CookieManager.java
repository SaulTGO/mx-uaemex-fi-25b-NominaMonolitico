package mx.uaemex.fi.isii.nomina.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

/**
 * Class for cookie's basic management
 */
@Component
public class CookieManager {

    /**
     * Method for creating a cookie
     * @param name Cookie's name
     * @param value Cookie's value
     * @return cookie
     */
    public Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setMaxAge(1000);
        cookie.setHttpOnly(true);

        return cookie;
    }

    /**
     * Basic empty constructor
     */
    public CookieManager(){
    }

    /**
     * Method for creating a cookie
     * @param cookie for accessing to the cookie
     * @param response to handle and send the cookie's changes
     *
     */
    public void deleteCookie(Cookie cookie, HttpServletResponse response) {
        cookie.setValue("");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
