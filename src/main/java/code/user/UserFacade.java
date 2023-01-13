package code.user;

import code.user.dto.SignInRequest;
import code.user.dto.SignUpRequest;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class UserFacade {

    private final AuthService authService;

    private final UserRepository userRepository;

    public void signUp(SignUpRequest request) {
        authService.signUp(request);
    }

    public void signIn(SignInRequest request) {
        authService.signIn(request);
    }

    public void signOut() {
        authService.signOut();
    }

    public Optional<User> readUserDetails(String username) {
        return userRepository.getById(username);
    }

}
