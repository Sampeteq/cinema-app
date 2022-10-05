package com.example.ticket.domain;

import com.example.screening.domain.ScreeningFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TicketConfig {

    @Bean
    TicketFacade ticketAPI(TicketRepository ticketRepository, ScreeningFacade screeningFacade) {
        var ticketFactory = new TicketFactory();
        return new TicketFacade(ticketRepository, ticketFactory, screeningFacade);
    }
}
