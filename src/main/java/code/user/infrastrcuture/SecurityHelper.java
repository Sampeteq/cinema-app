package code.user.infrastrcuture;

import code.user.domain.User;
import code.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SecurityHelper {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        var mail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return userRepository
                .readyByMail(mail)
                .orElseThrow(() -> new UsernameNotFoundException(mail));
    }
}
