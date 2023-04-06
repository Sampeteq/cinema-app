package code.user.infrastrcuture.rest;

import code.user.domain.commands.SignInCommand;
import code.user.domain.commands.SignUpCommand;
import code.user.domain.commands.handlers.SignInCommandHandler;
import code.user.domain.commands.handlers.SignOutCommandHandler;
import code.user.domain.commands.handlers.SignUpCommandHandler;
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

    private final SignUpCommandHandler signUpCommandHandler;

    private final SignInCommandHandler signInCommandHandler;

    private final SignOutCommandHandler signOutCommandHandler;


    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpCommand command) {
        signUpCommandHandler.handle(command);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInCommand command) {
        signInCommandHandler.handle(command);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut() {
        signOutCommandHandler.handle();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

