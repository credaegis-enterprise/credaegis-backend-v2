package com.credaegis.backend.controller.organization;

import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.http.request.LoginRequest;
import com.credaegis.backend.http.request.MfaLoginRequest;
import com.credaegis.backend.http.request.PasswordResetRequest;
import com.credaegis.backend.http.response.api.CustomApiResponse;
import com.credaegis.backend.http.response.custom.LoginResponse;
import com.credaegis.backend.http.response.custom.SessionCheckResponse;
import com.credaegis.backend.service.organization.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = Constants.ROUTE_V1_ORGANIZATION +"/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;



    @PostMapping(path = "/reset-password")
    public ResponseEntity<CustomApiResponse<Void>> forgotPasswordController(@Valid @RequestBody PasswordResetRequest passwordResetRequest){

        authService.resetPassword(passwordResetRequest.getNewPassword(),passwordResetRequest.getConfirmPassword(),passwordResetRequest.getResetToken(),passwordResetRequest.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(new CustomApiResponse<>(null,"Password reset success, login with new password",true));
    }

    @PostMapping(path = "/forgot-password")
    public ResponseEntity<CustomApiResponse<Void>> forgotPasswordController(@RequestParam String email){

        authService.forgotPassword(email);
        return ResponseEntity.status(HttpStatus.OK).body(new CustomApiResponse<>(null,"Password reset link sent to email",true));
    }

    @PostMapping(path = "/login")
    public ResponseEntity<CustomApiResponse<LoginResponse>> loginController(@Valid @RequestBody LoginRequest loginRequest,
                                                                            HttpServletRequest request, HttpServletResponse response){
       Boolean mfaEnabled = authService.login(loginRequest,request,response);
        return ResponseEntity.status(HttpStatus.OK).body(new CustomApiResponse<>
                (new LoginResponse(mfaEnabled,Constants.ADMIN,Constants.ORGANIZATION_ACCOUNT_TYPE),"login success",true));
    }


    @PostMapping(path = "/mfa/login")
    public ResponseEntity<CustomApiResponse<LoginResponse>> mfaLoginController(@Valid @RequestBody MfaLoginRequest mfaLoginRequest,
                                                                      HttpServletRequest request, HttpServletResponse
                                                                                  response){

        authService.mfaLogin(mfaLoginRequest,request,response);
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(new LoginResponse(true,Constants.ADMIN,Constants.ORGANIZATION_ACCOUNT_TYPE),
                        "login success",true)
        );
    }


    @GetMapping(path = "/session-check")
    public ResponseEntity<CustomApiResponse<SessionCheckResponse>> sessionCheckController(@AuthenticationPrincipal CustomUser customUser,
                                                                                          Authentication authentication){
        if(authentication != null && authentication.isAuthenticated() )
            return ResponseEntity.status(HttpStatus.OK).
                    body(new CustomApiResponse<>(authService.sessionCheck(customUser.getId()),null,true));
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).
                    body(new CustomApiResponse<>(null,"Session expired",false));
    }
}
