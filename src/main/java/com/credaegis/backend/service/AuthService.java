package com.credaegis.backend.service;

import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.entity.User;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.http.request.LoginRequest;
import com.credaegis.backend.http.request.MfaLoginRequest;
import com.credaegis.backend.repository.UserRepository;
import dev.samstevens.totp.code.CodeVerifier;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {


    private final AuthenticationManager customAuthenticationManager;
    private final CodeVerifier codeVerifier;
    private final UserRepository userRepository;

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public Boolean login(@Valid LoginRequest loginRequest, HttpServletRequest
            request, HttpServletResponse response) {

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
                () -> ExceptionFactory.customValidationError("Invalid email")
        );

        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        securityContextRepository.saveContext(authenticator(authenticationRequest),request,response);
        if (user.getMfaEnabled())
            return true;

        return false;

    }

    public void mfaLogin(@Valid MfaLoginRequest mfaLoginRequest, HttpServletRequest
            request, HttpServletResponse response) {


        System.out.println("skskjskjskjksjk");
        User user = userRepository.findByEmail(mfaLoginRequest.getEmail()).orElseThrow(
                () -> ExceptionFactory.customValidationError("Invalid email")
        );
        if(!user.getMfaEnabled())
            throw  ExceptionFactory.customValidationError("Mfa is not enabled in your account");

        if(!codeVerifier.isValidCode(user.getMfaSecret(),mfaLoginRequest.getOtp()))
            throw ExceptionFactory.accessDeniedException("Entered OTP is incorrect");


        log.error("MFA login request for user: {}",mfaLoginRequest.getEmail());
        log.error("MFA login request for user: {}",mfaLoginRequest.getPassword());
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(
                mfaLoginRequest.getEmail(),
                mfaLoginRequest.getPassword()
        );

       securityContextRepository.saveContext(authenticator(authenticationRequest),request,response);
    }

    private SecurityContext authenticator(Authentication authenticationRequest) {
        Authentication authenticationResponse = customAuthenticationManager.authenticate(authenticationRequest);
        System.out.println(authenticationResponse.isAuthenticated());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authenticationResponse);
        return securityContext;
    }


}
