package com.credaegis.backend.controller;

import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.http.request.LoginRequest;
import com.credaegis.backend.http.response.api.CustomApiResponse;
import com.credaegis.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = Constants.ROUTEV1+"/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/login")
    public ResponseEntity<CustomApiResponse<Void>> loginController(@Valid @RequestBody LoginRequest loginRequest,
                                                             HttpServletRequest request, HttpServletResponse response){
        authService.login(loginRequest,request,response);
        return ResponseEntity.status(HttpStatus.OK).body(new CustomApiResponse<>
                (null,"Successfully logged in",true));
    }


    @GetMapping(path = "/session-check")
    public ResponseEntity<CustomApiResponse<Void>> sessionCheckController(Authentication authentication){
        if(authentication.isAuthenticated())
            return ResponseEntity.status(HttpStatus.OK).
                    body(new CustomApiResponse<>(null,null,true));
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                    body(new CustomApiResponse<>(null,null,false));
    }
}
