package com.cinema.bookings_views.infrastructure.rest;

import com.cinema.MockTimeProvider;
import com.cinema.SpringIT;
import com.cinema.bookings.application.dto.BookingMakeDto;
import com.cinema.bookings.application.services.BookingFacade;
import com.cinema.bookings.domain.BookingStatus;
import com.cinema.bookings_views.application.dto.BookingViewDto;
import com.cinema.catalog.application.services.CatalogFacade;
import com.cinema.user.application.dto.UserSignUpDto;
import com.cinema.user.application.services.UserFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.cinema.bookings.BookingTestHelper.createFilmCreateDto;
import static com.cinema.bookings.BookingTestHelper.createRoomCreateDto;
import static com.cinema.bookings.BookingTestHelper.createScreeningCrateDto;
import static com.cinema.catalog.ScreeningTestHelper.getScreeningDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "user1@mail.com")
class BookingViewControllerIT extends SpringIT {

    @Autowired
    private BookingFacade bookingFacade;

    @Autowired
    private CatalogFacade catalogFacade;

    @Autowired
    private UserFacade userFacade;

    @SpyBean
    private MockTimeProvider timeProvider;

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
    }

    @Test
    void bookings_are_read_by_user_id() throws Exception {
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

    private Stream<BookingViewDto> getBookingsFromResult(ResultActions searchSeatsResult) {
        return Arrays.stream(
                fromResultActions(searchSeatsResult, BookingViewDto[].class)
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
}
