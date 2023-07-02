package code.user.application.services;

import code.user.domain.User;
import code.user.infrastrcuture.db.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCurrentService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Long getCurrentUserId() {
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
