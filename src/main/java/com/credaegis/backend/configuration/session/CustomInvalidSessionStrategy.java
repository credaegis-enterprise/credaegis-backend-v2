package com.credaegis.backend.configuration.session;

import com.credaegis.backend.http.response.exception.CustomExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomInvalidSessionStrategy implements InvalidSessionStrategy {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        CustomExceptionResponse customExceptionResponse = new CustomExceptionResponse(
                "Session expired",
                false
        );

        String json = objectMapper.writeValueAsString(customExceptionResponse);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(json);
    }

}
