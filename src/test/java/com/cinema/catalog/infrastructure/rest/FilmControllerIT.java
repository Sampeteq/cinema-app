package com.cinema.catalog.infrastructure.rest;

import com.cinema.SpringIT;
import com.cinema.catalog.application.dto.FilmDto;
import com.cinema.catalog.application.dto.FilmMapper;
import com.cinema.catalog.domain.FilmRepository;
import com.cinema.catalog.domain.exceptions.FilmTitleNotUniqueException;
import com.cinema.catalog.domain.exceptions.FilmYearOutOfRangeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static com.cinema.catalog.FilmTestHelper.createFilm;
import static com.cinema.catalog.FilmTestHelper.createFilmCreateDto;
import static com.cinema.catalog.FilmTestHelper.createFilms;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FilmControllerIT extends SpringIT {

    private static final String FILMS_BASE_ENDPOINT = "/films";

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private FilmMapper filmMapper;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void film_is_created() throws Exception {
        //given
        var dto = createFilmCreateDto();

        //when
        var result = mockMvc.perform(
                post(FILMS_BASE_ENDPOINT)
                        .content(toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isCreated());
        assertThat(filmRepository.readById(1L))
                .isNotEmpty()
                .hasValueSatisfying(film -> {
                    assertEquals(dto.title(), film.getTitle());
                    assertEquals(dto.category(), film.getCategory());
                    assertEquals(dto.year(), film.getYear());
                    assertEquals(dto.durationInMinutes(), film.getDurationInMinutes());
                });
    }

    @Test
    @WithMockUser(authorities = "COMMON")
    void film_can_be_created_only_by_admin() throws Exception {
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
    void film_title_is_unique() throws Exception {
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

        var expectedMessage = new FilmTitleNotUniqueException().getMessage();
        result
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message", equalTo(expectedMessage)));
    }

    @ParameterizedTest
    @MethodSource("com.cinema.catalog.FilmTestHelper#getWrongFilmYears")
    @WithMockUser(authorities = "ADMIN")
    void film_year_is_previous_current_or_nex_one(Integer wrongYear) throws Exception {
        //given
        var cmd = createFilmCreateDto().withYear(wrongYear);

        //when
        var result = mockMvc.perform(
                post(FILMS_BASE_ENDPOINT)
                        .content(toJson(cmd))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        var expectedMessage = new FilmYearOutOfRangeException().getMessage();
        result
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message", equalTo(expectedMessage)));
    }

    @Test
    void films_are_read() throws Exception {
        //given
        var films = addFilms();

        //when
        var result = mockMvc.perform(
                get(FILMS_BASE_ENDPOINT)
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(films)));
    }

    private List<FilmDto> addFilms() {
        return createFilms()
                .stream()
                .map(filmRepository::add)
                .map(film -> filmMapper.mapToDto(film))
                .toList();
    }
}
