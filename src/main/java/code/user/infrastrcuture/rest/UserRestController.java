package code.user.infrastrcuture.rest;

import code.user.application.dto.UserPasswordNewDto;
import code.user.application.dto.UserSignInDto;
import code.user.application.dto.UserSignUpDto;
import code.user.application.services.UserPasswordNewService;
import code.user.application.services.UserPasswordResetService;
import code.user.application.services.UserSignInService;
import code.user.application.services.UserSignOutService;
import code.user.application.services.UserSignUpService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class UserRestController {

    private final UserSignUpService userSignUpService;

    private final UserSignInService userSignInService;

    private final UserSignOutService userSignOutService;

    private final UserPasswordResetService userPasswordResetService;

    private final UserPasswordNewService userPasswordNewService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid UserSignUpDto dto) {
        userSignUpService.signUp(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody UserSignInDto dto) {
        userSignInService.signIn(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut() {
        userSignOutService.signOut();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/password/reset")
    public ResponseEntity<?> resetPassword(@RequestParam String mail) {
        userPasswordResetService.resetUserPassword(mail);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/password/new")
    public ResponseEntity<?> setNewPassword(@RequestBody UserPasswordNewDto dto) {
        userPasswordNewService.setNewUserPassword(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

