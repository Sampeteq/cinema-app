package com.cinema.mails.application;

import com.cinema.mails.domain.Mail;
import com.cinema.mails.domain.MailType;
import com.cinema.mails.domain.MailRepository;
import com.cinema.mails.domain.MailSender;
import com.cinema.users.domain.events.UserPasswordResetEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
class UserPasswordResetHandlerService {

    private final MailSender mailSender;
    private final MailRepository mailRepository;

    @EventListener
    void handle(UserPasswordResetEvent event) {
        var subject = "Password reset";
        var message = "Your password reset token: " + event.userPasswordResetToken();
        var passwordResetMail = new Mail(
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
