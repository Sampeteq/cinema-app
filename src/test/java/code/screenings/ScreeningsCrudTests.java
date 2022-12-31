package code.screenings;

import code.SpringIntegrationTests;
import code.films.FilmFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;

import static code.WebTestUtils.toJson;
import static code.films.FilmTestUtils.addSampleFilms;
import static code.screenings.ScreeningTestUtils.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ScreeningsCrudTests extends SpringIntegrationTests {

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
        var sampleFilm = addSampleFilms(filmFacade).get(0);
        var sampleRoom = addSampleScreeningRooms(screeningFacade).get(0);
        var sampleAddScreeningDTO = sampleAddScreeningDto(
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
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].date").value(sampleAddScreeningDTO.date().toString()))
                .andExpect(jsonPath("$[0].freeSeats").value(sampleRoom.seatsQuantity()))
                .andExpect(jsonPath("$[0].minAge").value(sampleAddScreeningDTO.minAge()))
                .andExpect(jsonPath("$[0].filmId").value(sampleAddScreeningDTO.filmId().toString()))
                .andExpect(jsonPath("$[0].roomId").value(sampleAddScreeningDTO.roomId().toString()))
                .andExpect(jsonPath("$[0].seats.size()").value(sampleRoom.seatsQuantity()));
    }

    @ParameterizedTest
    @MethodSource("code.screenings.ScreeningTestUtils#wrongScreeningDates")
    @WithMockUser(roles = "ADMIN")
    void should_throw_exception_when_screening_year_is_not_current_or_next_one(LocalDateTime wrongDate) throws Exception {
        //given
        var sampleFilmId = addSampleFilms(filmFacade).get(0).id();
        var sampleRoomId = addSampleScreeningRooms(screeningFacade).get(0).id();
        var sampleAddScreeningDTO = sampleAddScreeningDto(
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
    @WithMockUser(roles = "ADMIN")
    void should_throw_exception_when_screening_room_is_busy() throws Exception {
        //given
        var sampleScreenings = addSampleScreenings(screeningFacade, filmFacade);
        var sampleAddScreeningDTO = sampleAddScreeningDto(
                sampleScreenings.get(0).filmId(),
                sampleScreenings.get(0).roomId()
        ).withDate(sampleScreenings.get(0).date());

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
                        "Screening room busy: " + sampleAddScreeningDTO.roomId()
                ));
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
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].number").value(sampleAddRoomDTO.number()))
                .andExpect(jsonPath("$[0].rowsQuantity").value(sampleAddRoomDTO.rowsQuantity()))
                .andExpect(jsonPath("$[0].seatsInOneRowQuantity").value(sampleAddRoomDTO.seatsQuantityInOneRow()))
                .andExpect(jsonPath("$[0].seatsQuantity").value(sampleAddRoomDTO.rowsQuantity() * sampleAddRoomDTO.seatsQuantityInOneRow()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void should_throw_exception_when_room_number_is_not_unique() throws Exception {
        //given
        var sampleRoom = addSampleScreeningRooms(screeningFacade).get(0);
        var sampleAddRoomDTO = sampleAddRoomDTO().withNumber(sampleRoom.number());

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

