package code.user.application.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserSignOutService {

    public void signOut() {
        SecurityContextHolder.clearContext();
    }
}
