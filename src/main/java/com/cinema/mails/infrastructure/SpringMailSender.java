package com.cinema.mails.infrastructure;

import com.cinema.mails.domain.Mail;
import com.cinema.mails.domain.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
@Profile("prod")
@RequiredArgsConstructor
class SpringMailSender implements MailSender {

    private final JavaMailSender javaMailSender;
    private final Clock clock;

    @Override
    public void sendMail(Mail mail) {
        var message = new SimpleMailMessage();
        message.setTo(mail.getReceiver());
        message.setSubject(mail.getSubject());
        message.setText(mail.getText());
        javaMailSender.send(message);
        mail.sentAt(clock);
    }
}
