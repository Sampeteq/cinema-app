package code.screenings;

import code.films.domain.FilmRepository;
import code.rooms.domain.RoomRepository;
import code.screenings.client.dto.ScreeningDto;
import code.screenings.client.dto.ScreeningMapper;
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

import static code.utils.FilmTestHelper.*;
import static code.utils.FilmTestHelper.createSampleScreenings;
import static code.utils.RoomTestHelper.createSampleRoom;
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