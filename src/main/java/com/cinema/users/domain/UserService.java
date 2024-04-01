package com.cinema.users.domain;

import com.cinema.mail.MailService;
import com.cinema.users.domain.exceptions.UserMailNotUniqueException;
import com.cinema.users.domain.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        if (userRepository.getByMail(mail).isPresent()) {
            throw new UserMailNotUniqueException();
        }
        var user = new User(mail, passwordEncoder.encode(password), User.Role.COMMON);
        userRepository.save(user);
    }

    public void createAdmin(String mail, String password) {
        if (userRepository.getByMail(mail).isEmpty()) {
            var admin = new User(mail, passwordEncoder.encode(password), User.Role.ADMIN);
            userRepository.save(admin);
            log.info("Admin added");
        }
    }

    @Transactional
    public void resetUserPassword(String mail) {
        var user = userRepository
                .getByMail(mail)
                .orElseThrow(UserNotFoundException::new);
        var passwordResetToken = UUID.randomUUID();
        user.assignPasswordResetToken(passwordResetToken);
        var subject = "Password reset";
        var text = "Your password reset token: " + passwordResetToken;
        mailService.sendMail(user.getMail(), subject, text);
    }

    @Transactional
    public void setNewUserPassword(String newUserPassword, UUID token) {
        var user = userRepository
                .getByPasswordResetToken(token)
                .orElseThrow(UserNotFoundException::new);
        var encodedPassword = passwordEncoder.encode(newUserPassword);
        user.setNewPassword(encodedPassword);
    }

    public User getByMail(String mail) {
        return userRepository
                .getByMail(mail)
                .orElseThrow(() -> new UsernameNotFoundException(mail));
    }
}
