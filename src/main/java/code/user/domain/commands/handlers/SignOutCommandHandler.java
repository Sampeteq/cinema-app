package code.user.domain.commands.handlers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SignOutCommandHandler {

    public void handle() {
        SecurityContextHolder.clearContext();
    }
}
