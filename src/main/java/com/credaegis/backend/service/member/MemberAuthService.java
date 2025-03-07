package com.credaegis.backend.service.member;



import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.entity.User;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.http.request.LoginRequest;
import com.credaegis.backend.http.request.MfaLoginRequest;
import com.credaegis.backend.http.response.custom.LoginResponse;
import com.credaegis.backend.http.response.custom.SessionCheckResponse;
import com.credaegis.backend.repository.UserRepository;
import dev.samstevens.totp.code.CodeVerifier;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class MemberAuthService {


    private final AuthenticationManager customAuthenticationManager;
    private final CodeVerifier codeVerifier;
    private final UserRepository userRepository;

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public LoginResponse login(@Valid LoginRequest loginRequest, HttpServletRequest
            request, HttpServletResponse response) {

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
                () -> ExceptionFactory.customValidationError("Invalid email")
        );

        String role = user.getRole().getRole().substring(5);
        if(!role.equals(Constants.MEMBER) && !role.equals(Constants.CLUSTER_ADMIN))
            throw ExceptionFactory.customValidationError("Only members and cluster admins can login here");


        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );


        SecurityContext securityContext = authenticator(authenticationRequest);

        if(!user.getMfaEnabled())
            securityContextRepository.saveContext(securityContext,request,response);

        return  LoginResponse.builder()
                .mfaEnabled(user.getMfaEnabled())
                .role(user.getRole().getRole().substring(5))
                .accountType(Constants.MEMBER_ACCOUNT_TYPE)
                .build();


    }

    public LoginResponse mfaLogin(@Valid MfaLoginRequest mfaLoginRequest, HttpServletRequest
            request, HttpServletResponse response) {


        User user = userRepository.findByEmail(mfaLoginRequest.getEmail()).orElseThrow(
                () -> ExceptionFactory.accessDeniedException("Invalid email")
        );

        String role = user.getRole().getRole().substring(5);

        if(!role.equals(Constants.MEMBER) && !role.equals(Constants.CLUSTER_ADMIN))
            throw ExceptionFactory.customValidationError("Only members and cluster admins can login here");


        if(!user.getMfaEnabled())
            throw  ExceptionFactory.customValidationError("Mfa is not enabled in your account");

        if(!codeVerifier.isValidCode(user.getMfaSecret(),mfaLoginRequest.getOtp()))
            throw ExceptionFactory.accessDeniedException("Entered OTP is incorrect");

        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(
                mfaLoginRequest.getEmail(),
                mfaLoginRequest.getPassword()
        );

        securityContextRepository.saveContext(authenticator(authenticationRequest),request,response);
        return LoginResponse.builder()
                .accountType(Constants.MEMBER_ACCOUNT_TYPE)
                .role(user.getRole().getRole().substring(5))
                .mfaEnabled(true)
                .build();
    }

    private SecurityContext authenticator(Authentication authenticationRequest) {
        Authentication authenticationResponse = customAuthenticationManager.authenticate(authenticationRequest);
        System.out.println(authenticationResponse.isAuthenticated());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authenticationResponse);
        return securityContext;
    }


    public SessionCheckResponse sessionCheck(String userId){

        User user = userRepository.findById(userId).orElseThrow(
                () -> ExceptionFactory.accessDeniedException("User not found")
        );
        return SessionCheckResponse.builder()
                .accountType(Constants.MEMBER_ACCOUNT_TYPE)
                .role(user.getRole().getRole().substring(5))
                .build();
    }


}
