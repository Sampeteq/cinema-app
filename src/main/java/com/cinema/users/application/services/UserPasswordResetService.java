package com.cinema.users.application.services;

import com.cinema.shared.exceptions.EntityNotFoundException;
import com.cinema.users.domain.events.UserPasswordResetEvent;
import com.cinema.users.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class UserPasswordResetService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    void resetUserPassword(String mail) {
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
