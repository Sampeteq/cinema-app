package com.cinema.mails.domain.ports;

import com.cinema.mails.domain.Mail;

public interface MailSender {
    void sendMail(Mail mail);
}
