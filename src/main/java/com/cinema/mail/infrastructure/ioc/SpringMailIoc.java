package com.cinema.mail.infrastructure.ioc;

import com.cinema.mail.domain.MailRepository;
import com.cinema.mail.domain.MailService;
import com.cinema.mail.infrastructure.db.JpaMailRepository;
import com.cinema.mail.infrastructure.db.JpaMailRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;

import java.time.Clock;

@Configuration
class SpringMailIoc {

    @Bean
    MailService mailService(MailRepository mailRepository, MailSender mailSender, Clock clock) {
        return new MailService(mailSender, mailRepository, clock);
    }

    @Bean
    MailRepository mailRepository(JpaMailRepository jpaMailRepository) {
        return new JpaMailRepositoryAdapter(jpaMailRepository);
    }
}
