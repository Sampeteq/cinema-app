package code.user.application.commands;

import code.user.domain.exceptions.UserAlreadyLoggedInException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignInHandler {

    private final AuthenticationManager authenticationManager;

    public void handle(SignInCommand command) {
        var username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        if (username.equals(command.mail())) {
            throw new UserAlreadyLoggedInException();
        }
        var token = new UsernamePasswordAuthenticationToken(command.mail(), command.password());
        var checkedToken = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(checkedToken);
    }
}
