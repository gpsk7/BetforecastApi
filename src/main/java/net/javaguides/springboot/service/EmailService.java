package net.javaguides.springboot.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class EmailService {
    public void sendEmail(String messageBody, String toEmail, String subject) throws MessagingException, UnsupportedEncodingException, UnsupportedEncodingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session2 = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("bairaboinamahesh1316@gmail.com","fsotfnvuyhnlsccm");
            }
        });

        MimeMessage msg = new MimeMessage(session2);
        //set message headers
        msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
        msg.addHeader("format", "flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");

        msg.setFrom(new InternetAddress("bairaboinamahesh1316@gmail.com", "Betforecast"));

        msg.setReplyTo(InternetAddress.parse("support@suchierp.com", false));

        msg.setSubject(subject, "UTF-8");

      //  msg.setContent(messageBody, "text/HTML; charset=UTF-8");
         msg.setText(messageBody, "UTF-8");


        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
        Transport.send(msg);
    }
}
