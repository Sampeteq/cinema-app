package com.cinema.users.application.services;

import com.cinema.users.application.dto.UserPasswordNewDto;
import com.cinema.users.application.dto.UserSignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserSignUpService userSignUpService;
    private final UserPasswordResetService userPasswordResetService;
    private final UserPasswordNewService userPasswordNewService;
    private final UserCurrentService userCurrentService;

    public void signUpUser(UserSignUpDto dto) {
        userSignUpService.signUpUser(dto);
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
