package code.user.application.services;

import code.user.domain.User;
import code.user.infrastrcuture.db.UserRepository;
import code.user.domain.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("prod")
@Slf4j
public class UserMainAdminCreateOnStartUpService {

    @Value("${admin.mail}")
    private String adminMail;
    @Value("${admin.password}")
    private String adminPassword;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ContextRefreshedEvent.class)
    public void createAdminOnStartUp() {
        if (userRepository.existsByMail(adminMail)) {
            log.info("Admin already exists");
        } else {
            var admin = User
                    .builder()
                    .mail(adminMail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(UserRole.ADMIN)
                    .build();
            userRepository.add(admin);
            log.info("Admin added");
        }
    }
}
