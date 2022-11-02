package code.reservations;

import code.screenings.ScreeningFacade;
import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ReservationConfig {

    @Bean
    ReservationFacade reservationFacade(
            ScreeningFacade screeningFacade,
            ScreeningTicketRepository screeningTicketRepository,
            EventBus eventBus
    ) {
        var screeningTicketReservationService = new ScreeningTicketReservationService(screeningFacade, eventBus);
        return new ReservationFacade(screeningTicketRepository, screeningTicketReservationService);
    }
}
