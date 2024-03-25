package com.cinema.mails.domain;

import com.cinema.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailRepository mailRepository;
    private final JavaMailSender javaMailSender;
    private final Clock clock;

    @Async
    public void sendMail(User user, String subject, String content) {
        var message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setText(content);
        message.setTo(user.getMail());
        javaMailSender.send(message);
        mailRepository.save(new Mail(subject, content, LocalDateTime.now(clock), user));
    }
}
