package practice.spring_gym_api.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import practice.spring_gym_api.entity.CoachEntity;
import practice.spring_gym_api.repository.CoachRepository;
import practice.spring_gym_api.security.token.CoachAuthToken;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class CoachAuthFilter extends OncePerRequestFilter {

    private final CoachRepository coachRepository;

    public CoachAuthFilter(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. Only apply to PUT and PATCH endpoints
        if(request.getMethod().equalsIgnoreCase("GET") ||
        request.getMethod().equalsIgnoreCase("DELETE") ||
        request.getMethod().equalsIgnoreCase("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Check for necessary headers.
        if(!Collections.list(request.getHeaderNames()).contains("x-coach-code")) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "text/plain;charset=UTF-8");
            response.getWriter().write("Must include header 'x-coach-code'");
            return;
        } else if (!Collections.list(request.getHeaderNames()).contains("x-coach-id")) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "text/plain;charset=UTF-8");
            response.getWriter().write("Must include header 'x-coach-id'");
            return;
        }

        // 3. Check the credentials of headers.
        Long coachId = Long.valueOf(request.getHeader("x-coach-id"));
        String coachCode = request.getHeader("x-coach-code");

        CoachEntity coachEntity = coachRepository.findById(coachId)
                .orElseThrow(() -> new IllegalStateException("Coach with an id of: " + coachId + " doesnt exist"));
        if(coachRepository.findByCoachCode(coachCode) == null) throw new IllegalStateException("Coach with a code of: " + coachCode + " doesnt exist");
        if(!coachEntity.equals(coachRepository.findByCoachCode(coachCode))) {
            throw new IllegalStateException("Coach with an id of: " + coachId + " isnt the same coach with a coach code of: " + coachCode);
        }

        var authorizedCoachAuthToken = new CoachAuthToken(List.of(new SimpleGrantedAuthority("ROLE_COACH")), coachId, coachCode);
        var newSecurityContext = SecurityContextHolder.createEmptyContext();
        newSecurityContext.setAuthentication(authorizedCoachAuthToken);
        SecurityContextHolder.setContext(newSecurityContext);

        filterChain.doFilter(request, response);
    }
}
