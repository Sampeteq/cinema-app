package code.user.application.services;

import code.user.domain.User;
import code.user.domain.UserRepository;
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
public class UserMainAdminCreationOnStartUpService {

    @Value("${onStartUp.mainAdminMail}")
    private String mainAdminMail;
    @Value("${onStartUp.mainAdminPassword}")
    private String mainAdminPassword;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ContextRefreshedEvent.class)
    public void createMainAdminUserOnStartUp() {
        if (userRepository.existsByMail(mainAdminMail)) {
            log.info("Main admin already exists");
        } else {
            var mainAdmin = User
                    .builder()
                    .mail(mainAdminMail)
                    .password(passwordEncoder.encode(mainAdminPassword))
                    .role(UserRole.ADMIN)
                    .build();

            userRepository.add(mainAdmin);
            log.info("Main admin added.Mail: {}" + " .Password: {}", mainAdminMail, mainAdminPassword);
        }
    }
}
