package com.cinema.users.application.services;

import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRepository;
import com.cinema.users.domain.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
@Slf4j
class UserAdminCreateService {

    private final String adminMail;
    private final String adminPassword;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAdminCreateService(
            @Value("${admin.mail}") String adminMail,
            @Value("${admin.password}") String adminPassword,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.adminMail = adminMail;
        this.adminPassword = adminPassword;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ContextRefreshedEvent.class)
    void createAdminOnStartUp() {
        if (userRepository.existsByMail(adminMail)) {
            log.info("Admin already exists");
        } else {
            var admin = new User(
                    adminMail,
                    passwordEncoder.encode(adminPassword),
                    UserRole.ADMIN
            );
            userRepository.add(admin);
            log.info("Admin added");
        }
    }
}
