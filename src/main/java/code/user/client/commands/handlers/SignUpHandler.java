package code.user.client.commands.handlers;

import code.user.client.commands.SignUpCommand;
import code.user.domain.User;
import code.user.domain.UserRepository;
import code.user.domain.UserRole;
import code.user.client.exceptions.NotSamePasswordsException;
import code.user.client.exceptions.UsernameAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignUpHandler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void handle(SignUpCommand command) {
        if (userRepository.existsByUsername(command.username())) {
            throw new UsernameAlreadyExistsException();
        }
        if (!(command.password().equals(command.repeatedPassword()))) {
            throw new NotSamePasswordsException();
        }
        var user = User
                .builder()
                .username(command.username())
                .password(passwordEncoder.encode(command.password()))
                .role(UserRole.COMMON)
                .build();
        userRepository.add(user);
    }
}
