package code.mails.application;

import code.mails.domain.Mail;
import code.mails.domain.MailService;
import code.mails.domain.MailType;
import code.user.domain.events.UserPasswordResetEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPasswordResetHandlerService {

    private final MailService mailService;

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
        mailService.sendMail(passwordResetMail);
    }
}
