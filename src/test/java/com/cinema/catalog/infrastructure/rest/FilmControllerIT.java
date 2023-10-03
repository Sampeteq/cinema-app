package com.cinema.catalog.infrastructure.rest;

import com.cinema.SpringIT;
import com.cinema.catalog.application.dto.FilmCreateDto;
import com.cinema.catalog.domain.FilmCategory;
import com.cinema.catalog.domain.FilmRepository;
import com.cinema.catalog.domain.exceptions.FilmTitleNotUniqueException;
import com.cinema.catalog.domain.exceptions.FilmYearOutOfRangeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static com.cinema.catalog.FilmTestHelper.createFilm;
import static com.cinema.catalog.FilmTestHelper.createFilmCreateDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FilmControllerIT extends SpringIT {

    private static final String FILMS_BASE_ENDPOINT = "/films";

    @Autowired
    private FilmRepository filmRepository;

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
    void film_is_created() throws Exception {
        //given
        var title = "Some title";
        var category = FilmCategory.COMEDY;
        var year = 2023;
        var durationInMinutes = 100;
        var dto = new FilmCreateDto(
                title,
                category,
                year,
                durationInMinutes
        );

        //when
        var result = mockMvc.perform(
                post(FILMS_BASE_ENDPOINT)
                        .content(toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isCreated());
        assertThat(filmRepository.readByTitle(dto.title()))
                .isNotEmpty()
                .hasValueSatisfying(film -> {
                    assertEquals(dto.title(), film.getTitle());
                    assertEquals(dto.category(), film.getCategory());
                    assertEquals(dto.year(), film.getYear());
                    assertEquals(dto.durationInMinutes(), film.getDurationInMinutes());
                });
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
    @WithMockUser(authorities = "USER")
    void film_is_deleted_only_by_admin() throws Exception {
        //given

        //when
        var result = mockMvc.perform(delete(FILMS_BASE_ENDPOINT + "/Film 1"));

        //then
        result.andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void film_is_deleted() throws Exception {
        //given
        var film = filmRepository.add(createFilm());

        //when
        var result = mockMvc.perform(delete(FILMS_BASE_ENDPOINT + "/" + film.getTitle()));

        //then
        result.andExpect(status().isNoContent());
        assertThat(filmRepository.existsByTitle(film.getTitle())).isFalse();
    }

    @Test
    void films_are_read() throws Exception {
        //given
        var film = filmRepository.add(createFilm());

        //when
        var result = mockMvc.perform(
                get(FILMS_BASE_ENDPOINT)
        );

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(1)))
                .andExpect(jsonPath("$[*].*", everyItem(notNullValue())))
                .andExpect(jsonPath("$[0].title").value(film.getTitle()))
                .andExpect(jsonPath("$[0].category").value(film.getCategory().name()))
                .andExpect(jsonPath("$[0].year").value(film.getYear()))
                .andExpect(jsonPath("$[0].durationInMinutes").value(film.getDurationInMinutes()));
    }
}
