package com.credaegis.backend.controller.member;


import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.http.request.LoginRequest;
import com.credaegis.backend.http.request.MfaLoginRequest;
import com.credaegis.backend.http.response.api.CustomApiResponse;
import com.credaegis.backend.http.response.custom.LoginResponse;
import com.credaegis.backend.http.response.custom.SessionCheckResponse;
import com.credaegis.backend.service.member.MemberAuthService;
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
@RequestMapping(value = Constants.ROUTE_V1_MEMBER +"/auth")
@AllArgsConstructor
public class MemberAuthController {

    private final MemberAuthService authService;

    @PostMapping(path = "/login")
    public ResponseEntity<CustomApiResponse<LoginResponse>> loginController(@Valid @RequestBody LoginRequest loginRequest,
                                                                            HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.status(HttpStatus.OK).body(new CustomApiResponse<>
                (authService.login(loginRequest,request,response),"login success",true));
    }


    @PostMapping(path = "/mfa/login")
    public ResponseEntity<CustomApiResponse<LoginResponse>> mfaLoginController(@Valid @RequestBody MfaLoginRequest mfaLoginRequest,
                                                                               HttpServletRequest request, HttpServletResponse
                                                                                       response){


        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(authService.mfaLogin(mfaLoginRequest,request,response),
                        "login success",true)
        );
    }


    @GetMapping(path = "/session-check")
//    @PreAuthorize("hasAnyRole('MEMBER','CLUSTER_ADMIN')")
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
