package code.user.application.internal;

import code.user.domain.User;
import code.user.domain.UserRepository;
import code.user.domain.UserRole;
import code.user.application.dto.SignInDto;
import code.user.application.dto.SignUpDto;
import code.user.infrastrcuture.exceptions.NotSamePasswordsException;
import code.user.infrastrcuture.exceptions.UsernameAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public void signUp(SignUpDto dto) {
        if (userRepository.existsById(dto.username())) {
            throw new UsernameAlreadyExistsException();
        }
        if (!(dto.password().equals(dto.repeatedPassword()))) {
            throw new NotSamePasswordsException();
        }
        var user = new User(
                dto.username(),
                passwordEncoder.encode(dto.password()),
                UserRole.COMMON
        );
        userRepository.save(user);
    }

    public void signIn(SignInDto dto) {
        var token = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
        var checkedToken = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(checkedToken);
    }

    public void signOut() {
        SecurityContextHolder.clearContext();
    }
}
