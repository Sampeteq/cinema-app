package code.user.application.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
class UserSignOutService {

    void signOut() {
        SecurityContextHolder.clearContext();
    }
}
