package com.credaegis.backend.service.organization;

import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.entity.User;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.http.request.LoginRequest;
import com.credaegis.backend.http.request.MfaLoginRequest;
import com.credaegis.backend.http.response.custom.SessionCheckResponse;
import com.credaegis.backend.repository.UserRepository;
import com.credaegis.backend.utility.EmailUtility;
import com.credaegis.backend.utility.PasswordUtility;
import dev.samstevens.totp.code.CodeVerifier;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager customAuthenticationManager;
    private final CodeVerifier codeVerifier;
    private final UserRepository userRepository;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final RabbitTemplate rabbitTemplate;
    private final PasswordEncoder passwordEncoder;
    private final PasswordUtility passwordUtility;
    private final EmailUtility emailUtility;


    @Value("${credaegis.frontend.base-url}")
    private String baseUrl;



    public void resetPassword(String newPassword,String confirmPassword, String resetToken,String email){

        User user = userRepository.findByEmail(email).orElseThrow(()->ExceptionFactory.customValidationError("Email not found, incorrect email entered"));

        if(!passwordUtility.isSamePassword(newPassword,confirmPassword))
            throw ExceptionFactory.customValidationError("Passwords do not match");

        if(user.getPasswordResetToken()==null || !user.getPasswordResetToken().equals(resetToken))
            throw ExceptionFactory.customValidationError("Incorrect reset token entered");

        if(System.currentTimeMillis() - user.getPasswordResetTokenCreationTime().getTime() > 300000)
            throw ExceptionFactory.customValidationError("Token has been expired");




        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenCreationTime(null);
        userRepository.save(user);
    }

    public void forgotPassword(String recipientEmail) {


        User user = userRepository.findByEmail(recipientEmail).orElseThrow(
                () -> ExceptionFactory.customValidationError("Email not found, incorrect email entered")
        );

        if(user.getPasswordResetToken()!=null){
            if(System.currentTimeMillis() - user.getPasswordResetTokenCreationTime().getTime() < 120000)
                throw ExceptionFactory.customValidationError("Password reset link already sent, please wait for "+
                        (120000 - (System.currentTimeMillis() - user.getPasswordResetTokenCreationTime().getTime()))/1000+" seconds for trying again");
        }

        user.setPasswordResetToken(UUID.randomUUID().toString());
        user.setPasswordResetTokenCreationTime(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);


        String resetLink = baseUrl+"/reset-password?token="+user.getPasswordResetToken()+"&email="+recipientEmail;
        String htmlContent =
                "<html>" +
                        "<body style='margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;'>" +
                        "<table align='center' width='100%' border='0' cellpadding='0' cellspacing='0' style='margin: 0; padding: 20px;'>" +
                        "<tr>" +
                        "<td align='center'>" +
                        "<table width='600px' border='0' cellpadding='0' cellspacing='0' style='background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);'>" +
                        "<tr>" +
                        "<td style='padding: 20px;'>" +
                        "<h2 style='color: #333333;'>Password Reset Request</h2>" +
                        "<p style='color: #555555;'>Hello,</p>" +
                        "<p style='color: #555555;'>We received a request to reset your password. The link is valid for 2 minutes, Please click the button below to reset your password:</p>" +
                        "<table align='center' border='0' cellpadding='0' cellspacing='0' style='margin: 20px auto;'>" +
                        "<tr>" +
                        "<td align='center' bgcolor='#007bff' style='border-radius: 5px;'>" +
                        "<a href= '"+resetLink+"' " +
                        "style='display: inline-block; padding: 10px 20px; color: #ffffff; text-decoration: none; font-size: 16px;'>" +
                        "Reset Password" +
                        "</a>" +
                        "</td>" +
                        "</tr>" +
                        "</table>" +
                        "<p style='color: #555555;'>If you did not request this, you can safely ignore this email.</p>" +
                        "<p style='color: #555555;'>Thanks,</p>" +
                        "<p style='color: #555555;'>The Team</p>" +
                        "</td>" +
                        "</tr>" +
                        "</table>" +
                        "</td>" +
                        "</tr>" +
                        "</table>" +
                        "</body>" +
                        "</html>"
        ;


         emailUtility.sendEmail(recipientEmail,"Password Reset Request",htmlContent,null,null);
//        rabbitTemplate.convertAndSend(Constants.DIRECT_EXCHANGE,Constants.EMAIL_QUEUE_KEY,emailDTO);


    }


    public Boolean login(@Valid LoginRequest loginRequest, HttpServletRequest
            request, HttpServletResponse response) {

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
                () -> ExceptionFactory.customValidationError("Invalid email")
        );


        log.error("User role: {}", user.getRole().getRole());
        if(!user.getRole().getRole().equals("ROLE_ADMIN"))
            throw ExceptionFactory.customValidationError("Only admins can login here");

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


        User user = userRepository.findByEmail(mfaLoginRequest.getEmail()).orElseThrow(
                () -> ExceptionFactory.customValidationError("Invalid email")
        );

        if(!user.getRole().getRole().equals("ROLE_ADMIN"))
            throw ExceptionFactory.customValidationError("Only admins can login here");

        if(!user.getMfaEnabled())
            throw  ExceptionFactory.customValidationError("Mfa is not enabled in your account");

        if(!codeVerifier.isValidCode(user.getMfaSecret(),mfaLoginRequest.getOtp()))
            throw ExceptionFactory.accessDeniedException("Entered OTP is incorrect");


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

    public SessionCheckResponse sessionCheck(String userId){

        User user = userRepository.findById(userId).orElseThrow(
                () -> ExceptionFactory.accessDeniedException("User not found")
        );
        return SessionCheckResponse.builder()
                .accountType(Constants.ORGANIZATION_ACCOUNT_TYPE)
                .role(user.getRole().getRole().substring(5))
                .build();
    }


}
