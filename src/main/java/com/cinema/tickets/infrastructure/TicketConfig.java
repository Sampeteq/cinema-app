package com.cinema.tickets.infrastructure;

import com.cinema.screenings.domain.ScreeningService;
import com.cinema.tickets.domain.TicketReadRepository;
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
            TicketReadRepository ticketReadRepository,
            ScreeningService screeningService,
            Clock clock
    ) {
        return new TicketService(
                ticketRepository,
                ticketReadRepository,
                screeningService,
                clock
        );
    }

    @Bean
    TicketRepository ticketRepository(TicketJpaRepository ticketJpaRepository) {
        return new TicketJpaRepositoryAdapter(ticketJpaRepository);
    }

    @Bean
    TicketReadRepository ticketReadRepository(TicketJpaReadRepository ticketJpaReadRepository) {
        return new TicketJpaReadRepositoryAdapter(ticketJpaReadRepository);
    }
}
