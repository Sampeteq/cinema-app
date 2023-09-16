package com.cinema;

import com.cinema.mails.domain.Mail;
import com.cinema.mails.domain.ports.MailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
@Slf4j
public class MockMailSender implements MailSender {

    @Override
    public void sendMail(Mail mail) {
      log.info("MailService is mocked");
    }
}
