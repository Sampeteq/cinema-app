package code.user.application.services;

import code.shared.exceptions.EntityNotFoundException;
import code.user.application.dto.UserPasswordNewDto;
import code.user.domain.ports.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserPasswordNewService {

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
