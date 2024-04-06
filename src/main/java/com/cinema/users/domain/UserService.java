package com.cinema.users.domain;

import com.cinema.mail.domain.MailService;
import com.cinema.users.domain.exceptions.UserMailNotUniqueException;
import com.cinema.users.domain.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        var user = new User(null, mail, passwordEncoder.encode(password), UserRole.COMMON, null);
        userRepository.save(user);
    }

    public void createAdmin(String mail, String password) {
        if (userRepository.getByMail(mail).isEmpty()) {
            var admin = new User(null, mail, passwordEncoder.encode(password), UserRole.ADMIN, null);
            userRepository.save(admin);
            log.info("Admin added");
        }
    }

    public void resetUserPassword(String mail) {
        var user = userRepository
                .getByMail(mail)
                .orElseThrow(UserNotFoundException::new);
        var passwordResetToken = UUID.randomUUID();
        user.assignPasswordResetToken(passwordResetToken);
        userRepository.save(user);
        var subject = "Password reset";
        var text = "Your password reset token: " + passwordResetToken;
        mailService.sendMail(user.getMail(), subject, text);
    }

    public void setNewUserPassword(String newUserPassword, UUID token) {
        var user = userRepository
                .getByPasswordResetToken(token)
                .orElseThrow(UserNotFoundException::new);
        var encodedPassword = passwordEncoder.encode(newUserPassword);
        user.setNewPassword(encodedPassword);
        userRepository.save(user);
    }

    public User getByMail(String mail) {
        return userRepository
                .getByMail(mail)
                .orElseThrow(() -> new UsernameNotFoundException(mail));
    }
}
