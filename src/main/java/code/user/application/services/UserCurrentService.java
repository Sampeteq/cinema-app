package code.user.application.services;

import code.user.domain.User;
import code.user.domain.ports.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class UserCurrentService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Long readCurrentUserId() {
        var mail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return userRepository
                .readyByMail(mail)
                .map(User::getId)
                .orElseThrow(() -> new UsernameNotFoundException(mail));
    }
}
