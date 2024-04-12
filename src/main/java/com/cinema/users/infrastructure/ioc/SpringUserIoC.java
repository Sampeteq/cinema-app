package com.cinema.users.infrastructure.ioc;

import com.cinema.mail.application.MailService;
import com.cinema.users.domain.UserRepository;
import com.cinema.users.application.UserService;
import com.cinema.users.infrastructure.initializer.UserAdminProperties;
import com.cinema.users.infrastructure.initializer.UserInitializer;
import com.cinema.users.infrastructure.db.JpaUserMapper;
import com.cinema.users.infrastructure.db.JpaUserRepository;
import com.cinema.users.infrastructure.db.JpaUserRepositoryAdapter;
import com.cinema.users.infrastructure.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
class SpringUserIoC {

    @Bean
    UserService userService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            MailService mailService
    ) {
        return new UserService(userRepository, passwordEncoder, mailService);
    }

    @Bean
    UserDetailsService userDetailsService(UserRepository userRepository) {
        return new UserDetailsServiceImpl(userRepository);
    }

    @Bean
    UserRepository userRepository(JpaUserRepository jpaUserRepository, JpaUserMapper jpaUserMapper) {
        return new JpaUserRepositoryAdapter(jpaUserRepository, jpaUserMapper);
    }

    @Bean
    JpaUserMapper jpaUserMapper() {
        return new JpaUserMapper();
    }

    @Bean
    @Profile("prod")
    UserInitializer userInitializer(UserAdminProperties userAdminProperties, UserService userService) {
        return new UserInitializer(userAdminProperties, userService);
    }
}
