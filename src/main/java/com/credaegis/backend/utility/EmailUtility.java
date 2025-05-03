package com.credaegis.backend.utility;


import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailUtility {


    private final JavaMailSender javaMailSender;


    @Value("${credaegis.email}")
    private String credaegisEmail;

    @Value("${spring.mail.password}")
    private String credaegisEmailPassword;

    public  void sendEmail(String to, String subject, String html, InputStream stream, String fileName) {
        try{


            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(credaegisEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            helper.addInline("logo", new ClassPathResource("static/credaegis-logo.png"));
            if (stream != null)
                 helper.addAttachment(fileName, new ByteArrayResource(stream.readAllBytes()));
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
