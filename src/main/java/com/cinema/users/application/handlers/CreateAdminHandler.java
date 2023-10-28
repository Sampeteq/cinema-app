package com.cinema.users.application.handlers;

import com.cinema.users.application.commands.CreateAdmin;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRepository;
import com.cinema.users.domain.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateAdminHandler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void handle(CreateAdmin command) {
      log.info("Command:{}", command);
        if (userRepository.existsByMail(command.adminMail())) {
            log.info("Admin already exists");
        }
        var user = new User(
                command.adminMail(),
                passwordEncoder.encode(command.adminPassword()),
                UserRole.ADMIN
        );
        userRepository.add(user);
    }
}
