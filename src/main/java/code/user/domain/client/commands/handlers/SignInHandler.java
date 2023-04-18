package code.user.domain.client.commands.handlers;

import code.user.domain.client.commands.SignInCommand;
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
        var token = new UsernamePasswordAuthenticationToken(command.username(), command.password());
        var checkedToken = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(checkedToken);
    }
}
