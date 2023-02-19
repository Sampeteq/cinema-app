package code.films;

import code.films.application.FilmFacade;
import code.films.application.dto.FilmDto;
import code.films.domain.FilmCategory;
import code.utils.FilmTestHelper;
import code.utils.SpringIntegrationTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static code.utils.FilmTestHelper.createCreateFilmDto;
import static code.utils.WebTestHelper.fromResultActions;
import static code.utils.WebTestHelper.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FilmsCrudTests extends SpringIntegrationTests {

    @Autowired
    private FilmFacade filmFacade;

    @Autowired
    private FilmTestHelper filmTestHelper;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_create_film() throws Exception {
        //given
        var dto = createCreateFilmDto();

        //when
        var result = mockMvc.perform(
                post("/films")
                        .content(toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isCreated());
        var createdFilm = fromResultActions(result, FilmDto.class);
        mockMvc
                .perform(get("/films"))
                .andExpect(content().json(toJson(List.of(createdFilm))));
    }

    @ParameterizedTest
    @MethodSource("code.utils.FilmTestHelper#getWrongFilmYears")
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_film_year_is_not_previous_or_current_or_next_one(Integer wrongYear)
            throws Exception {
        //given
        var dto = createCreateFilmDto().withYear(wrongYear);

        //when
        var result = mockMvc.perform(
                post("/films")
                        .content(toJson(dto))
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
        var sampleFilms = filmTestHelper.createFilms();

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
        var sampleFilm = filmFacade.createFilm(
                createCreateFilmDto().withFilmCategory(FilmCategory.COMEDY)
        );
        filmFacade.createFilm(
                createCreateFilmDto().withFilmCategory(FilmCategory.DRAMA)
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
