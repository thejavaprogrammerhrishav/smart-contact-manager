package com.smart.contact.util;

import java.util.Random;
import java.util.UUID;

public class VerificationCodeGenerator {
    private static final String OTP_NOS="0123456789";

    private static String genOTP(){
        Random r=new Random();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<6;i++){
            sb.append(OTP_NOS.charAt(r.nextInt(10)));
        }
        return sb.toString();
    }

    public static Verification getCodes(){
        return new Verification(genOTP(), UUID.randomUUID().toString());
    }
}
