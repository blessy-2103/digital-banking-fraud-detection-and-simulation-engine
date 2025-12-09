package com.fraudsim.engine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendFraudAlert(String toEmail, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(toEmail);
        mail.setSubject("⚠ Fraud Alert Notification");
        mail.setText(message);

        mailSender.send(mail);
    }
}
