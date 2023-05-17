package code.bookings;

import code.bookings.application.dto.BookingDto;
import code.bookings.application.dto.BookingMapper;
import code.bookings.domain.Booking;
import code.bookings.domain.BookingRepository;
import code.bookings.domain.BookingStatus;
import code.bookings.domain.exceptions.BookingAlreadyCancelledException;
import code.bookings.domain.exceptions.TooLateToBookingException;
import code.bookings.domain.exceptions.TooLateToCancelBookingException;
import code.films.domain.Film;
import code.films.domain.FilmRepository;
import code.rooms.domain.RoomRepository;
import code.screenings.application.dto.SeatDto;
import code.screenings.domain.Seat;
import code.screenings.domain.SeatStatus;
import code.user.domain.User;
import code.user.domain.UserRepository;
import code.utils.SpringIT;
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
import java.util.stream.Stream;

import static code.utils.BookingTestHelper.createBooking;
import static code.utils.FilmTestHelper.createFilmWithScreening;
import static code.utils.RoomTestHelper.createRoom;
import static code.utils.UserTestHelper.createUser;
import static code.utils.WebTestHelper.fromResultActions;
import static code.utils.WebTestHelper.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "user1@mail.com")
class BookingControllerIT extends SpringIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    @Qualifier("testClock")
    private Clock clock;

    private User user;

    @BeforeEach
    @WithMockUser(username = "user1@mail.com")
    void setUp() {
        this.user = userRepository.add(createUser("user1@mail.com"));
    }

    @Test
    void should_make_booking() throws Exception {
        //given
        var seat = prepareSeat();

        //when
        var result = mockMvc.perform(
                post("/bookings/")
                        .param("seatId", seat.getId().toString())
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
    void should_throw_exception_during_booking_when_less_than_24h_to_screening() throws Exception {
        //given
        var screeningDate = getCurrentDate(clock).minusHours(23);
        var seat = prepareSeat(screeningDate);

        //when
        var result = mockMvc.perform(
                post("/bookings/")
                        .param("seatId", seat.getId().toString())
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        new TooLateToBookingException().getMessage()
                ));
    }

    @Test
    void should_seat_be_busy_after_booking() throws Exception {
        //given
        var seat = prepareSeat();
        var screening = seat.getScreening();

        //when
        mockMvc.perform(
                post("/bookings/")
                        .param("seatId", seat.getId().toString())
        );

        //then
        var searchSeatsResult = mockMvc.perform(
                get("/screenings/" + screening.getId() + "/seats")
        );
        var isSeatBusy = getSeatsFromResult(searchSeatsResult).anyMatch(
                s -> s.id().equals(seat.getId()) && s.status().equals(SeatStatus.BUSY.name())
        );
        assertThat(isSeatBusy).isTrue();
    }

    @Test
    void should_reduce_screening_free_seats_by_one_after_booking() throws Exception {
        //given
        var seat = prepareSeat();
        var freeSeatsNumber = seat
                .getScreening()
                .getSeats()
                .stream()
                .filter(Seat::isFree)
                .count();

        //when
        mockMvc.perform(
                post("/bookings/")
                        .param("seatId", seat.getId().toString())
        );

        //then
        mockMvc.perform(
                        get("/screenings")
                )
                .andExpect(jsonPath("$[0].freeSeats").value(freeSeatsNumber - 1));
    }

    @Test
    void should_cancel_booking() throws Exception {
        //give
        var booking = prepareBooking(user.getId());

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
                b -> b.equals(bookingMapper.mapToDto(booking).withStatus(BookingStatus.CANCELLED.name()))
        );
        assertThat(isBookingCancelled).isTrue();
    }

    @Test
    void should_seat_be_free_again_and_increase_free_seats_by_one_after_booking_cancelling() throws Exception {
        //given
        var booking = prepareBooking(user.getId());
        var screening = booking.getSeat().getScreening();
        var seat = booking.getSeat();
        var freeSeatsNumber = screening.getSeats().size();

        //when
        var result = mockMvc.perform(
                post("/bookings/" + booking.getId() + "/cancel")
        );

        //then
        result.andExpect(status().isOk());
        var searchSeatsResult = mockMvc.perform(
                get("/screenings/" + screening.getId() + "/seats")
        );
        var isSeatFreeAgain = getSeatsFromResult(searchSeatsResult)
                .anyMatch(s -> s.id().equals(seat.getId()) && s.status().equals(SeatStatus.FREE.name()));
        assertThat(isSeatFreeAgain).isTrue();
        mockMvc
                .perform(get("/screenings"))
                .andExpect(jsonPath("$[0].freeSeats").value(freeSeatsNumber));
    }

    @Test
    void should_throw_exception_during_booking_when_booking_is_already_cancelled() throws Exception {
        //given
        var booking = prepareBooking(BookingStatus.CANCELLED, user.getId());

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
    void should_throw_exception_during_booking_cancelling_when_less_than_24h_to_screening() throws Exception {
        //given
        var hoursToScreening = 23;
        var booking = prepareBooking(hoursToScreening, user.getId());

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
    void should_get_all_user_bookings() throws Exception {
        //given
        var bookings = prepareBookings(user.getId());

        //when
        var result = mockMvc.perform(
                get("/bookings/my")
        );

        //then
        result.andExpect(status().isOk());
        var bookingsFromResult = getBookingsFromResult(result).toList();
        assertThat(bookingsFromResult).containsExactlyInAnyOrderElementsOf(bookingMapper.mapToDto(bookings));
    }

    private Seat prepareSeat() {
        var room = roomRepository.add(createRoom());
        var film = addFilm(createFilmWithScreening(room));
        return film
                .getScreenings()
                .get(0)
                .getSeats()
                .get(0);
    }

    private Seat prepareSeat(LocalDateTime screeningDate) {
        var room = roomRepository.add(createRoom());
        var film = addFilm(createFilmWithScreening(screeningDate, room));
        return film
                .getScreenings()
                .get(0)
                .getSeats()
                .get(0);
    }

    private List<Seat> prepareSeats() {
        var room = roomRepository.add(createRoom());
        var film = addFilm(createFilmWithScreening(room));
        return film
                .getScreenings()
                .get(0)
                .getSeats();
    }

    private Booking prepareBooking(Long userId) {
        var seat = prepareSeat();
        return addBooking(createBooking(seat, userId));
    }

    private Booking prepareBooking(BookingStatus status, Long userId) {
        var seat = prepareSeat();
        return addBooking(createBooking(seat, userId, status));
    }

    private Booking prepareBooking(int hoursToScreening, Long userId) {
        var screeningDate = getCurrentDate(clock).minusHours(hoursToScreening);
        var seat = prepareSeat(screeningDate);
        return addBooking(createBooking(seat, userId));
    }

    private List<Booking> prepareBookings(Long userId) {
        var seats = prepareSeats();
        var booking1 = addBooking(createBooking(seats.get(0), userId));
        var booking2 = addBooking(createBooking(seats.get(1), userId));
        return List.of(booking1, booking2);
    }

    private Film addFilm(Film film) {
        return filmRepository.add(film);
    }

    private Booking addBooking(Booking booking) {
        return bookingRepository.add(booking);
    }

    private LocalDateTime getCurrentDate(Clock clock) {
        return LocalDateTime.now(clock);
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

}
