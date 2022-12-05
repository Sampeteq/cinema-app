package code.films;

import code.SpringIntegrationTests;
import code.films.dto.FilmCategoryDto;
import code.films.exception.FilmYearException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static code.WebTestUtils.toJson;
import static code.films.FilmTestUtils.sampleAddFilmDTO;
import static code.films.FilmTestUtils.sampleAddFilmDTOs;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FilmsCrudTests extends SpringIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmFacade filmFacade;

    @Test
    @WithMockUser(roles = "ADMIN")
    void should_add_film() throws Exception {
        //given
        var sampleDTO = sampleAddFilmDTO();

        //when
        var result = mockMvc.perform(
                post("/films")
                        .content(toJson(sampleDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk());
        mockMvc.perform(
                get("/films")
        ).andExpect(
                jsonPath("$.size()").value(1)
        ).andExpect(
                jsonPath("$[0].title").value(sampleDTO.title())
        ).andExpect(
                jsonPath("$[0].category").value(sampleDTO.filmCategory().name())
        ).andExpect(
                jsonPath("$[0].year").value(sampleDTO.year())
        );
    }

    @ParameterizedTest
    @MethodSource("code.films.FilmTestUtils#wrongFilmYears")
    @WithMockUser(roles = "ADMIN")
    void should_throw_exception_when_film_year_is_not_previous_or_current_or_next_one(Integer wrongYear) throws Exception {
        //given
        var dto = sampleAddFilmDTO().withYear(wrongYear);

        //when
        var result = mockMvc.perform(
                post("/films")
                        .content(toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new FilmYearException(dto.year()).getMessage()));
    }

    @Test
    void should_search_all_films() throws Exception {
        //given
        var sampleFilms = sampleAddFilmDTOs()
                .stream()
                .map(dto -> filmFacade.add(dto))
                .toList();

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
        var sampleFilm = filmFacade.add(
                sampleAddFilmDTO().withFilmCategory(FilmCategoryDto.COMEDY)
        );
        filmFacade.add(
                sampleAddFilmDTO().withFilmCategory(FilmCategoryDto.DRAMA)
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
