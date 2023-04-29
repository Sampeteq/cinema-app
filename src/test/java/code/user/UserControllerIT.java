package code.user;

import code.user.client.commands.SignInCommand;
import code.user.domain.UserRepository;
import code.user.client.commands.handlers.exceptions.NotSamePasswordsException;
import code.user.client.commands.handlers.exceptions.UsernameAlreadyExistsException;
import code.utils.SpringIT;
import code.utils.UserTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static code.utils.UserTestHelper.createSampleSignUpCommand;
import static code.utils.UserTestHelper.createSampleUser;
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
        var signUpRequest = UserTestHelper.createSampleSignUpCommand();

        //when
        var result = mockMvc.perform(
                post("/signup")
                        .content(toJson(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isCreated());
        var signInDto = new SignInCommand(
                signUpRequest.username(),
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
        var user = userRepository.add(createSampleUser("user1"));
        var signUpRequest = UserTestHelper.createSampleSignUpCommand(user.getUsername());

        //when
        var result = mockMvc.perform(
                post("/signup")
                        .content(toJson(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new UsernameAlreadyExistsException().getMessage()));
    }

    @Test
    void should_throw_exception_when_password_are_not_the_same() throws Exception {
        //given
        var signUpRequest = createSampleSignUpCommand("password1", "password2");

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
}
