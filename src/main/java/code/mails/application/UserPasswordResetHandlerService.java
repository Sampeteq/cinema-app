package code.mails.application;

import code.mails.domain.Mail;
import code.mails.domain.ports.MailSender;
import code.mails.domain.MailType;
import code.user.domain.events.UserPasswordResetEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPasswordResetHandlerService {

    private final MailSender mailSender;

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
    }
}
