package com.cinema.users.infrastructure.security;

import com.cinema.users.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .getByMail(username)
                .map(user -> User
                        .builder()
                        .username(user.getMail())
                        .password(user.getPassword())
                        .authorities(user.getRole().name())
                        .build()
                )
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
