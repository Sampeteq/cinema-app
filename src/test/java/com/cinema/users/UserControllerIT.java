package com.cinema.users;

import com.cinema.BaseIT;
import com.cinema.users.UserFixtures;
import com.cinema.users.User;
import com.cinema.users.UserNewPasswordRequest;
import com.cinema.users.UserRepository;
import com.cinema.users.exceptions.UserMailNotUniqueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static com.cinema.users.UserFixtures.createUser;
import static com.cinema.users.UserFixtures.createUserCreateRequest;
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
        var userCreateRequest = UserFixtures.createUserCreateRequest();

        webTestClient
                .post()
                .uri(USERS_BASE_ENDPOINT)
                .bodyValue(userCreateRequest)
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(userRepository.findByMail(userCreateRequest.mail()))
                .isNotEmpty()
                .hasValueSatisfying(user -> {
                    assertEquals(userCreateRequest.mail(), user.getMail());
                    assertTrue(passwordEncoder.matches(userCreateRequest.password(), user.getPassword()));
                    assertEquals(User.Role.COMMON, user.getRole());
                });
    }

    @Test
    void user_name_cannot_be_duplicated() {
        var user = userRepository.save(createUser("user1@mail.com"));
        var userCreateRequest = createUserCreateRequest(user.getMail());

        webTestClient
                .post()
                .uri(USERS_BASE_ENDPOINT)
                .bodyValue(userCreateRequest)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message", equalTo(new UserMailNotUniqueException().getMessage()));
    }

    @Test
    void user_password_is_reset() {
        var user = userRepository.save(createUser());

        webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(USERS_BASE_ENDPOINT + "/password/reset")
                        .queryParam("mail", user.getMail())
                        .build()
                )
                .attribute("mail", user.getMail())
                .exchange()
                .expectStatus()
                .isOk();

        var userPasswordResetToken = userRepository
                .findByMail(user.getMail())
                .orElseThrow()
                .getPasswordResetToken();
        assertThat(userPasswordResetToken).isNotNull();
    }

    @Test
    void user_new_password_is_set() {
        var passwordResetToken = UUID.randomUUID();
        var addedUser = userRepository.save(createUser(passwordResetToken));
        var userNewPasswordRequest = new UserNewPasswordRequest(
                passwordResetToken,
                addedUser.getPassword() + "new"
        );

        webTestClient
                .patch()
                .uri(USERS_BASE_ENDPOINT + "/password/new")
                .bodyValue(userNewPasswordRequest)
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(
                userRepository.findByMail(addedUser.getMail())
        ).hasValueSatisfying(
                user -> assertTrue(
                        passwordEncoder.matches(userNewPasswordRequest.newPassword(), user.getPassword())
                )
        );
    }
}
