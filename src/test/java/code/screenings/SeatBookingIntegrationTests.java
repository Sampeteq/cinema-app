package code.screenings;

import code.screenings.dto.SeatView;
import code.utils.SpringIntegrationTests;
import code.screenings.dto.SeatBookingRequest;
import code.screenings.dto.SeatBookingView;
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

import static code.utils.ScreeningTestUtils.searchScreeningSeats;
import static code.utils.WebTestUtils.fromResultActions;
import static code.utils.WebTestUtils.toJson;
import static code.utils.ScreeningTestUtils.createScreening;
import static code.utils.UserTestUtils.signUpUser;
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
        signUpUser(userFacade);
        var screening = createScreening(screeningFacade);
        var seat = searchScreeningSeats(screening.id(), screeningFacade).get(0);
        var seatBookingRequest = createSeatBookingRequest(
                screening.id(),
                seat.id()
        );

        //when
        var result = mockMvc.perform(
                post("/seats-bookings")
                        .content(toJson(seatBookingRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk());
        var seatBookingView = fromResultActions(result, SeatBookingView.class);
        mockMvc.perform(
                        get("/seats-bookings/" + seatBookingView.id())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value(seatBookingRequest.firstName()))
                .andExpect(jsonPath("$.lastName").value(seatBookingRequest.lastName()))
                .andExpect(jsonPath("$.seat.id").value(seatBookingRequest.seatId().toString()));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_throw_exception_during_booking_when_less_than_24h_to_screening() throws Exception {
        //given
        signUpUser(userFacade);
        var currentDate = getCurrentDate(clock);
        var screeningDate = currentDate.minusHours(23);
        var screening = createScreening(screeningFacade, screeningDate);
        var seat = searchScreeningSeats(screening.id(), screeningFacade).get(0);
        var seatBookingRequest = createSeatBookingRequest(
                screening.id(),
                seat.id()
        );

        //when
        var result = mockMvc.perform(
                post("/seats-bookings")
                        .content(toJson(seatBookingRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        "Too late for seat booking: " + seatBookingRequest.seatId()
                ));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_make_seat_busy_and_reduce_screening_free_seats_by_one_after_booking() throws Exception {
        //given
        signUpUser(userFacade);
        var screening = ScreeningTestUtils.createScreening(screeningFacade);
        var seat = searchScreeningSeats(screening.id(), screeningFacade).get(0);
        var seatBookingRequest = createSeatBookingRequest(
                screening.id(),
                seat.id()
        );

        //when
        mockMvc.perform(
                post("/seats-bookings")
                        .content(toJson(seatBookingRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        var searchSeatsResult = mockMvc.perform(
                        get("/screenings/" + screening.id() + "/seats")
                );
        var isSeatBusy = Arrays
                .stream(fromResultActions(searchSeatsResult, SeatView[].class))
                .anyMatch(it -> it.id().equals(seat.id()) && it.status().equals("BUSY"));
        assertThat(isSeatBusy).isTrue();
        mockMvc.perform(
                        get("/screenings")
                )
                .andExpect(jsonPath("$[0].freeSeats").value(screening.freeSeats() - 1));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_cancel_booking() throws Exception {
        //give
        var username = signUpUser(userFacade);
        var screening = createScreening(screeningFacade);
        var seat = searchScreeningSeats(screening.id(), screeningFacade).get(0);
        var seatBooking = bookSeat(
                screening.id(),
                seat.id(),
                username
        );

        //when
        var result = mockMvc.perform(
                patch("/seats-bookings/" + seatBooking.id() + "/cancel")
        );

        //then
        result.andExpect(status().isOk());
        assertThat(
                screeningFacade.searchSeatBooking(seatBooking.id(), username)
                        .seat()
                        .status()
        ).isEqualTo(SeatStatus.FREE.name());
    }

    @Test
    @WithMockUser(username = "user1")
    void should_make_seat_free_and_increase_free_seats_by_one_after_booking_cancelling() throws Exception {
        //given
        var username = signUpUser(userFacade);
        var screening = ScreeningTestUtils.createScreening(screeningFacade);
        var seat = searchScreeningSeats(screening.id(), screeningFacade).get(0);
        var seatBooking = bookSeat(
                screening.id(),
                seat.id(),
                username
        );

        //when
        var result = mockMvc.perform(
                patch("/seats-bookings/" + seatBooking.id() + "/cancel")
        );

        //then
        result.andExpect(status().isOk());
        var searchSeatsResult = mockMvc.perform(
                get("/screenings/" + screening.id() + "/seats")
        );
        var isSeatFreeAgain = Arrays
                .stream(fromResultActions(searchSeatsResult, SeatView[].class))
                .anyMatch(it -> it.id().equals(seat.id()) && it.status().equals("FREE"));
        assertThat(isSeatFreeAgain).isTrue();
        mockMvc.perform(
                        get("/screenings")
                )
                .andExpect(jsonPath("$[0].freeSeats").value(screening.freeSeats()));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_throw_exception_when_booking_is_already_cancelled() throws Exception {
        //given
        var username = signUpUser(userFacade);
        var screening = ScreeningTestUtils.createScreening(screeningFacade);
        var seat = searchScreeningSeats(screening.id(), screeningFacade).get(0);
        var seatBooking = bookSeat(
                screening.id(),
                seat.id(),
                username
        );
        screeningFacade.cancelSeatBooking(seatBooking.id(), clock);

        //when
        var result = mockMvc.perform(
                patch("/seats-bookings/" + seatBooking.id() + "/cancel")
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
    void should_throw_exception_during_seat_booking_cancelling_when_less_than_24h_to_screening()
            throws Exception {
        //given
        var username = signUpUser(userFacade);
        var hoursToScreening = 23;
        var seatBooking = bookSeat(username, hoursToScreening);

        //when
        var result = mockMvc.perform(
                patch("/seats-bookings/" + seatBooking.id() + "/cancel")
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        "Too late for seat booking cancelling: " + seatBooking.seat().id()
                ));
    }

    private static SeatBookingRequest createSeatBookingRequest(UUID screeningId, UUID seatId) {
        return new SeatBookingRequest(
                screeningId,
                seatId,
                "Name 1",
                "Lastname 1"
        );
    }

    private SeatBookingView bookSeat(UUID screeningId, UUID seatId, String username) {
        return screeningFacade.bookSeat(
                new SeatBookingRequest(
                        screeningId,
                        seatId,
                        "Name 1",
                        "Lastname 1"
                ),
                username,
                clock
        );
    }

    private SeatBookingView bookSeat(String username, int hoursToScreening) {
        var currentDate = getCurrentDate(clock);
        var screeningDate = currentDate.minusHours(hoursToScreening);
        var screening = createScreening(screeningFacade, screeningDate);
        var seat = searchScreeningSeats(screening.id(), screeningFacade).get(0);
        var timeDuringBooking = Clock.fixed(
                screeningDate.minusHours(hoursToScreening + 1).toInstant(ZoneOffset.UTC),
                ZoneOffset.UTC
        );
        return screeningFacade.bookSeat(
                createSeatBookingRequest(
                        screening.id(),
                        seat.id()
                ),
                username,
                timeDuringBooking
        );
    }

    private LocalDateTime getCurrentDate(Clock clock) {
        return LocalDateTime.now(clock);
    }
}
