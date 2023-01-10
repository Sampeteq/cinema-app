package code.user;

import code.user.dto.SignInRequest;
import code.user.dto.SignUpRequest;
import code.user.exception.UserException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@AllArgsConstructor
public class UserFacade {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public void signUp(SignUpRequest dto) {
        if (userRepository.existsById(dto.username())) {
            throw new UserException("Not unique username");
        }
        if (!(dto.password().equals(dto.repeatedPassword()))) {
            throw new UserException("Passwords must be the same");
        }
        var user = new User(
                dto.username(),
                passwordEncoder.encode(dto.password()),
                UserRole.COMMON
        );
        userRepository.add(user);
    }

    public void signIn(SignInRequest dto) {
        var token = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
        var checkedToken = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(checkedToken);
    }

    public void signOut() {
        SecurityContextHolder.clearContext();
    }

    public Optional<User> readUserDetails(String username) {
        return userRepository.getById(username);
    }

}
