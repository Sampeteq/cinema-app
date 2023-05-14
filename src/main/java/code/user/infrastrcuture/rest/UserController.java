package code.user.infrastrcuture.rest;

import code.user.application.commands.SignInCommand;
import code.user.application.commands.SignInHandler;
import code.user.application.commands.SignOutHandler;
import code.user.application.commands.SignUpCommand;
import code.user.application.commands.SignUpHandler;
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

    private final SignUpHandler signUpHandler;

    private final SignInHandler signInHandler;

    private final SignOutHandler signOutHandler;


    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpCommand command) {
        signUpHandler.handle(command);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInCommand command) {
        signInHandler.handle(command);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut() {
        signOutHandler.handle();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

