package com.cinema.mail.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;

import java.time.Clock;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class MailService {

    private final MailSender mailSender;
    private final MailRepository mailRepository;
    private final Clock clock;

    @Async
    public void sendMail(String receiver, String subject, String content) {
        var message = new SimpleMailMessage();
        message.setTo(receiver);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
        mailRepository.save(new Mail(null, receiver, subject, content, LocalDateTime.now(clock)));
    }
}
