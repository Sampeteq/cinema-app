package com.cinema.users.application.services;

import com.cinema.shared.events.EventPublisher;
import com.cinema.shared.exceptions.EntityNotFoundException;
import com.cinema.users.application.dto.UserCreateDto;
import com.cinema.users.application.dto.UserPasswordNewDto;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRepository;
import com.cinema.users.domain.UserRole;
import com.cinema.users.domain.events.UserPasswordResetEvent;
import com.cinema.users.domain.exceptions.UserMailAlreadyExistsException;
import com.cinema.users.domain.exceptions.UserNotSamePasswordsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisher eventPublisher;

    void createUser(UserCreateDto dto) {
        if (userRepository.existsByMail(dto.mail())) {
            throw new UserMailAlreadyExistsException();
        }
        if (!(dto.password().equals(dto.repeatedPassword()))) {
            throw new UserNotSamePasswordsException();
        }
        var user = new User(
                dto.mail(),
                passwordEncoder.encode(dto.password()),
                UserRole.COMMON
        );
        userRepository.add(user);
    }

    void resetUserPassword(String mail) {
        var user = userRepository
                .readyByMail(mail)
                .orElseThrow(() -> new EntityNotFoundException("User"));
        var passwordResetToken = UUID.randomUUID();
        user.setPasswordResetToken(passwordResetToken);
        userRepository.add(user);
        var userPasswordResetEvent = new UserPasswordResetEvent(mail, passwordResetToken);
        eventPublisher.publish(userPasswordResetEvent);
    }

    @Transactional
    public void setNewUserPassword(UserPasswordNewDto dto) {
        var user = userRepository
                .readByPasswordResetToken(dto.passwordResetToken())
                .orElseThrow(() -> new EntityNotFoundException("User"));
        var encodedPassword = passwordEncoder.encode(dto.newPassword());
        user.setNewPassword(encodedPassword);
    }

    @Transactional(readOnly = true)
    public Long readCurrentUserId() {
        var mail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return userRepository
                .readyByMail(mail)
                .map(User::getId)
                .orElseThrow(() -> new UsernameNotFoundException(mail));
    }
}
