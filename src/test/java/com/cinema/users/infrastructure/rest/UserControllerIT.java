package com.cinema.users.infrastructure.rest;

import com.cinema.SpringIT;
import com.cinema.users.application.dto.UserPasswordNewDto;
import com.cinema.users.domain.UserRepository;
import com.cinema.users.domain.UserRole;
import com.cinema.users.domain.exceptions.UserMailAlreadyExistsException;
import com.cinema.users.domain.exceptions.UserNotSamePasswordsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static com.cinema.users.UserTestHelper.createSignUpDto;
import static com.cinema.users.UserTestHelper.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserControllerIT extends SpringIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void user_is_signed_up() {
        //given
        var signUpDto = createSignUpDto();

        //then
        var spec = webTestClient
                .post()
                .uri("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(signUpDto)
                .exchange();


        spec.expectStatus().isCreated();
        assertThat(userRepository.readyByMail(signUpDto.mail()))
                .isNotEmpty()
                .hasValueSatisfying(user -> {
                    assertEquals(signUpDto.mail(), user.getUsername());
                    assertTrue(passwordEncoder.matches(signUpDto.password(), user.getPassword()));
                    assertEquals(UserRole.COMMON, user.getRole());
                });
    }

    @Test
    void user_name_cannot_be_duplicated() {
        //given
        var user = userRepository.add(createUser("user1@mail.com"));
        var signUpRequest = createSignUpDto(user.getUsername());

        //when
        var spec = webTestClient
                .post()
                .uri("/sign-up")
                .bodyValue(signUpRequest)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then
        var expectedMessage = new UserMailAlreadyExistsException().getMessage();
        spec
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    void user_passwords_cannot_be_different() {
        //given
        var signUpDto = createSignUpDto("password1", "password2");

        //when
        var spec = webTestClient
                .post()
                .uri("/sign-up")
                .bodyValue(signUpDto)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then

        var expectedMessage = new UserNotSamePasswordsException().getMessage();
        spec
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(expectedMessage));
    }

    @Test
    void user_password_is_reset() {
        //given
        var user = userRepository.add(createUser());

        //when
        var spec = webTestClient
                .patch()
                .uri(uriBuilder -> uriBuilder
                        .path("/password/reset")
                        .queryParam("mail", user.getMail())
                        .build()
                )
                .attribute("mail", user.getMail())
                .exchange();

        //then
        spec.expectStatus().isOk();
        var userPasswordResetToken = userRepository
                .readyByMail(user.getUsername())
                .orElseThrow()
                .getPasswordResetToken();
        assertThat(userPasswordResetToken).isNotNull();
    }

    @Test
    void user_new_password_is_set() {
        //given
        var passwordResetToken = UUID.randomUUID();
        var addedUser = userRepository.add(createUser(passwordResetToken));
        var userPasswordNewDto = new UserPasswordNewDto(
                passwordResetToken,
                addedUser.getPassword() + "new"
        );

        //when
        var spec = webTestClient
                .patch()
                .uri("/password/new")
                .bodyValue(userPasswordNewDto)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then
        spec.expectStatus().isOk();
        assertThat(
                userRepository.readyByMail(addedUser.getMail())
        ).hasValueSatisfying(
                user -> assertTrue(
                        passwordEncoder.matches(userPasswordNewDto.newPassword(), user.getPassword())
                )
        );
    }
}
