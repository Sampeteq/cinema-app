package code.user;

import code.utils.SpringIntegrationTests;
import code.user.dto.SignInDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static code.utils.WebTestUtils.toJson;
import static code.utils.UserTestUtils.addSampleUser;
import static code.utils.UserTestUtils.sampleSignUpDto;
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
        var sampleDto = sampleSignUpDto();

        //when
        var result = mockMvc.perform(
                post("/signup")
                        .content(toJson(sampleDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isCreated());
        var signInDto = new SignInDto(
                sampleDto.username(),
                sampleDto.password()
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
        var sampleUsername = addSampleUser(userFacade);
        var sampleDto = sampleSignUpDto(sampleUsername);

        //when
        var result = mockMvc.perform(
                post("/signup")
                        .content(toJson(sampleDto))
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
        var sampleDto = sampleSignUpDto("password1", "password2");

        //when
        var result = mockMvc.perform(
                post("/signup")
                        .content(toJson(sampleDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Passwords must be the same"));
    }
}
