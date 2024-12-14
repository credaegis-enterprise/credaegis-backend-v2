package com.credaegis.backend.controller;

import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.configuration.security.principal.CustomUser;
import com.credaegis.backend.entity.User;
import com.credaegis.backend.exception.custom.CustomException;
import com.credaegis.backend.http.request.PasswordChangeRequest;
import com.credaegis.backend.http.response.api.CustomApiResponse;
import com.credaegis.backend.http.response.custom.AccountInfoResponse;
import com.credaegis.backend.service.AccountService;
import dev.samstevens.totp.exceptions.QrGenerationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = Constants.ROUTEV1 + "/account")
@AllArgsConstructor
@Slf4j
public class AccountController {


    private final AccountService accountService;


    @GetMapping(path = "/me")
    public ResponseEntity<CustomApiResponse<AccountInfoResponse>> getMe(@AuthenticationPrincipal CustomUser customUser) {
        AccountInfoResponse accountInfoResponse = accountService.getMe(customUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(accountInfoResponse,"User details", true)
        );
    }



    @PutMapping(path = "/change-password")
    public ResponseEntity<CustomApiResponse<Void>> changePassword(@RequestBody @Valid
                                                                  PasswordChangeRequest passwordChangeRequest,
                                                                  @AuthenticationPrincipal CustomUser customUser,
                                                                  HttpServletRequest request,
                                                                  HttpServletResponse response) {

        accountService.changePassword(passwordChangeRequest,
                customUser.getPassword(),
                customUser.getId(),
                request, response);
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Password changed successfully", true)
        );
    }

    @PostMapping(path = "/mfa/register/{code}")
    public ResponseEntity<CustomApiResponse<Void>> registerMfa(@AuthenticationPrincipal CustomUser
                                                                       customUser, @PathVariable
                                                               String code) {

        Boolean success = accountService.registerMfa(code, customUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Mfa successfully registered", success)
        );
    }

    @PostMapping(path = "/mfa/generate-qr")
    public ResponseEntity<CustomApiResponse<String>> generateQr(@AuthenticationPrincipal CustomUser customUser)
            throws QrGenerationException {


        String imageUri = accountService.generateQrCodeMfa(customUser.getEmail(), customUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(imageUri, "QR code", true)
        );

    }

    @PutMapping(path = "/mfa/disable")
    public ResponseEntity<CustomApiResponse<Void>> disableMfa(@AuthenticationPrincipal CustomUser customUser) {
        accountService.disableMfa(customUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                new CustomApiResponse<>(null, "Mfa disabled", true)
        );
    }


}
