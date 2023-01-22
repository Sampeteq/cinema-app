package code.user;

import code.user.dto.SignInDto;
import code.user.dto.SignUpDto;
import code.user.exception.UserException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
class AuthManager {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    void signUp(SignUpDto dto) {
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
        userRepository.save(user);
    }

    void signIn(SignInDto dto) {
        var token = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
        var checkedToken = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(checkedToken);
    }

    void signOut() {
        SecurityContextHolder.clearContext();
    }
}
