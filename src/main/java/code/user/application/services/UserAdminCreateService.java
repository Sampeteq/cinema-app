package code.user.application.services;

import code.user.domain.User;
import code.user.domain.UserRole;
import code.user.domain.ports.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile("prod")
@Slf4j
class UserAdminCreateService {

    @Value("${admin.mail}")
    private String adminMail;
    @Value("${admin.password}")
    private String adminPassword;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
