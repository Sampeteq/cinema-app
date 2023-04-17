package code.bookings;

import code.bookings.domain.BookingRepository;
import code.bookings.domain.BookingStatus;
import code.films.domain.Seat;
import code.bookings.domain.exceptions.BookingAlreadyCancelledException;
import code.bookings.domain.exceptions.TooLateToBookingException;
import code.bookings.domain.exceptions.TooLateToCancelBookingException;
import code.bookings.infrastructure.rest.dto.BookingDto;
import code.bookings.infrastructure.rest.dto.SeatDto;
import code.bookings.infrastructure.rest.dto.mappers.BookingMapper;
import code.films.domain.FilmRepository;
import code.user.domain.UserRepository;
import code.utils.SpringIntegrationTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Stream;

import static code.utils.BookingTestHelper.createSampleBooking;
import static code.utils.BookingTestHelper.createSampleBookings;
import static code.utils.FilmTestHelper.createSampleFilmWithScreening;
import static code.utils.UserTestHelper.createSampleUser;
import static code.utils.WebTestHelper.fromResultActions;
import static code.utils.WebTestHelper.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookingsIntegrationTests extends SpringIntegrationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingMapper bookingMapper;


    @Autowired
    @Qualifier("testClock")
    private Clock clock;

    @BeforeEach
    void setUp() {
        userRepository.save(createSampleUser("user1"));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_make_booking() throws Exception {
        //given
        var film = filmRepository.save(createSampleFilmWithScreening());
        var seatId = film
                .getScreenings()
                .get(0)
                .getSeats()
                .get(0)
                .getId();

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
                .andExpect(content().json(toJson(bookingDto)));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_throw_exception_during_booking_when_less_than_24h_to_screening() throws Exception {
        //given
        var screeningDate = getCurrentDate(clock).minusHours(23);
        var film = filmRepository.save(createSampleFilmWithScreening(screeningDate));
        var seatId = film
                .getScreenings()
                .get(0)
                .getSeats()
                .get(0)
                .getId();

        //when
        var result = mockMvc.perform(
                post("/bookings/")
                        .param("seatId", seatId.toString())
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        new TooLateToBookingException().getMessage()
                ));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_seat_be_busy_after_booking() throws Exception {
        //given
        var film = filmRepository.save(createSampleFilmWithScreening());
        var screening = film
                .getScreenings()
                .get(0);
        var seatId = screening
                .getSeats()
                .get(0)
                .getId();

        //when
        mockMvc.perform(
                post("/bookings/")
                        .param("seatId", seatId.toString())
        );

        //then
        var searchSeatsResult = mockMvc.perform(
                get("/films/screenings/" + screening.getId() + "/seats")
        );
        var isSeatBusy = getSeatsFromResult(searchSeatsResult).anyMatch(
                s -> s.id().equals(seatId) && s.status().equals("BUSY")
        );
        assertThat(isSeatBusy).isTrue();
    }

    @Test
    @WithMockUser(username = "user1")
    void should_reduce_screening_free_seats_by_one_after_booking() throws Exception {
        //given
        var film = filmRepository.save(createSampleFilmWithScreening());
        var screening = film.getScreenings().get(0);
        var seatId = film
                .getScreenings()
                .get(0)
                .getSeats()
                .get(0)
                .getId();
        var freeSeatsNumber = screening
                .getSeats()
                .stream()
                .filter(Seat::isFree)
                .count();

        //when
        mockMvc.perform(
                post("/bookings/")
                        .param("seatId", seatId.toString())
        );

        //then
        mockMvc.perform(
                        get("/films/screenings")
                )
                .andExpect(jsonPath("$[0].freeSeats").value(freeSeatsNumber - 1));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_cancel_booking() throws Exception {
        //give
        var seat = filmRepository.save(createSampleFilmWithScreening())
                .getScreenings()
                .get(0)
                .getSeats()
                .get(0);
        var booking = bookingRepository.save(createSampleBooking(seat, "user1"));

        //when
        var result = mockMvc.perform(
                post("/bookings/" + booking.getId() + "/cancel")
        );

        //then
        result.andExpect(status().isOk());
        var searchBookingsResult = mockMvc.perform(
                get("/bookings/my")
        );
        var isBookingCancelled = getBookingsFromResult(searchBookingsResult).anyMatch(
                b -> b.equals(bookingMapper.mapToDto(booking).withStatus("CANCELLED"))
        );
        assertThat(isBookingCancelled).isTrue();
    }

    @Test
    @WithMockUser(username = "user1")
    void should_seat_be_free_again_and_increase_free_seats_by_one_after_booking_cancelling() throws Exception {
        //given
        var screening = filmRepository
                .save(createSampleFilmWithScreening())
                .getScreenings()
                .get(0);
        var freeSeatsNumber = screening
                .getSeats()
                .stream()
                .count();
        var seat = screening.getSeats().get(0);
        var booking = bookingRepository.save(createSampleBooking(seat, "user1"));

        //when
        var result = mockMvc.perform(
                post("/bookings/" + booking.getId() + "/cancel")
        );

        //then
        result.andExpect(status().isOk());
        var searchSeatsResult = mockMvc.perform(
                get("/films/screenings/" + screening.getId() + "/seats")
        );
        var isSeatFreeAgain = getSeatsFromResult(searchSeatsResult)
                .anyMatch(s -> s.id().equals(seat.getId()) && s.status().equals("FREE"));
        assertThat(isSeatFreeAgain).isTrue();
        mockMvc
                .perform(get("/films/screenings"))
                .andExpect(jsonPath("$[0].freeSeats").value(freeSeatsNumber));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_throw_exception_during_booking_when_booking_is_already_cancelled() throws Exception {
        //given
        var seat = filmRepository
                .save(createSampleFilmWithScreening())
                .getScreenings()
                .get(0)
                .getSeats()
                .get(0);
        var booking = bookingRepository.save(
                createSampleBooking(seat, "user1").withStatus(BookingStatus.CANCELLED)
        );

        //when
        var result = mockMvc.perform(
                post("/bookings/" + booking.getId() + "/cancel")
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        new BookingAlreadyCancelledException().getMessage()
                ));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_throw_exception_during_booking_cancelling_when_less_than_24h_to_screening() throws Exception {
        //given
        var hoursToScreening = 23;
        var screeningDate = getCurrentDate(clock).minusHours(hoursToScreening);
        var seat = filmRepository
                .save(createSampleFilmWithScreening(screeningDate))
                .getScreenings()
                .get(0)
                .getSeats()
                .get(0);
        var booking = bookingRepository.save(createSampleBooking(seat, "user1"));

        //when
        var result = mockMvc.perform(
                post("/bookings/" + booking.getId() + "/cancel")
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        new TooLateToCancelBookingException().getMessage()
                ));
    }

    @Test
    @WithMockUser(username = "user1")
    void should_get_all_user_bookings() throws Exception {
        //given
        var sampleScreening = filmRepository
                .save(createSampleFilmWithScreening())
                .getScreenings()
                .get(0);
        var seat1 = sampleScreening
                .getSeats()
                .get(0);
        var seat2 = sampleScreening
                .getSeats()
                .get(0);
        var userBookings = bookingRepository.saveAll(createSampleBookings(seat1, seat2, "user1"));

        //when
        var result = mockMvc.perform(
                get("/bookings/my")
        );

        //then
        result.andExpect(status().isOk());
        var bookingsFromResult = getBookingsFromResult(result).toList();
        assertThat(bookingsFromResult).containsExactlyInAnyOrderElementsOf(bookingMapper.mapToDto(userBookings));
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

    private LocalDateTime getCurrentDate(Clock clock) {
        return LocalDateTime.now(clock);
    }

}
