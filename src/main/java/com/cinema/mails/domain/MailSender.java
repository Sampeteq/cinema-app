package com.cinema.mails.domain;

import com.cinema.mails.domain.Mail;

public interface MailSender {
    void sendMail(Mail mail);
}
