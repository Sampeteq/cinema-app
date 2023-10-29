package com.cinema.users.application.queries.handlers;

import com.cinema.users.application.queries.ReadCurrentUserId;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadCurrentUserIdHandler {

    private final UserRepository userRepository;

    public Long handle(ReadCurrentUserId query) {
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
