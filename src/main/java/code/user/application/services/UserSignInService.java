package code.user.application.services;

import code.user.application.dto.UserSignInDto;
import code.user.domain.exceptions.UserAlreadyLoggedInException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSignInService {

    private final AuthenticationManager authenticationManager;

    public void handle(UserSignInDto dto) {
        var username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        if (username.equals(dto.mail())) {
            throw new UserAlreadyLoggedInException();
        }
        var token = new UsernamePasswordAuthenticationToken(dto.mail(), dto.password());
        var checkedToken = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(checkedToken);
    }
}
