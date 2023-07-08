package code.bookings.infrastructure.rest;

import code.SpringIT;
import code.bookings.application.dto.BookingDto;
import code.bookings.application.dto.BookingMapper;
import code.bookings.domain.Booking;
import code.bookings.domain.BookingStatus;
import code.bookings.domain.exceptions.BookingAlreadyCancelledException;
import code.bookings.domain.exceptions.BookingCancelTooLateException;
import code.bookings.domain.exceptions.BookingTooLateException;
import code.bookings.domain.ports.BookingRepository;
import code.catalog.application.dto.SeatDto;
import code.catalog.domain.Seat;
import code.catalog.domain.ports.FilmRepository;
import code.catalog.domain.ports.RoomRepository;
import code.user.domain.User;
import code.user.domain.ports.UserRepository;
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

import static code.bookings.helpers.BookingTestHelper.createBooking;
import static code.catalog.helpers.FilmTestHelper.createFilm;
import static code.catalog.helpers.RoomTestHelper.createRoom;
import static code.catalog.helpers.ScreeningTestHelper.createScreening;
import static code.user.helpers.UserTestHelper.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "user1@mail.com")
class BookingRestControllerIT extends SpringIT {

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

    public static final String BOOKINGS_BASE_ENDPOINT = "/bookings/";

    @BeforeEach
    void setUp() {
        this.user = userRepository.add(createUser("user1@mail.com"));
    }

    @Test
    void should_make_booking() throws Exception {
        //given
        var seat = prepareSeat();
        var expectedDto = List.of(
                new BookingDto(
                1L,
                BookingStatus.ACTIVE,
                seat.getScreening().getFilm().getTitle(),
                seat.getScreening().getDate(),
                seat.getScreening().getRoom().getCustomId(),
                seat.getRowNumber(),
                seat.getNumber()
                )
        );

        //when
        var result = mockMvc.perform(
                post(BOOKINGS_BASE_ENDPOINT).param("seatId", seat.getId().toString())
        );

        //then
        result.andExpect(status().isOk());
        mockMvc.perform(
                get(BOOKINGS_BASE_ENDPOINT + "my/")
        ).andExpect(content().json(toJson(expectedDto)));
    }

    @Test
    void should_throw_exception_during_booking_when_less_than_1h_to_screening() throws Exception {
        //given
        var screeningDate = getCurrentDate(clock).minusMinutes(59);
        var seat = prepareSeat(screeningDate);

        //when
        var result = mockMvc.perform(
                post(BOOKINGS_BASE_ENDPOINT)
                        .param("seatId", seat.getId().toString())
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        new BookingTooLateException().getMessage()
                ));
    }

    @Test
    void should_not_seat_be_free_after_booking() throws Exception {
        //given
        var seat = prepareSeat();
        var screening = seat.getScreening();

        //when
        mockMvc.perform(
                post(BOOKINGS_BASE_ENDPOINT)
                        .param("seatId", seat.getId().toString())
        );

        //then
        var searchSeatsResult = mockMvc.perform(
                get("/screenings/" + screening.getId() + "/seats")
        );
        var isSeatBusy = getSeatsFromResult(searchSeatsResult).anyMatch(
                s -> s.id().equals(seat.getId()) && !s.isFree()
        );
        assertThat(isSeatBusy).isTrue();
    }

    @Test
    void should_cancel_booking() throws Exception {
        //give
        var booking = prepareBooking(user.getId());

        //when
        var result = mockMvc.perform(
                post(BOOKINGS_BASE_ENDPOINT + booking.getId() + "/cancel")
        );

        //then
        result.andExpect(status().isOk());
        var searchBookingsResult = mockMvc.perform(
                get("/bookings/my")
        );
        var isBookingCancelled = getBookingsFromResult(searchBookingsResult).anyMatch(
                b -> b.equals(bookingMapper.mapToDto(booking).withStatus(BookingStatus.CANCELLED))
        );
        assertThat(isBookingCancelled).isTrue();
    }

    @Test
    void should_seat_be_free_again_after_booking_cancelling() throws Exception {
        //given
        var booking = prepareBooking(user.getId());
        var screening = booking.getSeat().getScreening();
        var seat = booking.getSeat();

        //when
        var result = mockMvc.perform(
                post(BOOKINGS_BASE_ENDPOINT + booking.getId() + "/cancel")
        );

        //then
        result.andExpect(status().isOk());
        var searchSeatsResult = mockMvc.perform(
                get("/screenings/" + screening.getId() + "/seats")
        );
        var isSeatFreeAgain = getSeatsFromResult(searchSeatsResult)
                .anyMatch(s -> s.id().equals(seat.getId()) && s.isFree());
        assertThat(isSeatFreeAgain).isTrue();
    }

    @Test
    void should_throw_exception_during_booking_when_booking_is_already_cancelled() throws Exception {
        //given
        var booking = prepareBooking(user.getId(), BookingStatus.CANCELLED);

        //when
        var result = mockMvc.perform(
                post(BOOKINGS_BASE_ENDPOINT + booking.getId() + "/cancel")
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
        var booking = prepareBooking(user.getId(), hoursToScreening);

        //when
        var result = mockMvc.perform(
                post(BOOKINGS_BASE_ENDPOINT + booking.getId() + "/cancel")
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        new BookingCancelTooLateException().getMessage()
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
        var film = createFilm();
        var room = roomRepository.add(createRoom());
        film.addScreening(createScreening(film, room));
        return filmRepository
                .add(film)
                .getScreenings()
                .get(0)
                .getSeats()
                .get(0);
    }

    private Seat prepareSeat(LocalDateTime screeningDate) {
        var film = createFilm();
        var room = roomRepository.add(createRoom());
        film.addScreening(createScreening(film, room, screeningDate));
        return filmRepository
                .add(film)
                .getScreenings()
                .get(0)
                .getSeats()
                .get(0);
    }

    private List<Seat> prepareSeats() {
        var film = createFilm();
        var room = roomRepository.add(createRoom());
        film.addScreening(createScreening(film, room));
        return filmRepository
                .add(film)
                .getScreenings()
                .get(0)
                .getSeats();
    }

    private Booking prepareBooking(Long userId) {
        var seat = prepareSeat();
        return bookingRepository.add(createBooking(seat, userId));
    }

    private Booking prepareBooking(Long userId, BookingStatus status) {
        var seat = prepareSeat();
        return bookingRepository.add(createBooking(seat, userId, status));
    }

    private Booking prepareBooking(Long userId, int hoursToScreening) {
        var screeningDate = getCurrentDate(clock).minusHours(hoursToScreening);
        var seat = prepareSeat(screeningDate);
        return bookingRepository.add(createBooking(seat, userId));
    }

    private List<Booking> prepareBookings(Long userId) {
        var seats = prepareSeats();
        var booking1 = bookingRepository.add(createBooking(seats.get(0), userId));
        var booking2 = bookingRepository.add(createBooking(seats.get(1), userId));
        return List.of(booking1, booking2);
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
