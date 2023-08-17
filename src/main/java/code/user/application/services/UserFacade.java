package code.user.application.services;

import code.user.application.dto.UserPasswordNewDto;
import code.user.application.dto.UserSignInDto;
import code.user.application.dto.UserSignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserSignUpService userSignUpService;
    private final UserSignInService userSignInService;
    private final UserSignOutService userSignOutService;
    private final UserPasswordResetService userPasswordResetService;
    private final UserPasswordNewService userPasswordNewService;
    private final UserCurrentService userCurrentService;

    public void signUpUser(UserSignUpDto dto) {
        userSignUpService.signUp(dto);
    }

    public void signInUser(UserSignInDto dto) {
        userSignInService.signIn(dto);
    }

    public void signOutUser() {
        userSignOutService.signOut();
    }

    public void resetUserPassword(String mail) {
        userPasswordResetService.resetUserPassword(mail);
    }

    public void setNewUserPassword(UserPasswordNewDto dto) {
        userPasswordNewService.setNewUserPassword(dto);
    }

    public Long readCurrentUserId() {
        return userCurrentService.readCurrentUserId();
    }
}
