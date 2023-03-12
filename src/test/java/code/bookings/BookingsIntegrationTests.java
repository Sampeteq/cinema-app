package code.bookings;

import code.bookings.application.dto.BookingDto;
import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import code.bookings.domain.BookingScreening;
import code.bookings.domain.BookingSeat;
import code.bookings.domain.BookingStatus;
import code.films.application.dto.SeatDto;
import code.films.domain.SeatRepository;
import code.utils.FilmTestHelper;
import code.utils.SpringIntegrationTests;
import code.utils.UserTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static code.utils.WebTestHelper.fromResultActions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookingsIntegrationTests extends SpringIntegrationTests {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private FilmTestHelper filmTestHelper;

    @Autowired
    private UserTestHelper userTestHelper;

    @Autowired
    @Qualifier("testClock")
    private Clock clock;

    @BeforeEach
    void setUp() {
        userTestHelper.signUpUser("user1");
    }

    @Test
    @WithMockUser(username = "user1")
    void should_booked_seat() throws Exception {
        //given
        var screening = filmTestHelper.addSampleScreening();
        var seat = filmTestHelper.searchScreeningSeats(screening.id()).get(0);
        var seatId = seat.id();

        //when
        var result = mockMvc.perform(
                post("/bookings/")
                        .param("seatId", seatId.toString())
        );

        //then
        result.andExpect(status().isOk());
        var bookingDto = fromResultActions(result, BookingDto.class);
        mockMvc.perform(
                        get("/bookings/my/" + bookingDto.id())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.id().toString()))
                .andExpect(jsonPath("$.status").value(BookingStatus.ACTIVE.name()))
                .andExpect(jsonPath("$.seatId").value(seatId.toString()));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_throw_exception_during_booking_when_less_than_24h_to_screening() throws Exception {
        //given
        var currentDate = getCurrentDate(clock);
        var screeningDate = currentDate.minusHours(23);
        var screening = filmTestHelper.addSampleScreening(screeningDate);
        var seat = filmTestHelper.searchScreeningSeats(screening.id()).get(0);
        var seatId = seat.id();

        //when
        var result = mockMvc.perform(
                post("/bookings/")
                        .param("seatId", seatId.toString())
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        "Too late to booking"
                ));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_seat_be_busy_and_reduce_screening_free_seats_by_one_after_booking() throws Exception {
        //given
        var screening = filmTestHelper.addSampleScreening();
        var seat = filmTestHelper.searchScreeningSeats(screening.id()).get(0);
        var seatId = seat.id();

        //when
        mockMvc.perform(
                post("/bookings/")
                        .param("seatId", seatId.toString())
        );

        //then
        var searchSeatsResult = mockMvc.perform(
                get("/films/screenings/" + screening.id() + "/seats")
        );
        var isSeatBusy = getSeatsFromResult(searchSeatsResult).anyMatch(
                s -> s.equals(seat.withStatus("BUSY"))
        );
        assertThat(isSeatBusy).isTrue();
        mockMvc.perform(
                        get("/films/screenings")
                )
                .andExpect(jsonPath("$[0].freeSeats").value(screening.freeSeats() - 1));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_cancel_booking() throws Exception {
        //give
        var screening = filmTestHelper.addSampleScreening();
        var seat = filmTestHelper.searchScreeningSeats(screening.id()).get(0);
        var booking = addBooking(seat.id(), "user1");

        //when
        var result = mockMvc.perform(
                post("/bookings/" + booking.id() + "/cancel")
        );

        //then
        result.andExpect(status().isOk());
        var searchBookingsResult = mockMvc.perform(
                get("/bookings/my")
        );
        var isBookingCancelled = getBookingsFromResult(searchBookingsResult).anyMatch(
                b -> b.equals(booking.withStatus("CANCELLED"))
        );
        assertThat(isBookingCancelled).isTrue();
    }

    @Test
    @WithMockUser(username = "user1")
    void should_seat_be_free_again_and_increase_free_seats_by_one_after_booking_cancelling() throws Exception {
        //given
        var screening = filmTestHelper.addSampleScreening();
        var seat = filmTestHelper.searchScreeningSeats(screening.id()).get(0);
        var booking = addBooking(seat.id(), "user1");

        //when
        var result = mockMvc.perform(
                post("/bookings/" + booking.id() + "/cancel")
        );

        //then
        result.andExpect(status().isOk());
        var searchSeatsResult = mockMvc.perform(
                get("/films/screenings/" + screening.id() + "/seats")
        );
        var isSeatFreeAgain = getSeatsFromResult(searchSeatsResult)
                .anyMatch(it -> it.equals(seat.withStatus("FREE")));
        assertThat(isSeatFreeAgain).isTrue();
        mockMvc.perform(
                        get("/films/screenings")
                )
                .andExpect(jsonPath("$[0].freeSeats").value(screening.freeSeats()));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_throw_exception_during_booking_when_booking_is_already_cancelled() throws Exception {
        //given
        var screening = filmTestHelper.addSampleScreening();
        var seat = filmTestHelper.searchScreeningSeats(screening.id()).get(0);
        var booking = addBooking(seat.id(), "user1", BookingStatus.CANCELLED);

        //when
        var result = mockMvc.perform(
                post("/bookings/" + booking.id() + "/cancel")
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        "Booking already cancelled"
                ));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_throw_exception_during_booking_cancelling_when_less_than_24h_to_screening()
            throws Exception {
        //given
        var hoursToScreening = 23;
        var booking = addBooking("user1", hoursToScreening);

        //when
        var result = mockMvc.perform(
                post("/bookings/" + booking.id() + "/cancel")
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        "Too late to cancel booking"
                ));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_return_all_user_bookings() throws Exception {
        //given
        var userBookings = bookSeats("user1");

        //when
        var result = mockMvc.perform(
                get("/bookings/my")
        );

        //then
        result.andExpect(status().isOk());
        var bookingsFromResult = getBookingsFromResult(result).toList();
        assertThat(bookingsFromResult).containsExactlyInAnyOrderElementsOf(userBookings);
    }

    private List<BookingDto> bookSeats(String username) {
        var screening = filmTestHelper.addSampleScreening();
        var seats = filmTestHelper.searchScreeningSeats(screening.id());
        var booking1 = addBooking(seats.get(0).id(), username);
        var booking2 = addBooking(seats.get(1).id(), username);
        return List.of(booking1, booking2);
    }

    private Stream<SeatDto> getSeatsFromResult(ResultActions searchSeatsResult) throws Exception {
        return Arrays.stream(
                fromResultActions(searchSeatsResult, SeatDto[].class)
        );
    }

    private Stream<BookingDto> getBookingsFromResult(ResultActions searchSeatsResult) throws Exception {
        return Arrays.stream(
                fromResultActions(searchSeatsResult, BookingDto[].class)
        );
    }

    private Booking createBooking(UUID seatId, String username) {
        var seat = seatRepository.findById(seatId).get();
        var screening = seat.getScreening();
        var bookingScreening = BookingScreening
                .builder()
                .id(screening.getId())
                .timeToScreeningInHours(screening.timeToScreeningStartInHours(clock))
                .build();
        var bookingSeat = BookingSeat
                .builder()
                .id(seatId)
                .isAvailable(true)
                .screening(bookingScreening)
                .build();
        return new Booking(
                UUID.randomUUID(),
                BookingStatus.ACTIVE,
                bookingSeat,
                username
        );
    }

    private BookingDto addBooking(UUID seatId, String username) {
        var booking = createBooking(seatId, username);
        return bookingRepository
                .save(booking)
                .toDto();
    }

    private BookingDto addBooking(UUID seatId, String username, BookingStatus status) {
        var booking = createBooking(seatId, username).withStatus(status);
        return bookingRepository.save(booking).toDto();
    }

    private BookingDto addBooking(String username, int hoursToScreening) {
        var currentDate = getCurrentDate(clock);
        var screeningDate = currentDate.minusHours(hoursToScreening);
        var screening = filmTestHelper.addSampleScreening(screeningDate);
        var seat = filmTestHelper.searchScreeningSeats(screening.id()).get(0);
        var booking = createBooking(seat.id(), username);
        return bookingRepository
                .save(booking)
                .toDto();
    }

    private LocalDateTime getCurrentDate(Clock clock) {
        return LocalDateTime.now(clock);
    }

}
