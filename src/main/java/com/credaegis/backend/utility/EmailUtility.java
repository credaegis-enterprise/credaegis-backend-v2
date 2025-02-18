package com.credaegis.backend.utility;


import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
public class EmailUtility {


    private final JavaMailSender javaMailSender;

    public  void sendEmail(String to, String subject, String html, File file) {
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            if (file != null)
                 helper.addAttachment(file.getName(), file);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
