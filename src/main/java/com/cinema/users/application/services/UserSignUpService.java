package com.cinema.users.application.services;

import com.cinema.users.application.dto.UserSignUpDto;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRole;
import com.cinema.users.domain.exceptions.UserMailAlreadyExistsException;
import com.cinema.users.domain.exceptions.UserNotSamePasswordsException;
import com.cinema.users.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UserSignUpService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    void signUp(UserSignUpDto dto) {
        if (userRepository.existsByMail(dto.mail())) {
            throw new UserMailAlreadyExistsException();
        }
        if (!(dto.password().equals(dto.repeatedPassword()))) {
            throw new UserNotSamePasswordsException();
        }
        var user = new User(
                dto.mail(),
                passwordEncoder.encode(dto.password()),
                UserRole.COMMON
        );
        userRepository.add(user);
    }
}
