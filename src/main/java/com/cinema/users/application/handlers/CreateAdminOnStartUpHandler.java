package com.cinema.users.application.handlers;

import com.cinema.users.application.properties.AdminProperties;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRepository;
import com.cinema.users.domain.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
@RequiredArgsConstructor
@Slf4j
class CreateAdminOnStartUpHandler {

    private final UserRepository userRepository;
    private final AdminProperties adminProperties;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ContextRefreshedEvent.class)
    void createAdminOnStartUp() {
        if (userRepository.existsByMail(adminProperties.getMail())) {
            log.info("Admin already exists");
        }
        var user = new User(
                adminProperties.getMail(),
                passwordEncoder.encode(adminProperties.getPassword()),
                UserRole.ADMIN
        );
        userRepository.add(user);
    }
}
