package com.smart.contact.util;

import java.util.regex.Pattern;

public class PasswordMatcher {
    private static final String pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*!])(?=\\S+$).{8,20}$";
    public static boolean validate(String password){
       // return true;
        return Pattern.compile(pattern).matcher(password).matches();
    }
}
