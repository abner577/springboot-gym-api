package practice.spring_gym_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import practice.spring_gym_api.security.filter.ValidRequestFilter;
import practice.spring_gym_api.security.filter.WorkerAuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ValidRequestFilter validRequestFilter;
    private final WorkerAuthFilter workerAuthFilter;

    public SecurityConfig(ValidRequestFilter validRequestFilter, WorkerAuthFilter workerAuthFilter) {
        this.validRequestFilter = validRequestFilter;
        this.workerAuthFilter = workerAuthFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(validRequestFilter, AuthorizationFilter.class)
                .addFilterBefore(workerAuthFilter, AuthorizationFilter.class)
                .build();
    }
}
