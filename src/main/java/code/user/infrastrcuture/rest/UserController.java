package code.user.infrastrcuture.rest;

import code.user.application.commands.UserLoginCommand;
import code.user.application.handlers.UserLoginHandler;
import code.user.application.handlers.UserSignOutHandler;
import code.user.application.commands.UserSignUpCommand;
import code.user.application.handlers.UserSignUpHandler;
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

    private final UserSignUpHandler userSignUpHandler;

    private final UserLoginHandler userLoginHandler;

    private final UserSignOutHandler userSignOutHandler;


    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid UserSignUpCommand command) {
        userSignUpHandler.handle(command);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody UserLoginCommand command) {
        userLoginHandler.handle(command);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut() {
        userSignOutHandler.handle();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

