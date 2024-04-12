package com.cinema.mail.application;

import com.cinema.mail.domain.Mail;
import com.cinema.mail.domain.MailRepository;
import com.cinema.mail.domain.MailSenderPort;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class MailService {

    private final MailSenderPort mailSenderPort;
    private final MailRepository mailRepository;
    private final Clock clock;

    public void sendMail(String receiver, String subject, String content) {
        var mail = new Mail(null, receiver, subject, content, LocalDateTime.now(clock));
        mailSenderPort.send(mail);
        mailRepository.save(mail);
    }
}
