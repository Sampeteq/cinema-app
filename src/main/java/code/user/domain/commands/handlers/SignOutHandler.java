package code.user.domain.commands.handlers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SignOutHandler {

    public void handle() {
        SecurityContextHolder.clearContext();
    }
}
