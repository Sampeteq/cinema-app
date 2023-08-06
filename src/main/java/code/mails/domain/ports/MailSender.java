package code.mails.domain.ports;

import code.mails.domain.Mail;

public interface MailSender {
    void sendMail(Mail mail);
}
