package code.user.infrastrcuture.rest;

import code.user.application.dto.UserPasswordNewDto;
import code.user.application.dto.UserSignInDto;
import code.user.application.dto.UserSignUpDto;
import code.user.application.services.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
class UserController {

    private final UserFacade userFacade;

    @PostMapping("/signup")
    ResponseEntity<String> signUpUser(@RequestBody @Valid UserSignUpDto dto) {
        userFacade.signUpUser(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    ResponseEntity<String> signInUser(@RequestBody UserSignInDto dto) {
        userFacade.signInUser(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/signout")
    ResponseEntity<String> signOutUser() {
        userFacade.signOutUser();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/password/reset")
    ResponseEntity<String> resetUserPassword(@RequestParam String mail) {
        userFacade.resetUserPassword(mail);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/password/new")
    ResponseEntity<String> setNewUserPassword(@RequestBody UserPasswordNewDto dto) {
        userFacade.setNewUserPassword(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

