package code.user;

import code.user.dto.SignInRequest;
import code.user.dto.SignUpRequest;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class UserFacade {

    private final AuthManager authManager;

    private final UserRepository userRepository;

    public void signUp(SignUpRequest request) {
        authManager.signUp(request);
    }

    public void signIn(SignInRequest request) {
        authManager.signIn(request);
    }

    public void signOut() {
        authManager.signOut();
    }

    public Optional<User> readUserDetails(String username) {
        return userRepository.getById(username);
    }

}
