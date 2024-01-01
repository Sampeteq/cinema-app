package com.cinema.users.domain;

import com.cinema.users.domain.exceptions.UserMailNotUniqueException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFactory {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(String mail, String password, UserRole role) {
        if (userRepository.existsByMail(mail)) {
            throw new UserMailNotUniqueException();
        }
        return new User(
                mail,
                passwordEncoder.encode(password),
                role
        );
    }
}