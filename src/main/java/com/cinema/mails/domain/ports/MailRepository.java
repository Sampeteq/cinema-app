package com.cinema.mails.domain.ports;

import com.cinema.mails.domain.Mail;

public interface MailRepository {
    Mail add(Mail mail);
}
