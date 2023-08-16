package code.mails.infrastructure;

import code.mails.domain.Mail;
import code.mails.domain.ports.MailSender;
import code.shared.time.TimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
@RequiredArgsConstructor
class SpringMailSender implements MailSender {

    private final JavaMailSender javaMailSender;
    private final TimeProvider timeProvider;

    @Override
    public void sendMail(Mail mail) {
        var message = new SimpleMailMessage();
        message.setTo(mail.getReceiver());
        message.setSubject(mail.getSubject());
        message.setText(mail.getText());
        javaMailSender.send(message);
        mail.sentAt(timeProvider.getCurrentDate());
    }
}
