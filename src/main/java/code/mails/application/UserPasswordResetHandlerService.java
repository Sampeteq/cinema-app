package code.mails.application;

import code.mails.domain.Mail;
import code.mails.domain.MailType;
import code.mails.domain.ports.MailRepository;
import code.mails.domain.ports.MailSender;
import code.user.domain.events.UserPasswordResetEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPasswordResetHandlerService {

    private final MailSender mailSender;
    private final MailRepository mailRepository;

    @EventListener
    public void handle(UserPasswordResetEvent event) {
        var subject = "Password reset";
        var message = "Your password reset token: " + event.userPasswordResetToken();
        var passwordResetMail =  Mail.create(
                event.userMail(),
                subject,
                message,
                MailType.USER_PASSWORD_RESET
        );
        mailSender.sendMail(passwordResetMail);
        log.info("Mail sent");
        mailRepository.add(passwordResetMail);
        log.info("Mail saved in database");
    }
}
