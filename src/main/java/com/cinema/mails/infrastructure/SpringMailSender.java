package com.cinema.mails.infrastructure;

import com.cinema.mails.domain.MailMessage;
import com.cinema.mails.domain.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
@Profile("prod")
@RequiredArgsConstructor
class SpringMailSender implements MailService {

    private final JavaMailSender javaMailSender;
    private final Clock clock;

    @Override
    public void sendMail(MailMessage mailMessage) {
        var message = new SimpleMailMessage();
        message.setTo(mailMessage.getReceiver());
        message.setSubject(mailMessage.getSubject());
        message.setText(mailMessage.getText());
        javaMailSender.send(message);
        mailMessage.sentAt(clock);
    }
}
