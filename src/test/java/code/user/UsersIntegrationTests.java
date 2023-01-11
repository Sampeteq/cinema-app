package code.user;

import code.user.dto.SignInRequest;
import code.utils.SpringIntegrationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static code.utils.UserTestUtils.createSignUpRequest;
import static code.utils.UserTestUtils.signUpUser;
import static code.utils.WebTestUtils.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UsersIntegrationTests extends SpringIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserFacade userFacade;

    @Test
    void should_sign_up_and_sing_in() throws Exception {
        //given
        var signUpRequest = createSignUpRequest();

        //when
        var result = mockMvc.perform(
                post("/signup")
                        .content(toJson(signUpRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isCreated());
        var signInRequest = new SignInRequest(
                signUpRequest.username(),
                signUpRequest.password()
        );
        mockMvc.perform(
                post("/signin")
                        .content(toJson(signInRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void should_throw_exception_when_username_is_not_unique() throws Exception {
        //given
        var username = signUpUser(userFacade);
        var signUpRequest = createSignUpRequest(username);

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
        var signUpRequest = createSignUpRequest("password1", "password2");

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
