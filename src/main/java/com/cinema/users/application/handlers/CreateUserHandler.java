package com.cinema.users.application.handlers;

import com.cinema.users.application.commands.CreateUser;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRepository;
import com.cinema.users.domain.UserRole;
import com.cinema.users.domain.exceptions.UserMailNotUniqueException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateUserHandler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void handle(CreateUser command, UserRole role) {
        if (userRepository.existsByMail(command.mail())) {
            throw new UserMailNotUniqueException();
        }
        var user = new User(
                command.mail(),
                passwordEncoder.encode(command.password()),
                role
        );
        userRepository.add(user);
    }
}
