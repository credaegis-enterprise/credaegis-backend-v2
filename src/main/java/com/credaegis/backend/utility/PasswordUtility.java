package com.credaegis.backend.utility;

import com.credaegis.backend.exception.custom.ExceptionFactory;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class PasswordUtility {


    private final PasswordEncoder passwordEncoder;

    public  Boolean isSamePassword(String password1, String password2) {

        return password1.equals(password2);
    }

    public  Boolean isOldPasswordCorrect(String oldPassword, String confirmOldPassword) {
        return  passwordEncoder.matches(confirmOldPassword,oldPassword);
    }

    public void isPasswordValid(String oldPassword,String confirmOldPassword, String newPassword,
                                       String confirmNewPassword) {

        System.out.println("Old Password: "+oldPassword);
        System.out.println("Confirm Old Password: "+confirmOldPassword);
        System.out.println("New Password: "+newPassword);
        System.out.println("Confirm New Password: "+confirmNewPassword);
        if(!isSamePassword(newPassword,confirmNewPassword)) {
            throw ExceptionFactory.customValidationError("Entered confirm password is not correct");
        }

        if(!isOldPasswordCorrect(oldPassword,confirmOldPassword)){
            throw ExceptionFactory.customValidationError("Passwords do not match with old password");

        }
        if(passwordEncoder.matches(newPassword,oldPassword)){
            throw ExceptionFactory.customValidationError("Password cannot be same as old one");
        }


    }
}
