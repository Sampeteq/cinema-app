package com.cinema.users.application.handlers;

import com.cinema.shared.events.EventPublisher;
import com.cinema.users.application.commands.ResetUserPassword;
import com.cinema.users.domain.UserRepository;
import com.cinema.users.domain.events.UserPasswordResetEvent;
import com.cinema.users.domain.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ResetUserPasswordHandler {

    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;

    public void handle(ResetUserPassword command) {
        var user = userRepository
                .readyByMail(command.mail())
                .orElseThrow(UserNotFoundException::new);
        var passwordResetToken = UUID.randomUUID();
        user.setPasswordResetToken(passwordResetToken);
        userRepository.add(user);
        var userPasswordResetEvent = new UserPasswordResetEvent(command.mail(), passwordResetToken);
        eventPublisher.publish(userPasswordResetEvent);
    }
}
