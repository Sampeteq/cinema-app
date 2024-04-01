package com.cinema.mail;

import com.cinema.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailSender mailSender;
    private final MailRepository mailRepository;
    private final Clock clock;

    @Async
    public void sendMail(String subject, String content, User user) {
        var message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setText(content);
        message.setTo(user.getMail());
        mailSender.send(message);
        mailRepository.save(new Mail(subject, content, LocalDateTime.now(clock), user));
    }
}
