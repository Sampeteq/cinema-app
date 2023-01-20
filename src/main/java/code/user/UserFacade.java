package code.user;

import code.user.dto.SignInDto;
import code.user.dto.SignUpDto;
import lombok.AllArgsConstructor;

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

    public Optional<User> readUserDetails(String username) {
        return userRepository.getById(username);
    }

}
