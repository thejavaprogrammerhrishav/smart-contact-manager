package com.smart.contact.util;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class PasswordDto {

    private String password;
    private String cpassword;

    public PasswordDto(String password, String cpassword) {
        this.password = password;
        this.cpassword = cpassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCpassword() {
        return cpassword;
    }

    public void setCpassword(String cpassword) {
        this.cpassword = cpassword;
    }

    public boolean isEmpty(){
        return password.isEmpty() && cpassword.isEmpty();
    }

    @Override
    public String toString() {
        return "PasswordDto{" +
                "password='" + password + '\'' +
                ", cpassword='" + cpassword + '\'' +
                '}';
    }
}
