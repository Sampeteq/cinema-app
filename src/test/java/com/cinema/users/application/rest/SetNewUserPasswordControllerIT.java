package com.cinema.users.application.rest;

import com.cinema.SpringIT;
import com.cinema.users.application.commands.SetNewUserPassword;
import com.cinema.users.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static com.cinema.users.UserFixture.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SetNewUserPasswordControllerIT extends SpringIT {

    private static final String USERS_BASE_ENDPOINT = "/users";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void user_new_password_is_set() {
        //given
        var passwordResetToken = UUID.randomUUID();
        var addedUser = userRepository.add(createUser(passwordResetToken));
        var command = new SetNewUserPassword(
                passwordResetToken,
                addedUser.getPassword() + "new"
        );

        //when
        var spec = webTestClient
                .patch()
                .uri(USERS_BASE_ENDPOINT + "/password/new")
                .bodyValue(command)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then
        spec.expectStatus().isOk();
        assertThat(
                userRepository.readyByMail(addedUser.getMail())
        ).hasValueSatisfying(
                user -> assertTrue(
                        passwordEncoder.matches(command.newPassword(), user.getPassword())
                )
        );
    }
}
