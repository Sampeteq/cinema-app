package com.example.ticket;

import com.example.screening.ScreeningFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TicketConfig {

    @Bean
    TicketFacade ticketAPI(TicketRepository ticketRepository, ScreeningFacade screeningFacade) {
        return new TicketFacade(ticketRepository, screeningFacade);
    }
}
