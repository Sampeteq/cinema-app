package code.user.client.commands.handlers;

import code.user.client.commands.SignUpCommand;
import code.user.domain.exceptions.NotSamePasswordsException;
import code.user.domain.exceptions.UsernameAlreadyExistsException;
import code.user.domain.User;
import code.user.domain.UserRepository;
import code.user.domain.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignUpHandler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void handle(SignUpCommand command) {
        if (userRepository.existsByUsername(command.mail())) {
            throw new UsernameAlreadyExistsException();
        }
        if (!(command.password().equals(command.repeatedPassword()))) {
            throw new NotSamePasswordsException();
        }
        var user = User
                .builder()
                .mail(command.mail())
                .password(passwordEncoder.encode(command.password()))
                .role(UserRole.COMMON)
                .build();
        userRepository.add(user);
    }
}
