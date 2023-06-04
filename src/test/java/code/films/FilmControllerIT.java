package code.films;

import code.films.application.dto.FilmDto;
import code.films.application.dto.FilmMapper;
import code.films.domain.FilmCategory;
import code.films.domain.FilmRepository;
import code.films.domain.exceptions.FilmWrongYearException;
import code.SpringIT;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static code.films.FilmTestHelper.createCreateFilmCommand;
import static code.films.FilmTestHelper.createFilm;
import static code.films.FilmTestHelper.createFilms;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FilmControllerIT extends SpringIT {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private FilmMapper filmMapper;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_create_film() throws Exception {
        //given
        var cmd = createCreateFilmCommand();

        //when
        var result = mockMvc.perform(
                post("/films")
                        .content(toJson(cmd))
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
        var cmd = createCreateFilmCommand().withYear(wrongYear);

        //when
        var result = mockMvc.perform(
                post("/films")
                        .content(toJson(cmd))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new FilmWrongYearException().getMessage()));
    }

    @Test
    void should_get_all_films() throws Exception {
        //given
        var sampleFilms = filmRepository.addMany(createFilms());

        //when
        var result = mockMvc.perform(
                get("/films")
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(filmMapper.mapToDto(sampleFilms))));
    }

    @Test
    void should_get_films_by_params() throws Exception {
        //given
        var filmMeetingParams = filmRepository.add(
                createFilm(FilmCategory.COMEDY)
        );
        filmRepository.add(
                createFilm(FilmCategory.DRAMA)
        );

        //when
        var result = mockMvc.perform(
                get("/films")
                        .param("category", filmMeetingParams.getCategory().name())
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(List.of(filmMapper.mapToDto(filmMeetingParams)))));
    }
}
