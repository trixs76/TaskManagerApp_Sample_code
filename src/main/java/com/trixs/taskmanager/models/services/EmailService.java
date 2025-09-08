package com.trixs.taskmanager.models.services;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;            //setup in application.properties

    public void sendTaskReminder(String to, String subject, String text) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // ✅ tu je správny Authenticator
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);
            System.out.println("✅ Email sent to " + to);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to sent mail!", e);
        }
    }
}