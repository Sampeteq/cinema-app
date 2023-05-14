package code.screenings;

import code.films.domain.FilmRepository;
import code.rooms.domain.RoomRepository;
import code.screenings.application.dto.ScreeningDto;
import code.screenings.application.dto.ScreeningMapper;
import code.screenings.application.dto.SeatMapper;
import code.screenings.domain.exceptions.ScreeningCollisionException;
import code.screenings.domain.exceptions.WrongScreeningDateException;
import code.utils.SpringIT;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;

import static code.utils.FilmTestHelper.createCreateScreeningCommand;
import static code.utils.FilmTestHelper.createFilm;
import static code.utils.FilmTestHelper.createScreening;
import static code.utils.FilmTestHelper.createScreenings;
import static code.utils.RoomTestHelper.createRoom;
import static code.utils.WebTestHelper.fromResultActions;
import static code.utils.WebTestHelper.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ScreeningControllerIT extends SpringIT {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ScreeningMapper screeningMapper;

    @Autowired
    private SeatMapper seatMapper;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_create_screening() throws Exception {
        //given
        var sampleFilm = filmRepository.add(createFilm());
        var sampleRoom = roomRepository.add(createRoom());
        var cmd = createCreateScreeningCommand(sampleFilm.getId(), sampleRoom.getId());

        //when
        var result = mockMvc.perform(
                post("/screenings")
                        .content(toJson(cmd))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isCreated());
        var createdScreening = fromResultActions(result, ScreeningDto.class);
        mockMvc
                .perform(get("/screenings"))
                .andExpect(content().json(toJson(List.of(createdScreening))));
    }

    @ParameterizedTest
    @MethodSource("code.utils.FilmTestHelper#getWrongScreeningDates")
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_screening_year_is_not_current_or_next_one(LocalDateTime wrongDate)
            throws Exception {
        //given
        var filmId = filmRepository.add(createFilm()).getId();
        var roomId = roomRepository.add(createRoom()).getId();
        var cmd = createCreateScreeningCommand(filmId, roomId).withDate(wrongDate);

        //when
        var result = mockMvc.perform(
                post("/screenings")
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
        var film = createFilm();
        var room = roomRepository.add(createRoom());
        var screening = createScreening(film, room);
        film.addScreening(screening);
        filmRepository.add(film);


        var cmd = createCreateScreeningCommand(
                film.getId(),
                screening.getRoom().getId()
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
                .andExpect(content().string(new ScreeningCollisionException().getMessage()));
    }

    @Test
    void should_get_all_screenings() throws Exception {
        //given
        var film = createFilm();
        var room = roomRepository.add(createRoom());
        var screenings = createScreenings(film, room);
        screenings.forEach(film::addScreening);
        filmRepository.add(film);

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
    void should_get_screenings_by_params() throws Exception {
        //given
        var film = createFilm();
        var room = roomRepository.add(createRoom());
        var screeningMeetParams = createScreening(film, room);
        film.addScreening(screeningMeetParams);
        filmRepository.add(film);

        //when
        var result = mockMvc.perform(
                get("/screenings")
                        .param("filmId", screeningMeetParams.getFilm().getId().toString())
                        .param("date", screeningMeetParams.getDate().toString())
        );
        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(List.of(screeningMapper.mapToDto(screeningMeetParams)))));
    }

    @Test
    void should_get_seats_for_screening() throws Exception {
        //given
        var film = createFilm();
        var room = roomRepository.add(createRoom());
        var screening = createScreening(film, room);
        film.addScreening(screening);
        filmRepository.add(film);

        //when
        var result = mockMvc.perform(
                get("/screenings/" + screening.getId() + "/seats")
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(seatMapper.toDto(screening.getSeats()))));
    }
}
