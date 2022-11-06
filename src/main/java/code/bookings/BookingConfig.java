package code.bookings;

import code.screenings.ScreeningFacade;
import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class BookingConfig {

    @Bean
    BookingFacade bookingFacade(
            ScreeningFacade screeningFacade,
            ScreeningTicketRepository screeningTicketRepository,
            EventBus eventBus
    ) {
        var screeningTicketReservationService = new ScreeningTicketBookingService(screeningFacade, eventBus);
        return new BookingFacade(screeningTicketRepository, screeningTicketReservationService);
    }
}
