package code.screenings;

import code.SpringIntegrationTests;
import code.films.FilmFacade;
import code.films.FilmTestUtils;
import code.screenings.dto.AddScreeningDTO;
import code.screenings.exception.ScreeningFreeSeatsQuantityBiggerThanRoomOneException;
import code.screenings.exception.ScreeningRoomAlreadyExistsException;
import code.screenings.exception.ScreeningRoomBusyException;
import code.screenings.exception.ScreeningYearException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static code.WebTestUtils.toJson;
import static code.films.FilmTestUtils.sampleAddFilmDTO;
import static code.screenings.ScreeningTestUtils.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ScreeningsIntegrationTests extends SpringIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScreeningFacade screeningFacade;

    @Autowired
    private FilmFacade filmFacade;


    @Test
    @WithMockUser(roles = "ADMIN")
    void should_add_screening() throws Exception {
        //given
        var sampleFilmId = filmFacade.add(sampleAddFilmDTO()).id();
        var sampleRoomUuid = screeningFacade.addRoom(sampleAddRoomDTO()).uuid();
        var sampleAddScreeningDTO = sampleAddScreeningDTO(sampleFilmId, sampleRoomUuid);
        //when
        var result = mockMvc.perform(
          post("/screenings")
                  .content(toJson(sampleAddScreeningDTO))
                  .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk());
        mockMvc.perform(get("/screenings"))
                .andExpect(jsonPath("$[0].date").value(sampleAddScreeningDTO.date().toString()))
                .andExpect(jsonPath("$[0].minAge").value(sampleAddScreeningDTO.minAge()))
                .andExpect(jsonPath("$[0].freeSeats").value(sampleAddScreeningDTO.freeSeatsQuantity()))
                .andExpect(jsonPath("$[0].filmId").value(sampleAddScreeningDTO.filmId()))
                .andExpect(jsonPath("$[0].roomUuid").value(sampleAddScreeningDTO.roomUuid().toString()));
    }

    @ParameterizedTest
    @MethodSource("code.screenings.ScreeningTestUtils#getWrongScreeningYears")
    @WithMockUser(roles = "ADMIN")
    void should_throw_exception_when_screening_year_is_not_current_or_next_one(Integer wrongScreeningYear) throws Exception {
        //given
        var sampleFilmId = filmFacade.add(sampleAddFilmDTO()).id();
        var sampleRoomUuid = screeningFacade.addRoom(sampleAddRoomDTO()).uuid();
        var sampleAddScreeningDTO = sampleAddScreeningDTO(sampleFilmId, sampleRoomUuid, wrongScreeningYear);

        //when
        var result = mockMvc.perform(
                post("/screenings")
                        .content(toJson(sampleAddScreeningDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new ScreeningYearException(wrongScreeningYear).getMessage()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void should_throw_exception_when_screening_room_is_busy() throws Exception {
        //given
        var sampleScreenings = addSampleScreenings(screeningFacade, filmFacade);
        var sampleAddScreeningDTO = AddScreeningDTO
                .builder()
                .filmId(sampleScreenings.get(0).filmId())
                .roomUuid(sampleScreenings.get(0).roomUuid())
                .date(sampleScreenings.get(0).date())
                .minAge(13)
                .freeSeatsQuantity(200)
                .build();

        //when
        var result = mockMvc.perform(
                post("/screenings")
                        .content(toJson(sampleAddScreeningDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new ScreeningRoomBusyException(sampleScreenings.get(0).roomUuid()).getMessage()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void should_throw_exception_when_screening_free_seats_quantity_is_bigger_than_room_one() throws Exception {
        //given
        var sampleFilmId = filmFacade.add(sampleAddFilmDTO()).id();
        var sampleAddRoomDTO = sampleAddRoomDTO();
        var sampleRoomUuid= screeningFacade.addRoom(sampleAddRoomDTO).uuid();
        var sampleAddScreeningDTO = AddScreeningDTO
                .builder()
                .freeSeatsQuantity(sampleAddRoomDTO.freeSeats() + 1)
                .roomUuid(sampleRoomUuid)
                .minAge(13)
                .date(LocalDateTime.parse("2022-05-05T16:30"))
                .filmId(sampleFilmId)
                .build();

        //when
        var result = mockMvc.perform(
          post("/screenings")
                  .content(toJson(sampleAddScreeningDTO))
                  .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new ScreeningFreeSeatsQuantityBiggerThanRoomOneException().getMessage()));
    }

    @Test
    void should_search_all_screenings() throws Exception {
        //given
        var sampleScreenings = addSampleScreenings(screeningFacade, filmFacade);

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
        var sampleScreenings = addSampleScreenings(screeningFacade, filmFacade);
        var filmId = sampleScreenings.get(0).filmId();
        var screeningDate = sampleScreenings.get(0).date();
        var filteredScreening= sampleScreenings
                .stream()
                .filter(screening -> screening.id().equals(filmId))
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
    @WithMockUser(roles = "ADMIN")
    void should_add_screening_room() throws Exception {
        //given
        var sampleAddRoomDTO = sampleAddRoomDTO();

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
                .andExpect(jsonPath("$[0].number").value(sampleAddRoomDTO.number()))
                .andExpect(jsonPath("$[0].freeSeats").value(sampleAddRoomDTO.freeSeats()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void should_throw_exception_when_room_number_is_not_unique() throws Exception {
        //given
        var sampleAddRoomDTO = sampleAddRoomDTO();
        screeningFacade.addRoom(sampleAddRoomDTO);

        //when
        var result = mockMvc.perform(
                post("/screenings-rooms")
                        .content(toJson(sampleAddRoomDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new ScreeningRoomAlreadyExistsException(sampleAddRoomDTO.number()).getMessage()));
    }
}
