package com.cinema.users.application.commands.handlers;

import com.cinema.users.application.commands.SetNewUserPassword;
import com.cinema.users.domain.UserRepository;
import com.cinema.users.domain.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SetNewUserPasswordHandler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void handle(SetNewUserPassword command) {
        var user = userRepository
                .getByPasswordResetToken(command.passwordResetToken())
                .orElseThrow(UserNotFoundException::new);
        var encodedPassword = passwordEncoder.encode(command.newPassword());
        user.setNewPassword(encodedPassword);
        userRepository.add(user);
    }
}
