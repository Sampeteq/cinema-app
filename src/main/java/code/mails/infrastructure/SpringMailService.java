package code.mails.infrastructure;

import code.mails.domain.Mail;
import code.mails.domain.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpringMailService implements MailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendMail(Mail mail) {
        var message = new SimpleMailMessage();
        message.setTo(mail.getTo());
        message.setSubject(mail.getSubject());
        message.setText(mail.getText());
        javaMailSender.send(message);
    }
}
