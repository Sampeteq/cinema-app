package com.cinema.mails.infrastructure;

import com.cinema.mails.domain.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

interface MailJpaRepository extends JpaRepository<Mail, Long> {
}
