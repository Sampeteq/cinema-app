package code.screenings;

import code.utils.SpringIntegrationTests;
import code.screenings.dto.FilmCategoryView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static code.utils.FilmTestUtils.*;
import static code.utils.WebTestUtils.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FilmsCrudTests extends SpringIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScreeningFacade screeningFacade;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_add_film() throws Exception {
        //given
        var filmCreatingRequest = createFilmCreatingRequest();

        //when
        var result = mockMvc.perform(
                post("/films")
                        .content(toJson(filmCreatingRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk());
        mockMvc.perform(
                get("/films")
        ).andExpect(
                jsonPath("$.size()").value(1)
        ).andExpect(
                jsonPath("$[0].title").value(filmCreatingRequest.title())
        ).andExpect(
                jsonPath("$[0].category").value(filmCreatingRequest.filmCategory().name())
        ).andExpect(
                jsonPath("$[0].year").value(filmCreatingRequest.year())
        ).andExpect(
                jsonPath("$[0].durationInMinutes").value(filmCreatingRequest.durationInMinutes())
        );
    }

    @ParameterizedTest
    @MethodSource("code.utils.FilmTestUtils#getWrongFilmYears")
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_film_year_is_not_previous_or_current_or_next_one(Integer wrongYear)
            throws Exception {
        //given
        var filmCreatingRequest = createFilmCreatingRequest().withYear(wrongYear);

        //when
        var result = mockMvc.perform(
                post("/films")
                        .content(toJson(filmCreatingRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string("A film year must be previous, current or next one"));
    }

    @Test
    void should_search_all_films() throws Exception {
        //given
        var sampleFilms = createFilms(screeningFacade);

        //when
        var result = mockMvc.perform(
                get("/films")
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(sampleFilms)));
    }

    @Test
    void should_search_films_by_params() throws Exception {
        //given
        var sampleFilm = screeningFacade.createFilm(
                createFilmCreatingRequest().withFilmCategory(FilmCategoryView.COMEDY)
        );
        screeningFacade.createFilm(
                createFilmCreatingRequest().withFilmCategory(FilmCategoryView.DRAMA)
        );

        //when
        var result = mockMvc.perform(
                get("/films")
                        .param("category", sampleFilm.category().name())
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(content().json(toJson(List.of(sampleFilm))));
    }
}
