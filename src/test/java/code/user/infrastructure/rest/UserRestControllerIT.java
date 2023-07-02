package code.user.infrastructure.rest;

import code.SpringIT;
import code.user.application.dto.UserSignInDto;
import code.user.domain.exceptions.UserAlreadyLoggedInException;
import code.user.domain.exceptions.UserMailAlreadyExistsException;
import code.user.domain.exceptions.UserNotSamePasswordsException;
import code.user.helpers.UserTestHelper;
import code.user.domain.ports.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static code.user.helpers.UserTestHelper.createSignInCommand;
import static code.user.helpers.UserTestHelper.createSignUpDto;
import static code.user.helpers.UserTestHelper.createUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserRestControllerIT extends SpringIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    void should_sign_up_and_sing_in() throws Exception {
        //given
        var signUpRequest = UserTestHelper.createSignUpDto();

        //when
        var result = mockMvc.perform(
                post("/signup")
                        .content(toJson(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isCreated());
        var signInDto = new UserSignInDto(
                signUpRequest.mail(),
                signUpRequest.password()
        );
        mockMvc.perform(
                post("/signin")
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
                post("/signup")
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
                post("/signup")
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
        var signInCommand = createSignInCommand("user1@mail.com");

        //when
        var result = mockMvc.perform(
                post("/signin")
                        .content(toJson(signInCommand))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new UserAlreadyLoggedInException().getMessage()));
    }
}
