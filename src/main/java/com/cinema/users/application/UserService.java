package com.cinema.users.application;

import com.cinema.shared.events.EventPublisher;
import com.cinema.users.application.dto.CreateUserDto;
import com.cinema.users.application.dto.SetNewUserPasswordDto;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserFactory;
import com.cinema.users.domain.UserPasswordResetEvent;
import com.cinema.users.domain.UserRepository;
import com.cinema.users.domain.UserRole;
import com.cinema.users.domain.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserFactory userFactory;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisher eventPublisher;

    public void createUser(CreateUserDto dto) {
        var user = userFactory.createUser(dto.mail(), dto.password(), UserRole.COMMON);
        userRepository.add(user);
    }

    public void createAdmin(CreateUserDto dto) {
        if (!userRepository.existsByMail(dto.mail())) {
            var admin = userFactory.createUser(dto.mail(), dto.password(), UserRole.ADMIN);
            userRepository.add(admin);
            log.info("Admin added");
        }
    }

    public void resetUserPassword(String mail) {
        var user = userRepository
                .getByMail(mail)
                .orElseThrow(UserNotFoundException::new);
        var passwordResetToken = UUID.randomUUID();
        user.setPasswordResetToken(passwordResetToken);
        userRepository.add(user);
        var userPasswordResetEvent = new UserPasswordResetEvent(mail, passwordResetToken);
        eventPublisher.publish(userPasswordResetEvent);
    }

    public void setNewUserPassword(SetNewUserPasswordDto dto) {
        var user = userRepository
                .getByPasswordResetToken(dto.passwordResetToken())
                .orElseThrow(UserNotFoundException::new);
        var encodedPassword = passwordEncoder.encode(dto.newPassword());
        user.setNewPassword(encodedPassword);
        userRepository.add(user);
    }

    public User getLoggedUser() {
        var mail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return userRepository
                .getByMail(mail)
                .orElseThrow(() -> new UsernameNotFoundException(mail));
    }
}
