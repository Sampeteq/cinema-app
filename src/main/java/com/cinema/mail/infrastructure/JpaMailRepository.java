package com.cinema.mail.infrastructure;

import com.cinema.mail.domain.Mail;
import com.cinema.mail.domain.MailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

interface JpaMailRepository extends JpaRepository<Mail, Long> {
}

@Repository
@RequiredArgsConstructor
class JpaMailRepositoryAdapter implements MailRepository {

    private final JpaMailRepository jpaMailRepository;

    @Override
    public Mail save(Mail mail) {
        return jpaMailRepository.save(mail);
    }
}
