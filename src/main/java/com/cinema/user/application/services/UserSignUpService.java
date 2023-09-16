package com.cinema.user.application.services;

import com.cinema.user.application.dto.UserSignUpDto;
import com.cinema.user.domain.User;
import com.cinema.user.domain.UserRole;
import com.cinema.user.domain.exceptions.UserMailAlreadyExistsException;
import com.cinema.user.domain.exceptions.UserNotSamePasswordsException;
import com.cinema.user.domain.ports.UserRepository;
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
