package com.cinema.mail.infrastructure.ioc;

import com.cinema.mail.domain.MailRepository;
import com.cinema.mail.domain.MailSenderPort;
import com.cinema.mail.domain.MailService;
import com.cinema.mail.infrastructure.db.JpaMailRepository;
import com.cinema.mail.infrastructure.db.JpaMailRepositoryAdapter;
import com.cinema.mail.infrastructure.smtp.SpringMailSenderPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;

import java.time.Clock;

@Configuration
class SpringMailIoc {

    @Bean
    MailService mailService(MailRepository mailRepository, MailSenderPort mailSenderPort, Clock clock) {
        return new MailService(mailSenderPort, mailRepository, clock);
    }

    @Bean
    MailRepository mailRepository(JpaMailRepository jpaMailRepository) {
        return new JpaMailRepositoryAdapter(jpaMailRepository);
    }

    @Bean
    MailSenderPort mailSenderPort(MailSender mailSender) {
        return new SpringMailSenderPort(mailSender);
    }
}
