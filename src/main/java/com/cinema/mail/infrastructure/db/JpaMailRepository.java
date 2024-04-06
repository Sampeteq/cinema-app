package com.cinema.mail.infrastructure.db;

import com.cinema.mail.domain.Mail;
import com.cinema.mail.domain.MailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

interface JpaMailRepository extends JpaRepository<JpaMail, Long> {
}

@Repository
@RequiredArgsConstructor
class JpaMailRepositoryAdapter implements MailRepository {

    private final JpaMailRepository jpaMailRepository;

    @Override
    public void save(Mail mail) {
        jpaMailRepository.save(toJpa(mail));
    }

    private static JpaMail toJpa(Mail mail) {
        return new JpaMail(
                mail.getId(),
                mail.getReceiver(),
                mail.getSubject(),
                mail.getContent(),
                mail.getSentAt()
        );
    }
}
