package code.user.application.handlers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserSignOutHandler {

    public void handle() {
        SecurityContextHolder.clearContext();
    }
}
