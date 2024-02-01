package com.cinema.mails;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final Clock clock;

    @Async
    public void sendMail(MailMessage mailMessage) {
        var message = new SimpleMailMessage();
        message.setTo(mailMessage.getReceiver());
        message.setSubject(mailMessage.getSubject());
        message.setText(mailMessage.getText());
        javaMailSender.send(message);
        mailMessage.sentAt(clock);
    }
}
