package code.user.infrastrcuture;

import code.user.UserFacade;
import code.user.dto.SignInDto;
import code.user.dto.SignUpDto;
import code.user.exception.UserException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
class UserController {

    private final UserFacade userFacade;

    @PostMapping("/signup")
    ResponseEntity<?> signUp(@RequestBody @Valid SignUpDto dto) {
        userFacade.signUp(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    ResponseEntity<?> signIn(@RequestBody SignInDto dto) {
        userFacade.signIn(dto);
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

    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<?> handle(AuthenticationException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
