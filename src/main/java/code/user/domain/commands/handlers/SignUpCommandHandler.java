package code.user.domain.commands.handlers;

import code.user.domain.User;
import code.user.domain.UserRepository;
import code.user.domain.UserRole;
import code.user.domain.commands.SignUpCommand;
import code.user.domain.exceptions.NotSamePasswordsException;
import code.user.infrastrcuture.exceptions.UsernameAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignUpCommandHandler {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void handle(SignUpCommand command) {
        if (userRepository.existsById(command.username())) {
            throw new UsernameAlreadyExistsException();
        }
        if (!(command.password().equals(command.repeatedPassword()))) {
            throw new NotSamePasswordsException();
        }
        var user = new User(
                command.username(),
                passwordEncoder.encode(command.password()),
                UserRole.COMMON
        );
        userRepository.save(user);
    }
}