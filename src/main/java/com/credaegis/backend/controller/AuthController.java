package com.credaegis.backend.controller;

import com.credaegis.backend.Constants;
import com.credaegis.backend.dto.request.LoginRequest;
import com.credaegis.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Constants.ROUTEV1+"/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/login")
    public void loginController(@Valid @RequestBody LoginRequest loginRequest,
                                HttpServletRequest request, HttpServletResponse response){
        System.out.println("sssss");
        authService.login(loginRequest,request,response);
    }
}
