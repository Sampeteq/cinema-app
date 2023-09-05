package code.bookings_views.infrastructure.rest;

import code.MockTimeProvider;
import code.SpringIT;
import code.bookings.application.dto.BookingMakeDto;
import code.bookings.application.services.BookingFacade;
import code.bookings.domain.BookingStatus;
import code.bookings_views.application.dto.BookingViewDto;
import code.catalog.application.services.CatalogFacade;
import code.user.application.dto.UserSignUpDto;
import code.user.application.services.UserFacade;
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

import static code.bookings.BookingTestHelper.createFilmCreateDto;
import static code.bookings.BookingTestHelper.createRoomCreateDto;
import static code.bookings.BookingTestHelper.createScreeningCrateDto;
import static code.catalog.ScreeningTestHelper.getScreeningDate;
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
