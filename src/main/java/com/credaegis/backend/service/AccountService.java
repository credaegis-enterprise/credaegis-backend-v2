package com.credaegis.backend.service;

import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.http.request.PasswordChangeRequest;
import com.credaegis.backend.repository.UserRepository;
import com.credaegis.backend.utility.PasswordUtility;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class AccountService {

    private final UserRepository userRepository;
    private final PasswordUtility passwordUtility;
    private final PasswordEncoder passwordEncoder;
//    private SecretGenerator secretGenerator;
//    private QrDataFactory qrDataFactory;
//    private QrGenerator qrGenerator;

    public void changePassword(PasswordChangeRequest passwordChangeRequest,
                               String oldPassword,
                               String userId,
                               HttpServletRequest request, HttpServletResponse response) {

        passwordUtility.isPasswordValid(oldPassword,passwordChangeRequest.getOldPassword(),
        passwordChangeRequest.getNewPassword(),passwordChangeRequest.getConfirmPassword());
        userRepository.updatePassword(userId, passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            request.getSession().invalidate();
        }

    }


    public void registerQrCode(){

    }




}
