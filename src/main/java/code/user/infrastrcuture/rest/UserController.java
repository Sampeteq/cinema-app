package code.user.infrastrcuture.rest;

import code.user.application.commands.UserLoginCommand;
import code.user.application.services.UserLoginService;
import code.user.application.services.UserSignOutService;
import code.user.application.commands.UserSignUpCommand;
import code.user.application.services.UserSignUpService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserSignUpService userSignUpService;

    private final UserLoginService userLoginService;

    private final UserSignOutService userSignOutService;


    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid UserSignUpCommand command) {
        userSignUpService.signUpUser(command);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody UserLoginCommand command) {
        userLoginService.loginUser(command);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut() {
        userSignOutService.signOutUser();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

