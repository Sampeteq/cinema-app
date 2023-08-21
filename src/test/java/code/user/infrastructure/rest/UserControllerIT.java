package code.user.infrastructure.rest;

import code.SpringIT;
import code.user.application.dto.UserPasswordNewDto;
import code.user.application.dto.UserSignInDto;
import code.user.domain.exceptions.UserAlreadyLoggedInException;
import code.user.domain.exceptions.UserMailAlreadyExistsException;
import code.user.domain.exceptions.UserNotSamePasswordsException;
import code.user.domain.ports.UserRepository;
import code.user.helpers.UserTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static code.user.helpers.UserTestHelper.createSignInDto;
import static code.user.helpers.UserTestHelper.createSignUpDto;
import static code.user.helpers.UserTestHelper.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerIT extends SpringIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    void should_sign_up_and_sing_in() throws Exception {
        //given
        var signUpDto = UserTestHelper.createSignUpDto();

        //when
        var result = mockMvc.perform(
                post("/sign-up")
                        .content(toJson(signUpDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isCreated());
        var signInDto = new UserSignInDto(
                signUpDto.mail(),
                signUpDto.password()
        );
        mockMvc.perform(
                post("/sign-in")
                        .content(toJson(signInDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void should_throw_exception_when_username_is_not_unique() throws Exception {
        //given
        var user = userRepository.add(createUser("user1@mail.com"));
        var signUpRequest = UserTestHelper.createSignUpDto(user.getUsername());

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
    void should_throw_exception_when_password_are_not_the_same() throws Exception {
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
    @WithMockUser(username = "user1@mail.com")
    void should_throw_exception_when_user_is_already_logged_in() throws Exception {
        //given
        var signInDto = createSignInDto("user1@mail.com");

        //when
        var result = mockMvc.perform(
                post("/sign-in")
                        .content(toJson(signInDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new UserAlreadyLoggedInException().getMessage()));
    }

    @Test
    void should_reset_user_password() throws Exception {
        //given
        var user = userRepository.add(createUser());

        //when
        var result = mockMvc.perform(
                post("/password/reset").param("mail", user.getUsername())
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
    void should_set_new_user_password() throws Exception {
        //given
        var passwordResetToken = UUID.randomUUID();
        var user = userRepository.add(createUser(passwordResetToken));
        var userPasswordNewDto = new UserPasswordNewDto(
                passwordResetToken,
                user.getPassword() + "new"
        );

        //when
        var result = mockMvc.perform(
                post("/password/new").content(
                        toJson(userPasswordNewDto)
                ).contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk());
        var userSignInDto = new UserSignInDto(
                user.getUsername(),
                userPasswordNewDto.newPassword()
        );
        mockMvc.perform(
                post("/sign-in").content(
                        toJson(userSignInDto)
                ).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
}
