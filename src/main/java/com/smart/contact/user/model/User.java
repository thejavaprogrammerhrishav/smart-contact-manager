package com.smart.contact.user.model;

import com.smart.contact.contacts.model.Contact;
import com.smart.contact.util.DateDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name Cannot Be Empty")
    @Size(min = 5, max = 50, message = "Name Length Should Be Within 5 - 50 Characters")
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotBlank(message = "Contact Cannot Be Empty")
    @Size(min = 10, max = 16, message = "Contact Length Invalid, must be between 10 - 16 characters")
    @Pattern(regexp = "^[6789]\\d{9}$", message = "Invalid Contact")
    @Column(name = "contact", length = 15, nullable = false)
    private String contact;

    @NotBlank(message = "Email Cannot Be Empty")
    @Email(message = "Invalid Email")
    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @NotBlank(message = "About Cannot Be Empty")
    @Size(min = 1, max = 520, message = "About Content Length Should Be Between 1 - 520 Characters")
    @Column(name = "about", length = 520, nullable = false)
    private String about;

    @NotBlank(message = "Username Cannot Be Empty")
    @Size(min = 6, max = 100, message = "Username Must Be Between 6 - 30 Characters")
    @Column(name = "username", length = 100, nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password Cannot Be Empty")
    @Column(name = "password", length = 520, nullable = false)
    private String password;

    @Lob
    @Column(name = "img", length = 1024 * 1024, nullable = false)
    private byte[] img;

    @Column(name = "role", length = 20, nullable = false)
    private String role;

    private boolean enabled;

    @Column(name = "date", length = 255, nullable = false)
    private String date;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user", orphanRemoval = true)
    private List<Contact> contactList = new ArrayList<>();

    public User() {
    }

    public User(Long id, String name, String contact, String email, String about, String username, String password, byte[] img, String role, boolean enabled, String date, List<Contact> contactList) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.about = about;
        this.username = username;
        this.password = password;
        this.img = img;
        this.role = role;
        this.enabled = enabled;
        this.date = date;
        this.contactList = contactList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, contact, email, about, username, password, role, enabled, date, contactList);
        result = 31 * result + Arrays.hashCode(img);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                ", about='" + about + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", enabled=" + enabled +
                ", date='" + date + '\'' +
                ", contactList=" + contactList +
                '}';
    }
}