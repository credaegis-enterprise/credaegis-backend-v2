package com.credaegis.backend.configuration.security;

import com.credaegis.backend.http.response.exception.CustomExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {


        String message = authException.getClass().getSimpleName().equals("BadCredentialsException") ?  "Incorrect email pr password, please try again" :  "Unauthorized, please login";
        CustomExceptionResponse customExceptionResponse = new CustomExceptionResponse(
                message,
                false
        );

        String json = objectMapper.writeValueAsString(customExceptionResponse);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(json);
    }
}
