package code.mails.domain.ports;

import code.mails.domain.Mail;

public interface MailRepository {
    Mail add(Mail mail);
}
