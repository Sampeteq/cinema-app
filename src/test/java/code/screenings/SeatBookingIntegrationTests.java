package code.screenings;

import code.screenings.dto.SeatDto;
import code.utils.SpringIntegrationTests;
import code.screenings.dto.BookSeatDto;
import code.screenings.dto.SeatBookingDto;
import code.user.UserFacade;
import code.utils.ScreeningTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.UUID;

import static code.utils.ScreeningTestUtils.searchSampleScreeningSeats;
import static code.utils.WebTestUtils.fromResultActions;
import static code.utils.WebTestUtils.toJson;
import static code.utils.ScreeningTestUtils.createSampleScreening;
import static code.utils.UserTestUtils.addSampleUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SeatBookingIntegrationTests extends SpringIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScreeningFacade screeningFacade;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    @Qualifier("testClock")
    private Clock clock;

    @Test
    @WithMockUser(username = "user1")
    void should_booked_seat() throws Exception {
        //given
        addSampleUser(userFacade);
        var sampleScreening = createSampleScreening(screeningFacade);
        var sampleSeat = searchSampleScreeningSeats(sampleScreening.id(), screeningFacade).get(0);
        var sampleBookSeatDTO = sampleBookSeatDTO(
                sampleScreening.id(),
                sampleSeat.id()
        );

        //when
        var result = mockMvc.perform(
                post("/seats-bookings")
                        .content(toJson(sampleBookSeatDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk());
        var dto = fromResultActions(result, SeatBookingDto.class);
        mockMvc.perform(
                        get("/seats-bookings/" + dto.id())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value(sampleBookSeatDTO.firstName()))
                .andExpect(jsonPath("$.lastName").value(sampleBookSeatDTO.lastName()))
                .andExpect(jsonPath("$.seat.id").value(sampleBookSeatDTO.seatId().toString()));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_throw_exception_during_booking_when_less_than_24h_to_screening() throws Exception {
        //given
        addSampleUser(userFacade);
        var currentDate = getCurrentDate(clock);
        var screeningDate = currentDate.minusHours(23);
        var sampleScreening = createSampleScreening(screeningFacade, screeningDate);
        var sampleSeat = searchSampleScreeningSeats(sampleScreening.id(), screeningFacade).get(0);
        var sampleBookSeatDTO = sampleBookSeatDTO(
                sampleScreening.id(),
                sampleSeat.id()
        );

        //when
        var result = mockMvc.perform(
                post("/seats-bookings")
                        .content(toJson(sampleBookSeatDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        "Too late for seat booking: " + sampleBookSeatDTO.seatId()
                ));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_make_seat_busy_and_reduce_screening_free_seats_by_one_after_booking() throws Exception {
        //given
        addSampleUser(userFacade);
        var sampleScreening = ScreeningTestUtils.createSampleScreening(screeningFacade);
        var sampleSeat = searchSampleScreeningSeats(sampleScreening.id(), screeningFacade).get(0);
        var sampleBookSeatDTO = sampleBookSeatDTO(
                sampleScreening.id(),
                sampleSeat.id()
        );

        //when
        mockMvc.perform(
                post("/seats-bookings")
                        .content(toJson(sampleBookSeatDTO))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        var searchSeatsResult = mockMvc.perform(
                        get("/screenings/" + sampleScreening.id() + "/seats")
                );
        var isSeatBusy = Arrays
                .stream(fromResultActions(searchSeatsResult, SeatDto[].class))
                .anyMatch(it -> it.id().equals(sampleSeat.id()) && it.status().equals("BUSY"));
        assertThat(isSeatBusy).isTrue();
        mockMvc.perform(
                        get("/screenings")
                )
                .andExpect(jsonPath("$[0].freeSeats").value(sampleScreening.freeSeats() - 1));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_cancel_booking() throws Exception {
        //give
        var sampleUsername = addSampleUser(userFacade);
        var sampleScreening = ScreeningTestUtils.createSampleScreening(screeningFacade);
        var sampleSeat = searchSampleScreeningSeats(sampleScreening.id(), screeningFacade).get(0);
        var sampleSeatBooking = bookSampleSeat(
                sampleScreening.id(),
                sampleSeat.id(),
                sampleUsername
        );

        //when
        var result = mockMvc.perform(
                patch("/seats-bookings/" + sampleSeatBooking.id() + "/cancel")
        );

        //then
        result.andExpect(status().isOk());
        assertThat(
                screeningFacade.searchSeatBooking(sampleSeatBooking.id(), sampleUsername)
                        .seat()
                        .status()
        ).isEqualTo(SeatStatus.FREE.name());
    }

    @Test
    @WithMockUser(username = "user1")
    void should_make_seat_free_and_increase_free_seats_by_one_after_booking_cancelling() throws Exception {
        //given
        var sampleUsername = addSampleUser(userFacade);
        var sampleScreening = ScreeningTestUtils.createSampleScreening(screeningFacade);
        var seat = searchSampleScreeningSeats(sampleScreening.id(), screeningFacade).get(0);
        var sampleSeatBooking = bookSampleSeat(
                sampleScreening.id(),
                seat.id(),
                sampleUsername
        );

        //when
        var result = mockMvc.perform(
                patch("/seats-bookings/" + sampleSeatBooking.id() + "/cancel")
        );

        //then
        result.andExpect(status().isOk());
        var searchSeatsResult = mockMvc.perform(
                get("/screenings/" + sampleScreening.id() + "/seats")
        );
        var isSeatFreeAgain = Arrays
                .stream(fromResultActions(searchSeatsResult, SeatDto[].class))
                .anyMatch(it -> it.id().equals(seat.id()) && it.status().equals("FREE"));
        assertThat(isSeatFreeAgain).isTrue();
        mockMvc.perform(
                        get("/screenings")
                )
                .andExpect(jsonPath("$[0].freeSeats").value(sampleScreening.freeSeats()));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_throw_exception_when_booking_is_already_cancelled() throws Exception {
        //given
        var sampleUsername = addSampleUser(userFacade);
        var sampleScreening = ScreeningTestUtils.createSampleScreening(screeningFacade);
        var seat = searchSampleScreeningSeats(sampleScreening.id(), screeningFacade).get(0);
        var sampleSeatBooking = bookSampleSeat(
                sampleScreening.id(),
                seat.id(),
                sampleUsername
        );
        screeningFacade.cancelSeatBooking(sampleSeatBooking.id(), clock);

        //when
        var result = mockMvc.perform(
                patch("/seats-bookings/" + sampleSeatBooking.id() + "/cancel")
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        "Seat not booked yet: " + seat.id()
                ));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_canceling_booking_be_impossible_when_less_than_24h_to_screening() throws Exception {
        //given
        var sampleUsername = addSampleUser(userFacade);
        var currentDate = getCurrentDate(clock);
        var hoursUntilBooking = 23;
        var sampleScreeningDate = currentDate.minusHours(hoursUntilBooking);
        var sampleScreening = createSampleScreening(screeningFacade, sampleScreeningDate);
        var sampleSeat = searchSampleScreeningSeats(sampleScreening.id(), screeningFacade).get(0);
        var timeDuringBooking = Clock.fixed(
                sampleScreeningDate.minusHours(hoursUntilBooking + 1).toInstant(ZoneOffset.UTC),
                ZoneOffset.UTC
        );
        var sampleSeatBooking = screeningFacade.bookSeat(
                sampleBookSeatDTO(
                        sampleScreening.id(),
                        sampleSeat.id()
                ),
                sampleUsername,
                timeDuringBooking
        );

        //when
        var result = mockMvc.perform(
                patch("/seats-bookings/" + sampleSeatBooking.id() + "/cancel")
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        "Too late for seat booking cancelling: " + sampleSeat.id()
                ));
    }

    private static BookSeatDto sampleBookSeatDTO(UUID sampleScreeningId, UUID sampleSeatId) {
        return new BookSeatDto(
                sampleScreeningId,
                sampleSeatId,
                "Name 1",
                "Lastname 1"
        );
    }

    private SeatBookingDto bookSampleSeat(UUID sampleScreeningId, UUID sampleSeatId, String username) {
        return screeningFacade.bookSeat(
                new BookSeatDto(
                        sampleScreeningId,
                        sampleSeatId,
                        "Name 1",
                        "Lastname 1"
                ),
                username,
                clock
        );
    }

    private LocalDateTime getCurrentDate(Clock clock) {
        return LocalDateTime.now(clock);
    }
}
