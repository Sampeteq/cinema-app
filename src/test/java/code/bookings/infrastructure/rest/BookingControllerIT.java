package code.bookings.infrastructure.rest;

import code.MockTimeProvider;
import code.SpringIT;
import code.bookings.application.dto.BookingMakeDto;
import code.bookings.application.dto.BookingViewDto;
import code.bookings.application.services.BookingFacade;
import code.bookings.domain.BookingStatus;
import code.bookings.domain.exceptions.BookingAlreadyCancelledException;
import code.bookings.domain.exceptions.BookingAlreadyExists;
import code.bookings.domain.exceptions.BookingCancelTooLateException;
import code.bookings.domain.exceptions.BookingTooLateException;
import code.catalog.application.dto.SeatDto;
import code.catalog.application.services.CatalogFacade;
import code.shared.time.TimeProvider;
import code.user.application.dto.UserSignUpDto;
import code.user.application.services.UserFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
class BookingControllerIT extends SpringIT {

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private CatalogFacade catalogFacade;

    @Autowired
    private BookingFacade bookingFacade;

    @MockBean
    private TimeProvider timeProvider;

    public static final String BOOKINGS_BASE_ENDPOINT = "/bookings/";

    @BeforeEach
    void setUp() {
        var username = "user1@mail.com";
        var password = "12345";
        userFacade.signUpUser(
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
    void should_read_seats_for_screening() throws Exception {
        //given
        var seats = prepareSeats();

        //when
        var result = mockMvc.perform(
                get(BOOKINGS_BASE_ENDPOINT + "seats").param("screeningId", "1")
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(seats)));
    }

    @Test
    void should_make_booking() throws Exception {
        //given
        var filmTitle = "Title 1";
        var roomCustomId = "1";
        var screeningDate = getScreeningDate(timeProvider.getCurrentDate());
        prepareSeat(filmTitle, roomCustomId, screeningDate);
        var screeningId = 1L;
        var seatRowNumber = 1;
        var seatNumber = 1;
        var bookingMakeDto = new BookingMakeDto(
                screeningId,
                seatRowNumber,
                seatNumber
        );

        //when
        var result = mockMvc.perform(
                post(BOOKINGS_BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(bookingMakeDto))
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
        var screeningId = 1L;
        var rowNumber = 1;
        var seatNumber = 1;
        prepareBooking(screeningId, rowNumber, seatNumber);
        var bookingMakeDto = new BookingMakeDto(
                screeningId,
                rowNumber,
                seatNumber
        );

        //when
        var result = mockMvc.perform(
                post(BOOKINGS_BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(bookingMakeDto))
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
        var screeningId = 1L;
        var rowNumber = 1;
        var seatNumber = 1;
        var bookingMakeDto = new BookingMakeDto(
                screeningId,
                rowNumber,
                seatNumber
        );

        //when
        var result = mockMvc.perform(
                post(BOOKINGS_BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(bookingMakeDto))
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
        var screeningId = 1L;
        var rowNumber = 1;
        var seatNumber = 1;
        var bookingMakeDto = new BookingMakeDto(
                screeningId,
                rowNumber,
                seatNumber
        );

        //when
        mockMvc.perform(
                post(BOOKINGS_BASE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(bookingMakeDto))
        );

        //then
        var searchSeatsResult = mockMvc.perform(
                get(BOOKINGS_BASE_ENDPOINT + "seats").param("screeningId", "1")
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
                get(BOOKINGS_BASE_ENDPOINT + "seats").param("screeningId", "1")
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
    void should_read_all_user_bookings() throws Exception {
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

    private List<SeatDto> prepareSeats() {
        catalogFacade.createFilm(createFilmCreateDto());
        catalogFacade.createRoom(createRoomCreateDto());
        var screeningDate = getScreeningDate(timeProvider.getCurrentDate());
        catalogFacade.createScreening(
                createScreeningCrateDto().withDate(screeningDate)
        );
        var screeningId = 1L;
        return bookingFacade.readSeatsByScreeningId(screeningId);
    }

    private void prepareSeat() {
        catalogFacade.createFilm(createFilmCreateDto());
        catalogFacade.createRoom(createRoomCreateDto());
        var screeningDate = getScreeningDate(timeProvider.getCurrentDate());
        catalogFacade.createScreening(
                createScreeningCrateDto().withDate(screeningDate)
        );
    }

    private void prepareSeat(LocalDateTime screeningDate) {
        catalogFacade.createFilm(createFilmCreateDto());
        catalogFacade.createRoom(createRoomCreateDto());
        catalogFacade.createScreening(
                createScreeningCrateDto().withDate(screeningDate)
        );
    }

    private void prepareSeat(String filmTitle, String roomCustomId, LocalDateTime screeningDate) {
        catalogFacade.createFilm(
                createFilmCreateDto().withTitle(filmTitle)
        );
        catalogFacade.createRoom(
                createRoomCreateDto().withCustomId(roomCustomId)
        );
        catalogFacade.createScreening(
                createScreeningCrateDto().withDate(screeningDate)
        );
    }

    private void prepareBooking() {
        prepareSeat();
        var screeningId = 1L;
        var rowNumber = 1;
        var seatNumber = 1;
        var dto = new BookingMakeDto(
                screeningId,
                rowNumber,
                seatNumber
        );
        bookingFacade.makeBooking(dto);
    }

    private void prepareBooking(Long screeningId, int rowNumber, int seatNumber) {
        prepareSeat();
        var dto = new BookingMakeDto(
                screeningId,
                rowNumber,
                seatNumber
        );
        bookingFacade.makeBooking(dto);
    }

    private void prepareBooking(LocalDateTime screeningDate) {
        prepareSeat(screeningDate);
        var screeningId = 1L;
        Mockito
                .when(timeProvider.getCurrentDate())
                .thenReturn(screeningDate.plusHours(25));
        var rowNumber = 1;
        var seatNumber =  1;
        var bookingMakeDto = new BookingMakeDto(
                screeningId,
                rowNumber,
                seatNumber
        );
        bookingFacade.makeBooking(bookingMakeDto);
    }

    private void prepareBooking(String filmTitle, String roomCustomId, LocalDateTime screeningDate) {
        prepareSeat(filmTitle, roomCustomId, screeningDate);
        var screeningId = 1L;
        var rowNumber = 1;
        var seatNumber =  1;
        var bookingMakeDto = new BookingMakeDto(
                screeningId,
                rowNumber,
                seatNumber
        );
        bookingFacade.makeBooking(bookingMakeDto);
    }

    private void prepareCancelledBooking() {
        prepareSeat();
        var screeningId = 1L;
        var rowNumber = 1;
        var seatNumber =  1;
        var bookingMakeDto = new BookingMakeDto(
                screeningId,
                rowNumber,
                seatNumber
        );
        bookingFacade.makeBooking(bookingMakeDto);
        var bookingId = 1L;
        bookingFacade.cancelBooking(bookingId);
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