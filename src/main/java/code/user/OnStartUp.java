package code.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
class OnStartUp {

    private static final String MAIN_ADMIN_USERNAME = "MainAdmin";

    private static final String MAIN_ADMIN_PASSWORD = "12345";

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @EventListener(ContextRefreshedEvent.class)
    public void createMainAdmin() {
        if (userRepository.existsById(MAIN_ADMIN_USERNAME)) {
            log.info("Main admin already exists");
        } else {
            var mainAdmin = new User(
                    MAIN_ADMIN_USERNAME,
                    passwordEncoder.encode(MAIN_ADMIN_PASSWORD),
                    UserRole.ADMIN
            );
            userRepository.save(mainAdmin);
            log.info("Main admin added.Username: {}" + " .Password: {}", MAIN_ADMIN_USERNAME, MAIN_ADMIN_PASSWORD);
        }
    }
}
