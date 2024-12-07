package com.credaegis.backend.controller;

import com.credaegis.backend.Constants;
import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.http.request.PasswordChangeRequest;
import com.credaegis.backend.http.response.api.CustomApiResponse;
import com.credaegis.backend.service.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Constants.ROUTEV1+"/account")
@AllArgsConstructor
public class AccountController {


    private final AccountService accountService;

    public ResponseEntity<CustomApiResponse<Void>> changePassword(@RequestBody @Valid
                                                                  PasswordChangeRequest passwordChangeRequest,
                                                                  @AuthenticationPrincipal CustomUser customUser) {

        accountService.changePassword(passwordChangeRequest.getEnteredPassword(),
                passwordChangeRequest.getNewPassword(),customUser.getUser().getId() );
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null,"Password changed successfully",true)
        );
    }
}
