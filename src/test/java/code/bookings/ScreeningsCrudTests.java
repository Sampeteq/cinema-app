package code.bookings;

import code.utils.FilmTestHelper;
import code.utils.ScreeningTestHelper;
import code.utils.SpringIntegrationTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;

import static code.utils.ScreeningTestHelper.createCreateScreeningDto;
import static code.utils.ScreeningTestHelper.createScreeningRoomDto;
import static code.utils.WebTestHelper.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ScreeningsCrudTests extends SpringIntegrationTests {

    @Autowired
    private ScreeningTestHelper screeningTestHelper;

    @Autowired
    private FilmTestHelper filmTestHelper;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_create_screening() throws Exception {
        //given
        var film = filmTestHelper.createFilm();
        var room = screeningTestHelper.createScreeningRoom();
        var dto = createCreateScreeningDto(
                film.id(),
                room.id()
        );

        //when
        var result = mockMvc.perform(
                post("/screenings")
                        .content(toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk());
        mockMvc.perform(get("/screenings"))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].date").value(dto.date().toString()))
                .andExpect(jsonPath("$[0].minAge").value(dto.minAge()))
                .andExpect(jsonPath("$[0].filmId").value(dto.filmId().toString()))
                .andExpect(jsonPath("$[0].roomId").value(dto.roomId().toString()))
                .andExpect(jsonPath("$[0].freeSeats").value(room.seatsQuantity()));
    }

    @ParameterizedTest
    @MethodSource("code.utils.ScreeningTestHelper#getWrongScreeningDates")
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_screening_year_is_not_current_or_next_one(LocalDateTime wrongDate)
            throws Exception {
        //given
        var filmId = filmTestHelper.createFilm().id();
        var roomId = screeningTestHelper.createScreeningRoom().id();
        var dto = createCreateScreeningDto(
                filmId,
                roomId
        ).withDate(wrongDate);

        //when
        var result = mockMvc.perform(
                post("/screenings")
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
    void should_throw_exception_when_there_is_time_and_room_collision_between_screenings() throws Exception {
        //given
        var screening = screeningTestHelper.createScreening();
        var dto = createCreateScreeningDto(
                screening.filmId(),
                screening.roomId(),
                screening.date().plusMinutes(10)
        );

        //when
        var result = mockMvc.perform(
                post("/screenings")
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
        var screenings = screeningTestHelper.createScreenings();

        //when
        var result = mockMvc.perform(
                get("/screenings")
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(screenings)));
    }

    @Test
    void should_search_seats_for_screening() throws Exception {
        //given
        var screening = screeningTestHelper.createScreening();
        var seats = screeningTestHelper.searchScreeningSeats(screening.id());

        //when
        var result = mockMvc.perform(
                get("/screenings/" + screening.id() + "/seats")
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(seats)));
    }

    @Test
    void should_search_screenings_by_search_params() throws Exception {
        //given
        var screenings = screeningTestHelper.createScreenings();
        var filmId = screenings.get(0).filmId();
        var screeningDate = screenings.get(0).date();
        var filteredScreening = screenings
                .stream()
                .filter(screening -> screening.filmId().equals(filmId))
                .filter(screening -> screening.date().equals(screeningDate))
                .toList();

        //when
        var result = mockMvc.perform(
                get("/screenings")
                        .param("filmId", filmId.toString())
                        .param("date", screeningDate.toString())
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(filteredScreening)));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_create_screening_room() throws Exception {
        //given
        var dto = createScreeningRoomDto();

        //when
        var result = mockMvc.perform(
                post("/rooms")
                        .content(toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk());
        mockMvc.perform(
                        get("/rooms")
                )
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].number").value(dto.number()))
                .andExpect(jsonPath("$[0].rowsQuantity").value(dto.rowsQuantity()))
                .andExpect(jsonPath("$[0].seatsInOneRowQuantity").value(dto.seatsQuantityInOneRow()))
                .andExpect(jsonPath("$[0].seatsQuantity").value(dto.rowsQuantity() * dto.seatsQuantityInOneRow()));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_room_number_is_not_unique() throws Exception {
        //given
        var room = screeningTestHelper.createScreeningRoom();
        var dto = createScreeningRoomDto().withNumber(room.number());

        //when
        var result = mockMvc.perform(
                post("/rooms")
                        .content(toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        "Screening room already exists: " + dto.number()
                ));
    }
}

