package code.user.application.services;

import code.user.application.dto.UserSignUpDto;
import code.user.domain.User;
import code.user.domain.UserRole;
import code.user.domain.exceptions.UserMailAlreadyExistsException;
import code.user.domain.exceptions.UserNotSamePasswordsException;
import code.user.domain.ports.UserRepository;
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
        var user = User.create(
                dto.mail(),
                passwordEncoder.encode(dto.password()),
                UserRole.COMMON
        );
        userRepository.add(user);
    }
}
