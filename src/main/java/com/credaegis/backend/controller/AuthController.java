package com.credaegis.backend.controller;

import com.credaegis.backend.dto.request.LoginRequest;
import com.credaegis.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping(path = "/login")
    public void loginController(@Valid @RequestBody LoginRequest loginRequest,
                                HttpServletRequest request, HttpServletResponse response){
        authService.login(loginRequest,request,response);
    }
}
