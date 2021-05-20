package com.smart.contact.util;

import com.smart.contact.contacts.model.Contact;
import com.smart.contact.user.model.User;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;

public class HTMLExporter {

    private User user;
    private List<Contact> list;

    private HTMLExporter(User user) {
        this.user = user;
        this.list = user.getContactList();
    }

    public static HTMLExporter getInstance(User user) {
        return new HTMLExporter(user);
    }

    private String generateNode(Contact contact) {
        StringBuilder sb = new StringBuilder();
        sb.append("<tr>");
        sb.append("<td colspan=\"2\">").append(contact.getName()).append("</td>");
        sb.append("<td colspan=\"2\">").append(contact.getName()).append("</td>");
        sb.append("<td colspan=\"2\">");
        sb.append("<img src=\"data:image/png;base64,").append(Base64.getEncoder().encodeToString(contact.getImg())).append("\" style=\"width: 100% !important;\">");
        sb.append("</td>");
        sb.append("<td colspan=\"1\">").append(contact.getContact1()).append("</td>");
        sb.append("<td colspan=\"1\">").append(contact.getContact2()).append("</td>");
        sb.append("<td colspan=\"1\">").append(contact.getContact3()).append("</td>");
        sb.append("<td colspan=\"3\">").append(contact.getEmail()).append("</td>");
        sb.append("<td colspan=\"1\">").append(contact.getWork()).append("</td>");
        sb.append("<td colspan=\"1\">").append(contact.getDate()).append("</td>");
        sb.append("<td colspan=\"3\">").append(contact.getAbout()).append("</td>");
        sb.append("</tr>");

        return sb.toString();
    }

    private String fileReader() throws IOException {
        String path="static/files/export.html";
        String line;
        StringBuilder sb=new StringBuilder();

        File file=new ClassPathResource(path).getFile();
        InputStream in=new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        while((line=br.readLine()) != null){
            sb.append(line);
        }
        return sb.toString();
    }

    private String setUserData(String content) throws Exception {
        if(content != null){
            if(content.contains(UserFields.NAME.getText()))
                content=content.replace(UserFields.NAME.getText(),user.getName());

            if(content.contains(UserFields.CONTACT.getText()))
                content=content.replace(UserFields.CONTACT.getText(),user.getContact());

            if(content.contains(UserFields.EMAIL.getText()))
                content=content.replace(UserFields.EMAIL.getText(),user.getEmail());

            if(content.contains(UserFields.ABOUT.getText()))
                content=content.replace(UserFields.ABOUT.getText(),user.getAbout());

            if(content.contains(UserFields.DATE.getText()))
                content=content.replace(UserFields.DATE.getText(),user.getDate());

            return content;
        }else{
            throw new Exception("Empty Content");
        }
    }

    public String download(){
        try{
            String content=setUserData(fileReader());
            String nodes = list.stream().map(this::generateNode).collect(Collectors.joining(" "));
            if(content.contains("${contactList}"))
                content=content.replace("${contactList}",nodes);
            return content;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private enum UserFields{
        NAME("${user.name}"),CONTACT("${user.contact}"),EMAIL("${user.email}"),ABOUT("${user.about}"),DATE("${user.date}");

        private final String text;

        private UserFields(String text){
            this.text=text;
        }

        public String getText() {
            return text;
        }
    }
}

