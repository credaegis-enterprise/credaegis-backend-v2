package com.credaegis.backend.controller;

import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.http.request.PasswordChangeRequest;
import com.credaegis.backend.http.response.api.CustomApiResponse;
import com.credaegis.backend.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = Constants.ROUTEV1 + "/account")
@AllArgsConstructor
public class AccountController {


    private final AccountService accountService;

    @PostMapping(path = "/change-password")
    public ResponseEntity<CustomApiResponse<Void>> changePassword(@RequestBody @Valid
                                                                  PasswordChangeRequest passwordChangeRequest,
                                                                  @AuthenticationPrincipal CustomUser customUser,
                                                                  HttpServletRequest request,
                                                                  HttpServletResponse response) {

        accountService.changePassword(passwordChangeRequest,
                customUser.getPassword(),
                customUser.getUser().getId(),
                request, response);
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Password changed successfully", true)
        );
    }
}
