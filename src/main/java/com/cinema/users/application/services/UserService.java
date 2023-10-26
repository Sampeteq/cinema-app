package com.cinema.users.application.services;

import com.cinema.shared.events.EventPublisher;
import com.cinema.users.application.dto.UserCreateDto;
import com.cinema.users.application.dto.UserPasswordNewDto;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRepository;
import com.cinema.users.domain.UserRole;
import com.cinema.users.domain.events.UserPasswordResetEvent;
import com.cinema.users.domain.exceptions.UserMailAlreadyExistsException;
import com.cinema.users.domain.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisher eventPublisher;

    public void createCommonUser(UserCreateDto dto) {
        createUser(dto, UserRole.COMMON);
    }

    public void createAdmin(UserCreateDto dto) {
        createUser(dto, UserRole.ADMIN);
    }

    public void resetUserPassword(String mail) {
        var user = userRepository
                .readyByMail(mail)
                .orElseThrow(UserNotFoundException::new);
        var passwordResetToken = UUID.randomUUID();
        user.setPasswordResetToken(passwordResetToken);
        userRepository.add(user);
        var userPasswordResetEvent = new UserPasswordResetEvent(mail, passwordResetToken);
        eventPublisher.publish(userPasswordResetEvent);
    }

    public void setNewUserPassword(UserPasswordNewDto dto) {
        var user = userRepository
                .readByPasswordResetToken(dto.passwordResetToken())
                .orElseThrow(UserNotFoundException::new);
        var encodedPassword = passwordEncoder.encode(dto.newPassword());
        user.setNewPassword(encodedPassword);
        userRepository.add(user);
    }

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

    private void createUser(UserCreateDto dto, UserRole role) {
        if (userRepository.existsByMail(dto.mail())) {
            throw new UserMailAlreadyExistsException();
        }
        var user = new User(
                dto.mail(),
                passwordEncoder.encode(dto.password()),
                role
        );
        userRepository.add(user);
    }
}
