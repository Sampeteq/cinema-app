package code.user;

import code.user.dto.SignInRequest;
import code.user.dto.SignUpRequest;
import code.user.exception.UserException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    void signUp(SignUpRequest request) {
        if (userRepository.existsById(request.username())) {
            throw new UserException("Not unique username");
        }
        if (!(request.password().equals(request.repeatedPassword()))) {
            throw new UserException("Passwords must be the same");
        }
        var user = new User(
                request.username(),
                passwordEncoder.encode(request.password()),
                UserRole.COMMON
        );
        userRepository.add(user);
    }

    void signIn(SignInRequest request) {
        var token = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        var checkedToken = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(checkedToken);
    }
    void signOut() {
        SecurityContextHolder.clearContext();
    }
}
