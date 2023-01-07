package code.screenings;

import code.SpringIntegrationTests;
import code.screenings.dto.FilmCategoryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static code.WebTestUtils.toJson;
import static code.screenings.FilmTestUtils.sampleCreateFilmDto;
import static code.screenings.FilmTestUtils.sampleCreateFilmDtos;
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
        var sampleDTO = sampleCreateFilmDto();

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
        ).andExpect(
                        jsonPath("$[0].durationInMinutes").value(sampleDTO.durationInMinutes())
                );
    }

    @ParameterizedTest
    @MethodSource("code.screenings.FilmTestUtils#wrongFilmYears")
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_film_year_is_not_previous_or_current_or_next_one(Integer wrongYear) throws Exception {
        //given
        var dto = sampleCreateFilmDto().withYear(wrongYear);

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
        var sampleFilms = sampleCreateFilmDtos()
                .stream()
                .map(dto -> screeningFacade.createFilm(dto))
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
        var sampleFilm = screeningFacade.createFilm(
                sampleCreateFilmDto().withFilmCategory(FilmCategoryDto.COMEDY)
        );
        screeningFacade.createFilm(
                sampleCreateFilmDto().withFilmCategory(FilmCategoryDto.DRAMA)
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
