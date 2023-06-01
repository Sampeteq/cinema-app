package code.user;

import code.user.application.commands.UserLoginCommand;
import code.user.domain.UserRepository;
import code.user.domain.exceptions.MailAlreadyExistsException;
import code.user.domain.exceptions.NotSamePasswordsException;
import code.user.domain.exceptions.UserAlreadyLoggedInException;
import code.utils.SpringIT;
import code.utils.UserTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static code.utils.UserTestHelper.createSignInCommand;
import static code.utils.UserTestHelper.createSignUpCommand;
import static code.utils.UserTestHelper.createUser;
import static code.utils.WebTestHelper.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerIT extends SpringIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    void should_sign_up_and_sing_in() throws Exception {
        //given
        var signUpRequest = UserTestHelper.createSignUpCommand();

        //when
        var result = mockMvc.perform(
                post("/signup")
                        .content(toJson(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isCreated());
        var signInCmd = new UserLoginCommand(
                signUpRequest.mail(),
                signUpRequest.password()
        );
        mockMvc.perform(
                post("/signin")
                        .content(toJson(signInCmd))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void should_throw_exception_when_username_is_not_unique() throws Exception {
        //given
        var user = userRepository.add(createUser("user1@mail.com"));
        var signUpRequest = UserTestHelper.createSignUpCommand(user.getUsername());

        //when
        var result = mockMvc.perform(
                post("/signup")
                        .content(toJson(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new MailAlreadyExistsException().getMessage()));
    }

    @Test
    void should_throw_exception_when_password_are_not_the_same() throws Exception {
        //given
        var signUpRequest = createSignUpCommand("password1", "password2");

        //when
        var result = mockMvc.perform(
                post("/signup")
                        .content(toJson(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new NotSamePasswordsException().getMessage()));
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
