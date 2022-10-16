package code.ticket;

import code.screening.ScreeningFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class TicketConfig {

    @Bean
    TicketFacade ticketAPI(TicketRepository ticketRepository, ScreeningFacade screeningFacade) {
        return new TicketFacade(ticketRepository, screeningFacade);
    }

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }
}
