package code.user.infrastrcuture.rest;

import code.user.application.commands.UserSignInCommand;
import code.user.application.services.UserSignInService;
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
public class UserRestController {

    private final UserSignUpService userSignUpHandler;

    private final UserSignInService userSignInHandler;

    private final UserSignOutService userSignOutHandler;


    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid UserSignUpCommand command) {
        userSignUpHandler.handle(command);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody UserSignInCommand command) {
        userSignInHandler.handle(command);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut() {
        userSignOutHandler.handle();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

