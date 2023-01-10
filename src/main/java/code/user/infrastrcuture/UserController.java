package code.user.infrastrcuture;

import code.user.UserFacade;
import code.user.dto.SignInRequest;
import code.user.dto.SignUpRequest;
import code.user.exception.UserException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
class UserController {

    private final UserFacade userFacade;

    @PostMapping("/signup")
    ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest request) {
        userFacade.signUp(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    ResponseEntity<?> signIn(@RequestBody SignInRequest request) {
        userFacade.signIn(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/signout")
    ResponseEntity<?> signOut() {
        userFacade.signOut();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

@RestControllerAdvice
class UserExceptionHandler {

    @ExceptionHandler(UserException.class)
    ResponseEntity<?> handle(UserException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
