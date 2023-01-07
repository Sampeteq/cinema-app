package code.screenings;

import code.utils.SpringIntegrationTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static code.utils.FilmTestUtils.createSampleFilm;
import static code.utils.ScreeningTestUtils.*;
import static code.utils.WebTestUtils.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ScreeningsCrudTests extends SpringIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScreeningFacade screeningFacade;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_create_screening() throws Exception {
        //given
        var sampleFilm = createSampleFilm(screeningFacade);
        var sampleRoom = createSampleScreeningRoom(screeningFacade);
        var sampleAddScreeningDTO = sampleCreateScreeningDto(
                sampleFilm.id(),
                sampleRoom.id()
        );

        //when
        var result = mockMvc.perform(
                post("/screenings")
                        .content(toJson(sampleAddScreeningDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk());
        mockMvc.perform(get("/screenings"))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].date").value(sampleAddScreeningDTO.date().toString()))
                .andExpect(jsonPath("$[0].freeSeats").value(sampleRoom.seatsQuantity()))
                .andExpect(jsonPath("$[0].minAge").value(sampleAddScreeningDTO.minAge()))
                .andExpect(jsonPath("$[0].filmId").value(sampleAddScreeningDTO.filmId().toString()))
                .andExpect(jsonPath("$[0].roomId").value(sampleAddScreeningDTO.roomId().toString()))
                .andExpect(jsonPath("$[0].seats.size()").value(sampleRoom.seatsQuantity()));
    }

    @ParameterizedTest
    @MethodSource("code.utils.ScreeningTestUtils#wrongScreeningDates")
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_screening_year_is_not_current_or_next_one(LocalDateTime wrongDate)
            throws Exception {
        //given
        var sampleFilmId = createSampleFilm(screeningFacade).id();
        var sampleRoomId = createSampleScreeningRoom(screeningFacade).id();
        var sampleAddScreeningDTO = sampleCreateScreeningDto(
                sampleFilmId,
                sampleRoomId
        ).withDate(wrongDate);

        //when
        var result = mockMvc.perform(
                post("/screenings")
                        .content(toJson(sampleAddScreeningDTO))
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
        var sampleScreening = createSampleScreening(screeningFacade);
        var sampleDto = sampleCreateScreeningDto(
                sampleScreening.filmId(),
                sampleScreening.roomId(),
                sampleScreening.date().plusMinutes(10)
        );

        //when
        var result = mockMvc.perform(
                post("/screenings")
                        .content(toJson(sampleDto))
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
        var sampleScreenings = createSampleScreenings(screeningFacade);

        //when
        var result = mockMvc.perform(
                get("/screenings")
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(sampleScreenings)));
    }

    @Test
    void should_search_screenings_by_search_params() throws Exception {
        //given
        var sampleScreenings = createSampleScreenings(screeningFacade);
        var filmId = sampleScreenings.get(0).filmId();
        var screeningDate = sampleScreenings.get(0).date();
        var filteredScreening = sampleScreenings
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
        var sampleAddRoomDTO = sampleCreateRoomDto();

        //when
        var result = mockMvc.perform(
                post("/screenings-rooms")
                        .content(toJson(sampleAddRoomDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk());
        mockMvc.perform(
                        get("/screenings-rooms")
                )
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].number").value(sampleAddRoomDTO.number()))
                .andExpect(jsonPath("$[0].rowsQuantity").value(sampleAddRoomDTO.rowsQuantity()))
                .andExpect(jsonPath("$[0].seatsInOneRowQuantity").value(sampleAddRoomDTO.seatsQuantityInOneRow()))
                .andExpect(jsonPath("$[0].seatsQuantity").value(sampleAddRoomDTO.rowsQuantity() * sampleAddRoomDTO.seatsQuantityInOneRow()));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void should_throw_exception_when_room_number_is_not_unique() throws Exception {
        //given
        var sampleRoom = createSampleScreeningRoom(screeningFacade);
        var sampleAddRoomDTO = sampleCreateRoomDto().withNumber(sampleRoom.number());

        //when
        var result = mockMvc.perform(
                post("/screenings-rooms")
                        .content(toJson(sampleAddRoomDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        "Screening room already exists: " + sampleAddRoomDTO.number()
                ));
    }
}

