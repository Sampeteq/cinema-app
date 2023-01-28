package code.user.application;

import code.user.domain.UserRepository;
import code.user.domain.dto.SignInDto;
import code.user.domain.dto.SignUpDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@AllArgsConstructor
public class UserFacade {

    private final AuthManager authManager;

    private final UserRepository userRepository;

    public void signUp(SignUpDto dto) {
        authManager.signUp(dto);
    }

    public void signIn(SignInDto dto) {
        authManager.signIn(dto);
    }

    public void signOut() {
        authManager.signOut();
    }

    public Optional<UserDetails> searchUserDetails(String username) {
        return userRepository.searchUserDetailsByUsername(username);
    }
}
