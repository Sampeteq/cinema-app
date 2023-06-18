package code.catalog.infrastructure.rest;

import code.SpringIT;
import code.catalog.domain.exceptions.FilmWrongYearException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static code.catalog.helpers.FilmTestHelper.createFilmCreateDto;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FilmCreateAdminRestControllerIT extends SpringIT {

    public static final String FILMS_BASE_ENDPOINT = "/films";

    @Test
    @WithMockUser(authorities = "COMMON")
    void should_only_admin_create_film() throws Exception {
        //given

        //when
        var result = mockMvc.perform(
                post(FILMS_BASE_ENDPOINT)
        );

        //then
        result.andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_create_film() throws Exception {
        //given
        var cmd = createFilmCreateDto();

        //when
        var result = mockMvc.perform(
                post(FILMS_BASE_ENDPOINT)
                        .content(toJson(cmd))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isCreated());
        mockMvc
                .perform(get(FILMS_BASE_ENDPOINT + "/screenings"))
                .andExpect(jsonPath("$[0].title", equalTo(cmd.title())))
                .andExpect(jsonPath("$[0].category", equalTo(cmd.category().name())))
                .andExpect(jsonPath("$[0].year", equalTo(cmd.year())))
                .andExpect(jsonPath("$[0].durationInMinutes", equalTo(cmd.durationInMinutes())));
    }

    @ParameterizedTest
    @MethodSource("code.catalog.helpers.FilmTestHelper#getWrongFilmYears")
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_film_year_is_not_previous_or_current_or_next_one(Integer wrongYear)
            throws Exception {
        //given
        var cmd = createFilmCreateDto().withYear(wrongYear);

        //when
        var result = mockMvc.perform(
                post(FILMS_BASE_ENDPOINT)
                        .content(toJson(cmd))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new FilmWrongYearException().getMessage()));
    }
}
