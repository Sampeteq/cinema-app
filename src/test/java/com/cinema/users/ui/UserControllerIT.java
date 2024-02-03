package com.cinema.users.ui;

import com.cinema.BaseIT;
import com.cinema.users.UserFixture;
import com.cinema.users.application.dto.SetNewUserPasswordDto;
import com.cinema.users.domain.UserRepository;
import com.cinema.users.domain.UserRole;
import com.cinema.users.domain.exceptions.UserMailNotUniqueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static com.cinema.users.UserFixture.createCrateUserDto;
import static com.cinema.users.UserFixture.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserControllerIT extends BaseIT {

    private static final String USERS_BASE_ENDPOINT = "/public/users";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    void user_is_created() {
        //given
        var crateUserDto = UserFixture.createCrateUserDto();

        //then
        var spec = webTestClient
                .post()
                .uri(USERS_BASE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(crateUserDto)
                .exchange();


        spec.expectStatus().isOk();
        assertThat(userRepository.findByMail(crateUserDto.mail()))
                .isNotEmpty()
                .hasValueSatisfying(user -> {
                    assertEquals(crateUserDto.mail(), user.getUsername());
                    assertTrue(passwordEncoder.matches(crateUserDto.password(), user.getPassword()));
                    assertEquals(UserRole.COMMON, user.getRole());
                });
    }

    @Test
    void user_name_cannot_be_duplicated() {
        //given
        var user = userRepository.save(createUser("user1@mail.com"));
        var dto = createCrateUserDto(user.getUsername());

        //when
        var spec = webTestClient
                .post()
                .uri(USERS_BASE_ENDPOINT)
                .bodyValue(dto)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then
        var expectedMessage = new UserMailNotUniqueException().getMessage();
        spec
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    void user_password_is_reset() {
        //given
        var user = userRepository.save(createUser());

        //when
        var spec = webTestClient
                .patch()
                .uri(uriBuilder -> uriBuilder
                        .path(USERS_BASE_ENDPOINT + "/password/reset")
                        .queryParam("mail", user.getMail())
                        .build()
                )
                .attribute("mail", user.getMail())
                .exchange();

        //then
        spec.expectStatus().isOk();
        var userPasswordResetToken = userRepository
                .findByMail(user.getUsername())
                .orElseThrow()
                .getPasswordResetToken();
        assertThat(userPasswordResetToken).isNotNull();
    }

    @Test
    void user_new_password_is_set() {
        //given
        var passwordResetToken = UUID.randomUUID();
        var addedUser = userRepository.save(createUser(passwordResetToken));
        var setNewUserPasswordDto = new SetNewUserPasswordDto(
                passwordResetToken,
                addedUser.getPassword() + "new"
        );

        //when
        var spec = webTestClient
                .patch()
                .uri(USERS_BASE_ENDPOINT + "/password/new")
                .bodyValue(setNewUserPasswordDto)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then
        spec.expectStatus().isOk();
        assertThat(
                userRepository.findByMail(addedUser.getMail())
        ).hasValueSatisfying(
                user -> assertTrue(
                        passwordEncoder.matches(setNewUserPasswordDto.newPassword(), user.getPassword())
                )
        );
    }
}
