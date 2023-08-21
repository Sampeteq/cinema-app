package code.user.infrastrcuture.rest;

import code.user.application.dto.UserPasswordNewDto;
import code.user.application.dto.UserSignInDto;
import code.user.application.dto.UserSignUpDto;
import code.user.application.services.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
class UserController {

    private final UserFacade userFacade;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    void signUpUser(@RequestBody @Valid UserSignUpDto dto) {
        userFacade.signUpUser(dto);
    }

    @PostMapping("/sign-in")
    void signInUser(@RequestBody UserSignInDto dto) {
        userFacade.signInUser(dto);
    }

    @PostMapping("/sign-out")
    void signOutUser() {
        userFacade.signOutUser();
    }

    @PostMapping("/password/reset")
    void resetUserPassword(@RequestParam String mail) {
        userFacade.resetUserPassword(mail);
    }

    @PostMapping("/password/new")
    void setNewUserPassword(@RequestBody @Valid UserPasswordNewDto dto) {
        userFacade.setNewUserPassword(dto);
    }
}

