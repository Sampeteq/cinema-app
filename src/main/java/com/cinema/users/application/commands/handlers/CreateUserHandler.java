package com.cinema.users.application.commands.handlers;

import com.cinema.users.application.commands.CreateUser;
import com.cinema.users.domain.UserRepository;
import com.cinema.users.domain.UserRole;
import com.cinema.users.domain.factories.UserFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserHandler {

    private final UserFactory userFactory;
    private final UserRepository userRepository;

    public void handle(CreateUser command) {
        var user = userFactory.createUser(command.mail(), command.password(), UserRole.COMMON);
        userRepository.add(user);
    }
}
