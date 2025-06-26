package practice.spring_gym_api.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import practice.spring_gym_api.security.token.WorkerAuthToken;

import java.io.IOException;
import java.util.Collections;

@Component
public class WorkerAuthFilter extends OncePerRequestFilter {

    private final AuthenticationConfiguration configuration;

    public WorkerAuthFilter(AuthenticationConfiguration configuration){
        this.configuration = configuration;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getMethod().equalsIgnoreCase("GET") ||
        request.getMethod().equalsIgnoreCase("PATCH")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1. Check for necessary headings
        if(!Collections.list(request.getHeaderNames()).contains("x-worker-id")){
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "text/plain;charset=UTF-8");
            response.getWriter().write("Must include header 'x-worker-id'");
            return;
        } else if (!Collections.list(request.getHeaderNames()).contains("x-worker-code")) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "text/plain;charset=UTF-8");
            response.getWriter().write("IMust include header 'x-worker-code'");
            return;
        }

        // 2. Set the authenticated token returned from the Provider in the context.
        Long workerId = Long.valueOf(request.getHeader("x-worker-id"));
        String workerCode = request.getHeader("x-worker-code");
        var unauthenticatedToken = new WorkerAuthToken(workerId, workerCode);

        try {
            AuthenticationManager authenticationManager = configuration.getAuthenticationManager();
            var authTokenReturnedFromProvider = authenticationManager.authenticate(unauthenticatedToken);

            var newContext = SecurityContextHolder.createEmptyContext();
            newContext.setAuthentication(authTokenReturnedFromProvider);
            SecurityContextHolder.setContext(newContext);

        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "text/plain;charset=UTF-8");
            response.getWriter().write("Authentication failed: " + e.getMessage());
            return;
        }

        // 3. Call next filter
        filterChain.doFilter(request, response);
    }
}
