package com.cinema.users.infrastructure.rest;

import com.cinema.SpringIT;
import com.cinema.users.application.dto.UserPasswordNewDto;
import com.cinema.users.domain.UserRepository;
import com.cinema.users.domain.UserRole;
import com.cinema.users.domain.exceptions.UserMailAlreadyExistsException;
import com.cinema.users.domain.exceptions.UserNotSamePasswordsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static com.cinema.users.UserTestHelper.createSignUpDto;
import static com.cinema.users.UserTestHelper.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerIT extends SpringIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void user_is_signed_up() throws Exception {
        //given
        var signUpDto = createSignUpDto();

        //when
        var result = mockMvc.perform(
                post("/sign-up")
                        .content(toJson(signUpDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isCreated());
        assertThat(userRepository.readyByMail(signUpDto.mail()))
                .isNotEmpty()
                .hasValueSatisfying(user -> {
                    assertEquals(signUpDto.mail(), user.getUsername());
                    assertTrue(passwordEncoder.matches(signUpDto.password(), user.getPassword()));
                    assertEquals(UserRole.COMMON, user.getRole());
                });
    }

    @Test
    void user_name_cannot_be_duplicated() throws Exception {
        //given
        var user = userRepository.add(createUser("user1@mail.com"));
        var signUpRequest = createSignUpDto(user.getUsername());

        //when
        var result = mockMvc.perform(
                post("/sign-up")
                        .content(toJson(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new UserMailAlreadyExistsException().getMessage()));
    }

    @Test
    void user_passwords_cannot_be_different() throws Exception {
        //given
        var signUpRequest = createSignUpDto("password1", "password2");

        //when
        var result = mockMvc.perform(
                post("/sign-up")
                        .content(toJson(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new UserNotSamePasswordsException().getMessage()));
    }

    @Test
    void user_password_is_reset() throws Exception {
        //given
        var user = userRepository.add(createUser());

        //when
        var result = mockMvc.perform(
                patch("/password/reset").param("mail", user.getUsername())
        );

        //then
        result.andExpect(status().isOk());
        var userPasswordResetToken = userRepository
                .readyByMail(user.getUsername())
                .orElseThrow()
                .getPasswordResetToken();
        assertThat(userPasswordResetToken).isNotNull();
    }

    @Test
    void user_new_password_is_set() throws Exception {
        //given
        var passwordResetToken = UUID.randomUUID();
        var addedUser = userRepository.add(createUser(passwordResetToken));
        var userPasswordNewDto = new UserPasswordNewDto(
                passwordResetToken,
                addedUser.getPassword() + "new"
        );

        //when
        var result = mockMvc.perform(
                patch("/password/new").content(
                        toJson(userPasswordNewDto)
                ).contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk());
        assertThat(
                userRepository.readyByMail(addedUser.getMail())
        ).hasValueSatisfying(
                user -> assertTrue(
                        passwordEncoder.matches(userPasswordNewDto.newPassword(), user.getPassword())
                )
        );
    }
}
