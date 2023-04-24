package code.user.infrastrcuture;

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
public class OnStartUpMainAdminCreateHandler {

    @Value("${onStartUp.mainAdminUsername}")
    private String mainAdminUsername;

    @Value("${onStartUp.mainAdminPassword}")
    private String mainAdminPassword;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @EventListener(ContextRefreshedEvent.class)
    public void handle() {
        if (userRepository.existsByUsername(mainAdminUsername)) {
            log.info("Main admin already exists");
        } else {
            var mainAdmin = new User(
                    mainAdminUsername,
                    passwordEncoder.encode(mainAdminPassword),
                    UserRole.ADMIN
            );
            userRepository.add(mainAdmin);
            log.info("Main admin added.Username: {}" + " .Password: {}", mainAdminUsername, mainAdminPassword);
        }
    }
}
