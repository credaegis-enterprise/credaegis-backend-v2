package com.credaegis.backend.service;

import com.credaegis.backend.exception.custom.ExceptionFactory;
import com.credaegis.backend.repository.UserRepository;
import com.credaegis.backend.utility.PasswordUtilty;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountService {

    private UserRepository userRepository;

    public void changePassword(String enteredPassword , String newPassword,String userId) {
        if(!PasswordUtilty.isSamePassword(enteredPassword,newPassword)) {
            throw ExceptionFactory.customValidationError("Entered password doesn't match");
        }

        userRepository.updatePassword(userId, newPassword);

    }


}
