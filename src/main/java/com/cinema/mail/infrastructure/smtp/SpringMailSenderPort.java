package com.cinema.mail.infrastructure.smtp;

import com.cinema.mail.domain.Mail;
import com.cinema.mail.domain.MailSenderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
public class SpringMailSenderPort implements MailSenderPort {

    private final MailSender mailSender;

    @Override
    @Async
    public void send(Mail mail) {
        var message = new SimpleMailMessage();
        message.setTo(mail.getReceiver());
        message.setSubject(mail.getSubject());
        message.setText(mail.getContent());
        mailSender.send(message);
    }
}
