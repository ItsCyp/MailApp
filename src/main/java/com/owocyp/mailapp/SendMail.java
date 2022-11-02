package com.owocyp.mailapp;

import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

    public static void Send(String to, String subject, String mailText) {
        try{
            FileReader reader = new FileReader("src\\main\\resources\\config.properties");
            Properties properties = new Properties();
            properties.load(reader);
            String usernameMailSender = properties.getProperty("usernameMailSender");
            String passwordMailSender = properties.getProperty("passwordMailSender");

            Base64.Decoder decoder = Base64.getDecoder();
            byte[] bytesUsername = decoder.decode(usernameMailSender);
            byte[] bytesPassword = decoder.decode(passwordMailSender);

            Properties prop = new Properties();
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "465");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.socketFactory.port", "465");
            prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            Session session = Session.getInstance(prop,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(new String(bytesUsername), new String(bytesPassword));
                        }
                    });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(new String(bytesUsername)));
                message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(to)
                );
                message.setSubject(subject);
                message.setText(mailText);

                Transport.send(message);

            } catch (MessagingException e) {
                e.printStackTrace();
            }

        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
