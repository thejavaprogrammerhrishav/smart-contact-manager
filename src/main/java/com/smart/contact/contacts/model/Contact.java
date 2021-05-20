package com.smart.contact.contacts.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart.contact.user.model.User;
import java.util.Arrays;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 50, nullable = false)
    @NotBlank(message = "Name Cannot Be Empty")
    @Size(min = 1, max = 50, message = "Name Length Must Be Between 1 - 50 Characters")
    private String name;

    @Column(name = "nickname", length = 50, nullable = false)
    @NotBlank(message = "Nick Name Cannot Be Empty")
    @Size(min = 1, max = 50, message = "Nick Name Length Must Be Between 1 - 50 Characters")
    private String nickname;

    @Column(name = "contact1", length = 15, nullable = false)
    @NotBlank(message = "1st Contact Cannot Be Empty")
    @Size(min = 10, max = 16, message = "1st Contact Length Must Be Between 10 - 16 Characters")
    @Pattern(regexp = "^[6789]\\d{9}$", message = "Invalid Contact")
    private String contact1;

    @Column(name = "contact2", length = 15, nullable = false)
    @NotBlank(message = "2nd Contact Cannot Be Empty")
    @Size(min = 10, max = 16, message = "2nd Contact Length Must Be Between 10 - 16 Characters")
    @Pattern(regexp = "^[6789]\\d{9}$", message = "Invalid Contact")
    private String contact2;

    @Column(name = "contact3", length = 15, nullable = false)
    @NotBlank(message = "3rd Contact Cannot Be Empty")
    @Size(min = 10, max = 16, message = "3rd Contact Length Must Be Between 10 - 16 Characters")
    @Pattern(regexp = "^[6789]\\d{9}$", message = "Invalid Contact")
    private String contact3;

    @Column(name = "email", length = 255, nullable = false)
    @NotBlank(message = "Email Cannot Be Empty")
    @Email(message = "Invalid Email")
    private String email;

    @Column(name = "work", length = 255, nullable = false)
    @NotBlank(message = "Work Cannot Be Empty")
    @Size(min = 1, max = 255, message = "Work Length Must Be Between 1 - 255 Characters")
    private String work;

    @Column(name = "about", length = 520, nullable = false)
    @NotBlank(message = "About Cannot Be Empty")
    @Size(min = 1, max = 520, message = "About Length Must Be Between 10 - 16 Characters")
    private String about;

    @Lob
    @Column(name = "img", length = 1024 * 1024, nullable = false)
    private byte[] img;

    @Column(name = "date", length = 255, nullable = false)
    private String date;

    @ManyToOne
    @JoinColumn(name = "userid")
    @JsonIgnore
    private User user;

    public Contact() {
    }

    public Contact(long id, String name, String nickname, String contact1, String contact2, String contact3, String email, String work, String about, byte[] img, String date, User user) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.contact1 = contact1;
        this.contact2 = contact2;
        this.contact3 = contact3;
        this.email = email;
        this.work = work;
        this.about = about;
        this.img = img;
        this.date = date;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContact1() {
        return contact1;
    }

    public void setContact1(String contact1) {
        this.contact1 = contact1;
    }

    public String getContact2() {
        return contact2;
    }

    public void setContact2(String contact2) {
        this.contact2 = contact2;
    }

    public String getContact3() {
        return contact3;
    }

    public void setContact3(String contact3) {
        this.contact3 = contact3;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return id == contact.id && name.equals(contact.name) && nickname.equals(contact.nickname) && contact1.equals(contact.contact1) && contact2.equals(contact.contact2) && contact3.equals(contact.contact3) && email.equals(contact.email) && work.equals(contact.work) && about.equals(contact.about) && Arrays.equals(img, contact.img) && date.equals(contact.date) && user.equals(contact.user);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, nickname, contact1, contact2, contact3, email, work, about, date, user);
        result = 31 * result + Arrays.hashCode(img);
        return result;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", contact1='" + contact1 + '\'' +
                ", contact2='" + contact2 + '\'' +
                ", contact3='" + contact3 + '\'' +
                ", email='" + email + '\'' +
                ", work='" + work + '\'' +
                ", about='" + about + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}