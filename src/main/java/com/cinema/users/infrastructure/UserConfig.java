package com.cinema.users.infrastructure;

import com.cinema.mails.MailService;
import com.cinema.users.domain.UserRepository;
import com.cinema.users.domain.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
class UserConfig {

    @Bean
    UserService userService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            MailService mailService
    ) {
        return new UserService(userRepository, passwordEncoder, mailService);
    }

    @Bean
    UserRepository userRepository(JpaUserRepository jpaUserRepository) {
        return new JpaUserRepositoryAdapter(jpaUserRepository);
    }
}
