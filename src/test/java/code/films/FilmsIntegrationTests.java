package code.films;

import code.bookings.infrastructure.rest.dto.mappers.SeatMapper;
import code.films.domain.FilmCategory;
import code.films.domain.FilmRepository;
import code.films.domain.exceptions.ScreeningCollisionException;
import code.films.domain.exceptions.WrongFilmYearException;
import code.films.domain.exceptions.WrongScreeningDateException;
import code.films.infrastructure.rest.dto.FilmDto;
import code.films.infrastructure.rest.dto.ScreeningDto;
import code.films.infrastructure.rest.mappers.FilmMapper;
import code.films.infrastructure.rest.mappers.ScreeningMapper;
import code.rooms.domain.RoomRepository;
import code.utils.SpringIntegrationTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;

import static code.utils.FilmTestHelper.*;
import static code.utils.RoomTestHelper.createSampleRoom;
import static code.utils.WebTestHelper.fromResultActions;
import static code.utils.WebTestHelper.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FilmsIntegrationTests extends SpringIntegrationTests {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private FilmMapper filmMapper;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ScreeningMapper screeningMapper;

    @Autowired
    private SeatMapper seatMapper;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_create_film() throws Exception {
        //given
        var cmd = createSampleCreateFilmCommand();

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
    @MethodSource("code.utils.FilmTestHelper#sampleWrongFilmYears")
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_film_year_is_not_previous_or_current_or_next_one(Integer wrongYear)
            throws Exception {
        //given
        var dto = createSampleCreateFilmCommand().withYear(wrongYear);

        //when
        var result = mockMvc.perform(
                post("/films")
                        .content(toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new WrongFilmYearException().getMessage()));
    }

    @Test
    void should_get_all_films() throws Exception {
        //given
        var sampleFilms = filmRepository.addMany(createSampleFilms());

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
                createSampleFilm().withCategory(FilmCategory.COMEDY)
        );
        var filmNotMeetingParams = filmRepository.add(
                createSampleFilm().withCategory(FilmCategory.DRAMA)
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

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_create_screening() throws Exception {
        //given
        var sampleFilm = filmRepository.add(createSampleFilm());
        var sampleRoom = roomRepository.add(createSampleRoom());
        var cmd = createSampleCreateScreeningCommand(sampleFilm.getId(), sampleRoom.getId());

        //when
        var result = mockMvc.perform(
                post("/films/screenings")
                        .content(toJson(cmd))
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
        var filmId = filmRepository.add(createSampleFilm()).getId();
        var roomId = roomRepository.add(createSampleRoom()).getId();
        var cmd = createSampleCreateScreeningCommand(filmId, roomId).withDate(wrongDate);

        //when
        var result = mockMvc.perform(
                post("/films/screenings")
                        .content(toJson(cmd))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new WrongScreeningDateException().getMessage()));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_there_is_collision_between_screenings() throws Exception {
        //given
        var film = createSampleFilm();
        var screening = createSampleScreening(film);
        film.addScreening(screening);
        filmRepository.add(film);


        var cmd = createSampleCreateScreeningCommand(
                film.getId(),
                screening.getRoom().getId()
        ).withDate(screening.getDate().plusMinutes(10));

        //when
        var result = mockMvc.perform(
                post("/films/screenings")
                        .content(toJson(cmd))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new ScreeningCollisionException().getMessage()));
    }

    @Test
    void should_get_all_screenings() throws Exception {
        //given
        var film = createSampleFilm();
        var screenings = createSampleScreenings(film);
        screenings.forEach(film::addScreening);
        filmRepository.add(film);

        //when
        var result = mockMvc.perform(
                get("/films/screenings")
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(screeningMapper.mapToDto(screenings))));
    }

    @Test
    void should_get_seats_for_screening() throws Exception {
        //given
        var film = createSampleFilm();
        var screening = createSampleScreening(film);
        film.addScreening(screening);
        filmRepository.add(film);

        //when
        var result = mockMvc.perform(
                get("/films/screenings/" + screening.getId() + "/seats")
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(seatMapper.toDto(screening.getSeats()))));
    }

    @Test
    void should_get_screenings_by_params() throws Exception {
        //given
        var film = createSampleFilm();
        var screeningMeetParams = createSampleScreening(film);
        film.addScreening(screeningMeetParams);
        filmRepository.add(film);

        //when
        var result = mockMvc.perform(
                get("/films/screenings")
                        .param("filmId", screeningMeetParams.getFilm().getId().toString())
                        .param("date", screeningMeetParams.getDate().toString())
        );
        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(List.of(screeningMapper.mapToDto(screeningMeetParams)))));
    }
}
