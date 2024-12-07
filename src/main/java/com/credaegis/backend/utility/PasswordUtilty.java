package com.credaegis.backend.utility;

public class PasswordUtilty {

    public static Boolean isSamePassword(String password1, String password2) {
        return password1.equals(password2);
    }

    public static Boolean checkValidity(String password){
        return true;
    }
}
