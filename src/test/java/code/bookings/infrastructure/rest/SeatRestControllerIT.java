package code.bookings.infrastructure.rest;

import code.MockTimeProvider;
import code.SpringIT;
import code.bookings.application.services.ScreeningCreatedHandlerService;
import code.bookings.application.services.SeatReadService;
import code.catalog.application.dto.SeatDto;
import code.catalog.application.services.ScreeningCreateService;
import code.catalog.domain.events.ScreeningCreatedEvent;
import code.shared.time.TimeProvider;
import com.teketik.test.mockinbean.MockInBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SeatRestControllerIT extends SpringIT {

    @Autowired
    private ScreeningCreatedHandlerService screeningCreatedHandlerService;

    @Autowired
    private SeatReadService seatReadService;

    @MockInBean(ScreeningCreateService.class)
    private TimeProvider timeProvider;

    @BeforeEach
    void setUp() {
        var currentDate = new MockTimeProvider().getCurrentDate();
        Mockito
                .when(timeProvider.getCurrentDate())
                .thenReturn(currentDate);
    }

    private static final String SEATS_BASE_ENDPOINT = "/seats";

    @Test
    void should_read_seats_for_screening() throws Exception {
        //given
        var seats = prepareSeats(timeProvider.getCurrentDate());

        //when
        var result = mockMvc.perform(
                get(SEATS_BASE_ENDPOINT).param("screeningId", "1")
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(seats)));
    }

    private List<SeatDto> prepareSeats(LocalDateTime currentDate) {
        var id = 1L;
        var date = currentDate.plusDays(7);
        var rowsQuantity = 10;
        var seatsQuantity = 15;
        var screeningCreatedEvent = new ScreeningCreatedEvent(
                id,
                date,
                rowsQuantity,
                seatsQuantity
        );
        screeningCreatedHandlerService.handle(screeningCreatedEvent);
        return seatReadService.readSeatsByScreeningId(id);
    }
}
