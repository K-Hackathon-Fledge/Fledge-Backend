package com.fledge.fledgeserver.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fledge.fledgeserver.exception.dto.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.fledge.fledgeserver.exception.ErrorCode.MEMBER_NOT_FOUND;

@Slf4j
@Component
@RequiredArgsConstructor
public class GlobalExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            handleCustomException(response, e);
        } catch (UsernameNotFoundException e) {
            handleUsernameNotFoundException(response, e);
        }
    }

    private void handleCustomException(HttpServletResponse response, CustomException e) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        var errorResponse = ErrorResponse.toResponseEntity(e.getErrorCode(), e.getMessage()).getBody();

        String json = objectMapper.writeValueAsString(errorResponse);
        response.setStatus(errorResponse.getStatus());
        response.getWriter().write(json);
    }

    private void handleUsernameNotFoundException(HttpServletResponse response, UsernameNotFoundException e) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        var errorResponse = ErrorResponse.toResponseEntity(MEMBER_NOT_FOUND, e.getMessage()).getBody();

        String json = objectMapper.writeValueAsString(errorResponse);
        response.setStatus(errorResponse.getStatus());
        response.getWriter().write(json);
    }
}
