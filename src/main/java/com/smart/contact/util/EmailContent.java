package com.smart.contact.util;

import com.smart.contact.contacts.model.Contact;
import com.smart.contact.user.model.User;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class EmailContent {

    private User user;
    private Verification verification;

    private EmailContent(User user) {
        this.user = user;
    }

    public static EmailContent getInstance(User user) {
        return new EmailContent(user);
    }


    private String fileReader() throws IOException {
        String path = "static/files/otp.html";
        String line;
        StringBuilder sb = new StringBuilder();

        File file = new ClassPathResource(path).getFile();
        InputStream in = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    private String setUserData(String content) throws Exception {
        if (content != null) {
            verification = VerificationCodeGenerator.getCodes();

            if (content.contains(UserFields.NAME.getText()))
                content = content.replace(UserFields.NAME.getText(), user.getName());

            if (content.contains(UserFields.USERNAME.getText()))
                content = content.replace(UserFields.USERNAME.getText(), user.getUsername());

            if (content.contains(UserFields.OTP.getText()))
                content = content.replace(UserFields.OTP.getText(), verification.getCode());

            if (content.contains(UserFields.KEY.getText()))
                content = content.replace(UserFields.KEY.getText(), verification.getUuid());


            return content;
        } else {
            throw new Exception("Empty Content");
        }
    }

    public String email() throws Exception {
        return setUserData(fileReader());
    }

    public Verification getVerification() {
        return verification;
    }

    private enum UserFields {
        NAME("${NAME}"), USERNAME("${USERNAME}"), OTP("${OTP_CODE}"), KEY("${VERIFICATION_KEY}");

        private final String text;

        private UserFields(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}

