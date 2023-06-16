package code.catalog.infrastructure.rest;

import code.SpringIT;
import code.catalog.application.dto.ScreeningDto;
import code.catalog.domain.Screening;
import code.catalog.domain.exceptions.RoomsNoAvailableException;
import code.catalog.domain.exceptions.ScreeningWrongDateException;
import code.catalog.infrastructure.db.FilmRepository;
import code.catalog.infrastructure.db.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;

import static code.catalog.helpers.ScreeningTestHelper.SCREENING_DATE;
import static code.catalog.helpers.ScreeningTestHelper.createScreening;
import static code.catalog.helpers.FilmTestHelper.createFilm;
import static code.catalog.helpers.RoomTestHelper.createRoom;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ScreeningCreateRestControllerIT extends SpringIT {

    public static final String FILMS_BASE_ENDPOINT = "/films";
    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_create_screening() throws Exception {
        //given
        var sampleFilm = filmRepository.add(createFilm());
        var sampleRoom = roomRepository.add(createRoom());
        var screeningDate = SCREENING_DATE;

        //when
        var result = mockMvc.perform(
                post("/films/" + sampleFilm.getId() + "/screenings")
                        .param("screeningDate", screeningDate.toString())
        );

        //then
        result.andExpect(status().isCreated());
        var createdScreening = fromResultActions(result, ScreeningDto.class);
        mockMvc
                .perform(get(FILMS_BASE_ENDPOINT))
                .andExpect(
                        jsonPath("$[0].screenings[0].id", equalTo(createdScreening.id().intValue()))
                )
                .andExpect(
                        jsonPath("$[0].screenings[0].date", equalTo(createdScreening.date().toString()))
                );
    }

    @ParameterizedTest
    @MethodSource("code.catalog.ScreeningTestHelper#getWrongScreeningDates")
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_screening_year_is_not_current_or_next_one(LocalDateTime wrongDate)
            throws Exception {
        //given
        var filmId = filmRepository.add(createFilm()).getId();
        var roomId = roomRepository.add(createRoom()).getId();

        //when
        var result = mockMvc.perform(
                post(FILMS_BASE_ENDPOINT + "/" + filmId + "/screenings")
                        .param("screeningDate", wrongDate.toString())
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new ScreeningWrongDateException().getMessage()));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_there_is_collision_between_screenings() throws Exception {
        //given
        var screening = prepareScreening();
        var screeningDate = screening.getDate().plusMinutes(10);

        //when
        var result = mockMvc.perform(
                post(FILMS_BASE_ENDPOINT + "/" + screening.getFilm().getId() + "/screenings")
                        .param("screeningDate", screeningDate.toString())
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new RoomsNoAvailableException().getMessage()));
    }

    private Screening prepareScreening() {
        var film = createFilm();
        var room = roomRepository.add(createRoom());
        var screening = createScreening(film, room);
        film.addScreening(screening);
        return filmRepository
                .add(film)
                .getScreenings()
                .get(0);
    }
}
