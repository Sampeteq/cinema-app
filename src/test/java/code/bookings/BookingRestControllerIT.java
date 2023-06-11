package code.bookings;

import code.bookings.application.dto.BookingDto;
import code.bookings.application.dto.BookingId;
import code.bookings.application.dto.BookingMapper;
import code.bookings.domain.Booking;
import code.bookings.infrastructure.db.BookingRepository;
import code.bookings.domain.BookingStatus;
import code.bookings.domain.exceptions.BookingAlreadyCancelledException;
import code.bookings.domain.exceptions.BookingTooLateException;
import code.bookings.domain.exceptions.BookingCancelTooLateException;
import code.films.application.dto.FilmScreeningSeatDto;
import code.films.infrastructure.db.FilmRepository;
import code.rooms.infrastructure.db.RoomRepository;
import code.films.domain.FilmScreeningSeat;
import code.user.domain.User;
import code.user.infrastrcuture.db.UserRepository;
import code.SpringIT;
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

import static code.bookings.BookingTestHelper.createBooking;
import static code.films.FilmTestHelper.createFilm;
import static code.films.FilmScreeningTestHelper.createScreening;
import static code.rooms.RoomTestHelper.createRoom;
import static code.user.UserTestHelper.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        var bookingId = fromResultActions(result, BookingId.class)
                .id()
                .intValue();
        mockMvc.perform(
                        get("/bookings/my/" + bookingId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(bookingId)))
                .andExpect(jsonPath("$.screening.filmTitle", equalTo(seat.getScreening().getFilm().getTitle())))
                .andExpect(jsonPath("$.screening.date", notNullValue()))
                .andExpect(jsonPath("$.status", equalTo(BookingStatus.ACTIVE.name())))
                .andExpect(jsonPath("$.screening.seat.rowNumber", equalTo(seat.getRowNumber())))
                .andExpect(jsonPath("$.screening.seat.number", equalTo(seat.getNumber())));
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
                post("/bookings/")
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
    void should_seat_be_free_again_after_booking_cancelling() throws Exception {
        //given
        var booking = prepareBooking(user.getId());
        var screening = booking.getSeat().getScreening();
        var seat = booking.getSeat();

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
                .anyMatch(s -> s.id().equals(seat.getId()) && s.isFree());
        assertThat(isSeatFreeAgain).isTrue();
    }

    @Test
    void should_throw_exception_during_booking_when_booking_is_already_cancelled() throws Exception {
        //given
        var booking = prepareBooking(user.getId(), BookingStatus.CANCELLED);

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
        var booking = prepareBooking(user.getId(), hoursToScreening);

        //when
        var result = mockMvc.perform(
                post("/bookings/" + booking.getId() + "/cancel")
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

    private FilmScreeningSeat prepareSeat() {
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

    private FilmScreeningSeat prepareSeat(LocalDateTime screeningDate) {
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

    private List<FilmScreeningSeat> prepareSeats() {
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

    private Stream<FilmScreeningSeatDto> getSeatsFromResult(ResultActions searchSeatsResult) throws Exception {
        return Arrays.stream(
                fromResultActions(searchSeatsResult, FilmScreeningSeatDto[].class)
        );
    }

    private Stream<BookingDto> getBookingsFromResult(ResultActions searchSeatsResult) throws Exception {
        return Arrays.stream(
                fromResultActions(searchSeatsResult, BookingDto[].class)
        );
    }

}
