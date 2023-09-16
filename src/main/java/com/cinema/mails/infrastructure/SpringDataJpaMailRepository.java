package com.cinema.mails.infrastructure;

import com.cinema.mails.domain.Mail;
import com.cinema.mails.domain.ports.MailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SpringDataJpaMailRepository implements MailRepository {

    private final JpaMailRepository jpaMailRepository;

    @Override
    public Mail add(Mail mail) {
        return jpaMailRepository.save(mail);
    }
}

interface JpaMailRepository extends JpaRepository<Mail, Long> {

}
