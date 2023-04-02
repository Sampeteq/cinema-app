package code.bookings;

import code.bookings.application.dto.FilmDto;
import code.bookings.application.dto.ScreeningDto;
import code.bookings.domain.FilmCategory;
import code.utils.FilmTestHelper;
import code.utils.SpringIntegrationTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;

import static code.utils.FilmTestHelper.sampleCreateFilmDto;
import static code.utils.WebTestHelper.fromResultActions;
import static code.utils.WebTestHelper.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FilmsIntegrationTests extends SpringIntegrationTests {

    @Autowired
    private FilmTestHelper filmTestHelper;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_create_film() throws Exception {
        //given
        var dto = sampleCreateFilmDto();

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
    @MethodSource("code.utils.FilmTestHelper#sampleWrongFilmYears")
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_film_year_is_not_previous_or_current_or_next_one(Integer wrongYear)
            throws Exception {
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
        var sampleFilms = filmTestHelper.addSampleFilms();

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
        var filmWithParams = filmTestHelper.addSampleFilm(FilmCategory.COMEDY);
        filmTestHelper.addSampleFilm(FilmCategory.DRAMA);

        //when
        var result = mockMvc.perform(
                get("/films")
                        .param("category", filmWithParams.category().name())
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(content().json(toJson(List.of(filmWithParams))));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_create_screening() throws Exception {
        //given
        var film = filmTestHelper.addSampleFilmWithoutScreenings();
        var room = filmTestHelper.addSampleRoom();
        var dto = FilmTestHelper.sampleCreateScreeningDto(
                film.id(),
                room.id()
        );

        //when
        var result = mockMvc.perform(
                post("/films/screenings")
                        .content(toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isCreated());
        var createdScreening = fromResultActions(result, ScreeningDto.class);
        mockMvc
                .perform(get("/films/screenings"))
                .andExpect(content().json(toJson(List.of(createdScreening))));
    }

    @ParameterizedTest
    @MethodSource("code.utils.FilmTestHelper#sampleWrongScreeningDates")
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_screening_year_is_not_current_or_next_one(LocalDateTime wrongDate)
            throws Exception {
        //given
        var filmId = filmTestHelper.addSampleFilm().id();
        var roomId = filmTestHelper.addSampleRoom().id();
        var dto = FilmTestHelper.sampleCreateScreeningDto(
                filmId,
                roomId
        ).withDate(wrongDate);

        //when
        var result = mockMvc.perform(
                post("/films/screenings")
                        .content(toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        "A screening date year must be current or next one"
                ));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_there_is_collision_between_screenings() throws Exception {
        //given
        var screening = filmTestHelper.addSampleScreening();
        var dto = FilmTestHelper.sampleCreateScreeningDto(
                screening.filmId(),
                screening.roomId(),
                screening.date().plusMinutes(10)
        );

        //when
        var result = mockMvc.perform(
                post("/films/screenings")
                        .content(toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Time and room collision between screenings"));
    }

    @Test
    void should_search_all_screenings() throws Exception {
        //given
        var screenings = filmTestHelper.sampleScreenings();

        //when
        var result = mockMvc.perform(
                get("/films/screenings")
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(screenings)));
    }

    @Test
    void should_search_seats_for_screening() throws Exception {
        //given
        var screening = filmTestHelper.addSampleScreening();
        var seats = filmTestHelper.searchScreeningSeats(screening.id());

        //when
        var result = mockMvc.perform(
                get("/films/screenings/" + screening.id() + "/seats")
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(seats)));
    }

    @Test
    void should_search_screenings_by_search_params() throws Exception {
        //given
        var screenings = filmTestHelper.sampleScreenings();
        var filmId = screenings.get(0).filmId();
        var screeningDate = screenings.get(0).date();
        var filteredScreening = screenings
                .stream()
                .filter(screening -> screening.filmId().equals(filmId))
                .filter(screening -> screening.date().equals(screeningDate))
                .toList();

        //when
        var result = mockMvc.perform(
                get("/films/screenings")
                        .param("filmId", filmId.toString())
                        .param("date", screeningDate.toString())
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(filteredScreening)));
    }
}
