package ru.clevertec.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.clevertec.exception.handling.model.IncorrectData;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RestClientException exception) {
            handleException(response, exception, HttpStatus.SERVICE_UNAVAILABLE);
        } catch (RuntimeException exception) {
            handleException(response, exception, HttpStatus.UNAUTHORIZED);
        }
    }

    public void handleException(HttpServletResponse response, Exception exception, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        response.setCharacterEncoding("utf-8");
        IncorrectData incorrectData = new IncorrectData(
                LocalDateTime.now(),
                exception.getMessage(),
                status.value());
        String responseMessage = mapper.writeValueAsString(incorrectData);
        response.getWriter().write(responseMessage);
    }
}