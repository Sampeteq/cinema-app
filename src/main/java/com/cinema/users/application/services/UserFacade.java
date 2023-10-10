package com.cinema.users.application.services;

import com.cinema.users.application.dto.UserCreateDto;
import com.cinema.users.application.dto.UserPasswordNewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    public void createUser(UserCreateDto dto) {
        userService.createUser(dto);
    }

    public void resetUserPassword(String mail) {
        userService.resetUserPassword(mail);
    }

    public void setNewUserPassword(UserPasswordNewDto dto) {
        userService.setNewUserPassword(dto);
    }

    public Long readCurrentUserId() {
        return userService.readCurrentUserId();
    }
}
