package com.cinema.tickets.infrastructure;

import com.cinema.screenings.domain.ScreeningService;
import com.cinema.tickets.domain.TicketRepository;
import com.cinema.tickets.domain.TicketService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class TicketConfig {

    @Bean
    TicketService ticketService(
            TicketRepository ticketRepository,
            ScreeningService screeningService,
            Clock clock
    ) {
        return new TicketService(
                ticketRepository,
                screeningService,
                clock
        );
    }

    @Bean
    TicketRepository ticketRepository(TicketJpaRepository ticketJpaRepository) {
        return new TicketJpaRepositoryAdapter(ticketJpaRepository);
    }
}
