package ru.clevertec.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.clevertec.exception.handling.model.IncorrectData;
import ru.clevertec.filter.ExceptionFilter;
import ru.clevertec.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter filter;
    private final ExceptionFilter exceptionFilter;
    private final ObjectMapper mapper;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(handling ->
                        handling.authenticationEntryPoint(
                                        (request, response, ex) -> exceptionFilter.handleException(response, ex, HttpStatus.UNAUTHORIZED))
                                .accessDeniedHandler((request, response, authException) -> {
                                    int status = HttpStatus.FORBIDDEN.value();
                                    response.setCharacterEncoding("utf-8");
                                    response.setContentType("application/json");
                                    response.setStatus(status);
                                    response.getWriter()
                                            .write(mapper.writeValueAsString(new IncorrectData(LocalDateTime.now(), authException.getMessage(), status)));
                                }))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        authorize
                                .anyRequest().permitAll())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionFilter, JwtFilter.class)
                .build();
    }
}