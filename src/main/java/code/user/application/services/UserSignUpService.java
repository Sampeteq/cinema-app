package code.user.application.services;

import code.user.application.commands.UserSignUpCommand;
import code.user.domain.User;
import code.user.infrastrcuture.db.UserRepository;
import code.user.domain.UserRole;
import code.user.domain.exceptions.UserMailAlreadyExistsException;
import code.user.domain.exceptions.UserNotSamePasswordsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSignUpService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void handle(UserSignUpCommand command) {
        if (userRepository.existsByMail(command.mail())) {
            throw new UserMailAlreadyExistsException();
        }
        if (!(command.password().equals(command.repeatedPassword()))) {
            throw new UserNotSamePasswordsException();
        }
        var user = new User(
                command.mail(),
                passwordEncoder.encode(command.password()),
                UserRole.COMMON
        );
        userRepository.add(user);
    }
}
