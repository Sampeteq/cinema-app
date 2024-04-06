package com.cinema.mail.domain;

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
