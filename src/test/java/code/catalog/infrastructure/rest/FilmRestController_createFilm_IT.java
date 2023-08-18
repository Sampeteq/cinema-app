package code.catalog.infrastructure.rest;

import code.SpringIT;
import code.catalog.application.dto.FilmDto;
import code.catalog.domain.exceptions.FilmTitleNotUniqueException;
import code.catalog.domain.exceptions.FilmYearOutOfRangeException;
import code.catalog.domain.ports.FilmRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static code.catalog.helpers.FilmTestHelper.createFilm;
import static code.catalog.helpers.FilmTestHelper.createFilmCreateDto;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FilmRestController_createFilm_IT extends SpringIT {

    public static final String FILMS_BASE_ENDPOINT = "/films";

    @Autowired
    private FilmRepository filmRepository;

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
        var expectedDto = List.of(
                new FilmDto(
                1L,
                cmd.title(),
                cmd.category(),
                cmd.year(),
                cmd.durationInMinutes()
                )
        );

        //when
        var result = mockMvc.perform(
                post(FILMS_BASE_ENDPOINT)
                        .content(toJson(cmd))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isCreated());
        mockMvc
                .perform(get(FILMS_BASE_ENDPOINT))
                .andExpect(content().json(toJson(expectedDto)));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_title_is_not_unique() throws Exception {
        //given
        var film = filmRepository.add(createFilm());
        var filmCreateDto = createFilmCreateDto().withTitle(film.getTitle());

        //when
        var result = mockMvc.perform(
                post(FILMS_BASE_ENDPOINT)
                        .content(toJson(filmCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new FilmTitleNotUniqueException().getMessage()));
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
                .andExpect(content().string(new FilmYearOutOfRangeException().getMessage()));
    }
}
