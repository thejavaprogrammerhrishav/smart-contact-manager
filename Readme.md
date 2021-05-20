# Smart Contact Manager

### A complete contact manager solution

Made By: Hrishav Dhawaj Purkayastha | programmerhrishav1234@gmail.com

---

### Features

- Easy to Use
- Require Prior Registration
- Handy Dashboard
- Multiple Export Options
- Effective Search
- HTML Email For Forgot Password
- No Limit on Data Storage
- Disable Un-authorized access
- Fully Secured
- Profile & Change Password Options
- Beautiful Home Page

---
### Programming Language and Frameworks Used

- Java 1.8
- Mysql 
- Springboot
- Hibernate
- Spring Security
- Bootstrap

---

### Configuration

Please follow the steps to configure the project

- Step 1

  Properties Configuration
   ```properties
    spring.datasource.driver-class-name=your-driver-class-name
    spring.datasource.url=your-url-for-jdbc-to-connect-to-DB
    spring.datasource.username=username-for-DB
    spring.datasource.password=password-for-DB
    spring.jpa.show-sql=whether-to-show-sql-to-console
    spring.jpa.hibernate.ddl-auto=mechanism-to-create-tables <Preferred "update">
    spring.jpa.properties.hibernate.dialect=dialect-for-your-DB
    spring.jpa.properties.hibernate.format_sql=whether-to-format-sql-printed-to-console
    spring.application.name=smart-contact-manager
    server.port=your-server-port  [I preferred 1361, you can choose any]
   ```

- Step 2

  Forgot Password Email Configuration
    ```properties
    system.mail.username=email-to-be-used-to-send-otp-mails
    system.mail.password=password-for-email-to-be-used-to-send-otp-mails
    system.mail.host=smtp.gmail.com    #fixed unless you use different
    system.mail.port=465               #fixed unless you use different
    ```

---
Just make sure that under myaccount settings in gmail, in the Less Secure App Access *Allow less secure apps* must be
turned _ON_
for the email to be used as system mail as mentioned above.

In some cases, there may be some authentication error for sending OTP email after turning ON the less secure app access

In order to solve that follow:

[Display Captcha Gmail](https://accounts.google.com/DisplayUnlockCaptcha)

Actually you have to disable display captcha for the specified email as gmail shows a captcha verification to
authenticate user from unknown source and any other source that is not using proper oauth2 authentication
---
There may also be a chance that image upload error is also showing, in that case you have to configure the properties of
this project and also the settings of your database

# smart-contact-manager
