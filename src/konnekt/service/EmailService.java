package konnekt.service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class EmailService {

    private final String fromEmail;
    private final String password;

    public EmailService() {
        Properties config = new Properties();
        try (InputStream input = getClass().getResourceAsStream("/konnekt/resources/config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found in resources");
            }
            config.load(input);
            fromEmail = config.getProperty("EMAIL_ADDRESS");
            password = config.getProperty("EMAIL_APP_PASSWORD");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load email configuration", e);
        }
    }

    public void sendEmail(String toEmail, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
           
            try {
                message.setFrom(new InternetAddress(fromEmail, "Konnekt"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                message.setFrom(new InternetAddress(fromEmail)); // fallback
            }

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent successfully to " + toEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}