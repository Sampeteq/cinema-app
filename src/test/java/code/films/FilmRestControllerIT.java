package code.films;

import code.SpringIT;
import code.films.application.dto.FilmDto;
import code.films.application.dto.FilmMapper;
import code.films.domain.Film;
import code.films.infrastructure.db.FilmRepository;
import code.films.domain.exceptions.FilmWrongYearException;
import code.rooms.infrastructure.db.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;

import static code.films.FilmScreeningTestHelper.createScreening;
import static code.films.FilmTestHelper.createFilmCreateCommand;
import static code.films.FilmTestHelper.createFilms;
import static code.rooms.RoomTestHelper.createRoom;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FilmRestControllerIT extends SpringIT {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FilmMapper filmMapper;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_create_film() throws Exception {
        //given
        var cmd = createFilmCreateCommand();

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
    @MethodSource("code.films.FilmTestHelper#getWrongFilmYears")
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_film_year_is_not_previous_or_current_or_next_one(Integer wrongYear)
            throws Exception {
        //given
        var cmd = createFilmCreateCommand().withYear(wrongYear);

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
    void should_read_all_films() throws Exception {
        //given
        var addedFilms = addFilmsWithScreenings();

        //when
        var result = mockMvc.perform(
                get("/films")
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].screenings", everyItem(notNullValue())))
                .andExpect(content().json(toJson(filmMapper.mapToDto(addedFilms))));
    }

    private List<Film> addFilmsWithScreenings() {
        var films = createFilms();
        var room = roomRepository.add(createRoom());
        var screening1 = createScreening(
                films.get(0),
                room,
                LocalDateTime.of(2023,9,1,16,30)
        );
        var screening2 = createScreening(
                films.get(1),
                room,
                LocalDateTime.of(2023,9,3,18,30)
        );
        films.get(0).addScreening(screening1);
        films.get(1).addScreening(screening2);
        return films
                .stream()
                .map(filmRepository::add)
                .toList();
    }
}
