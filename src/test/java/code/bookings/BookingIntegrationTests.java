package code.bookings;

import code.bookings.dto.BookDto;
import code.bookings.dto.BookingDto;
import code.bookings.dto.SeatDto;
import code.utils.ScreeningTestHelper;
import code.utils.SpringIntegrationTests;
import code.utils.UserTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static code.utils.WebTestHelper.fromResultActions;
import static code.utils.WebTestHelper.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookingIntegrationTests extends SpringIntegrationTests {

    @Autowired
    private BookingFacade bookingFacade;

    @Autowired
    private ScreeningTestHelper screeningTestHelper;

    @Autowired
    private UserTestHelper userTestHelper;

    @Autowired
    @Qualifier("testClock")
    private Clock clock;

    @Test
    @WithMockUser(username = "user1")
    void should_booked_seat() throws Exception {
        //given
        userTestHelper.signUpUser();
        var screening = screeningTestHelper.createScreening();
        var seat = screeningTestHelper.searchScreeningSeats(screening.id()).get(0);
        var bookSeatDto = createBookSeatDto(
                screening.id(),
                seat.id()
        );

        //when
        var result = mockMvc.perform(
                post("/seats-bookings")
                        .content(toJson(bookSeatDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk());
        var bookingDto = fromResultActions(result, BookingDto.class);
        mockMvc.perform(
                        get("/seats-bookings/my/" + bookingDto.id())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value(bookSeatDto.firstName()))
                .andExpect(jsonPath("$.lastName").value(bookSeatDto.lastName()))
                .andExpect(jsonPath("$.seat.id").value(bookSeatDto.seatId().toString()));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_throw_exception_during_booking_when_less_than_24h_to_screening() throws Exception {
        //given
        userTestHelper.signUpUser();
        var currentDate = getCurrentDate(clock);
        var screeningDate = currentDate.minusHours(23);
        var screening = screeningTestHelper.createScreening(screeningDate);
        var seat = screeningTestHelper.searchScreeningSeats(screening.id()).get(0);
        var dto = createBookSeatDto(
                screening.id(),
                seat.id()
        );

        //when
        var result = mockMvc.perform(
                post("/seats-bookings")
                        .content(toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        "Too late for seat booking: " + dto.seatId()
                ));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_make_seat_busy_and_reduce_screening_free_seats_by_one_after_booking() throws Exception {
        //given
        userTestHelper.signUpUser();
        var screening = screeningTestHelper.createScreening();
        var seat = screeningTestHelper.searchScreeningSeats(screening.id()).get(0);
        var bookSeatDto = createBookSeatDto(
                screening.id(),
                seat.id()
        );

        //when
        mockMvc.perform(
                post("/seats-bookings")
                        .content(toJson(bookSeatDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        var searchSeatsResult = mockMvc.perform(
                get("/screenings/" + screening.id() + "/seats")
        );
        var isSeatBusy = Arrays
                .stream(fromResultActions(searchSeatsResult, SeatDto[].class))
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
        var username = userTestHelper.signUpUser();
        var screening = screeningTestHelper.createScreening();
        var seat = screeningTestHelper.searchScreeningSeats(screening.id()).get(0);
        var booking = bookSeat(
                screening.id(),
                seat.id(),
                username
        );

        //when
        var result = mockMvc.perform(
                patch("/seats-bookings/" + booking.id() + "/cancel")
        );

        //then
        result.andExpect(status().isOk());
        assertThat(
                bookingFacade.searchSeatBooking(booking.id(), username)
                        .seat()
                        .status()
        ).isEqualTo(SeatStatus.FREE.name());
    }

    @Test
    @WithMockUser(username = "user1")
    void should_make_seat_free_and_increase_free_seats_by_one_after_booking_cancelling() throws Exception {
        //given
        var username = userTestHelper.signUpUser();
        var screening = screeningTestHelper.createScreening();
        var seat = screeningTestHelper.searchScreeningSeats(screening.id()).get(0);
        var booking = bookSeat(
                screening.id(),
                seat.id(),
                username
        );

        //when
        var result = mockMvc.perform(
                patch("/seats-bookings/" + booking.id() + "/cancel")
        );

        //then
        result.andExpect(status().isOk());
        var searchSeatsResult = mockMvc.perform(
                get("/screenings/" + screening.id() + "/seats")
        );
        var isSeatFreeAgain = Arrays
                .stream(fromResultActions(searchSeatsResult, SeatDto[].class))
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
        var username = userTestHelper.signUpUser();
        var screening = screeningTestHelper.createScreening();
        var seat = screeningTestHelper.searchScreeningSeats(screening.id()).get(0);
        var booking = bookSeat(
                screening.id(),
                seat.id(),
                username
        );
        bookingFacade.cancelSeatBooking(booking.id(), clock);

        //when
        var result = mockMvc.perform(
                patch("/seats-bookings/" + booking.id() + "/cancel")
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
        var username = userTestHelper.signUpUser();
        var hoursToScreening = 23;
        var booking = bookSeat(username, hoursToScreening);

        //when
        var result = mockMvc.perform(
                patch("/seats-bookings/" + booking.id() + "/cancel")
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        "Too late for seat booking cancelling: " + booking.seat().id()
                ));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_return_all_user_seats_bookings() throws Exception {
        //given
        var username = userTestHelper.signUpUser();
        var userBookings = bookSeats(username);

        //when
        var result = mockMvc.perform(
                get("/seats-bookings/my")
        );

        //then
        result.andExpect(status().isOk());
        var bookingsFromResult = Arrays
                .stream(fromResultActions(result, BookingDto[].class))
                .toList();
        assertThat(bookingsFromResult).containsExactlyInAnyOrderElementsOf(userBookings);
    }

    private List<BookingDto> bookSeats(String username) {
        var screening = screeningTestHelper.createScreening();
        var seats = screeningTestHelper.searchScreeningSeats(screening.id());
        var booking1 = bookSeat(screening.id(), seats.get(0).id(), username);
        var booking2 = bookSeat(screening.id(), seats.get(1).id(), username);
        return List.of(booking1, booking2);
    }

    private static BookDto createBookSeatDto(UUID screeningId, UUID seatId) {
        return new BookDto(
                screeningId,
                seatId,
                "Name 1",
                "Lastname 1"
        );
    }

    private BookingDto bookSeat(UUID screeningId, UUID seatId, String username) {
        return bookingFacade.bookSeat(
                new BookDto(
                        screeningId,
                        seatId,
                        "Name 1",
                        "Lastname 1"
                ),
                username,
                clock
        );
    }

    private BookingDto bookSeat(String username, int hoursToScreening) {
        var currentDate = getCurrentDate(clock);
        var screeningDate = currentDate.minusHours(hoursToScreening);
        var screening = screeningTestHelper.createScreening(screeningDate);
        var seat = screeningTestHelper.searchScreeningSeats(screening.id()).get(0);
        var timeDuringBooking = Clock.fixed(
                screeningDate.minusHours(hoursToScreening + 1).toInstant(ZoneOffset.UTC),
                ZoneOffset.UTC
        );
        return bookingFacade.bookSeat(
                createBookSeatDto(
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
