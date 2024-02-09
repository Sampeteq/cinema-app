package com.cinema.users.application;

import com.cinema.mails.MailMessage;
import com.cinema.mails.MailService;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRepository;
import com.cinema.users.domain.exceptions.UserMailNotUniqueException;
import com.cinema.users.domain.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public void createUser(String mail, String password) {
        if (userRepository.findByMail(mail).isPresent()) {
            throw new UserMailNotUniqueException();
        }
        var user = new User(mail, passwordEncoder.encode(password), User.Role.COMMON);
        userRepository.save(user);
    }

    public void createAdmin(String mail, String password) {
        if (userRepository.findByMail(mail).isEmpty()) {
            var admin = new User(mail, passwordEncoder.encode(password), User.Role.ADMIN);
            userRepository.save(admin);
            log.info("Admin added");
        }
    }

    @Transactional
    public void resetUserPassword(String mail) {
        var user = userRepository
                .findByMail(mail)
                .orElseThrow(UserNotFoundException::new);
        var passwordResetToken = UUID.randomUUID();
        user.setPasswordResetToken(passwordResetToken);
        var mailMessage = new MailMessage(mail, "Password reset", "Your password reset token: " + passwordResetToken);
        mailService.sendMail(mailMessage);
    }

    @Transactional
    public void setNewUserPassword(String newUserPassword, UUID token) {
        var user = userRepository
                .findByPasswordResetToken(token)
                .orElseThrow(UserNotFoundException::new);
        var encodedPassword = passwordEncoder.encode(newUserPassword);
        user.setNewPassword(encodedPassword);
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
