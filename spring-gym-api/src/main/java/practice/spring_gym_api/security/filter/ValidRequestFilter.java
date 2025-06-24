package practice.spring_gym_api.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

@Component
public class ValidRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Decide whether to apply the filter
        if(!Collections.list(request.getHeaderNames()).contains("x-valid")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Check Credentials
        if(!Objects.equals(request.getHeader("x-valid"), "yes")) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "text/plain;charset=UTF-8");
            response.getWriter().write("Invalid request");
            return;
        }

        // 3. Call next filter
        filterChain.doFilter(request, response);
    }
}
