package com.frosqh.bigbrother;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import javax.mail.Session;

public class SendMail {

    public static void send(String address, String content, String subject, boolean html){
        String to = address;

        String from = "BigBrother";

        String host = "smtp.gmail.com";

        String username = "frosko.bigbrother@gmail.com";

        String password = "frok-bigbrother";

        Properties properties = System.getProperties();

        properties.setProperty("mail.smtp.host",host);
        properties.setProperty("mail.smtp.auth","true");
        properties.setProperty("mail.smtp.port","587");
        properties.setProperty("mail.smtp.starttls.enable","true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);
            }
        });

        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject(subject);
            if (html){
                message.setText(content,"utf-8","html");
            } else {
                message.setText(content);
            }

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
