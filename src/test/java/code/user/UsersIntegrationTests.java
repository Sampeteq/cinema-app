package code.user;

import code.user.dto.SignInDto;
import code.utils.SpringIntegrationTests;
import code.utils.UserTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static code.utils.UserTestHelper.createSignUpDto;
import static code.utils.WebTestHelper.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UsersIntegrationTests extends SpringIntegrationTests {

    @Autowired
    private UserTestHelper userTestHelper;

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
        var signInDto = new SignInDto(
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
        var username = userTestHelper.signUpUser("user1");
        var signUpRequest = UserTestHelper.createSignUpDto(username);

        //when
        var result = mockMvc.perform(
                post("/signup")
                        .content(toJson(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Not unique username"));
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
                .andExpect(content().string("Passwords must be the same"));
    }
}
