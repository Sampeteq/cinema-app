package code.bookings.infrastructure.rest;

import code.MockTimeProvider;
import code.SpringIT;
import code.bookings.application.dto.BookingViewDto;
import code.bookings.application.services.BookingCancelService;
import code.bookings.application.services.BookingMakeService;
import code.bookings.domain.BookingStatus;
import code.bookings.domain.exceptions.BookingAlreadyCancelledException;
import code.bookings.domain.exceptions.BookingAlreadyExists;
import code.bookings.domain.exceptions.BookingCancelTooLateException;
import code.bookings.domain.exceptions.BookingTooLateException;
import code.catalog.application.dto.SeatDto;
import code.catalog.application.services.FilmCreateService;
import code.catalog.application.services.RoomCreateService;
import code.catalog.application.services.ScreeningCreateService;
import code.shared.time.TimeProvider;
import code.user.application.dto.UserSignUpDto;
import code.user.application.services.UserSignUpService;
import com.teketik.test.mockinbean.MockInBean;
import com.teketik.test.mockinbean.MockInBeans;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static code.bookings.helpers.BookingTestHelper.createFilmCreateDto;
import static code.bookings.helpers.BookingTestHelper.createRoomCreateDto;
import static code.bookings.helpers.BookingTestHelper.createScreeningCrateDto;
import static code.catalog.helpers.ScreeningTestHelper.getScreeningDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "user1@mail.com")
class BookingRestControllerIT extends SpringIT {

    @Autowired
    private UserSignUpService userSignUpService;

    @Autowired
    private FilmCreateService filmCreateService;

    @Autowired
    private RoomCreateService roomCreateService;

    @Autowired
    private ScreeningCreateService screeningCreateService;

    @Autowired
    private BookingMakeService bookingMakeService;

    @Autowired
    private BookingCancelService bookingCancelService;

    @MockInBeans(
            value = {
                    @MockInBean(BookingMakeService.class),
                    @MockInBean(BookingCancelService.class)
            }
    )
    private TimeProvider timeProvider;

    public static final String BOOKINGS_BASE_ENDPOINT = "/bookings/";

    @BeforeEach
    void setUp() {
        var username = "user1@mail.com";
        var password = "12345";
        userSignUpService.signUp(
                new UserSignUpDto(
                        username,
                        password,
                        password
                )
        );
        var currentDate = new MockTimeProvider().getCurrentDate();
        Mockito
                .when(timeProvider.getCurrentDate())
                .thenReturn(currentDate);
    }

    @Test
    void should_make_booking() throws Exception {
        //given
        var filmTitle = "Title 1";
        var roomCustomId = "1";
        var screeningDate = getScreeningDate(timeProvider.getCurrentDate());
        var seatRowNumber = 1;
        var seatNumber = 1;
        prepareSeat(filmTitle, roomCustomId, screeningDate);

        //when
        var result = mockMvc.perform(
                post(BOOKINGS_BASE_ENDPOINT).param("seatId", "1")
        );

        //then
        result.andExpect(status().isOk());
        var expectedDto = List.of(
                new BookingViewDto(
                        1L,
                        BookingStatus.ACTIVE,
                        filmTitle,
                        screeningDate,
                        roomCustomId,
                        seatRowNumber,
                        seatNumber
                )
        );
        mockMvc.perform(
                get(BOOKINGS_BASE_ENDPOINT + "my/")
        ).andExpect(content().json(toJson(expectedDto)));
    }

    @Test
    void should_throw_exception_during_booking_when_booking_already_exists() throws Exception {
        //given
        var seatId = 1L;
        prepareBooking(seatId);

        //when
        var result = mockMvc.perform(
                post(BOOKINGS_BASE_ENDPOINT).param("seatId", String.valueOf(seatId))
        );

        //then
        result
                .andExpect(
                        status().isBadRequest()
                )
                .andExpect(
                        content().string(
                                new BookingAlreadyExists().getMessage()
                        )
                );
    }

    @Test
    void should_throw_exception_during_booking_when_less_than_1h_to_screening() throws Exception {
        //given
        var screeningDate = getScreeningDate(timeProvider.getCurrentDate());
        prepareSeat(screeningDate);
        Mockito
                .when(timeProvider.getCurrentDate())
                .thenReturn(screeningDate.minusMinutes(59));

        //when
        var result = mockMvc.perform(
                post(BOOKINGS_BASE_ENDPOINT)
                        .param("seatId", "1")
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
        prepareSeat();

        //when
        mockMvc.perform(
                post(BOOKINGS_BASE_ENDPOINT)
                        .param("seatId", "1")
        );

        //then
        var searchSeatsResult = mockMvc.perform(
                get("/seats").param("screeningId", "1")
        );
        var isSeatBusy = getSeatsFromResult(searchSeatsResult).anyMatch(
                s -> s.id().equals(1L) && !s.isFree()
        );
        assertThat(isSeatBusy).isTrue();
    }

    @Test
    void should_cancel_booking() throws Exception {
        //give
        prepareBooking();

        //when
        var result = mockMvc.perform(
                post(BOOKINGS_BASE_ENDPOINT + 1L + "/cancel")
        );

        //then
        result.andExpect(status().isOk());
        var searchBookingsResult = mockMvc.perform(
                get("/bookings/my")
        );
        var isBookingCancelled = getBookingsFromResult(searchBookingsResult).anyMatch(
                b -> b.id().equals(1L) && b.status().equals(BookingStatus.CANCELLED)
        );
        assertThat(isBookingCancelled).isTrue();
    }

    @Test
    void should_seat_be_free_again_after_booking_cancelling() throws Exception {
        //given
        prepareBooking();

        //when
        var result = mockMvc.perform(
                post(BOOKINGS_BASE_ENDPOINT + 1 + "/cancel")
        );

        //then
        result.andExpect(status().isOk());
        var searchSeatsResult = mockMvc.perform(
                get("/seats").param("screeningId", "1")
        );
        var isSeatFreeAgain = getSeatsFromResult(searchSeatsResult)
                .anyMatch(s -> s.id().equals(1L) && s.isFree());
        assertThat(isSeatFreeAgain).isTrue();
    }

    @Test
    void should_throw_exception_during_booking_when_booking_is_already_cancelled() throws Exception {
        //given
        prepareCancelledBooking();

        //when
        var result = mockMvc.perform(
                post(BOOKINGS_BASE_ENDPOINT + 1L + "/cancel")
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
        var screeningDate = getScreeningDate(timeProvider.getCurrentDate());
        prepareBooking(screeningDate);
        Mockito
                .when(timeProvider.getCurrentDate())
                .thenReturn(screeningDate.minusHours(23));

        //when
        var result = mockMvc.perform(
                post(BOOKINGS_BASE_ENDPOINT + 1L + "/cancel")
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
        var filmTitle = "Title 1";
        var roomCustomId = "1";
        var screeningDate = getScreeningDate(timeProvider.getCurrentDate());
        var seatRowNumber = 1;
        var seatNumber = 1;
        prepareBooking(filmTitle, roomCustomId, screeningDate);

        //when
        var result = mockMvc.perform(
                get("/bookings/my")
        );

        //then
        result.andExpect(status().isOk());
        var bookingsFromResult = getBookingsFromResult(result).toList();
        var expected = List.of(
                new BookingViewDto(
                        1L,
                        BookingStatus.ACTIVE,
                        filmTitle,
                        screeningDate,
                        roomCustomId,
                        seatRowNumber,
                        seatNumber
                )
        );
        assertThat(bookingsFromResult).containsExactlyInAnyOrderElementsOf(expected);
    }

    private void prepareSeat() {
        filmCreateService.creteFilm(createFilmCreateDto());
        roomCreateService.createRoom(createRoomCreateDto());
        var screeningDate = getScreeningDate(timeProvider.getCurrentDate());
        screeningCreateService.createScreening(
                createScreeningCrateDto().withDate(screeningDate)
        );
    }

    private void prepareSeat(LocalDateTime screeningDate) {
        filmCreateService.creteFilm(createFilmCreateDto());
        roomCreateService.createRoom(createRoomCreateDto());
        screeningCreateService.createScreening(
                createScreeningCrateDto().withDate(screeningDate)
        );
    }

    private void prepareSeat(String filmTitle, String roomCustomId, LocalDateTime screeningDate) {
        filmCreateService.creteFilm(
                createFilmCreateDto().withTitle(filmTitle)
        );
        roomCreateService.createRoom(
                createRoomCreateDto().withCustomId(roomCustomId)
        );
        screeningCreateService.createScreening(
                createScreeningCrateDto().withDate(screeningDate)
        );
    }

    private void prepareBooking() {
        prepareSeat();
        var seatId = 1L;
        bookingMakeService.makeBooking(seatId);
    }

    private void prepareBooking(Long seatId) {
        prepareSeat();
        bookingMakeService.makeBooking(seatId);
    }

    private void prepareBooking(LocalDateTime screeningDate) {
        prepareSeat(screeningDate);
        var seatId = 1L;
        Mockito
                .when(timeProvider.getCurrentDate())
                .thenReturn(screeningDate.plusHours(25));
        bookingMakeService.makeBooking(seatId);
    }

    private void prepareBooking(String filmTitle, String roomCustomId, LocalDateTime screeningDate) {
        prepareSeat(filmTitle, roomCustomId, screeningDate);
        var seat1Id = 1L;
        bookingMakeService.makeBooking(seat1Id);
    }

    private void prepareCancelledBooking() {
        prepareSeat();
        var seatId = 1L;
        bookingMakeService.makeBooking(seatId);
        var bookingId = 1L;
        bookingCancelService.cancelBooking(bookingId);
    }

    private Stream<SeatDto> getSeatsFromResult(ResultActions searchSeatsResult) throws Exception {
        return Arrays.stream(
                fromResultActions(searchSeatsResult, SeatDto[].class)
        );
    }

    private Stream<BookingViewDto> getBookingsFromResult(ResultActions searchSeatsResult) throws Exception {
        return Arrays.stream(
                fromResultActions(searchSeatsResult, BookingViewDto[].class)
        );
    }

}
