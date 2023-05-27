package code.user.application.services;

import code.user.application.commands.UserSignUpCommand;
import code.user.domain.User;
import code.user.domain.UserRepository;
import code.user.domain.UserRole;
import code.user.domain.exceptions.MailAlreadyExistsException;
import code.user.domain.exceptions.NotSamePasswordsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSignUpService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUpUser(UserSignUpCommand command) {
        if (userRepository.existsByMail(command.mail())) {
            throw new MailAlreadyExistsException();
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
