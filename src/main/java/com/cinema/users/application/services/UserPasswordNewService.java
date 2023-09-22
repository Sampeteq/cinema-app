package com.cinema.users.application.services;

import com.cinema.shared.exceptions.EntityNotFoundException;
import com.cinema.users.application.dto.UserPasswordNewDto;
import com.cinema.users.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class UserPasswordNewService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void setNewUserPassword(UserPasswordNewDto dto) {
        var user = userRepository
                .readByPasswordResetToken(dto.passwordResetToken())
                .orElseThrow(() -> new EntityNotFoundException("User"));
        var encodedPassword = passwordEncoder.encode(dto.newPassword());
        user.setNewPassword(encodedPassword);
    }
}
