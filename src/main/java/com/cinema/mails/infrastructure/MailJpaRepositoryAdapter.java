package com.cinema.mails.infrastructure;

import com.cinema.mails.domain.Mail;
import com.cinema.mails.domain.MailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class MailJpaRepositoryAdapter implements MailRepository {

    private final MailJpaRepository mailJpaRepository;

    @Override
    public Mail save(Mail mail) {
        return mailJpaRepository.save(mail);
    }
}
