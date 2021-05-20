package com.smart.contact.util;

import com.smart.contact.user.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

@Component
public class EmailSender {

    @Value("${system.mail.username}")
    private String username;

    @Value("${system.mail.password}")
    private String password;

    @Value("${system.mail.host}")
    private String host;

    @Value("${system.mail.port}")
    private String port;

    public Verification message(User user) {
        Properties prop = new Properties();
        prop.setProperty("mail.smtp.host", host);
        prop.setProperty("mail.smtp.port", port);
        prop.setProperty("mail.smtp.ssl.enable", "true");
        prop.setProperty("mail.smtp.auth", "true");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(username);
            message.addRecipient(RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject("Smart Contact Manager - Password Reset");

            EmailContent instance = EmailContent.getInstance(user);

            Multipart part = new MimeMultipart();
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(instance.email(), "text/html");

            part.addBodyPart(bodyPart);
            message.setContent(part);

            Transport.send(message);

            return instance.getVerification();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
