package mx.uaemex.fi.isii.nomina.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        Cookie[] cookies = request.getCookies();
        boolean hasSession = false;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("session".equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isEmpty()) {
                    hasSession = true;
                    break;
                }
            }
        }

        if (!hasSession) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}