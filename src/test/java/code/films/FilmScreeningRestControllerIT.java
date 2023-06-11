package code.films;

import code.films.application.dto.FilmScreeningSeatMapper;
import code.films.infrastructure.db.FilmRepository;
import code.films.domain.FilmScreening;
import code.rooms.infrastructure.db.RoomRepository;
import code.films.application.dto.FilmScreeningDto;
import code.films.application.dto.FilmScreeningMapper;
import code.films.domain.exceptions.RoomsNoAvailableException;
import code.films.domain.exceptions.FilmScreeningWrongDateException;
import code.SpringIT;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;

import static code.films.FilmScreeningTestHelper.createCreateScreeningCommand;
import static code.films.FilmTestHelper.createFilm;
import static code.films.FilmScreeningTestHelper.createScreening;
import static code.films.FilmScreeningTestHelper.createScreenings;
import static code.rooms.RoomTestHelper.createRoom;
import static org.hamcrest.Matchers.empty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FilmScreeningRestControllerIT extends SpringIT {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FilmScreeningMapper screeningMapper;

    @Autowired
    private FilmScreeningSeatMapper seatMapper;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_create_screening() throws Exception {
        //given
        var sampleFilm = filmRepository.add(createFilm());
        var sampleRoom = roomRepository.add(createRoom());
        var cmd = createCreateScreeningCommand(sampleFilm.getId());

        //when
        var result = mockMvc.perform(
                post("/screenings")
                        .content(toJson(cmd))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isCreated());
        var createdScreening = fromResultActions(result, FilmScreeningDto.class);
        mockMvc
                .perform(get("/screenings"))
                .andExpect(content().json(toJson(List.of(createdScreening))));
    }

    @ParameterizedTest
    @MethodSource("code.films.FilmScreeningTestHelper#getWrongScreeningDates")
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_screening_year_is_not_current_or_next_one(LocalDateTime wrongDate)
            throws Exception {
        //given
        var filmId = filmRepository.add(createFilm()).getId();
        var roomId = roomRepository.add(createRoom()).getId();
        var cmd = createCreateScreeningCommand(filmId).withDate(wrongDate);

        //when
        var result = mockMvc.perform(
                post("/screenings")
                        .content(toJson(cmd))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new FilmScreeningWrongDateException().getMessage()));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_there_is_collision_between_screenings() throws Exception {
        //given
        var screening = prepareScreening();

        var cmd = createCreateScreeningCommand(
                screening.getFilm().getId()
        ).withDate(screening.getDate().plusMinutes(10));

        //when
        var result = mockMvc.perform(
                post("/screenings")
                        .content(toJson(cmd))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new RoomsNoAvailableException().getMessage()));
    }

    @Test
    void should_read_all_screenings() throws Exception {
        //given
        var screenings = prepareScreenings();

        //when
        var result = mockMvc.perform(
                get("/screenings")
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(screeningMapper.mapToDto(screenings))));
    }

    @Test
    void should_not_read_finished_screenings() throws Exception {
        //given
        prepareFinishedScreenings();

        //when
        var result = mockMvc.perform(
                get("/screenings")
        );

        //then
        result.andExpect(jsonPath("$", empty()));
    }

    @Test
    void should_read_screenings_by_params() throws Exception {
        //given
        var film = filmRepository.add(createFilm());
        var room = roomRepository.add(createRoom());
        film.addScreening(createScreening(film, room));
        var screeningMeetParams = filmRepository
                .add(film)
                .getScreenings()
                .get(0);

        //when
        var result = mockMvc.perform(
                get("/screenings")
                        .param("filmTitle", screeningMeetParams.getFilm().getTitle())
                        .param("date", screeningMeetParams.getDate().toString())
        );
        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(List.of(screeningMapper.mapToDto(screeningMeetParams)))));
    }

    @Test
    void should_read_seats_for_screening() throws Exception {
        //given
        var screening = prepareScreening();

        //when
        var result = mockMvc.perform(
                get("/screenings/" + screening.getId() + "/seats")
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(seatMapper.toDto(screening.getSeats()))));
    }

    private FilmScreening prepareScreening() {
        var film = createFilm();
        var room = roomRepository.add(createRoom());
        var screening = createScreening(film, room);
        film.addScreening(screening);
        return filmRepository
                .add(film)
                .getScreenings()
                .get(0);
    }

    private List<FilmScreening> prepareScreenings() {
        var film = createFilm();
        var room = roomRepository.add(createRoom());
        var screenings = createScreenings(film, room);
        screenings.forEach(film::addScreening);
        return filmRepository.add(film).getScreenings();
    }

    private List<FilmScreening> prepareFinishedScreenings() {
        var film = createFilm();
        var room = roomRepository.add(createRoom());
        var screenings = createScreenings(film, room);
        screenings.forEach(FilmScreening::finish);
        screenings.forEach(film::addScreening);
        filmRepository.add(film);
        return screenings;
    }
}
