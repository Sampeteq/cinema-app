package com.cinema.users.infrastructure.ui;

import com.cinema.BaseIT;
import com.cinema.users.domain.User;
import com.cinema.users.domain.UserRepository;
import com.cinema.users.domain.UserRole;
import com.cinema.users.domain.exceptions.UserMailNotUniqueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static com.cinema.users.UserFixtures.MAIL;
import static com.cinema.users.UserFixtures.PASSWORD;
import static com.cinema.users.UserFixtures.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JpaUserControllerIT extends BaseIT {

    private static final String USERS_BASE_ENDPOINT = "/public/users";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void user_is_created() {
        var userCreateDto = new UserCreateDto(MAIL, PASSWORD);

        webTestClient
                .post()
                .uri(USERS_BASE_ENDPOINT)
                .bodyValue(userCreateDto)
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(userRepository.getByMail(userCreateDto.mail()))
                .isNotEmpty()
                .hasValueSatisfying(user -> {
                    assertEquals(userCreateDto.mail(), user.getMail());
                    assertTrue(passwordEncoder.matches(userCreateDto.password(), user.getPassword()));
                    assertEquals(UserRole.COMMON, user.getRole());
                });
    }

    @Test
    void user_name_cannot_be_duplicated() {
        var user = userRepository.save(createUser());
        var userCreateDto = new UserCreateDto(user.getMail(), PASSWORD);

        webTestClient
                .post()
                .uri(USERS_BASE_ENDPOINT)
                .bodyValue(userCreateDto)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$").isEqualTo(new UserMailNotUniqueException().getMessage());
    }

    @Test
    void user_password_is_reset() {
        var user = userRepository.save(createUser());

        webTestClient
                .patch()
                .uri(USERS_BASE_ENDPOINT + "/password/reset" + "?mail=" + user.getMail())
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(userRepository.getByMail(user.getMail()))
                .map(User::getPasswordResetToken)
                .isNotEmpty();
    }

    @Test
    void user_new_password_is_set() {
        var passwordResetToken = UUID.randomUUID();
        var addedUser = userRepository.save(createUser(passwordResetToken));
        var userNewPasswordDto = new UserNewPasswordDto(
                passwordResetToken,
                addedUser.getPassword() + "new"
        );

        webTestClient
                .patch()
                .uri(USERS_BASE_ENDPOINT + "/password/new")
                .bodyValue(userNewPasswordDto)
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(userRepository.getByMail(addedUser.getMail()))
                .map(User::getPassword)
                .isNotEmpty()
                .get()
                .matches(password -> passwordEncoder.matches(userNewPasswordDto.newPassword(), password));
    }
}
