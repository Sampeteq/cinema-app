package com.cinema.users.application;

import com.cinema.mails.MailMessage;
import com.cinema.mails.MailService;
import com.cinema.users.application.dto.CreateUserDto;
import com.cinema.users.application.dto.SetNewUserPasswordDto;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserFactory;
import com.cinema.users.domain.UserRepository;
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
    private final MailService mailService;

    public void createUser(CreateUserDto dto) {
        var user = userFactory.createUser(dto.mail(), dto.password(), User.Role.COMMON);
        userRepository.save(user);
    }

    public void createAdmin(CreateUserDto dto) {
        if (userRepository.findByMail(dto.mail()).isEmpty()) {
            var admin = userFactory.createUser(dto.mail(), dto.password(), User.Role.ADMIN);
            userRepository.save(admin);
            log.info("Admin added");
        }
    }

    public void resetUserPassword(String mail) {
        var user = userRepository
                .findByMail(mail)
                .orElseThrow(UserNotFoundException::new);
        var passwordResetToken = UUID.randomUUID();
        user.setPasswordResetToken(passwordResetToken);
        userRepository.save(user);
        var mailMessage = new MailMessage(mail, "Password reset", "Your password reset token: " + passwordResetToken);
        mailService.sendMail(mailMessage);
    }

    public void setNewUserPassword(SetNewUserPasswordDto dto) {
        var user = userRepository
                .findByPasswordResetToken(dto.passwordResetToken())
                .orElseThrow(UserNotFoundException::new);
        var encodedPassword = passwordEncoder.encode(dto.newPassword());
        user.setNewPassword(encodedPassword);
        userRepository.save(user);
    }

    public User getLoggedUser() {
        var mail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return userRepository
                .findByMail(mail)
                .orElseThrow(() -> new UsernameNotFoundException(mail));
    }
}
