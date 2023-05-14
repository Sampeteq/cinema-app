package code.user.application.commands;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SignOutHandler {

    public void handle() {
        SecurityContextHolder.clearContext();
    }
}
