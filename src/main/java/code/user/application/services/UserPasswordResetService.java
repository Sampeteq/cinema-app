package code.user.application.services;

import code.shared.EntityNotFoundException;
import code.user.domain.events.UserPasswordResetEvent;
import code.user.domain.ports.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserPasswordResetService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void resetUserPassword(String mail) {
        var user = userRepository
                .readyByMail(mail)
                .orElseThrow(() -> new EntityNotFoundException("User"));
        var passwordResetToken = UUID.randomUUID();
        user.setPasswordResetToken(passwordResetToken);
        userRepository.add(user);
        var userPasswordResetEvent = new UserPasswordResetEvent(mail, passwordResetToken);
        applicationEventPublisher.publishEvent(userPasswordResetEvent);
    }
}
