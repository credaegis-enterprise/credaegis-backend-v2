package com.credaegis.backend.service;

import com.credaegis.backend.constant.Constants;
import com.credaegis.backend.dto.OrganizationInfoDTO;
import com.credaegis.backend.entity.User;
import com.credaegis.backend.exception.custom.CustomException;
import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.http.request.PasswordChangeRequest;
import com.credaegis.backend.http.response.custom.AccountInfoResponse;
import com.credaegis.backend.repository.UserRepository;
import com.credaegis.backend.utility.PasswordUtility;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class AccountService {

    private final UserRepository userRepository;
    private final PasswordUtility passwordUtility;
    private final PasswordEncoder passwordEncoder;
    private SecretGenerator secretGenerator;
    private QrGenerator qrGenerator;
    private CodeVerifier codeVerifier;



    public AccountInfoResponse getMe(String userId){

        User user = userRepository.findById(userId).orElseThrow(ExceptionFactory::resourceNotFound);
        OrganizationInfoDTO organizationInfoDTO = OrganizationInfoDTO.builder()
                .id(user.getOrganization().getId())
                .name(user.getOrganization().getName())
                .address(user.getOrganization().getAddress())
                .pincode(user.getOrganization().getPincode())
                .build();

        return AccountInfoResponse.builder()
                .user(user)
                .organizationInfoDTO(organizationInfoDTO)
                .build();

    }

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


    public String generateQrCodeMfa(String email,String userId) {

        try {
            String secret = secretGenerator.generate();
            QrData data = new QrData.Builder()
                    .label(email)
                    .secret(secret)
                    .digits(6)
                    .algorithm(HashingAlgorithm.SHA1)
                    .issuer(Constants.APP_NAME)
                    .build();

            userRepository.updateMfaSecret(secret, userId);
            return getDataUriForImage(qrGenerator.generate(data), qrGenerator.getImageMimeType());
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw new CustomException("Error in generating qr-code", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public Boolean registerMfa(String code, String userId){
        User user = userRepository.findById(userId).orElseThrow(ExceptionFactory::resourceNotFound);
        if(user.getMfaEnabled()){
            throw ExceptionFactory.customValidationError("mfa is already enabled");
        }
        if(codeVerifier.isValidCode(user.getMfaSecret(),code)){
            userRepository.enableMfa(true,userId);
            return true;
        }
        else{
            throw ExceptionFactory.customValidationError("Mfa registration failed, incorrect otp");
        }

    }

    public void disableMfa(String userId){
        User user = userRepository.findById(userId).orElseThrow(ExceptionFactory::resourceNotFound);
        if(!user.getMfaEnabled()){
            throw ExceptionFactory.customValidationError("mfa is already disabled");
        }
        userRepository.enableMfa(false,userId);
    }




}
