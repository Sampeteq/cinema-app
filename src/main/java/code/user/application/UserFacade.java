package code.user.application;

import code.user.application.internal.AuthService;
import code.user.domain.UserRepository;
import code.user.application.dto.SignInDto;
import code.user.application.dto.SignUpDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserFacade {

    private final AuthService authService;

    private final UserRepository userRepository;

    public void signUp(SignUpDto dto) {
        authService.signUp(dto);
    }

    public void signIn(SignInDto dto) {
        authService.signIn(dto);
    }

    public void signOut() {
        authService.signOut();
    }

    public Optional<UserDetails> searchUserDetails(String username) {
        return userRepository.searchUserDetailsByUsername(username);
    }
}
