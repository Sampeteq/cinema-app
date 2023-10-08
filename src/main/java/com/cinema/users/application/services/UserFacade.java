package com.cinema.users.application.services;

import com.cinema.users.application.dto.UserPasswordNewDto;
import com.cinema.users.application.dto.UserCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserCreateService userCreateService;
    private final UserPasswordResetService userPasswordResetService;
    private final UserPasswordNewService userPasswordNewService;
    private final UserCurrentService userCurrentService;

    public void createUser(UserCreateDto dto) {
        userCreateService.createUser(dto);
    }

    public void resetUserPassword(String mail) {
        userPasswordResetService.resetUserPassword(mail);
    }

    public void setNewUserPassword(UserPasswordNewDto dto) {
        userPasswordNewService.setNewUserPassword(dto);
    }

    public Long readCurrentUserId() {
        return userCurrentService.readCurrentUserId();
    }
}
